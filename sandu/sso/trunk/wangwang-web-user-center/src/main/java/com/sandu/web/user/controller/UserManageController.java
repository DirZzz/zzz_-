package com.sandu.web.user.controller;

import com.github.pagehelper.PageInfo;
import com.sandu.api.base.input.BasePlatformUpdate;
import com.sandu.api.base.model.BaseCompany;
import com.sandu.api.base.model.BasePlatform;
import com.sandu.api.base.service.BaseCompanyService;
import com.sandu.api.base.service.BasePlatformService;
import com.sandu.api.operatorLog.model.SysUserOperatorLog;
import com.sandu.api.system.model.SysDictionary;
import com.sandu.api.system.service.BaseAreaService;
import com.sandu.api.system.service.SysDictionaryService;
import com.sandu.api.user.input.*;
import com.sandu.api.user.model.LoginUser;
import com.sandu.api.user.model.SysUser;
import com.sandu.api.user.model.UserManageDTO;
import com.sandu.api.user.model.bo.CompanyInfoBO;
import com.sandu.api.user.output.*;
import com.sandu.api.user.service.SysRoleGroupService;
import com.sandu.api.user.service.SysUserService;
import com.sandu.api.user.service.manage.SysUserManageService;
import com.sandu.api.user.service.manage.UserManageService;
import com.sandu.authz.annotation.RequiresPermissions;
import com.sandu.common.LoginContext;
import com.sandu.common.ResponseEnvelope;
import com.sandu.common.exception.BizException;
import com.sandu.common.util.ExcelUtil;
import com.sandu.common.util.SpringContextHolder;
import com.sandu.util.DateUtils;
import com.sandu.util.ValidatorUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@RestController
@RequestMapping(value = "/v1/user/manage")
public class UserManageController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysDictionaryService sysDictionaryService;

    @Resource
    private BaseCompanyService baseCompanyService;

    @Resource
    private SysRoleGroupService sysRoleGroupService;

    @Resource
    private BasePlatformService basePlatformService;

    @Autowired
    private SysUserManageService sysUserManageService;

    @Autowired
    private BaseAreaService baseAreaService;

    private static final String USER_TYPE = "userType";

    private static final String USER_RESOURCE = "saleChannel";

    //套餐用户账号类型(0-购买;1-续费;2-试用;3-代购;4-升级)
    private static final String PACKAGE_ACCOUNT_TYPE_BUY = "购买";
    private static final String PACKAGE_ACCOUNT_TYPE_RENEWAL_FEE = "续费";
    private static final String PACKAGE_ACCOUNT_TYPE_TRY = "试用";
    private static final String PACKAGE_ACCOUNT_TYPE_PURCHASING = "代购";
    private static final String PACKAGE_ACCOUNT_TYPE_RISE = "升级";

    //旧用户账号类型
    private static final String OLD_ACCOUNT_TYPE_TRY = "试用";
    private static final String OLD_ACCOUNT_TYPE_FORMAL = "正式";

    //非套餐用户
    private static final String NON_PACKAGE_NAME = "非套餐用户";

    //厂商字典表可配置用户类型
    private static final String VENDOR_DICTIONARY_USER_TYPE = "vendor";

    //三度公司
    private static final String SANDU_DICTIONARY_USER_TYPE = "sandu";

    private static final String BRAND_BUSINESS_TYPE = "brandBusinessType";

    // 用户类型2：厂商；3:经销商
    private static final String ERR_MSG = "系统错误，请稍后再试!";

    private final Integer  USETYPEFORMAL=1;//正式用户
    private final Integer  USETYPETRYOUT=0;//试用用户

    @InitBinder
    protected void init(HttpServletRequest request, ServletRequestDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
    }

    @PostMapping(value = "/userList")
    public Object getUserList(@ModelAttribute @Valid UserManageSearch search, BindingResult validResult) {

        if (validResult.hasFieldErrors()) {
            return new ResponseEnvelope(false, validResult.getFieldError().getDefaultMessage());
        }

        UserManageService userManageService;
        try {

            userManageService = getUserManageServiceImpl(search.getUserMethod());

            PageInfo<UserManageDTO> pageInfo = userManageService.getUserList(search);

            if (StringUtils.isNotBlank(search.getServicesName())){

                List<UserManageVO> vos = transformReturnData(pageInfo.getList());
                return new ResponseEnvelope<>(true, pageInfo.getTotal(), vos);
            }else{

                List<UserManageDTO> servicesAccount = pageInfo.getList().stream().filter(dto -> Objects.equals(1, dto.getServicesFlag())).collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(servicesAccount)){
                    List<Long> userIds = servicesAccount.stream().map(UserManageDTO::getId).collect(Collectors.toList());
                    //获取套餐的信息
                    List<Map<String,Object>> lists =  sysUserService.findUserPackageInfoByUserIds(userIds);
                    //封装套餐信息
                    Map<Integer, List<Map<String, Object>>> groupByMap = lists.stream().collect(Collectors.groupingBy(it -> {
                        return (Integer)it.get("userId");
                    }));

                    pageInfo.getList().forEach(item ->{
                        List<Map<String, Object>> data = groupByMap.get(Integer.parseInt(item.getId() + ""));
                        if (!CollectionUtils.isEmpty(data)){
                            Map<String, Object> resultData = data.get(0);
                            item.setBusinessType(emptyParameterProcess(resultData.get("businessType")));
                            item.setDuration(emptyParameterProcess(resultData.get("duration")));
                            item.setPriceUnit(emptyParameterProcess(resultData.get("priceUnit")));
                            item.setEffectiveEnd((Date) resultData.get("effectiveEnd"));
                            item.setServicesName((String) resultData.get("servicesName"));
                            item.setServicesId(emptyParameterProcess(resultData.get("servicesId")));
                        }
                    });
                }
                List<UserManageVO> vos = transformReturnData(pageInfo.getList());
                return new ResponseEnvelope<>(true, pageInfo.getTotal(), vos);
            }
        } catch (Exception e) {
            log.error("系统错误", e);
            return new ResponseEnvelope<>(false, "系统错误");
        }
    }

    private Integer emptyParameterProcess(Object obj) {
        return Optional.ofNullable(obj)
                .map(
                        item -> Integer.parseInt(item.toString())
                )
                .orElse(null);
    }


    @RequestMapping(value = "/addUser")
    public Object addUser(@RequestBody @Valid UserAdd userAdd, BindingResult validResult) {

        try {
            vaildatorParam(userAdd, validResult);

            LoginUser loginUser = LoginContext.getLoginUser(LoginUser.class);
            UserManageService userManageService;
            userManageService = getUserManageServiceImpl(userAdd.getUserMethod());
            UserBatchExcelVO userBatchExcelVO = userManageService.addUser(userAdd, loginUser.getId());
            if (null != userBatchExcelVO && null != userBatchExcelVO.getUserId() && userBatchExcelVO.getUserId() > 0) {
                return new ResponseEnvelope<>(true, "新建用户成功！");
            }
        } catch (BizException biz) {
            return new ResponseEnvelope<>(false, biz.getMessage());
        } catch (IllegalArgumentException ill) {
            return new ResponseEnvelope<>(false, ill.getMessage());
        } catch (RuntimeException run) {
            return new ResponseEnvelope<>(false, run.getMessage());
        } catch (Exception e) {
            log.error("系统错误", e);
            return new ResponseEnvelope<>(false, "系统错误");
        }
        return new ResponseEnvelope<>(false, "新建用户失败！");
    }

    private void vaildatorParam(UserAdd userAdd, BindingResult validResult) {
        if (validResult.hasFieldErrors()) {
            throw new IllegalArgumentException(validResult.getFieldError().getDefaultMessage());
        }
        if (Objects.nonNull(userAdd)) {
            ValidatorUtil.checkMobileCorrect(userAdd.getMobile());
            int result = sysUserService.check2BMobileIsExist(userAdd.getMobile());
            if (result > 0) {
                throw new BizException("该手机号已存在");
            }
        }
    }


    @RequestMapping(value = "/batchAddUser")
    public Object batchAddUser(@RequestBody @Valid UserAdd userAdd, BindingResult validResult,HttpServletResponse response) {


        String message = "";
        try {
            Assert.notNull(userAdd.getIncreaseNum(), "增加人数不能为空");

            vaildatorParam(userAdd, validResult);

            LoginUser loginUser = LoginContext.getLoginUser(LoginUser.class);
            UserManageService userManageService;

            List<UserBatchExcelVO> userBatchExcelVOList = new ArrayList<>();
            UserBatchExcelVO userBatchExcelVO = null;


            userManageService = getUserManageServiceImpl(userAdd.getUserMethod());
            if(null == userAdd.getIsRandom()){
                userAdd.setIsRandom(0);
            }

            for (int i = 0; i < userAdd.getIncreaseNum(); i++) {

                userBatchExcelVO = userManageService.addUser(userAdd, loginUser.getId());

                if (null != userBatchExcelVO) {
                    userBatchExcelVOList.add(userBatchExcelVO);
                }
            }

            //导出Excel
            if (null != userBatchExcelVOList && userBatchExcelVOList.size() > 0) {

                //excel标题
                String[] title = {"登录名", "密码"};

                //excel文件名
                String fileName = "用户表" + System.currentTimeMillis() + ".xls";

                //sheet名
                String sheetName = "信息表";
                String[][] content = new String[userBatchExcelVOList.size()][];
                for (int i = 0; i < userBatchExcelVOList.size(); i++) {
                    content[i] = new String[title.length];
                    userBatchExcelVO = userBatchExcelVOList.get(i);
                    content[i][0] = userBatchExcelVO.getNickName();
                    content[i][1] = userBatchExcelVO.getPassWord();
                }

                //创建HSSFWorkbook
                HSSFWorkbook hssfWorkbook = ExcelUtil.getHSSFWorkbook(sheetName, title, content, null);

                //响应到客户端
                ExcelUtil.setResponseHeader(response, fileName);
                OutputStream os = response.getOutputStream();
                hssfWorkbook.write(os);
                os.flush();
                os.close();
                hssfWorkbook.close();
            }

        } catch (BizException biz) {
            message = biz.getMessage();
        } catch (IllegalArgumentException ill) {
            message = ill.getMessage();
        }catch (RuntimeException run){
            message = run.getMessage();
        } catch (Exception e) {
            log.error("系统错误", e);
            message = "系统错误";
        }

        if(StringUtils.isNotBlank(message)){
            return new ResponseEnvelope<>(false, message);
        }
        return new ResponseEnvelope<>(true, "用户新增成功");
    }

    @RequestMapping(value = "/userRandomPassWord")
    public Object userRandomPassWord(Integer length){

        length = null == length ? 6 : length;
        String val = "";
        Random random = new Random();

        //length为几位密码
        Map<Integer,Integer> randomMap = new HashMap<>();
        List<Integer> randomList = new ArrayList<>();
        for (int i = 0; i < length; i++) {

            Integer num =  random.nextInt(2);
            randomMap.put(num,num);
            randomList.add(num);

            if (i == length - 1 && randomMap.size() == 1) {

                Integer beforeNum = randomMap.get(randomList.get(0));
                if (beforeNum > 0) {
                    num = 0;
                } else {
                    num = 1;
                }
            }

            String charOrNum = num % 2 == 0 ? "char" : "num";

            //输出字母还是数字
            if ("char".equalsIgnoreCase(charOrNum)) {

                //输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char) (random.nextInt(26) + temp);

            } else if ("num".equalsIgnoreCase(charOrNum)) {

                val += String.valueOf(random.nextInt(10));
            }
        }

        return val;
    }


    @RequestMapping(value = "/userDel")
    public Object userDel(String ids) {

        Assert.hasLength(ids, "用户id不能为空");

        LoginUser loginUser = LoginContext.getLoginUser(LoginUser.class);

        //删除用户操作，支持批量删除
        try {
            boolean userDel = sysUserService.handlerUserDel(ids, loginUser.getId());
            if (userDel) {
                return new ResponseEnvelope<>(true, "用户删除成功");
            }
        } catch (IllegalArgumentException ill) {
            return new ResponseEnvelope<>(false, ill.getMessage());
        } catch (Exception e) {
            log.error("系统错误", e);
            return new ResponseEnvelope<>(false, "系统错误");
        }
        return new ResponseEnvelope<>(true, "用户删除失败");
    }

    @RequestMapping(value = "/editUser")
    public Object editUser(@RequestBody UserEdit userEdit) {

        LoginUser loginUser = LoginContext.getLoginUser(LoginUser.class);
		if (userEdit.getAccountEnableTime() != null && userEdit.getAccountFreezeTime() == null) {
			return new ResponseEnvelope<>(false, "此账户未被停用，无法设置启用时间");
		}
        UserManageService userManageService;
        try {
            userManageService = getUserManageServiceImpl(userEdit.getUserMethod());
            userManageService.editUser(userEdit, loginUser.getId());
            this.setUserFreezeLog(userEdit, loginUser);
            return new ResponseEnvelope<>(true, "修改用户成功！");
        }
        catch (BizException biz) {
            return new ResponseEnvelope<>(false, biz.getMessage());
        }catch (RuntimeException run){
            log.error("系统异常(run)",run);
            return new ResponseEnvelope<>(false,StringUtils.isEmpty(run.getMessage()) ? "系统异常" : run.getMessage());
        }catch (Exception e) {
            log.error("系统错误", e);
            return new ResponseEnvelope<>(false, "系统错误");
        }

    }

    private void setUserFreezeLog(UserEdit userEdit, LoginUser loginUser) {
        AccountFreezeManage manage = new AccountFreezeManage();
        manage.setLoginUser(loginUser);
        manage.setUserIds(Collections.singletonList(userEdit.getId().intValue()));
		{
			manage.setState("freeze");
			manage.setDate(userEdit.getAccountFreezeTime());
			sysUserManageService.mangeFreeze(manage);
		}
        if (userEdit.getAccountEnableTime() != null) {
            manage.setState("enable");
            manage.setDate(userEdit.getAccountEnableTime());
            sysUserManageService.mangeFreeze(manage);
        }
    }

    @RequestMapping(value = "/userInfo")
    public Object getUserInfo(@RequestParam(value = "userId", required = true) Long userId) {

        Assert.notNull(userId, "用户id不能为空");

        Map<String, Object> dataMap = new HashMap<>();
        try {

            SysUser userInfo = sysUserService.get(userId.intValue());
            buildReturnData(dataMap, userInfo);
            return new ResponseEnvelope<>(true, dataMap);
        } catch (IllegalArgumentException ill) {
            return new ResponseEnvelope<>(false, ill.getMessage());
        } catch (Exception e) {
            log.error("系统错误", e);
            return new ResponseEnvelope<>(false, "系统错误");
        }

    }


    @PostMapping(value = "/batchModifyPassword")
    public Object batchModifyPassword(boolean random,String password,String userIds,HttpServletResponse response){

        if (StringUtils.isBlank(userIds)){
            return new ResponseEnvelope<>(false,"请选择您要修改的账号密码");
        }

        LoginUser loginUser = LoginContext.getLoginUser(LoginUser.class);
        try{
            sysUserService.batchModifyPassword(random,password,loginUser.getLoginName(),userIds);
            //导出excel
            List<SysUser> lists =  sysUserService.listByIds(Arrays.stream(userIds.split(",")).map(Integer::parseInt).collect(Collectors.toList()));
            if (!CollectionUtils.isEmpty(lists)){
                //excel标题
                String[] title = {"登录名", "密码"};

                //excel文件名
                String fileName = "用户表" + System.currentTimeMillis() + ".xls";

                //sheet名
                String sheetName = "信息表";
                String[][] content = new String[lists.size()][];
                SysUser sysUser;
                for (int i = 0; i < lists.size(); i++) {
                    content[i] = new String[title.length];
                    sysUser = lists.get(i);
                    content[i][0] = sysUser.getNickName();
                    content[i][1] = sysUser.getInitialPassword();
                }

                //创建HSSFWorkbook
                HSSFWorkbook hssfWorkbook = ExcelUtil.getHSSFWorkbook(sheetName, title, content, null);

                //响应到客户端
                ExcelUtil.setResponseHeader(response, fileName);
                OutputStream os = response.getOutputStream();
                hssfWorkbook.write(os);
                os.flush();
                os.close();
                hssfWorkbook.close();
            }
            return new ResponseEnvelope<>(true,"批量修改用户密码成功");
        }catch (Exception e){
            log.error("系统异常",e);
            return new ResponseEnvelope<>(false,"批量修改用户密码异常");
        }
    }


    @PostMapping(value = "/batchModify")
    public com.sandu.commons.ResponseEnvelope batchModifyPackage(String userIds, Integer servicesId){
        if (StringUtils.isBlank(userIds)){
            return new com.sandu.commons.ResponseEnvelope(false,"请选择您要修改的用户套餐");
        }
        try{
            boolean flag = sysUserService.batchModifyPackage(Arrays.stream(userIds.split(",")).filter(Objects::nonNull).map(Integer::parseInt).collect(Collectors.toList()), servicesId);
        }catch (IllegalArgumentException ill){
            return new com.sandu.commons.ResponseEnvelope(false,ill.getMessage());
        }catch (RuntimeException run){
            return new com.sandu.commons.ResponseEnvelope(false,run.getMessage());
        }catch (Exception e){
            log.error("系统异常",e);
            return new com.sandu.commons.ResponseEnvelope(false,"批量修改套餐异常");
        }
        return new com.sandu.commons.ResponseEnvelope(true,"修改成功!!!");
    }


    private void buildReturnData(Map<String, Object> dataMap, SysUser userInfo) {
        dataMap.put("userInfo", userInfo);

        setUserEffectiveTime(userInfo, dataMap);

        //获取用户的套餐
        if (Objects.nonNull(userInfo.getServicesFlag())) {
            //查询用户套餐返回
            Map<String, Object> serviceInfo = sysUserService.getPackServicesName(userInfo.getId());
            dataMap.put("serviceInfo", serviceInfo);
        }

        //转化用户的省市区信息
        String addressName = getUserAreaInfo(userInfo);
        dataMap.put("addressName", addressName);

        //转换用户类型
        SysDictionary sysDictionary = sysDictionaryService.getSysDictionary(USER_TYPE, userInfo.getUserType());
        dataMap.put("userTypeName", Objects.nonNull(sysDictionary) ? sysDictionary.getName() : "未知用户类型");

        //用户来源
        SysDictionary userResource = sysDictionaryService.getSysDictionary(USER_RESOURCE, userInfo.getUserSource());
        dataMap.put("userResourceName", Objects.nonNull(userResource) ? userResource.getName() : "未知来源");
    }

    private void setUserEffectiveTime(SysUser userInfo, Map<String, Object> dataMap) {

        if (Objects.equals(userInfo.getServicesFlag(), 1)) {
            //获取套餐类型信息
            Map<String, Object> map = sysUserManageService.getPackageAccountTime(userInfo.getId());
            //将套餐的失效时间同步到这个字段 =>{} 为了兼容老数据
            userInfo.setFailureTime((Date) map.get("failureTime"));
           // dataMap.put("vaildTime", map.get("vaildTime"));
        }
        dataMap.put("vaildTime", dealValidTime(userInfo));
//        else {
//            //用户时长
//            dataMap.put("vaildTime", dealValidTime(userInfo));
//            if (Objects.nonNull(userInfo.getUseType())) {
//                if (Objects.equals(0, userInfo.getValidTime())) {
//                    dataMap.put("vaildTime", userInfo.getValidTime() + "天");
//                } else {
//                    dataMap.put("vaildTime", userInfo.getValidTime() + "月");
//                }
//            }
//        }
    }


    /**
     * update by whl
     * 处理有效时间显示,查询SysUserOperatorLog表后，集合不为空则根据存储时间类型计算时间，计算有效时间
     * @param sysUser
     * @return
     */
    private String dealValidTime (SysUser sysUser){
        List<SysUserOperatorLog> logList = sysUserManageService.getOperatorLog(sysUser.getId());
        log.info("处理有效时间：useType{}"+sysUser.getUseType()+"validTime{}"+sysUser.getValidTime());
        Integer year=0;
        Integer month=0;
        Integer day=0;
        boolean flag=false;//标记位，记录中存在转正式则为true
        if (logList!=null&&logList.size()>0){//循环取出记录中的时间，根据时间类型不同分开计算
            log.debug("用户对于的操作日志数据：{}"+logList.toString());
            for (SysUserOperatorLog operatorLog:logList){
                String value = operatorLog.getValue();
                if (StringUtils.isNotBlank(value)){
                    if (value.contains("Y")){
                        String[] ms = value.split("Y");
                        year = year + Integer.valueOf(ms[0]);
                        int size = ms.length;
                        if(size > 1){
                            value = ms[1];
                        }
                    }
                    if (value.contains("M")){
                        String[] ms = value.split("M");
                        month=month+Integer.valueOf(ms[0]);
                        int size = ms.length;
                        if(size > 1) {
                            value = ms[1];
                        }
                    }
                    if (value.contains("D")){
                        String[] ms = value.split("D");
                        day=day+Integer.valueOf(ms[0]);
                    }
                }
                //如果有转正式记录则标记为true
                if (101==operatorLog.getEventCode()){
                    flag=true;
                }
            }
        }
        /**
         * 将log 表中时间处理完后，还需要处理sys_user表中原来的有效时间
         */
        //如果flag为true，说明用户由试用转正式，原来存在sys_user表中的有效时间以天为单位，在log表中可能会有转正式时候的记录和延长时长时的记录
        if(flag){
            if(null!=sysUser.getValidTime()){
                day=day+sysUser.getValidTime();
            }
        }else{
            if(USETYPETRYOUT.equals(sysUser.getUseType())&&null!=sysUser.getValidTime()){//log表中记录的都是试用用户延长有效时间记录，单位是“天”
                day=day+sysUser.getValidTime();
            }else if(USETYPEFORMAL.equals(sysUser.getUseType())&&null!=sysUser.getValidTime()){ //log表中记录的都是正式用户延长有效时间记录，单位是“月”
                month=month+sysUser.getValidTime();
            }
        }
        Date date=new Date(System.currentTimeMillis());
        Date date1 = DateUtils.dateAfterYear(date, year);//1.计算当前时间后多少年时间
        Date date2 = DateUtils.dateAfterMonth(date1, month);//2.计算在1的基础上加多少月的时间
        Date date3 = DateUtils.dateAfterDay(date2, day);//3.在2的基础上加多少天的时间
        String validTimeStr = DateUtils.getYearMonthDayFromDateToDate(date, date3);//计算当前时间到某个时间点之间的年月日
        return  validTimeStr;
    }

    private String getUserAreaInfo(SysUser userInfo) {
        StringBuilder sb = new StringBuilder();
        if (Optional.ofNullable(userInfo.getProvinceCode()).isPresent()) {
            String provinceName = baseAreaService.selectByAreaCode(userInfo.getProvinceCode());
            if (StringUtils.isNotEmpty(provinceName)) {
                sb.append(provinceName);
            }
        }

        if (Optional.ofNullable(userInfo.getCityCode()).isPresent()) {
            String cityName = baseAreaService.selectByAreaCode(userInfo.getCityCode());
            if (StringUtils.isNotEmpty(cityName)) {
                sb.append(cityName);
            }

        }

        if (Optional.ofNullable(userInfo.getAreaCode()).isPresent()) {
            String areaName = baseAreaService.selectByAreaCode(userInfo.getAreaCode());
            if (StringUtils.isNotEmpty(areaName)) {
                sb.append(areaName);
            }
        }

        if (Optional.ofNullable(userInfo.getStreetCode()).isPresent()) {
            String streetName = baseAreaService.selectByAreaCode(userInfo.getStreetCode());
            if (StringUtils.isNotEmpty(streetName)) {
                sb.append(streetName);
            }
        }

        return sb.toString();
    }


    @RequestMapping(value = "/getCreatUserType")
    public Object getCompanyCreatUserType(@RequestParam(value = "companyType", required = false) Integer companyType, @RequestParam(value = "companyId") Integer companyId) {

        //Assert.notNull(companyType, "公司类型不能为空");

        Assert.notNull(companyId, "公司id不能为空");

        try {
            //兼容公司列表进入编辑用户页面
            if (Objects.isNull(companyType)) {
                BaseCompany b = baseCompanyService.queryById(Long.parseLong(companyId + ""));
                companyType = b.getBusinessType();
            }
            SysDictionary sysDictionary = sysDictionaryService.getSysDictionary(BRAND_BUSINESS_TYPE, companyType);

            if (Objects.nonNull(sysDictionary)) {
                List<SysDictionary> lists = sysDictionaryService.getListByType(sysDictionary.getValuekey());
                if (Objects.nonNull(lists) && !lists.isEmpty()) {
                    //产商跟三度空间需要做特殊处理
                    List<SysDictionary> userTypes = filterUserType(lists, companyType, companyId);
                    return new ResponseEnvelope<>(true, userTypes);
                }
            }
            return new ResponseEnvelope<>(false, Collections.EMPTY_LIST);
        } catch (IllegalArgumentException ill) {
            return new ResponseEnvelope<>(false, ill.getMessage());
        } catch (Exception e) {
            log.error("系统错误", e);
            return new ResponseEnvelope<>(false, "系统错误");
        }
    }

    @RequestMapping(value = "/companyList")
    public Object getCompanyList(String companyName) {
        try {
            List<CompanyInfoBO> lists = baseCompanyService.queryCompany(companyName);
            return new ResponseEnvelope<>(true, lists);
        } catch (Exception e) {
            log.error("系统错误", e);
            return new ResponseEnvelope<>(false, "系统错误");
        }
    }

    /**
     * 获取用户权限
     *
     * @param userId
     * @return
     */
    @GetMapping("/role/get")
    public Object getUserRole(Integer userId) {
        if (userId == null) {
            log.error("获取用户角色：参数 userId(Integer) 不能为空！");
            return new ResponseEnvelope<>(false, "参数 userId(Integer) 不能为空！");
        }

        try {
            List<UserRoleDetailVO> listUserRole = sysRoleGroupService.getUserRole(userId);
            return new ResponseEnvelope<>(true, listUserRole);
        } catch (Exception e) {
            log.error("获取用户角色：获取用户角色组异常", e);
            return new ResponseEnvelope<>(false, ERR_MSG);
        }
    }

    /**
     * 获取用户权限
     *
     * @param update
     * @return
     */
    @PostMapping("/role/update")
    @RequiresPermissions({"manufacturer.user.role.update", "dealer.user.role.update", "user.manage.role.update"})
    public Object updateUserRole(@RequestBody RoleUpdate update) {
        if (update.getUserId() == null) {
            log.error("更新用户角色：参数 userId(Integer) 不能为空！ ");
            return new ResponseEnvelope<>(false, "参数 userId(Integer) 不能为空！");
        }

//        if (StringUtils.isEmpty(roleIds)) {
//            log.error("更新用户角色：参数 roleIds(String) 不能为空！ ");
//            return new ResponseEnvelope<>(false, "参数 roleIds(String) 不能为空！");
//        }

        try {
            LoginUser loginUser = LoginContext.getLoginUser(LoginUser.class);
            sysRoleGroupService.updateUserRole(update, loginUser);

            return new ResponseEnvelope<>(true, "更新用户角色成功");
        } catch (Exception e) {
            log.error("更新用户角色：更新用户角色异常", e);
            return new ResponseEnvelope<>(false, getErrorMessage(e));
        }
    }

    /**
     * 角色组列表
     *
     * @param userId
     * @return
     */
    @GetMapping("/role/group/get")
    public Object getUserRoleGroup(Integer userId) {
        try {
            if (userId == null) {
                log.error("获取用户角色组：参数 userId(Integer) 不能为空！ ");
                return new ResponseEnvelope<>(false, "参数 userId(Integer) 不能为空！");
            }

            UserRoleGroupDetailVO userRoleGroupDetail = sysRoleGroupService.getUserRoleGroup(userId);
            return new ResponseEnvelope<>(true, userRoleGroupDetail);
        } catch (Exception e) {
            log.error("获取用户角色组：获取用户角色组异常", e);
            return new ResponseEnvelope<>(false, ERR_MSG);
        }
    }

    /**
     * 角色组列表
     *
     * @param type
     * @return
     */
    @GetMapping("/role/group/list")
    @RequiresPermissions({"manufacturer.user.rolegroup.view", "dealer.user.rolegroup.view", "company.user.rolegroup.view"})
    public Object listRoleGroup(Integer type) {
        try {
            if (type == null) {
                log.error("获取角色组列表：参数 type(Integer) 不能为空！ ");
                return new ResponseEnvelope<>(false, "参数 type(Integer) 不能为空！");
            }

            List<RoleGroupVO> listRoleGroup = sysRoleGroupService.listRoleGroup(type);
            return new ResponseEnvelope<>(true, listRoleGroup);
        } catch (Exception e) {
            log.error("获取角色组列表：获取角色组列表异常", e);
            return new ResponseEnvelope<>(false, ERR_MSG);
        }
    }

    /**
     * 更新用户角色组
     *
     * @return
     */
    @PostMapping("/role/group/update")
    @RequiresPermissions({"manufacturer.user.rolegroup.update", "dealer.user.rolegroup.update", "company.user.rolegroup.update"})
    public Object updateUserRoleGroup(@RequestBody RoleGroupUpdate roleGroup) {
        try {
            if (roleGroup.getUserId() == null || roleGroup.getUserId() <= 0) {
                log.error("更新用户角色组：参数 userId(Integer) 不能为空！ ");
                return new ResponseEnvelope<>(false, "参数 userId(Integer) 不能为空！");
            }

            // roleGroups = null or [] 时表示要取消所有的权限
            roleGroup.setRoleGroups((roleGroup.getRoleGroups() == null) ? new ArrayList<>(0) : roleGroup.getRoleGroups());
            LoginUser loginUser = LoginContext.getLoginUser(LoginUser.class);
            sysRoleGroupService.updateUserRoleGroup(roleGroup, loginUser);

            return new ResponseEnvelope<>(true, "更新用户角色组成功");
        } catch (Exception e) {
            log.error("更新用户角色组：更新用户角色组异常", e);
            return new ResponseEnvelope<>(false, getErrorMessage(e));
        }
    }

    /**
     * 获取平台列表
     *
     * @return
     */
    @GetMapping("/platform/list")
    @RequiresPermissions({"manufacturer.user.platform.view", "dealer.user.platform.view", "company.user.platform.view"})
    public Object listPlatform(Integer userId) {
        try {
            if (userId == null || userId <= 0) {
                log.error("更新用户平台：参数 userId(Integer) 不能为空！ ");
                return new ResponseEnvelope<>(false, "参数 userId(Integer) 不能为空！");
            }

            List<BasePlatformVO> listPlatform = basePlatformService.listPlatform(userId);
            return new ResponseEnvelope<>(true, listPlatform);
        } catch (Exception e) {
            log.error("更新用户平台：更新用户平台异常", e);
            return new ResponseEnvelope<>(false, getErrorMessage(e));
        }
    }

    /**
     * 获取用户已被授权的平台列表
     *
     * @return
     */
    @GetMapping("/platform/get")
    public Object getUserPlatform(Integer userId) {
        try {
            if (userId == null || userId <= 0) {
                log.error("获取用户平台：参数 userId(Integer) 不能为空！ ");
                return new ResponseEnvelope<>(false, "参数 userId(Integer) 不能为空！");
            }

            UserPlatformDetailVO userPlatformDetail = basePlatformService.getUserPlatform(userId);
            return new ResponseEnvelope<>(true, userPlatformDetail);
        } catch (Exception e) {
            log.error("获取用户平台：获取用户平台异常", e);
            return new ResponseEnvelope<>(false, ERR_MSG);
        }
    }

    /**
     * 更新用户平台信息
     *
     * @return
     */
    @PostMapping("/platform/update")
    @RequiresPermissions({"manufacturer.user.platform.update", "dealer.user.platform.update", "company.user.platform.update"})
    public Object updateUserPlatform(@RequestBody BasePlatformUpdate platform) {
        try {
            if (platform.getUserId() == null || platform.getUserId() <= 0) {
                log.error("更新用户平台：参数 userId(Integer) 不能为空！ ");
                return new ResponseEnvelope<>(false, "参数 userId(Integer) 不能为空！");
            }

            // platforms = null or [] 时表示要取消所有的权限
            platform.setPlatforms((platform.getPlatforms() == null) ? new ArrayList<>(0) : platform.getPlatforms());
            LoginUser loginUser = LoginContext.getLoginUser(LoginUser.class);
            basePlatformService.updateUserPlatform(platform, loginUser);

            return new ResponseEnvelope<>(true, "请求成功！");
        } catch (Exception e) {
            log.error("更新用户平台：更新用户平台异常", e);
            return new ResponseEnvelope<>(false, getErrorMessage(e));
        }
    }

    /**
     * 获取用户来源接口
     *
     * @return
     */
    @GetMapping(value = "/getUserResource")
    public Object getUserResource() {
        try {
            List<SysDictionary> userResources = sysDictionaryService.getListByType(USER_RESOURCE);
            return new ResponseEnvelope<>(true, userResources);
        } catch (Exception e) {
            log.error("系统错误", e);
            return new ResponseEnvelope<>(false, "系统错误");
        }
    }


    @RequestMapping(value = "/getFranchiserCompany")
    public Object getFranchiserCompany(@RequestParam(value = "companyId") Long companyId) {

        Assert.notNull(companyId, "厂商id不能为空");

        try {
            List<CompanyInfoBO> franchiserCompany = baseCompanyService.getFranchiserCompany(companyId);
            return new ResponseEnvelope<>(true, franchiserCompany);
        } catch (IllegalArgumentException ill) {
            return new ResponseEnvelope<>(false, ill.getMessage());
        } catch (Exception e) {
            log.error("系统错误", e);
            return new ResponseEnvelope<>(false, "系统错误");
        }
    }

    @GetMapping("/getLoginUserAdminPlatform")
    public Object getLoginUserAdminPlatform(@RequestParam(value = "account") String account,
                                            @RequestParam(value = "password") String password
    ) {

        Assert.notNull(account, "用户账号不能为空");
        Assert.notNull(account, "用户密码不能为空");

        try {

            SysUser bUser = sysUserManageService.get2BUser(account, password, null);
            Assert.notNull(bUser, "用户名或密码错误");

            List<BasePlatform> platforms = sysUserService.getLoginUserAdminPlatform(bUser.getId());
            return new ResponseEnvelope<>(true, platforms);
        } catch (IllegalArgumentException ill) {
            return new ResponseEnvelope<>(false, ill.getMessage());
        } catch (Exception e) {
            log.error("系统错误", e);
            return new ResponseEnvelope<>(false, "系统错误");
        }
    }

    @RequestMapping("/restoreData")
    public Object restoreSysUserData(){
        try{
            sysUserManageService.restoreSysUserData();
            return new ResponseEnvelope<>(true,"修复完成");
        }catch (Exception e){
            log.error("修复数据异常!!!");
            return new ResponseEnvelope<>(false,"修复数据异常");
        }
    }


    @PostMapping(value = "setMSType")
    public Object setMSType(@ModelAttribute @Valid UserMSTypeSet userMSTypeSet, BindingResult validResult){

        if (validResult.hasFieldErrors()) {
            return new ResponseEnvelope(false, validResult.getFieldError().getDefaultMessage());
        }

        if (UserMSTypeSet.msTypeEnum.addSon.name().equals(userMSTypeSet.getMsType())) {
            if (null == userMSTypeSet.getMasterUserId()) {
                return new ResponseEnvelope<>(false, "主账号参数不能为空");
            }
        }
        LoginUser loginUser = LoginContext.getLoginUser(LoginUser.class);
        if (null == loginUser) {
            return new ResponseEnvelope<>(false, "请重新登录");
        }

        try {

            sysUserManageService.setUpMSType(userMSTypeSet,loginUser);
        } catch (BizException e){
            return new ResponseEnvelope<>(false, e.getMessage());
        }

        return new ResponseEnvelope<>(false, "操作成功");
    }

    @PostMapping(value = "companySonUserList")
    public Object companySonUserList(@ModelAttribute @Valid UserMasterSonSearch userMasterSonSearch, BindingResult validResult){

        if (validResult.hasFieldErrors()) {
            return new ResponseEnvelope(false, validResult.getFieldError().getDefaultMessage());
        }
        if (null == userMasterSonSearch.getMasterUserId()) {
            return new ResponseEnvelope<>(false, "主账号参数不能为空");
        }
        SysUser sysUser = sysUserService.get(userMasterSonSearch.getMasterUserId());
        if (null == sysUser) {
            return new ResponseEnvelope<>(false, "当前账号不存在");
        } else {
            if ( 2 != sysUser.getMasterSonType()) {
                return new ResponseEnvelope<>(false, "当前账号非主账号");
            }

            if (null == sysUser.getCompanyId()) {
                return new ResponseEnvelope<>(false, "当前账号企业为空");
            }
        }
        userMasterSonSearch.setCompanyId(sysUser.getCompanyId().intValue());

        // 获取子账号列表
        PageInfo<UserMasterSonVO>  companyMasterSonVOList = sysUserManageService.companySonUserList(userMasterSonSearch);

        // 数据转换
        Map<Integer, String> typeMap = getUserTypeMap();
        if (null != companyMasterSonVOList.getList() && companyMasterSonVOList.getList().size() > 0) {

            companyMasterSonVOList.getList().stream().forEach(o -> {
                o.setUserTypeName(typeMap.get(o.getUserType()));
            });
        }

        return new ResponseEnvelope<>(true, "子账号列表获取成功",null,null,companyMasterSonVOList.getTotal(),companyMasterSonVOList.getList());
    }

    @PostMapping(value = "masterSonUserList")
    public Object masterSonUserList(@ModelAttribute @Valid UserMasterSonSearch userMasterSonSearch, BindingResult validResult,HttpServletRequest request){

        if (validResult.hasFieldErrors()) {
            return new ResponseEnvelope(false, validResult.getFieldError().getDefaultMessage());
        }

        LoginUser loginUser = LoginContext.getLoginUser(LoginUser.class);
        if (null == loginUser) {
            return new ResponseEnvelope<>(false, "请重新登录");
        }
        // 获取平台
        String platformCode = request.getHeader("Platform-Code").toString();
        if ("merchantManage".equals(platformCode)) {

            userMasterSonSearch.setMasterUserId(loginUser.getId().intValue());
        } else {

            if (null == userMasterSonSearch.getMasterUserId()) {
                return new ResponseEnvelope<>(false, "主账号参数为空");
            }
        }
        SysUser sysUser = sysUserService.get(userMasterSonSearch.getMasterUserId());
        if (2 != sysUser.getMasterSonType()) {
            return new ResponseEnvelope<>(false, "当前账号非主账号");
        }

        // 获取子账号列表
        PageInfo<UserMasterSonVO>  userMasterSonVOList = sysUserManageService.masterSonUserList(userMasterSonSearch);

        // 数据转换
        Map<Integer, String> typeMap = getUserTypeMap();
        if (null != userMasterSonVOList.getList() && userMasterSonVOList.getList().size() > 0) {

            userMasterSonVOList.getList().stream().forEach(o -> {
                o.setUserTypeName(typeMap.get(o.getUserType()));
            });
        }

        return new ResponseEnvelope<>(true, "子账号列表获取成功",null,null,userMasterSonVOList.getTotal(),userMasterSonVOList.getList());
    }

    @PostMapping(value = "queryMaster")
    public Object queryMaster(Integer userId){

        if (null == userId) {
            return new ResponseEnvelope<>(false, "参数为空");
        }

        Integer masterUserId = sysUserService.getMasterUserId(userId);
        if (null == masterUserId) {
            return new ResponseEnvelope<>(false, "该用户没有主账号");
        }

        SysUser sysUser = sysUserService.get(masterUserId);

        return new ResponseEnvelope<>(true, "主账号获取成功",sysUser.getNickName());
    }


    private String getErrorMessage(Exception ex) {
        return StringUtils.isEmpty(ex.getMessage()) ? ERR_MSG
                : (ex.getMessage().startsWith("Error")
                ? ex.getMessage().replace("Error:", "") : ERR_MSG);
    }

    private List<SysDictionary> filterUserType(List<SysDictionary> lists, Integer type, Integer sanduFlag) {
        List<Integer> userTypeValue;
        if (Objects.equals(sanduFlag, 2501)) {
            return sysDictionaryService.getListByType(USER_TYPE);
        } else if (Objects.equals(1, type)) {
            //删除经销商
            userTypeValue = lists.stream().filter(f -> {
                return Integer.parseInt(f.getAtt1()) != 3;
            }).map(item -> Integer.parseInt(item.getAtt1())).collect(Collectors.toList());
            return sysDictionaryService.getListByTypeAndValues(USER_TYPE, userTypeValue);
        } else {
            userTypeValue = lists.stream().map(item -> Integer.parseInt(item.getAtt1())).collect(Collectors.toList());
            return sysDictionaryService.getListByTypeAndValues(USER_TYPE, userTypeValue);
        }
    }

    private List<UserManageVO> transformReturnData(List<UserManageDTO> userList) {
        Map<Integer, String> typeMap = getUserTypeMap();
        if (Objects.nonNull(userList) && !userList.isEmpty()) {
            return userList.stream().map(user -> {
                //转化输出对象
                UserManageVO vo = transformVO(user);
                //计算账号有效时长
                calculateAccoutEffectiveTime(vo);
                //转换账号类型
                transformAccountType(vo);
                //设置用户类型返回
                vo.setType(typeMap.get(vo.getUserType()));
                return vo;
            }).collect(Collectors.toList());
        }
        return Collections.EMPTY_LIST;
    }

    private UserManageService getUserManageServiceImpl(String userMethod) {
        UserManageService userManageService = SpringContextHolder.getBean(userMethod);
        Assert.notNull(userManageService, "userMethod 错误");
        return userManageService;
    }

    private void transformAccountType(UserManageVO vo) {
        vo.setAccountType(Objects.equals(vo.getUseType(), 1) ? OLD_ACCOUNT_TYPE_FORMAL : OLD_ACCOUNT_TYPE_TRY);
//        if (Objects.equals(vo.getServicesFlag(), 1)) {
//            //套餐用户
//            switch (vo.getBusinessType()) {
//                case 0:
//                    vo.setAccountType(PACKAGE_ACCOUNT_TYPE_BUY);
//                    break;
//                case 1:
//                    vo.setAccountType(PACKAGE_ACCOUNT_TYPE_RENEWAL_FEE);
//                    break;
//                case 2:
//                    vo.setAccountType(PACKAGE_ACCOUNT_TYPE_TRY);
//                    break;
//                case 3:
//                    vo.setAccountType(PACKAGE_ACCOUNT_TYPE_PURCHASING);
//                    break;
//                case 4:
//                    vo.setAccountType(PACKAGE_ACCOUNT_TYPE_RISE);
//                    break;
//                default:
//                    vo.setAccountType("未知账号类型");
//                    break;
//            }
//        } else {
//            switch (vo.getUseType()) {
//                case 0:
//                    vo.setAccountType(OLD_ACCOUNT_TYPE_TRY);
//                    break;
//                case 1:
//                    vo.setAccountType(OLD_ACCOUNT_TYPE_FORMAL);
//                    break;
//                default:
//                    vo.setAccountType("未知账号类型");
//                    break;
//            }
//        }
    }

    private UserManageVO transformVO(UserManageDTO user) {
        UserManageVO vo = new UserManageVO();
        BeanUtils.copyProperties(user, vo);
        return vo;
    }

    private Map<Integer, String> getUserTypeMap() {
        List<SysDictionary> userTypes = sysDictionaryService.getListByType(USER_TYPE);
        return userTypes.stream().collect(Collectors.toMap(SysDictionary::getValue, SysDictionary::getName));
    }

    private void calculateAccoutEffectiveTime(UserManageVO vo) {
        SysUser sysUser = sysUserService.get(vo.getId().intValue());
        if (Objects.equals(vo.getServicesFlag(), 1)) {
            //套餐用户,计算套餐时长
            //totalPackageUserEffectiveTime(vo);
            //将套餐的失效时间同步到这个字段 =>{} 为了兼容老数据
            vo.setFailureTime(vo.getEffectiveEnd());
            vo.setEffectiveTime(dealValidTime(sysUser));
        } else {
            //用户时长
            vo.setEffectiveTime(dealValidTime(sysUser));
//            if (Objects.nonNull(vo.getUseType())) {
//                if (Objects.equals(0, vo.getValidTime())) {
//                    vo.setEffectiveTime(vo.getValidTime() + "天");
//                } else {
//                    vo.setEffectiveTime(vo.getValidTime() + "月");
//                }
//            }
            //设置用户套餐名称
            vo.setServicesName(NON_PACKAGE_NAME);
        }
    }

    /**
     * 计算套餐用户的失效时长
     *
     * @param vo
     */
    private void totalPackageUserEffectiveTime(UserManageVO vo) {

        StringBuilder sb = new StringBuilder();
        if (Objects.nonNull(vo.getDuration())) {
            sb.append(vo.getDuration());
        }
        if (Objects.isNull(vo.getPriceUnit())) {
            sb.append("未知时长");
        } else {
            switch (vo.getPriceUnit()) {
                case 0:
                    //年
                    sb.append("年");
                    break;
                case 1:
                    //月
                    sb.append("月");
                    break;
                case 2:
                    //日
                    sb.append("日");
                    break;
                default:
                    sb.append("未知时长");
                    break;
            }
        }

        vo.setEffectiveTime(sb.toString());
    }


    @PostMapping("freeze/manage")
    public Object manageFreeze(@Valid @RequestBody AccountFreezeManage manage, BindingResult validResult) {
        if (validResult.hasFieldErrors()) {
            throw new IllegalArgumentException(validResult.getFieldError().getDefaultMessage());
        }
		LoginUser loginUser = LoginContext.getLoginUser(LoginUser.class);
		manage.setLoginUser(loginUser);
		Map<String, String> msgMap = sysUserManageService.mangeFreeze(manage);
		String msg = "";
		boolean flag = true;
		for (Map.Entry<String, String> entry : msgMap.entrySet()) {
			msg = msg + String.format(entry.getKey(), entry.getValue()) + ";";
			flag = false;
		}
		if (msgMap.isEmpty()) {
			msg = "操作成功。";
		}
		return new ResponseEnvelope<>(flag, msg);
    }
}