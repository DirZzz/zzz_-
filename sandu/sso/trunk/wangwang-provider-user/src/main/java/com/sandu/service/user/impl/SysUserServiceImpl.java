package com.sandu.service.user.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.sandu.api.account.model.PayAccount;
import com.sandu.api.account.service.PayAccountService;
import com.sandu.api.base.model.BaseCompany;
import com.sandu.api.base.model.BaseCompanyMiniProgramConfig;
import com.sandu.api.base.model.BasePlatform;
import com.sandu.api.base.service.BaseCompanyMiniProgramConfigService;
import com.sandu.api.base.service.BaseCompanyService;
import com.sandu.api.base.service.BasePlatformService;
import com.sandu.api.operatorLog.model.SysUserOperatorLog;
import com.sandu.api.operatorLog.service.SysUserOperatorLogService;
import com.sandu.api.redis.RedisService;
import com.sandu.api.servicepurchase.constant.ServicesPurchaseConstant;
import com.sandu.api.servicepurchase.model.ServicesAccountRel;
import com.sandu.api.servicepurchase.model.ServicesBaseInfo;
import com.sandu.api.servicepurchase.model.ServicesPrice;
import com.sandu.api.servicepurchase.model.ServicesRoleRel;
import com.sandu.api.servicepurchase.serivce.ServicesAccountRelService;
import com.sandu.api.servicepurchase.serivce.ServicesBaseInfoService;
import com.sandu.api.servicepurchase.serivce.ServicesPriceService;
import com.sandu.api.servicepurchase.serivce.ServicesRoleRelService;
import com.sandu.api.servicepurchase.serivce.biz.ServicesPurchaseBizService;
import com.sandu.api.shop.model.CompanyShop;
import com.sandu.api.shop.service.CompanyShopArticleService;
import com.sandu.api.shop.service.CompanyShopDesignPlanService;
import com.sandu.api.shop.service.CompanyShopService;
import com.sandu.api.shop.service.ProjectCaseService;
import com.sandu.api.system.model.BaseMobileArea;
import com.sandu.api.system.model.ResPic;
import com.sandu.api.system.model.SysDictionary;
import com.sandu.api.system.service.BaseMobileAreaService;
import com.sandu.api.system.service.ResPicService;
import com.sandu.api.system.service.SysDictionaryService;
import com.sandu.api.user.input.*;
import com.sandu.api.user.model.*;
import com.sandu.api.user.model.bo.CompanyInfoBO;
import com.sandu.api.user.model.bo.UserInfoBO;
import com.sandu.api.user.output.DealersUserListVO;
import com.sandu.api.user.output.InternalUserListVO;
import com.sandu.api.user.output.UserCompanyInfoVO;
import com.sandu.api.user.output.UserWillExpireVO;
import com.sandu.api.user.service.SysRoleGroupService;
import com.sandu.api.user.service.SysRoleService;
import com.sandu.api.user.service.SysUserService;
import com.sandu.api.user.service.UserJurisdictionService;
import com.sandu.api.user.service.manage.SysUserManageService;
import com.sandu.common.Md5;
import com.sandu.common.Validator;
import com.sandu.common.constant.UserConstant;
import com.sandu.common.exception.BizException;
import com.sandu.common.exception.ExceptionCodes;
import com.sandu.common.gson.GsonUtil;
import com.sandu.common.http.HttpClientUtil;
import com.sandu.common.pay.IdGenerator;
import com.sandu.common.sms.Utils;
import com.sandu.common.util.SysUserUtil;
import com.sandu.commons.MessageUtil;
import com.sandu.commons.constant.SmsConstant;
import com.sandu.config.ResourceConfig;
import com.sandu.config.SmsConfig;
import com.sandu.pay.order.metadata.PayState;
import com.sandu.pay.order.model.PayModelGroupRef;
import com.sandu.pay.order.model.PayOrder;
import com.sandu.pay.order.service.PayModelGroupRefService;
import com.sandu.pay.order.service.PayOrderService;
import com.sandu.service.user.dao.SysUserDao;
import com.sandu.service.user.dao.UserMasterSonRefDao;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.sandu.api.servicepurchase.constant.ServicesPurchaseConstant.PRICE_UNIT_YEAR;

/**
 * CopyRight (c) 2017 Sandu Technology Inc.
 * <p>
 * sandu-wangwang
 *
 * @author Yoco (yocome@gmail.com)
 * @datetime 2017/12/9 14:11
 */
@Service("sysUserService")
public class SysUserServiceImpl implements SysUserService {
    private Logger logger = LoggerFactory.getLogger(SysRoleGroupServiceImpl.class);
    private final static String CLASS_LOG_PERFIX = "[用户中心服务]:";

    private final static Gson gson = new Gson();

    @Autowired
    private SysUserDao sysUserDao;
    @Autowired
    private UserMasterSonRefDao userMasterSonRefDao;
    @Autowired
    private RedisService redisService;
    @Autowired
    private BaseCompanyService baseCompanyService;
    @Autowired
    private ResPicService resPicService;
    @Autowired
    private BasePlatformService basePlatformService;
    @Autowired
    private UserJurisdictionService userJurisdictionService;
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private PayAccountService payAccountService;
    @Autowired
    private BaseCompanyMiniProgramConfigService miniProgramConfigService;
    @Autowired
    private PayOrderService payOrderService;
    @Autowired
    private SysDictionaryService sysDictionaryService;
    @Autowired
    private ServicesPurchaseBizService servicesPurchaseBizService;
    @Autowired
    private PayModelGroupRefService payModelGroupRefService;
    @Autowired
    private BaseMobileAreaService baseMobileAreaService;
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private CompanyShopService companyShopService;

    @Autowired
    private CompanyShopArticleService companyShopArticleService;

    @Autowired
    private CompanyShopDesignPlanService companyShopDesignPlanService;

    @Autowired
    private ProjectCaseService projectCaseService;

    @Autowired
    private ServicesAccountRelService servicesAccountRelService;

    @Autowired
    private ServicesBaseInfoService servicesBaseInfoService;

    @Autowired
    private SysRoleGroupService sysRoleGroupService;

    @Autowired
    private ServicesRoleRelService servicesRoleRelService;

    @Autowired
    private SysUserOperatorLogService sysUserOperatorLogService;

    @Autowired
    private ServicesPriceService servicesPriceService;

    private static final String CUSTOMER_ACCOUNT = "customerAccount";


    public static final String REGISTER_PREFIX = "register_prefix:";

    public static final String MARK_MESSAGE = "mark_message:";

    private static final List<Integer> adminPlatforms = Arrays.asList(11,20,21);

    @Value("${upload.base.path}")
    private String ROOT_PATH;

    @Value("${upload.default.path}")
    private String PIC_DEFAULT_PATH;

    @Value("${pic.type}")
    private String PIC_TYPE;

    
	/*@Override
	public SysUser get(Integer id) {
		// TODO Auto-generated method stub
		return sysUserDao.selectByPrimaryKey(id);
	}*/

    @Override
    public int update(SysUser record) {
        // TODO Auto-generated method stub
        return sysUserDao.updateByPrimaryKeySelective(record);
    }


    @Override
    public boolean checkPhone(String phone) {
        // TODO Auto-generated method stub
        UserQuery user = new UserQuery();
        user.setMobile(phone);
        user.setIsDeleted(0);
        //根据手机号查询是否已经被注册了
        List<UserInfoBO> usInfo = sysUserDao.selectList(user);
        if (!usInfo.isEmpty()) {
            return true;
        }
        return false;
    }

    @Override
    public ResponseBo checkPhone(String phone, String code, Long userId) {
        // TODO Auto-generated method stub
        logger.info("checkPhone():" + "phone:" + phone + "code:" + code);
        ResponseBo resBo = new ResponseBo();
        //获取当前时间
        long currentTime = System.currentTimeMillis();
        long sendCodeTime = 0L;
        Integer verifyCount = 0;
        String yzm = "";
        //获取缓存中信息
        //String map = redisService.getMap(UserConstant.SESSION_SMS_CODE, phone);
        String map = (String) redisTemplate.opsForHash().get(UserConstant.SESSION_SMS_CODE, phone);
        if (StringUtils.isBlank(map)) {
            resBo.setStatus(false);
            resBo.setMsg("请先获取验证码！");
            return resBo;
        }
        SmsVo smsVo = gson.fromJson(map, SmsVo.class);
        sendCodeTime = smsVo.getSendTime();
        yzm = smsVo.getCode();
        verifyCount = smsVo.getVerifyCount();
        if (verifyCount <= 0) {//是否失效
            //redisService.delMap(UserConstant.SESSION_SMS_CODE, phone);
            redisTemplate.opsForHash().delete(UserConstant.SESSION_SMS_CODE, phone);
            resBo.setStatus(false);
            resBo.setMsg("验证码已失效，请重新获取！");
            return resBo;
        }
        if (!code.equals(yzm)) {//匹配验证码
            // verifyCount--;
            smsVo.setVerifyCount(verifyCount);
            //redisService.addMap(UserConstant.SESSION_SMS_CODE, phone, gson.toJson(smsVo));
            redisTemplate.opsForHash().put(UserConstant.SESSION_SMS_CODE, phone, gson.toJson(smsVo));
            resBo.setStatus(false);
            resBo.setMsg("验证码不对，请重新输入！");
            return resBo;
        }
        if ((currentTime - sendCodeTime) > Utils.VALID_TIME) {//是否超时 默认3分钟
            resBo.setStatus(false);
            resBo.setMsg("验证码已超时，请重新获取！");
            return resBo;
        }


//		UserQuery user = new UserQuery();
        //user.setMobile(phone);
        //此时应该需要根据用户id去查询信息
        //List<UserInfoBO> usInfo = sysUserDao.selectList(user);
        SysUser sysUser = sysUserDao.selectByPrimaryKey(userId.intValue());
        //	 if (!sysUser.isEmpty()) {
        if (null != sysUser) {
            // SysUser user1 = usInfo.get(0);
            //修改手机号
            sysUser.setMobile(phone);
            this.update(sysUser);
            //redisService.delMap(UserConstant.SESSION_SMS_CODE, phone);
            redisTemplate.opsForHash().delete(UserConstant.SESSION_SMS_CODE, phone);
            resBo.setStatus(true);
            resBo.setMsg("修改手机号成功");
            return resBo;
        } else {
            resBo.setStatus(false);
            logger.info("[用户中心服务]:通过手机号查询用户信息为空：when type is 'register', this list is null ");
        }

        return resBo;
    }

    @Override
    public ResponseBo checkPhone(String phone, String code) {
        // TODO Auto-generated method stub
        logger.info("checkPhone():" + "phone:" + phone + "code:" + code);
        ResponseBo resBo = new ResponseBo();
        //获取当前时间
        long currentTime = System.currentTimeMillis();
        long sendCodeTime = 0L;
        Integer verifyCount = 0;
        String yzm = "";
        //获取缓存中信息
        //String map = redisService.getMap(UserConstant.SESSION_SMS_CODE, phone);

        String map = (String) redisTemplate.opsForHash().get(UserConstant.SESSION_SMS_CODE, phone);
        if (StringUtils.isBlank(map)) {
            resBo.setStatus(false);
            resBo.setMsg("请先获取验证码！");
            return resBo;
        }
        SmsVo smsVo = gson.fromJson(map, SmsVo.class);
        sendCodeTime = smsVo.getSendTime();
        yzm = smsVo.getCode();
        verifyCount = smsVo.getVerifyCount();
        if (verifyCount <= 0) {//是否失效
            //redisService.delMap(UserConstant.SESSION_SMS_CODE, phone);
            redisTemplate.opsForHash().delete(UserConstant.SESSION_SMS_CODE, phone);
            resBo.setStatus(false);
            resBo.setMsg("验证码已失效，请重新获取！");
            return resBo;
        }
        if (!code.equals(yzm)) {//匹配验证码
            // verifyCount--;
            smsVo.setVerifyCount(verifyCount);
            redisTemplate.opsForHash().put(UserConstant.SESSION_SMS_CODE, phone, gson.toJson(smsVo));
            resBo.setStatus(false);
            resBo.setMsg("验证码输入有误！");
            return resBo;
        }
        if ((currentTime - sendCodeTime) > Utils.VALID_TIME) {//是否超时 默认3分钟
            resBo.setStatus(false);
            resBo.setMsg("验证码已超时，请重新获取！");
            return resBo;
        }
        resBo.setStatus(true);
        return resBo;
    }

    @Override
    public Boolean sendMessage(String mobile) {
        String message = "";
        String code = Utils.generateRandomDigitString(6);
        logger.info("[用户中心服务]:发送短信验证码---验证码类型:{}, 手机号:{}, 验证码:{}", mobile, code);
        try {

            /* message = MessageFormat.format(Utils.app.getString("updateMobileContext"), code,
            		 Utils.VALID_TIME / 60000, Utils.SERVICE_PHONE);*/
            message = MessageFormat.format(SmsConfig.UPDATEMOBILECONTEXT, code,
                    Utils.VALID_TIME / 60000, Utils.SERVICE_PHONE);
            message = URLEncoder.encode(message, "UTF-8");

            long seqId = System.currentTimeMillis();
            String params = "phone=" + mobile + "&message=" + message + "&addserial=&seqid=" + seqId;
            String result = Utils.sendSMS(Utils.SEND_MESSAGE, params);
            if ("1".equals(result)) {
                return false;
            }
            SmsVo smsVo = new SmsVo();
            smsVo.setCode(code);
            smsVo.setSendTime(seqId);
            smsVo.setVerifyCount(3);
            //添加到缓存中
            //redisService.addMap(UserConstant.SESSION_SMS_CODE, mobile, gson.toJson(smsVo));
            redisTemplate.opsForHash().put(UserConstant.SESSION_SMS_CODE, mobile, gson.toJson(smsVo));
        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();
            return false;
        }
        return true;
    }


    @Override
    public Boolean sendMessage(String mobile, String message) {
        logger.info("[用户中心服务]:发送短信验证码-- 手机号:{}, 消息:{}", mobile, message);
        try {
            message = URLEncoder.encode(message, "UTF-8");
            long seqId = System.currentTimeMillis();
            String params = "phone=" + mobile + "&message=" + message + "&addserial=&seqid=" + seqId;
            String result = Utils.sendSMS(Utils.SEND_MESSAGE, params);
            logger.info("[用户中心服务]:发送短信-- 结果:{}", result);
            if ("1".equals(result)) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    @Override
    public void saveCode(String sysCode, String userId, String verifyCode) {
        // TODO Auto-generated method stub
        redisService.addMap(sysCode, userId, verifyCode);
    }

    @Override
    public void delCode(String sysCode, String userId) {
        // TODO Auto-generated method stub
        redisService.delMap(sysCode, userId);
    }

    @Override
    public String getCode(String sysCode, String userId) {
        // TODO Auto-generated method stub
        return redisService.getMap(sysCode, userId);
    }

    @Override
    public ResponseBo checkImgCode(String sysCode, String userId, String imgCode) {
        // TODO Auto-generated method stub
        logger.info("checkPhone():" + "sysCode:" + sysCode + "userId:" + userId + "imgCode:" + imgCode);
        ResponseBo resBo = new ResponseBo();
        //获取缓存中信息
        String map = redisService.getMap(sysCode, userId);
        if (null != map) {
            if (map.equals(imgCode)) {
                resBo.setStatus(true);
            } else {
                resBo.setStatus(false);
            }
        } else {
            resBo.setStatus(false);
        }

        return resBo;
    }

    @Override
    public SysUser get(Integer id) {
        // TODO Auto-generated method stub
        return sysUserDao.selectByPrimaryKey(id);
    }

    @Override
    public String getOpenid(String appid, String code) {
        BaseCompanyMiniProgramConfig config = miniProgramConfigService.getMiniProgramConfig(appid);
        if (config == null || StringUtils.isBlank(config.getAppSecret())) {
            logger.error("appid错误或者服务器未配置secret:" + appid);
            throw new BizException(ExceptionCodes.CODE_10010033, "appid错误或者服务器未配置secret");
        }

        String url = ResourceConfig.MINI_PROGRAM_GETOPENDID_URL + "?appid=" + appid
                + "&secret=" + config.getAppSecret() + "&js_code=" + code + "&grant_type=authorization_code";

        for (int i = 0; i < 3; i++) {
            String resultStr = HttpClientUtil.doGetMethod(url);
            Map<String, Object> map = GsonUtil.fromJson(resultStr, Map.class);
            if (map != null) {
                String openid = (String) map.get("openid");
                if (StringUtils.isBlank(openid)) {
                    logger.warn("获取openId异常,第" + (i + 1) + "次:appid(" + appid + "),code(" + code + "):" + resultStr);
                } else {
                    return openid;
                }
            } else {
                logger.warn("获取openId异常map,第" + (i + 1) + "次:appid(" + appid + "),code(" + code + "):" + resultStr);
            }

        }
        return null;
    }

    @Override
    public List<CompanyInfoBO> getFranchiserCompanyList(String account, String password, String platformCode, int isForLogin) {
        //如果不是手机号,则需要通过呢称查找手机号
        if (!Validator.MOBILE.validate(account)) {
            UserQuery userQuery = new UserQuery();
            userQuery.setNickName(account);
            userQuery.setPassword(password);
            userQuery.setIsDeleted(0);
            List<UserInfoBO> list = sysUserDao.selectList(userQuery);
            if (list != null && list.size() > 0) {
                account = list.get(0).getMobile();
            }
        }

        if (Validator.MOBILE.validate(account)) {
            //如果一个经销商多个企业,则返回登录企业列表
            List<CompanyInfoBO> companyList = baseCompanyService.queryFranchiserCompanys(account, password, platformCode, isForLogin);
            //查找logo地址
            if (companyList != null && companyList.size() > 0) {
                List<Long> logos = new ArrayList<Long>();
                for (CompanyInfoBO companyInfo : companyList) {
                    if (companyInfo.getLogo() != null) {
                        logos.add(companyInfo.getLogo());
                    }
                }
                if (logos.size() > 0) {
                    List<ResPic> resPics = resPicService.getByIds(logos);
                    if (resPics != null && resPics.size() > 0) {
                        for (ResPic pic : resPics) {
                            for (CompanyInfoBO companyInfo : companyList) {
                                if (companyInfo.getLogo() != null && companyInfo.getLogo().longValue() == pic.getId().longValue()) {
                                    companyInfo.setLogoPath(pic.getPicPath());
                                    break;
                                }
                            }

                        }
                    }
                }
            }
            return companyList;
        }
        return null;
    }

    @Override
    public int selectMobileByMobile(String mobile) {
        // TODO Auto-generated method stub
        return sysUserDao.selectMobileByMobile(mobile);
    }

    @Override
    public int updateByPhone(SysUser mobile) {
        // TODO Auto-generated method stub
        return sysUserDao.updateByPhone(mobile);
    }

    @Override
    public int updateMobileByUserId(SysUser sysUser) {
        // TODO Auto-generated method stub
        return sysUserDao.updateMobileByUserId(sysUser);
    }

    @Override
    public int selectMobileByUserInfo(String mobile) {
        // TODO Auto-generated method stub
        return sysUserDao.selectMobileByUserInfo(mobile);
    }

    @Override
    public List<CompanyInfoBO> selectCompanySumByMobile(String mobile) {
        // TODO Auto-generated method stub
        return sysUserDao.selectCompanySumByMobile(mobile);
    }

    @Override
    public List<CompanyInfoBO> selectCompanyUserSumByMobile(String mobile) {
        // TODO Auto-generated method stub
        return sysUserDao.selectCompanyUserSumByMobile(mobile);
    }

    @Override
    public List<CompanyInfoBO> selectCompanyMobileByMobile(String mobile) {
        // TODO Auto-generated method stub
        return sysUserDao.selectCompanyMobileByMobile(mobile);
    }

    @Override
    public int selectCompanyMobileCountByMobile(String mobile) {
        // TODO Auto-generated method stub
        return sysUserDao.selectCompanyMobileCountByMobile(mobile);
    }

    @Override
    public SysUser selectMobileById(Long id) {
        // TODO Auto-generated method stub
        return sysUserDao.selectMobileById(id);
    }

    @Override
    public SysUser selectMobileInfoByMobile(String mobile) {
        // TODO Auto-generated method stub
        return sysUserDao.selectMobileInfoByMobile(mobile);
    }

    @Override
    public List<CompanyInfoBO> selectCompanyIdAndMobileById(Long id) {
        // TODO Auto-generated method stub
        return sysUserDao.selectCompanyIdAndMobileById(id);
    }


    @Override
    public SysUser selectUserTypeById(Long id) {
        // TODO Auto-generated method stub
        return sysUserDao.selectUserTypeById(id);
    }

    @Override
    public int selectCountById(CompanyInfoBO bo) {
        // TODO Auto-generated method stub
        return sysUserDao.selectCountById(bo);
    }

    @Override
    public CompanyInfoBO selectCompanyIdByMobielAndId(CompanyInfoBO bo) {
        // TODO Auto-generated method stub
        return sysUserDao.selectCompanyIdByMobielAndId(bo);
    }

    @Override
    public List<CompanyInfoBO> selectCountByIdAndMobile(CompanyInfoBO bo) {
        // TODO Auto-generated method stub
        return sysUserDao.selectCountByIdAndMobile(bo);
    }

    @Override
    public int selectCountsByIdAndMobile(CompanyInfoBO bo) {
        // TODO Auto-generated method stub
        return sysUserDao.selectCountsByIdAndMobile(bo);
    }

    @Override
    public int selectCountsByUserIdAndMobile(CompanyInfoBO bo) {
        // TODO Auto-generated method stub
        return sysUserDao.selectCountsByUserIdAndMobile(bo);
    }

    @Override
    public List<SysUser> selectUserTypeByMobile(String mobile) {
        // TODO Auto-generated method stub
        return sysUserDao.selectUserTypeByMobile(mobile);
    }

    @Override
    public int selectCountMobileByMobile(String mobile) {
        // TODO Auto-generated method stub
        return sysUserDao.selectCountMobileByMobile(mobile);
    }

    @Override
    public int selectMobileAndUserTypeByUserInfo(String mobile) {
        // TODO Auto-generated method stub
        return sysUserDao.selectMobileAndUserTypeByUserInfo(mobile);
    }

    @Override
    public Long selectCompanyIdByMobile(String mobile) {
        // TODO Auto-generated method stub
        return sysUserDao.selectCompanyIdByMobile(mobile);
    }

    @Override
    public int updatePwdByMobile(SysUser sysUser) {
        // TODO Auto-generated method stub
        return sysUserDao.updatePwdByMobile(sysUser);
    }

    @Override
    public int updateMobileById(String mobile, Long id) {
        // TODO Auto-generated method stub
        return sysUserDao.updateMobileById(mobile, id);
    }


    private void checkMultipleFranchiserAccount(UserUpdateMobile userUpdateMobile, Long id) {
        CompanyInfoBO bo = new CompanyInfoBO();
        bo.setUserId(id);//用户id
        bo.setMobile(userUpdateMobile.getMobile());//用户手机号码

        //1.同一个企业的手机号是唯一的
        //2.不同企业，只有经销商类型用户的手机可以不唯一

        //1.先判断用户类型
        SysUser userType = this.selectUserTypeById(id);
        if (null != userType) {
            //查询传过来的额手机号码是否在数据库存在(根据手机号查询)

            //2.如果用户类型为经销商（3）
            if (3 == userType.getUserType()) {
                //如果该账号已经绑定了，就不需要验证
                List<SysUser> userTypeS = this.selectUserTypeByMobile(userUpdateMobile.getMobile());

                if (null != userTypeS) {
                    for (SysUser li : userTypeS) {
                        if (li.getUserType() != 3) {//绑定了非经销商用户
                            throw new BizException(ExceptionCodes.CODE_10010206, "非同一企业的经销商手机号允许重复，其他账号手机号不允许重复!");//obj==0
                        }
                    }
                }

                //3.查询要绑定的手机号的公司id 根据要绑定的手机号码和当前用户id
                CompanyInfoBO cbo = this.selectCompanyIdByMobielAndId(bo);

                if (null != cbo) {
                    //如果在后台注册的账号 手机为空，，那么根据要绑定的手机号去查询当前企业有没有相同的手机号
                    if (null == cbo.getMobile() || "".equals(cbo.getMobile())) {
                        cbo.setMobile(userUpdateMobile.getMobile());
                        int count = this.selectCountsByUserIdAndMobile(cbo);
                        if (count > 0) {
                            throw new BizException(ExceptionCodes.CODE_10010206, "非同一企业的经销商手机号允许重复，其他账号手机号不允许重复!");//obj==0
                        }
                    }
                    //要绑定的手机号码
                    cbo.setMobile(userUpdateMobile.getMobile());
                    cbo.setUserId(id);
                    //4.查询当前公司下面的手机号是否重复
                    List<CompanyInfoBO> companyInfoBOs = this.selectCountByIdAndMobile(cbo);
                    if (null != companyInfoBOs) {
                        if (companyInfoBOs.size() > 0) {
                            throw new BizException(ExceptionCodes.CODE_10010206, "非同一企业的经销商手机号允许重复，其他账号手机号不允许重复!");//obj==0
                        }

                    }
                } else {
                    throw new BizException(ExceptionCodes.CODE_10010206, "非同一企业的经销商手机号允许重复，其他账号手机号不允许重复!");//obj==0
                }
                if (null != userUpdateMobile.getEnterpriseFlag() && 2 == userUpdateMobile.getEnterpriseFlag()) {
                    //根据绑定的手机号码去查询 公司id  2018-4-26 wutehua
                    String mobile = sysUserDao.selectMobileByUserId(id);
                    if (null != mobile) {
                        String companyIds = sysUserDao.selectCompanyIdByNewMobile(mobile, id);
                        if (null != companyIds) {
                            int a = sysUserDao.selectCompanyCountByCompanyId(userUpdateMobile.getMobile(), companyIds);
                            if (a > 0) {
                                throw new BizException(ExceptionCodes.CODE_10010206, "非同一企业的经销商手机号允许重复，其他账号手机号不允许重复!");//obj==0
                            }
                        }
                    }
                }
    			/*if(null != userUpdateMobile.getComfirmFlag() && 1==userUpdateMobile.getComfirmFlag()){
	    			//根据绑定的手机号码去查询 公司id  2018-4-26 wutehua
    				String mobile = sysUserDao.selectMobileByUserId(id);
    				if(null != mobile){
    					String companyIds = sysUserDao.selectCompanyIdByNewMobile(mobile,id);
    	    			if(null != companyIds){
    	    				int a = sysUserDao.selectCompanyCountByCompanyId(userUpdateMobile.getMobile(), companyIds);
    	    				if(a>0){
    	        				throw new BizException(ExceptionCodes.CODE_10010206, "非同一企业的经销商手机号允许重复，其他账号手机号不允许重复!");//obj==0
    	        			}
    	    			}
    				}
    			}*/
                if (null != userUpdateMobile.getComfirmFlag() && 1 == userUpdateMobile.getComfirmFlag()) {
                    //查询要绑定的手机号码有没有被其他经销商绑定
                    int counts = this.selectCountMobileByMobile(userUpdateMobile.getMobile());
                    if (counts > 1) {
                        throw new BizException(ExceptionCodes.CODE_10010200, "该手机号已绑定其他账号!");//obj==1
                    }
                }

                //根据当前用户id去查询该手机号码有没有绑定
                //默认没有绑定
                boolean fals = false;
                SysUser sysUserMobile = this.selectMobileById(id);
                if (null != sysUserMobile) {
                    if (null != sysUserMobile.getMobile() && sysUserMobile.getMobile().equals(userUpdateMobile.getMobile())) {
                        //说明该用户已经绑定了
                        fals = true;
                    }
                }
                //没有绑定执行的操作
                if (!fals) {
                    //查询非本企业的手机号   根据手机号码 根据当前手机号码和公司id
                    if (null != userUpdateMobile.getComfirmFlag() && 1 == userUpdateMobile.getComfirmFlag()) {
                        int count = this.selectCountsByIdAndMobile(bo);
                        if (count > 0) {
                            throw new BizException(ExceptionCodes.CODE_10010200, "该手机号已绑定其他账号!");//obj==1
                        }
                    }
                }


            } else {
                //如果是绑定的是经销手机号是不允许的
                int a = this.selectMobileAndUserTypeByUserInfo(userUpdateMobile.getMobile());
                if (a > 0) {
                    throw new BizException(ExceptionCodes.CODE_10010206, "非同一企业的经销商手机号允许重复，其他账号手机号不允许重复!");//obj==0
                }

                int count = this.selectCountById(bo);
                if (count > 0) {
                    throw new BizException(ExceptionCodes.CODE_10010206, "非同一企业的经销商手机号允许重复，其他账号手机号不允许重复!");//obj==0
                }
            }
        } else {
            throw new BizException(ExceptionCodes.CODE_10010201, "用户类型为空!");//obj==0
        }
    }

    @Override
    public int modifyUserMobile(UserUpdateMobile userUpdateMobile, Long id, Integer userType) {

        //1.校验手机号重复性
        this.checkMultipleFranchiserAccount(userUpdateMobile, id);
        //2.修改手机号码及同步密码
        return this.updateMobileByMobiles(userUpdateMobile, id, userType);
    }

    //电话是否已被其他经销商用户使用
    private boolean isMobileUsedByOhterFranchiser(String mobile, Long id) {
        int a = sysUserDao.selectCountDealerByNewMobile(mobile, id);
        if (a > 0) {
            return true;
        }
        return false;
    }

    public int updateMobileByMobiles(UserUpdateMobile userUpdateMobile, Long id, Integer userType) {

        if (null != userType) {

            if (3 == userType)//经销商
            {
                //当前登录人的用户信息
                SysUser sysUser = sysUserDao.selectByPrimaryKey(id.intValue());
                // 单企业经销商
                boolean mutiFranchiserflag = this.selectMultipleFranchiserAccount(id);//true 多企业
                //修改当前帐号
                boolean mutiAccountFlag = (null != userUpdateMobile.getEnterpriseFlag() && 2 == userUpdateMobile.getEnterpriseFlag());//true 多企业
                //电话是否已被其他经销商用户使用
                boolean mobileUsedFlag = isMobileUsedByOhterFranchiser(userUpdateMobile.getMobile(), id);
                //修改当前账号 || 单企业经销商
                if (!mutiAccountFlag || !mutiFranchiserflag) {
                    //如果新电话没有被其他经销商使用
                    if (!mobileUsedFlag) {
                        return sysUserDao.updateMobileById(userUpdateMobile.getMobile(), id);
                    }//新电话已被其他经销商使用
                    else {
                        SysUser obj = sysUserDao.getFranchiserByMobile(userUpdateMobile.getMobile());
                        return sysUserDao.updateByUserId(id, userUpdateMobile.getMobile(), obj.getPassword(), obj.getLastLoginCompanyId(), obj.getPasswordUpdateFlag());
                    }
                }

                //同步修改多个账号
                if (mutiAccountFlag) {
                    //如果新电话没有被其他经销商使用
                    if (!mobileUsedFlag) {
                        return sysUserDao.updateFranchiserMobile(userUpdateMobile.getMobile(), sysUser.getMobile());
                    }//新电话已被其他经销商使用
                    else {
                        SysUser obj = sysUserDao.getFranchiserByMobile(userUpdateMobile.getMobile());
                        return sysUserDao.updateFranchiserByNewMobile(sysUser.getMobile(), obj.getPassword(), obj.getLastLoginCompanyId(), obj.getPasswordUpdateFlag(), userUpdateMobile.getMobile());
                    }
                }

            }//非经销商用户类型
            else {
                return this.updateMobileById(userUpdateMobile.getMobile(), id);
            }

        } else {
            throw new BizException(ExceptionCodes.CODE_10010201, "用户类型为空!");//obj==0
        }
        return 0;
    }

    @Override
    public boolean selectMultipleFranchiserAccount(Long id) {
        // TODO Auto-generated method stub
        //根据用户id去查询手机号码
        String mobile = this.selectDealerMobileById(id);
        if (StringUtils.isNoneBlank(mobile)) {
            //根据手机号码去查询是否有多个企业
            int a = this.selectEnterpriseCountByMobile(mobile);
            if (a > 1) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String selectDealerMobileById(Long id) {
        // TODO Auto-generated method stub
        return sysUserDao.selectDealerMobileById(id);
    }

    @Override
    public int selectEnterpriseCountByMobile(String mobile) {
        // TODO Auto-generated method stub
        return sysUserDao.selectEnterpriseCountByMobile(mobile);
    }

    @Override
    public int updateMinProUserInfo(String openId, String nickName, String headPic) {
        return sysUserDao.updateMinProUserInfo(openId, nickName, headPic);
    }

    @Override
    public String selectPwdById(Long id) {
        // TODO Auto-generated method stub
        return sysUserDao.selectPwdById(id);
    }

    @Override
    public String selectPwdByMobile(String mobile) {
        // TODO Auto-generated method stub
        return sysUserDao.selectPwdByMobile(mobile);
    }


    @Override
    public void updateFailureTimeByPrimaryKey(Date date, Long id, Date timeInMillis) {
        sysUserDao.updateFailureTimeByPrimaryKey(date, id, timeInMillis);
    }

    @Override
    public void updateFirstLoginTimeByPrimaryKey(Date date, Long id) {
        sysUserDao.updateFirstLoginTimeByPrimaryKey(date, id);
    }

    @Override
    @Transactional
    public int save(UserRegister userRegister) {

        //检验redis是否存在手机号,防止并发注册
        long result = redisService.incr(REGISTER_PREFIX + userRegister.getMobile());
        redisService.expire(REGISTER_PREFIX + userRegister.getMobile(), 10);
        if (result > 1) {
            throw new BizException(ExceptionCodes.CODE_10010401, "您输入的手机号已经注册");
        }

        //如果有填邀请码，校验邀请码是否真实有效
        if (StringUtils.isNotEmpty(userRegister.getInvitationCode())) {
            boolean checkFlag = this.checkInvitation(userRegister.getInvitationCode());
            if (!checkFlag) {
                throw new BizException(ExceptionCodes.CODE_10010402, "您输入的邀请码不存在");
            }
        }

        //检验手机号是否被占用
        handlerParams(userRegister.getMobile(), userRegister.getAuthCode());
        //保存用户
        SysUser sysUser = saveUser(userRegister);

        if (sysUser.getId().intValue() > 0) {
            //为新用户授权:平台,角色
            BasePlatform basePlatform = this.authorizationRole(sysUser);

            //新注册用户创建虚拟公司
            BaseCompany baseCompany = bulidVirtualBaseCompany(sysUser);

            //获取套餐信息
            Integer priceId = this.handlerPackageInfo(sysUser, basePlatform);

            //插入账号有效时长
            saveOperatorLog(sysUser.getId(),priceId);
        }
        return sysUser.getId().intValue();
    }

    private Integer handlerPackageInfo(SysUser sysUser, BasePlatform basePlatform) {
        logger.info("处理套餐信息userType =>{}" + sysUser.getUserType());
        Double giveAccount = 3000d;
        Map<String, Object> resultMap = null;

        //用户类型不为中介时需要获取套餐
        if (sysUser.getUserType().intValue() != 11) {
            try {
                resultMap = (Map<String, Object>) servicesPurchaseBizService.mobile2bRegisterUsePackage(sysUser);
            } catch (Exception e) {
                redisService.del(REGISTER_PREFIX + sysUser.getMobile());
                sysUserDao.delByPrimaryKey(sysUser.getId());
                throw new BizException(ExceptionCodes.CODE_10010400, "您要注册的角色还没开通套餐,请联系客服!");
            }
            if (resultMap == null || resultMap.isEmpty()) {
                redisService.del(REGISTER_PREFIX + sysUser.getMobile());
                sysUserDao.delByPrimaryKey(sysUser.getId());
                throw new BizException(ExceptionCodes.CODE_10010400, "您要注册的角色还没开通套餐,请联系客服!");
            }
            giveAccount = handlerServiceInfo(sysUser, resultMap);
            sysUserDao.updateServiceFlag(sysUser.getId(), 1);
        }

        PayAccount payAccount = buildPayAccount(basePlatform.getId(), sysUser.getId(), sysUser.getMobile(), 0d);
//        PayOrder payOrder = buildPayOrder(sysUser, giveAccount.intValue(), "同城联盟注册赠送度币", "Give");
//        payOrderService.add(payOrder);
        payAccountService.insertPayAccount(payAccount);

        return Integer.parseInt(resultMap.get("priceId") + "");
    }

    /**
     * 注册开通平台权限
     *
     * @param sysUser
     */
    private BasePlatform authorizationRole(SysUser sysUser) {
        //插入平台权限
        logger.info("为用户开通平台权限");
        BasePlatform basePlatform = basePlatformService.queryByPlatformCode("mobile2b");
        if (sysUser.getUserType() == 11) {
            this.saveUserJurisdiction(sysUser.getId(), basePlatform.getId(), sysUser.getMobile());
        }

        //分配基本角色
        logger.info("为用户配置基本角色");
        SysRole sysRole = sysRoleService.getRoleByCode("CITY_UNION_BASE_ROLE");
        if (sysRole != null)
            this.saveUserRole(sysRole.getId(), sysUser.getId(), sysUser.getMobile());

        return basePlatform;
    }

    /**
     * 注册保存用户信息
     *
     * @param userRegister
     * @return
     */
    private SysUser saveUser(UserRegister userRegister) {
        SysUser sysUser = buildSysUser(userRegister);
        //生成账号时生产也难怪UUID add by WangHaiLIn
        String uuid = UUID.randomUUID().toString().replace("-", "");
        sysUser.setUuid(uuid);
        sysUserDao.save(sysUser);
        logger.info("添加用户成功 =>{}" + sysUser.getId() + ",userName =>{}" + sysUser.getUserName());
        return sysUser;
    }

    private Double handlerServiceInfo(SysUser sysUser, Map<String, Object> resultMap) {
        //调用system服务查看套餐赠送度币,计算账号失效时长
        try {
            if (resultMap != null && !resultMap.isEmpty()) {
                List<ServicesRoleRel> servicesRoleRels = (List<ServicesRoleRel>) resultMap.get("roleList");
                //配置套餐权限
                if (servicesRoleRels != null && !servicesRoleRels.isEmpty()) {
                    //插入用户角色
                    this.batchUserRoles(sysUser.getId(), servicesRoleRels);
                    //插入平台权限
                    this.batchUserJurisdiction(sysUser.getId(), sysUser.getMobile(), servicesRoleRels);
                }
                Integer sanduCurrency = (Integer) resultMap.get("sanduCurrency");
                if (sanduCurrency != null) {
                    return new Double(sanduCurrency);
                }
            }
        } catch (Exception e) {
            logger.error("调用system服务获取同城联盟用户注册套餐信息失败!");
        }
        return 0d;
    }

    private void batchUserJurisdiction(Long userId, String mobile, List<ServicesRoleRel> servicesRoleRels) {
        List<Integer> roleids = servicesRoleRels.stream().map(item -> item.getRoleId()).collect(Collectors.toList());

        Set<Long> platformIds = sysRoleService.getRolePlatformId(roleids);
        logger.info("需要开通的平台权限 =>{}" + (platformIds == null ? 0 : platformIds.toString()));

        if (platformIds != null) {
            //构建UserJurisdiction
            List<UserJurisdiction> userJurisdictions = platformIds.stream()
                    .map(platformId -> {
                        return buildUserJurisdiction(userId, platformId, mobile);
                    })
                    .collect(Collectors.toList());

            userJurisdictionService.batchUserJurisdictions(userJurisdictions);
        }
    }

    private void batchUserRoles(Long userId, List<ServicesRoleRel> servicesRoleRels) {
        List<SysUserRole> userRoles = servicesRoleRels.stream().map(m -> {
            Date now = new Date();
            SysUserRole u = new SysUserRole();
            u.setUserId(userId);
            u.setGmtCreate(now);
            u.setGmtModified(now);
            u.setIsDeleted(0);
            u.setRoleId(new Long(m.getRoleId()));
            u.setCreator("system");
            u.setModifier("system");
            u.setSysCode(Utils.getCurrentDateTime(Utils.DATETIMESSS) + "_" + Utils.generateRandomDigitString(6));
            return u;
        }).collect(Collectors.toList());
        //批量插入用户角色权限
        if (userRoles != null && !userRoles.isEmpty()) {
            sysRoleService.batchUserRole(userRoles);
        }
    }

    private BaseCompany bulidVirtualBaseCompany(SysUser sysUser) {

        String companyCode = baseCompanyService.getcompanyCode();
        Date now = new Date();
        BaseCompany b = new BaseCompany();
        b.setCompanyName("SG" + getStringRandom(6));
        b.setBusinessType(getCompanyType(sysUser.getUserType()));
        b.setCompanyCode(companyCode);
        b.setSysCode(Utils.getCurrentDateTime(Utils.DATETIMESSS) + "_" + Utils.generateRandomDigitString(6));
        b.setCreator(sysUser.getMobile());
        b.setModifier(sysUser.getMobile());
        b.setGmtCreate(now);
        b.setGmtModified(now);
        b.setIsDeleted(0);
        b.setIsVirtual(1); //虚拟公司

        int companyId = baseCompanyService.save(b);
        if (companyId > 0) {
            sysUserDao.updateBusinessAdministrationId(sysUser.getId(), b.getId());
            sysUser.setCompanyId(b.getId());
        }
        return b;
    }

    private Integer getCompanyType(Integer userType) {
        Integer type = 0;
        switch (userType) {
            case 5://用户类型为5 =>设计师  company类型为6设计师公司
                type = 6;
                break;
            case 6://用户类型为6 =>装修公司  company类型为5装修公司
                type = 5;
                break;
            case 13://用户类型为13 =>工长  company类型为7工长公司
                type = 7;
                break;
            case 11://用户类型为11 =>中介  company类型为8中介公司
                type = 8;
                break;
            default:
                type = 8;//默认注册中介
                break;
        }
        return type;
    }

    private PayAccount buildPayAccount(Long platformId, Long userId, String mobile, Double balanceAmount) {
        PayAccount payAccount = new PayAccount();
        Date date = new Date();
        payAccount.setUserId(userId);
        payAccount.setPlatformId(platformId);
        payAccount.setCreator(mobile);
        payAccount.setGmtCreate(date);
        payAccount.setModifier(mobile);
        payAccount.setGmtModified(date);
        payAccount.setIsDeleted(0);
        payAccount.setBalanceAmount(balanceAmount * 10);
        payAccount.setConsumeAmount(0.0);
        payAccount.setPlatformBusinessType(Optional.ofNullable(platformId).isPresent() ? "2b" : null);
        return payAccount;
    }

    private void saveUserRole(Long roleid, Long returnId, String mobile) {

        Date date = new Date();
        logger.info(date + "");
        SysUserRole sr = new SysUserRole();
        sr.setUserId(new Long(returnId));
        sr.setRoleId(roleid);
        sr.setCreator(mobile);
        sr.setGmtCreate(date);
        sr.setModifier(mobile);
        sr.setGmtModified(date);
        sr.setIsDeleted(0);

        sysRoleService.saveUserRole(sr);
    }

    public void saveUserJurisdiction(Long userId, Long id, String mobile) {

        //构建UserJurisdiction
        Date date = new Date();
        UserJurisdiction userJurisdiction = new UserJurisdiction();
        userJurisdiction.setIsDeleted(0);
        userJurisdiction.setCreator(mobile);
        userJurisdiction.setGmtCreate(date);
        userJurisdiction.setModifier(mobile);
        userJurisdiction.setGmtModified(date);
        userJurisdiction.setPlatformId(id);
        userJurisdiction.setUserId(userId);
        userJurisdiction.setJurisdictionStatus(1);
        userJurisdiction.setModifierUserId(userId);

        userJurisdictionService.save(userJurisdiction);
    }

    private UserJurisdiction buildUserJurisdiction(Long userId, Long id, String mobile) {
        //构建UserJurisdiction
        Date date = new Date();
        UserJurisdiction userJurisdiction = new UserJurisdiction();
        userJurisdiction.setIsDeleted(0);
        userJurisdiction.setCreator(mobile);
        userJurisdiction.setGmtCreate(date);
        userJurisdiction.setModifier(mobile);
        userJurisdiction.setGmtModified(date);
        userJurisdiction.setPlatformId(id);
        userJurisdiction.setUserId(userId);
        userJurisdiction.setJurisdictionStatus(1);
        userJurisdiction.setModifierUserId(userId);

        return userJurisdiction;
    }


    private SysUser buildSysUser(UserRegister userRegister) {

        Date date = new Date();
        SysUser sysUser = new SysUser();
        sysUser.setUserName("san" + getStringRandom(6));
        sysUser.setNickName(userRegister.getMobile());
        sysUser.setMobile(userRegister.getMobile());
        sysUser.setPassword(userRegister.getPassword());
        sysUser.setCreator(userRegister.getMobile());
        sysUser.setGmtCreate(date);
        sysUser.setModifier(userRegister.getMobile());
        sysUser.setGmtModified(date);
        sysUser.setUserType(userRegister.getUserType() == null ? 11 : userRegister.getUserType());
        sysUser.setIsDeleted(0);
        sysUser.setUseType(userRegister.getUserType() == 11 || Objects.equals(userRegister.getExternalDesignerFlag(),1) ? 1 : 0);
        sysUser.setUserSource(Objects.equals(userRegister.getExternalDesignerFlag(),1) ? 7 : 6);
        if (null != userRegister.getSourceCompany()) {
            sysUser.setSourceCompany(userRegister.getSourceCompany());
        }
        if (null != userRegister.getInvitationCode() && !"".equals(userRegister.getInvitationCode())) {
            sysUser.setUsedInvitationCode(userRegister.getInvitationCode());
        }
        //根据用户手机号,给用户设置默认的省市
        this.setDefaultCity(sysUser);
        return sysUser;
    }

    private void setDefaultCity(SysUser sysUser) {
        String mobilePrefix = sysUser.getMobile().substring(0, 7);
        BaseMobileArea baseMobileArea = baseMobileAreaService.queryBaseMobileAreaByMobilePrefix(mobilePrefix);
        if (Optional.ofNullable(baseMobileArea).isPresent()) {
            sysUser.setProvinceCode(baseMobileArea.getProvinceCode());
            sysUser.setCityCode(baseMobileArea.getCityCode());
        }
    }


    //生成随机数字和字母
    public static String getStringRandom(int length) {
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

    /**
     * 注册参数处理
     *
     * @param mobile
     * @param authCode
     */
    private void handlerParams(String mobile, String authCode) {
        //检验验证码
        validateAuthCode(mobile, authCode);
        //判断手机号是否存在
        isExist(mobile);
    }

    private void isExist(String mobile) {

        //查询手机号是否被占用
        UserQuery userQuery = new UserQuery();
        userQuery.setMobile(mobile);
        userQuery.setPlatformType(2);
        userQuery.setIsDeleted(0);
        List<UserInfoBO> userInfoBOS = sysUserDao.selectList(userQuery);

        if (userInfoBOS.size() > 0) {
            throw new BizException(ExceptionCodes.CODE_10010200, "当前手机号已绑定其他账号!");
        }
    }

    @Override
    public int editPassword(String mobile, String newPassword, String authCode) {

        validateAuthCode(mobile, authCode);

        SysUser editSysUser = new SysUser();
        editSysUser.setPassword(newPassword);
        editSysUser.setMobile(mobile);
        editSysUser.setModifier(mobile);

        return sysUserDao.updatePassword(editSysUser);
    }

    @Override
    public int check2BMobileIsExist(String mobile) {
        return sysUserDao.count2BMobileIsExist(mobile);
    }

    private void validateAuthCode(String mobile, String authCode) {
        //检验验证码
        String map = (String) redisTemplate.opsForHash().get(UserConstant.SESSION_SMS_CODE, mobile);

        SmsVo smsVo = gson.fromJson(map, SmsVo.class);
        if (smsVo == null) {
            redisService.del(REGISTER_PREFIX + mobile);
            throw new BizException(ExceptionCodes.CODE_10010204, "请输入验证码!");
        }
        String code = smsVo.getCode();

        Long sendCodeTime = 0L;
        sendCodeTime = smsVo.getSendTime();

        Long currentTime = System.currentTimeMillis();

        if (StringUtils.isEmpty(code) || !(authCode.equals(code))) {
            redisService.del(REGISTER_PREFIX + mobile);
            throw new BizException(ExceptionCodes.CODE_10010204, "请输入正确的验证码!");
        }

        if ((currentTime - sendCodeTime) > Utils.VALID_TIME) {//是否超时 默认3分钟
            redisService.del(REGISTER_PREFIX + mobile);
            throw new BizException(ExceptionCodes.CODE_10010209, "验证码超时!");
        }
    }

    @Override
    public int modifyUserMobile2C(Integer userId, String mobile, String authCode) {
        //验证码检验
        validateAuthCode(mobile, authCode);
        //customer_base_info
        sysUserDao.updateCustomerBaseInfo(userId, mobile);
        return sysUserDao.updateUserMobile2C(userId, mobile);
    }


    /**
     * @description： 通过昵称查用户
     * @author : WangHaiLin
     * @date : 2018/6/1 15:05
     * @param: [nickNames]
     * @return: java.util.List<com.sandu.api.user.model.SysUser>
     */
    @Override
    public List<SysUser> getUserByNickName(List<String> nickNames) {
        if (nickNames != null && nickNames.size() > 0) {
            List<SysUser> userList = sysUserDao.getUserByNickName(nickNames);
            return userList;
        }
        return Collections.emptyList();
    }


    /**
     * @description：新增用户(商家后台购买套餐)
     * @author : WangHaiLin
     * @date : 2018/6/1 17:59
     * @param: [sysUser]
     * @return: int  新增用户的Id
     */
    @Override
    public Long insertUser(SysUser sysUser) {
        int i = sysUserDao.insertUser(sysUser);
        if (i > 0) {
            return sysUser.getId();
        }
        return 0L;
    }

    /**
     * @param userId id
     * @return 操作结果
     * @author : WangHaiLin
     * 删除内部账户
     */
    @Override
    public int deleteUser(Long userId) {
        return sysUserDao.deleteUser(userId);
    }

    /**
     * @param userUpdate 修改内部账户入参
     * @return 修改结果
     * @author : WangHaiLin
     * 修改内部账户
     */
    @Override
    public int updateUser(SysUser userUpdate) {
        return sysUserDao.updateUser(userUpdate);
    }

    /**
     * @param userId 用户Id
     * @return SysUser 查询账号信息
     * @author : WangHaiLin
     * 查询内部账户详情
     */
    @Override
    public SysUser selectUserByAccount(Long userId) {
        return sysUserDao.getUserById(userId);
    }

    /**
     * @param userQuery 列表查询入参
     * @return list 内部账号集合
     * @author : WangHaiLin
     * 查询内部账号列表
     */
    @Override
    public List<InternalUserListVO> selectInternalUserList(InternalUserQueryExtends userQuery) {
        return sysUserDao.selectInternalUserList(userQuery);
    }

    /**
     * @param userQueryExtends 查询条件
     * @return 返回结果
     * @author : WangHaiLin
     * 查询满足条件内部账号数量
     */
    @Override
    public int getInternalUserCount(InternalUserQueryExtends userQueryExtends) {
        return sysUserDao.selectInternalUserCount(userQueryExtends);
    }

    /**
     * @param userQuery 查询条件
     * @return list 经销商账号列表
     * @author : WangHaiLin
     * 查询经销商账号列表
     */
    @Override
    public List<DealersUserListVO> selectDealersUserList(DealersUserQueryPO userQuery) {
        return sysUserDao.selectDealersUserList(userQuery);
    }

    /**
     * 查询经销商账号数量
     *
     * @param userQuery 查询条件
     * @return int 数据数量
     */
    @Override
    public int getDealersUserCount(DealersUserQueryPO userQuery) {
        return sysUserDao.getDealersUserCount(userQuery);
    }

    /**
     * @param companyId 企业Id
     * @return list 用户类型集合
     * @author : WangHaiLin
     * 查询当前企业所有用户类型
     */
    @Override
    public List<Integer> getAllUserType(Long companyId) {
        return sysUserDao.getAllUserType(companyId);
    }

    @Override
    public int editUserInfo(Long id, UserInfoEdit userInfoEdit) {
        logger.info("修改用户信息begin =>{}" + userInfoEdit);

        /*if (StringUtils.isNotEmpty(userInfoEdit.getMobile())) {
            //检验手机是否被使用
            isExist(userInfoEdit.getMobile());
        }*/

        //数据转换
        SysUser sysUser = transformUser(id, userInfoEdit);

        return sysUserDao.updateByPrimaryKeySelective(sysUser);
    }


    private SysUser transformUser(Long id, UserInfoEdit userInfoEdit) {
        SysUser sysUser = new SysUser();
        sysUser.setId(id);
        sysUser.setAreaId(userInfoEdit.getAreaId());
        sysUser.setSex(userInfoEdit.getSex());
        sysUser.setUserName(userInfoEdit.getNickName());
        sysUser.setAge(userInfoEdit.getAge());
        sysUser.setGmtModified(new Date());
        sysUser.setPicId(userInfoEdit.getPicId());
        sysUser.setUsedInvitationCode(userInfoEdit.getUsedInvitationCode());
        sysUser.setSourceCompany(userInfoEdit.getSourceCompany());
        sysUser.setProvinceCode(userInfoEdit.getProvinceCode());
        sysUser.setCityCode(userInfoEdit.getCityCode());
        return sysUser;
    }


    @Override
    public int modify2BUserMobileAndPassword(UserUpdatePasswordAndMobile userParam, Integer userId) {
        SysUser currentUser = sysUserDao.selectByPrimaryKey(userId);
        //验证参数有效性
        this.checkModifyParam(currentUser, userParam.getMobile(), userParam.getComfirmFlag());
        //执行更新
        return this.executeModifyUserMobileAndPassword(currentUser, userParam.getMobile(), userParam.getPassword());
    }


    private void checkModifyParam(SysUser currentUser, String newMobile, Integer comfirmFlag) {
        //只修改密码
        if (isOnlyModifyPassword(currentUser.getMobile(), newMobile)) {
            return;
            // throw new BizException(ExceptionCodes.CODE_10010210, "该手机号码已绑定当前账号,请重新输入!");
        }

        int targetUserType = this.getUserType(newMobile);

        int currentUserType = currentUser.getUserType();
        if (StringUtils.isNotBlank(currentUser.getMobile())) {
            currentUserType = this.getUserType(currentUser.getMobile());
        }

        if (currentUserType == SysUser.USER_TYPE_FRANCHISER || currentUserType == SysUser.USER_TYPE_INDEPENDENT_FRANCHISER) {
            //目标账号也是经销商(不能当前账号和目标账号同时是独立经销商)
            if (targetUserType == SysUser.USER_TYPE_FRANCHISER ||
                    (targetUserType == SysUser.USER_TYPE_INDEPENDENT_FRANCHISER && currentUser.getUserType() != SysUser.USER_TYPE_INDEPENDENT_FRANCHISER)) {
                //修改一个或多个经销商账号:true为多个,false为一个

                //当前用户手机号对应的经销商id列表:如果修改当前账号,则需要查询当前账号对应的企业id,如果是修改所有经销商账号,则需要查询所有的经销商对应的企业id
                Set<Long> currentFranchiserCompanyIds = new HashSet<Long>();

                if (currentUser.getCompanyId() != null) {
                    currentFranchiserCompanyIds.add(currentUser.getCompanyId());
                }

                //新手机号经销商对应的企业id列表
                Set<Long> bindedFranchiserCompanyIds = sysUserDao.selectFranchiserCompanyIdsByMobile(newMobile);
                Set<Long> bindedFranchiserCompanyIdsBak = new HashSet<Long>(); //bindedFranchiserIds计算交集后,集合数据会丢失,所以需要提前做下备份
                if (currentFranchiserCompanyIds != null && bindedFranchiserCompanyIds != null) {
                    bindedFranchiserCompanyIdsBak.addAll(bindedFranchiserCompanyIds);
                    bindedFranchiserCompanyIds.retainAll(currentFranchiserCompanyIds);
                    if (bindedFranchiserCompanyIds.size() > 0) {
                        logger.error("此手机号已被占用!" + newMobile);
                        throw new BizException(ExceptionCodes.CODE_10010210, "该手机号已绑定同一企业经销商!");
                    }
                }
                if (bindedFranchiserCompanyIdsBak.size() > 0) {
                    //如果不是用户点击确认合并按钮,则提示合并
                    if (comfirmFlag == null || comfirmFlag != 1) {
                        logger.error("手机号已绑定其他经销商账号,确认是否合并:" + newMobile);
                        throw new BizException(ExceptionCodes.CODE_10010212, "该手机号已绑定其他经销商账号,是否合并.");
                    }
                }
            }
            //目标账号是企业非经销商账号(包括独立经销商)
            else if (targetUserType > 0) {
                logger.error("该手机号已被企业非经销商账号占用!:" + newMobile);
                throw new BizException(ExceptionCodes.CODE_10010211, "该手机号已被占用!");
            }
        } else {
            //企业用户不能被其他B端用户使用
            boolean userFlag = this.isMobileUsedByOther2BUser(newMobile);
            if (userFlag) {
                logger.error("该手机号已被B端用户占用!:" + newMobile);
                throw new BizException(ExceptionCodes.CODE_10010211, "该手机号已被占用!");
            }
        }
    }

    //是否只修改密码
    private boolean isOnlyModifyPassword(String currentMobile, String newMobile) {
        if (newMobile.equals(currentMobile)) {
            return true;
        }
        return false;
    }

    /**
     * 执行更新用户信息
     *
     * @param currentUser
     * @param newMobile
     * @return
     */
    private int executeModifyUserMobileAndPassword(SysUser currentUser, String newMobile, String newPassword) {
        Long userId = currentUser.getId();
        //经销商
        int currentUserType = currentUser.getUserType();
        if (StringUtils.isNotBlank(currentUser.getMobile())) {
            currentUserType = this.getUserType(currentUser.getMobile());
        }
        if (currentUserType == SysUser.USER_TYPE_FRANCHISER || currentUserType == SysUser.USER_TYPE_INDEPENDENT_FRANCHISER) {
            //只修改密码
            if (isOnlyModifyPassword(currentUser.getMobile(), newMobile)) {
                return sysUserDao.update2BUserInfoByMobile(newMobile, newPassword, currentUser.getLastLoginCompanyId(), currentUser.getMobile());
            } else {
                //电话是否已被其他经销商用户使用
                boolean mobileUsedFlag = this.isMobileUsedByOtherFranchiser(newMobile);
                //如果新电话没有被其他经销商使用
                if (!mobileUsedFlag) {
                    return sysUserDao.update2BUserInfoById(newMobile, newPassword, currentUser.getLastLoginCompanyId(), userId);
                }//新电话已被其他经销商使用,则合并
                else {
                    logger.info("经销商合并手机号:" + newMobile + ",pwd:" + newPassword);
                    //改当前账号的手机号
                    sysUserDao.update2BUserInfoById(newMobile, newPassword, currentUser.getLastLoginCompanyId(), userId);
                    //合并后,密码同步修改成新的
                    return sysUserDao.update2BUserInfoByMobile(newMobile, newPassword, currentUser.getLastLoginCompanyId(), newMobile);
                }
            }
        }//非经销商用户类型
        else {
            return sysUserDao.update2BUserInfoById(newMobile, newPassword, currentUser.getLastLoginCompanyId(), userId);
        }
    }


    /**
     * 执行更新用户信息
     *
     * @param currentUser
     * @param newMobile
     * @return
     */
    private int executeModifyUserMobileAndPasswordBak(SysUser currentUser, String newMobile, String newPassword, Integer enterpriseFlag) {
        Long userId = currentUser.getId();
        //经销商
        if (currentUser.getUserType() == SysUser.USER_TYPE_FRANCHISER) {
            if (newMobile.equals(currentUser.getMobile())) {

            }

            //电话是否已被其他经销商用户使用
            boolean mobileUsedFlag = this.isMobileUsedByOtherFranchiser(newMobile);
            //如果新电话没有被其他经销商使用
            if (!mobileUsedFlag && enterpriseFlag.intValue() == 1) {
                //修改当前企业
                return sysUserDao.update2BUserInfoById(newMobile, newPassword, currentUser.getLastLoginCompanyId(), userId);
            } else if (!mobileUsedFlag && enterpriseFlag.intValue() == 2) {
                return sysUserDao.update2BUserInfoByMobile(newMobile, newPassword, currentUser.getLastLoginCompanyId(), currentUser.getMobile());
            }
            //新电话已被其他经销商使用,则合并
            else {
                SysUser obj = this.getFranchiser(newMobile);
                if (enterpriseFlag.intValue() == 1) {
                    sysUserDao.update2BUserInfoById(newMobile, newPassword, currentUser.getLastLoginCompanyId(), userId);
                } else {
                    sysUserDao.update2BUserInfoByMobile(newMobile, newPassword, currentUser.getLastLoginCompanyId(), currentUser.getMobile());
                }
                //更新合并账号的密码
                return sysUserDao.update2bUserPasswordByMobile(newMobile, newPassword);
            }

        }//非经销商用户类型
        else {
            return sysUserDao.update2BUserInfoById(newMobile, newPassword, currentUser.getLastLoginCompanyId(), userId);
        }
    }


    @Override
    public int modify2BUserMobile(UserUpdateMobile userUpdateMobile, LoginUser loginUser) {
        SysUser currentUser = sysUserDao.selectByPrimaryKey(loginUser.getId().intValue());
        //验证参数有效性
        this.checkModifyParameter(currentUser, userUpdateMobile.getMobile(), userUpdateMobile.getEnterpriseFlag(), userUpdateMobile.getComfirmFlag(), loginUser.getAppKey());
        //执行更新
        return this.executeModifyUserMobile(currentUser, userUpdateMobile.getMobile(), userUpdateMobile.getEnterpriseFlag());
    }

    /**
     * 验证输入参数
     *
     * @param currentUser
     * @param newMobile
     */
    private void checkModifyParameter(SysUser currentUser, String newMobile, Integer enterpriseFlag, Integer comfirmFlag, String appKey) {
        if (newMobile.equals(currentUser.getMobile())) {
            throw new BizException(ExceptionCodes.CODE_10010210, "该手机号码已绑定当前账号,请重新输入!");
        }

        int targetUserType = this.getUserType(newMobile);
        int currentUserType = currentUser.getUserType();
        if (StringUtils.isNotBlank(currentUser.getMobile())) {
            currentUserType = this.getUserType(currentUser.getMobile());
        }

        if (currentUserType == SysUser.USER_TYPE_FRANCHISER || currentUserType == SysUser.USER_TYPE_INDEPENDENT_FRANCHISER) {

            //目标账号也是经销商(不能当前账号和目标账号同时是独立经销商)
            if (targetUserType == SysUser.USER_TYPE_FRANCHISER ||
                    (targetUserType == SysUser.USER_TYPE_INDEPENDENT_FRANCHISER && currentUserType != SysUser.USER_TYPE_INDEPENDENT_FRANCHISER)) {
                //修改一个或多个经销商账号:true为多个,false为一个
                boolean updateMutiAccountFlag = (enterpriseFlag != null && enterpriseFlag == 2);
                //当前用户手机号对应的经销商id列表:如果修改当前账号,则需要查询当前账号对应的企业id,如果是修改所有经销商账号,则需要查询所有的经销商对应的企业id
                Set<Long> currentFranchiserCompanyIds = new HashSet<Long>();
                if (updateMutiAccountFlag) {
                    currentFranchiserCompanyIds = sysUserDao.selectFranchiserCompanyIdsByMobile(currentUser.getMobile());
                } else {
                    if (currentUser.getCompanyId() != null) {
                        currentFranchiserCompanyIds.add(currentUser.getCompanyId());
                    }
                }
                //新手机号经销商对应的企业id列表
                Set<Long> bindedFranchiserCompanyIds = sysUserDao.selectFranchiserCompanyIdsByMobile(newMobile);
                Set<Long> bindedFranchiserCompanyIdsBak = new HashSet<Long>(); //bindedFranchiserIds计算交集后,集合数据会丢失,所以需要提前做下备份
                if (currentFranchiserCompanyIds != null && bindedFranchiserCompanyIds != null) {
                    bindedFranchiserCompanyIdsBak.addAll(bindedFranchiserCompanyIds);
                    bindedFranchiserCompanyIds.retainAll(currentFranchiserCompanyIds);
                    if (bindedFranchiserCompanyIds.size() > 0) {
                        logger.error("此手机号已被占用!" + newMobile);
                        throw new BizException(ExceptionCodes.CODE_10010210, "该手机号已绑定同一企业经销商!");
                    }
                }
                if (bindedFranchiserCompanyIdsBak.size() > 0) {
                    //如果不是用户点击确认合并按钮,则提示合并
                    if (comfirmFlag == null || comfirmFlag != 1) {
                        logger.error("手机号已绑定其他经销商账号,确认是否合并:" + newMobile);
                        throw new BizException(ExceptionCodes.CODE_10010212, "该手机号已绑定其他经销商账号,合并后密码随着更改,是否合并?");
                    } else {
                        //用户选择合并操作需要重新登录
                        redisService.del(UserConstant.MOBILE_2B_LOGIN_PREFIX + currentUser.getId());
                        redisService.del(UserConstant.MOBILE_2B_LOGIN_PREFIX + appKey);
                    }
                }
            }
            //目标账号是企业非经销商账号(包括独立经销商)
            else if (targetUserType > 0) {
                logger.error("该手机号已被企业非经销商账号占用!:" + newMobile);
                throw new BizException(ExceptionCodes.CODE_10010211, "该手机号已被占用!");
            }
        } else {
            //企业用户不能被其他B端用户使用
            boolean userFlag = this.isMobileUsedByOther2BUser(newMobile);
            if (userFlag) {
                logger.error("该手机号已被B端用户占用!:" + newMobile);
                throw new BizException(ExceptionCodes.CODE_10010211, "该手机号已被占用!");
            }
        }
    }

    /**
     * 是否被其他B端用户使用
     *
     * @param mobile
     * @return
     */
    private boolean isMobileUsedByOther2BUser(String mobile) {
        SysUser userQuery = new SysUser();
        userQuery.setMobile(mobile);
        userQuery.setPlatformType(SysUser.PLATFORM_TYPE_B);
        userQuery.setIsDeleted(0);
        List<SysUser> userList = sysUserDao.selectUserList(userQuery);
        if (userList != null && userList.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 获取用户类型,0表示用户不存在
     *
     * @param mobile
     * @return
     */
    private int getUserType(String mobile) {
        SysUser userQuery = new SysUser();
        userQuery.setMobile(mobile);
        userQuery.setPlatformType(SysUser.PLATFORM_TYPE_B);
        userQuery.setIsDeleted(0);
//        bk    List<SysUser> userList = sysUserDao.selectUserList(userQuery);
        List<SysUser> userList = sysUserDao.select2bUserByMobile(userQuery);
        if (userList != null && userList.size() > 0) {
            for (SysUser u : userList) {
                if (Objects.equals(u.getUserType(),SysUser.USER_TYPE_INDEPENDENT_FRANCHISER)) {
                    return SysUser.USER_TYPE_INDEPENDENT_FRANCHISER;
                }
            }
            return userList.get(0).getUserType();
        }
        return 0;
    }

    /**
     * 执行更新用户信息
     *
     * @param currentUser
     * @param newMobile
     * @return
     */
    private int executeModifyUserMobile(SysUser currentUser, String newMobile, Integer enterpriseFlag) {
        Long userId = currentUser.getId();
        // int currentUserType = this.getUserType(currentUser.getMobile());
        int currentUserType = currentUser.getUserType();
        if (StringUtils.isNotBlank(currentUser.getMobile())) {
            currentUserType = this.getUserType(currentUser.getMobile());
        }
        //经销商
        if (currentUserType == SysUser.USER_TYPE_FRANCHISER || currentUserType == SysUser.USER_TYPE_INDEPENDENT_FRANCHISER) {
            // 单企业经销商
            boolean mutiFranchiserflag = this.isMultipleFranchiserAccount(userId);
            //修改一个或多个经销商账号:true为多个,false为一个
            boolean updateMutiAccountFlag = (enterpriseFlag != null && enterpriseFlag == 2);
            //电话是否已被其他经销商用户使用
            boolean mobileUsedFlag = this.isMobileUsedByOtherFranchiser(newMobile);
            //修改当前账号 || 单企业经销商
            if (!updateMutiAccountFlag || !mutiFranchiserflag) {
                //如果新电话没有被其他经销商使用
                if (!mobileUsedFlag) {
                    return sysUserDao.updateUserMobileById(newMobile, userId);
                }//新电话已被其他经销商使用,则合并
                else {
                    SysUser obj = this.getFranchiser(newMobile);
                    //return sysUserDao.updateUserInfoById(userId, newMobile, obj.getPassword(), obj.getLastLoginCompanyId(), obj.getPasswordUpdateFlag());
                    sysUserDao.updateUserInfoById(userId, newMobile, currentUser.getPassword(), currentUser.getLastLoginCompanyId(), currentUser.getPasswordUpdateFlag());
                    //合并后,所有密码同步修改成当前用户密码
                    return sysUserDao.update2BUserInfoByMobile(newMobile, obj.getPassword(), currentUser.getLastLoginCompanyId(), newMobile);
                }
            }

            //同步修改多个账号
            if (updateMutiAccountFlag) {
                //如果新电话没有被其他经销商使用
                if (!mobileUsedFlag) {
                    return sysUserDao.updateFranchiserMobileByOldMobile(newMobile, currentUser.getMobile());
                }//新电话已被其他经销商使用,则合并
                else {
                    SysUser obj = this.getFranchiser(newMobile);
                    // return sysUserDao.updateUserByNewMobile(currentUser.getMobile(), obj.getPassword(), obj.getLastLoginCompanyId(), obj.getPasswordUpdateFlag(), newMobile);
                    sysUserDao.updateFranchiserByNewMobile(currentUser.getMobile(), currentUser.getPassword(), currentUser.getLastLoginCompanyId(), currentUser.getPasswordUpdateFlag(), newMobile);
                    //合并后,所有密码同步修改成当前用户密码
                    return sysUserDao.update2BUserInfoByMobile(newMobile, obj.getPassword(), currentUser.getLastLoginCompanyId(), newMobile);
                }
            }

        }//非经销商用户类型
        else {
            return sysUserDao.updateUserMobileById(newMobile, currentUser.getId());
        }
        return 0;
    }

    /**
     * 获取单个经销商信息
     *
     * @param mobile
     * @return
     */
    private SysUser getFranchiser(String mobile) {
        List<Integer> userTypeList = new ArrayList<Integer>();
        userTypeList.add(SysUser.USER_TYPE_FRANCHISER);
        userTypeList.add(SysUser.USER_TYPE_INDEPENDENT_FRANCHISER);
        SysUser userQuery = new SysUser();
        userQuery.setMobile(mobile);
        userQuery.setPlatformType(SysUser.PLATFORM_TYPE_B);
        userQuery.setUserTypeList(userTypeList);
        userQuery.setIsDeleted(0);
        List<SysUser> userList = sysUserDao.selectUserList(userQuery);
        if (userList != null && userList.size() > 0) {
            return userList.get(0);
        }
        return null;
    }

    /**
     * 电话是否已被其他经销商用户使用
     *
     * @param mobile
     * @return
     */
    private boolean isMobileUsedByOtherFranchiser(String mobile) {
        List<Integer> userTypeList = new ArrayList<Integer>();
        userTypeList.add(SysUser.USER_TYPE_FRANCHISER);
        userTypeList.add(SysUser.USER_TYPE_INDEPENDENT_FRANCHISER);
        SysUser userQuery = new SysUser();
        userQuery.setMobile(mobile);
        userQuery.setPlatformType(SysUser.PLATFORM_TYPE_B);
        userQuery.setUserTypeList(userTypeList);
        userQuery.setIsDeleted(0);
        List<SysUser> userList = sysUserDao.selectUserList(userQuery);
        if (userList != null && userList.size() > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isMultipleFranchiserAccount(Long id) {
        // TODO Auto-generated method stub
        //根据用户id去查询手机号码
        SysUser user = sysUserDao.selectByPrimaryKey(id.intValue());
        if (user != null && StringUtils.isNotBlank(user.getMobile())) {
            List<Integer> userTypeList = new ArrayList<Integer>();
            userTypeList.add(SysUser.USER_TYPE_FRANCHISER);
            userTypeList.add(SysUser.USER_TYPE_INDEPENDENT_FRANCHISER);
            SysUser userQuery = new SysUser();
            userQuery.setMobile(user.getMobile());
            userQuery.setPlatformType(SysUser.PLATFORM_TYPE_B);
            userQuery.setUserTypeList(userTypeList);
            userQuery.setIsDeleted(0);
            List<SysUser> userList = sysUserDao.selectUserList(userQuery);
            if (userList != null && userList.size() > 1) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Long getUserCompanyIdByAccount(String account) {
        return sysUserDao.getUserCompanyIdByAccount(account);
    }

    @Override
    public UserCompanyInfoVO getCompanyInfoByUserId(Long userId) {
        return sysUserDao.getCompanyInfoByUserId(userId);
    }

    @Override
    public int dealUserHeadPic(DealUserHeadPic headPic) {
        return sysUserDao.dealUserHeadPic(headPic);
    }

    @Override
    public boolean addUserFranchiser(Long userId, Long businessAdministrationId) {
        SysUser user = sysUserDao.selectByPrimaryKey(userId.intValue());
        if (user == null || user.getUserType() != SysUser.USER_TYPE_FRANCHISER) {
            throw new BizException(ExceptionCodes.CODE_10010230, "更新失败:非经销商用户!");
        }
        //检验用户是否存在虚拟企业 =>删除虚拟企业
        if (user.getBusinessAdministrationId() != null && user.getBusinessAdministrationId() != 0) {
            BaseCompany baseCompany = baseCompanyService.queryById(user.getBusinessAdministrationId());
            if (baseCompany != null && baseCompany.getIsVirtual() == 1) {
                baseCompanyService.delByPrimaryKey(baseCompany.getId());
            }
        }
        int count = sysUserDao.updateBusinessAdministrationIdByUserId(businessAdministrationId, userId);
        if (count > 0) {
            return true;
        }
        return false;
    }

    @Override
    public int modifiPhone(Long userId, String mobile, String authCode) {

        SysUser sysUser = sysUserDao.selectByPrimaryKey(userId.intValue());

        if (sysUser == null) {
            throw new BizException(ExceptionCodes.CODE_10010301, "获取不到用户信息");
        }

        handlerParams(mobile, authCode);

        return sysUserDao.updateMobileById(mobile, sysUser.getId());
    }

    @Override
    public boolean addGiftAmount(List<Long> userIds, Integer amount, String giveDesc) {
        logger.info("保存用户赠送度币begin,userIds =>{},amount =>{},giveDesc =>{}" + userIds + "," + amount + "," + giveDesc);
        //检验是否存在账号
        for (Long userId : userIds) {
            PayAccount p = this.isExistPayAccount(userId);
            SysUser user = sysUserDao.getUserById(userId);
            int result = 0;
            if (p != null) {
                p.setBalanceAmount(p.getBalanceAmount() + amount * 10);
                result = payAccountService.updateUserAmount(p);
            } else {
                PayAccount payAccount = buildPayAccount(null, userId, user.getMobile(), Double.parseDouble(amount + ""));
                result = payAccountService.insertPayAccount(payAccount).intValue();
            }
            if (result > 0) {
                //插入支付订单
                PayOrder payOrder = this.buildPayOrder(user, amount, giveDesc, "GIVE");
                payOrderService.add(payOrder);
            }
        }
        return true;
    }

    private PayOrder buildPayOrder(SysUser user, Integer amount, String giveDesc, String bizType) {
        PayOrder payOrder = new PayOrder();
        Date now = new Date();
        payOrder.setUserId(user.getId().intValue());
        payOrder.setProductType(giveDesc);
        payOrder.setProductId(null);
        payOrder.setOrderDate(new Date());
        payOrder.setPayType(PayState.GIVE_SUCCESS);
        payOrder.setPayState(PayState.GIVE_SUCCESS);
        payOrder.setBizType(bizType);
        payOrder.setOrderNo(IdGenerator.generateNo());
        payOrder.setTotalFee(amount == null ? 0 : amount * 10);
        payOrder.setAdjustFee(0);
        payOrder.setProductDesc(giveDesc);
        payOrder.setProductName(giveDesc);
        payOrder.setCreator(user.getMobile());
        payOrder.setModifier(user.getMobile());
        payOrder.setGmtCreate(now);
        payOrder.setGmtModified(now);
        payOrder.setIsDeleted(0);
        return payOrder;
    }

    private PayAccount isExistPayAccount(Long userId) {
        PayAccount payAccount = payAccountService.getPayAccountByUserId(userId);
        if (payAccount != null) {
            return payAccount;
        }
        return null;
    }

    @Override
    public String setPicPath(SysUser sysUser) {
        ResPic resPic = null;

        if (sysUser.getPicId() != null) {
            resPic = resPicService.get(sysUser.getPicId());
            if (null != resPic) {
                return resPic.getPicPath();
            }
        }
        //获取用户头像
        if (null == resPic) {
            Integer sexValue = sysUser.getSex();
            if (sexValue == null) {
                sexValue = 1;
            }
            SysDictionary dictSearch = new SysDictionary();
            dictSearch.setIsDeleted(0);
            dictSearch.setType("userDefaultPicture");
            dictSearch.setValue(sexValue);
            SysDictionary dict = sysDictionaryService.getSysDictionary("userDefaultPicture", sexValue);
            if (dict != null && dict.getPicId() != null) {
                if (dict != null && dict.getPicId() != null) {
                    ResPic userPic = resPicService.get(dict.getPicId());
                    if (userPic != null && userPic.getPicPath() != null) {
                        return userPic.getPicPath();
                    }
                }
            }
        }
        return null;
    }


    @Override
    public List<SysUser> getUserBuMobile(String mobile) {
        if (mobile != null) {
            return sysUserDao.selectUser(mobile);
        }
        return null;
    }

    @Override
    public boolean checkUserIsExistVirtualCompany(Integer userId) {
        SysUser sysUser = sysUserDao.selectByPrimaryKey(userId);
        /*if (sysUser == null || sysUser.getUserType().intValue() != SysUser.USER_TYPE_FRANCHISER || sysUser.getUserType().intValue() != SysUser.USER_TYPE_COMPANY) {
            throw new BizException(ExceptionCodes.CODE_10010230, "更新失败:非经销商或厂商用户!");
        }*/
        if (sysUser != null) {
            int result = 0;
            //检验经销商用户是否存在虚拟经销商企业 =>删除虚拟企业
            if (sysUser.getBusinessAdministrationId() != null && sysUser.getBusinessAdministrationId() != 0) {
                BaseCompany baseCompany = baseCompanyService.queryById(sysUser.getBusinessAdministrationId());
                if (baseCompany != null && baseCompany.getIsVirtual() == 1) {
                    result = baseCompanyService.delByPrimaryKey(baseCompany.getId());
                    if (result > 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public List<CompanyInfoBO> getFranchiserCompanyList(String account, String password, String platformCode) {
        //如果不是手机号,则需要通过呢称查找手机号
        if (!Validator.MOBILE.validate(account)) {
            UserQuery userQuery = new UserQuery();
            userQuery.setNickName(account);
            userQuery.setPassword(password);
            userQuery.setIsDeleted(0);
            List<UserInfoBO> list = sysUserDao.selectList(userQuery);
            if (list != null && list.size() > 0) {
                account = list.get(0).getMobile();
            }
        }

        if (Validator.MOBILE.validate(account)) {
            //如果一个经销商多个企业,则返回登录企业列表
            List<CompanyInfoBO> companyList = baseCompanyService.queryFranchiserCompanys(account, password, platformCode);
            //查找logo地址
            if (companyList != null && companyList.size() > 0) {
                List<Long> logos = new ArrayList<Long>();
                for (CompanyInfoBO companyInfo : companyList) {
                    if (companyInfo.getLogo() != null) {
                        logos.add(companyInfo.getLogo());
                    }
                }
                if (logos.size() > 0) {
                    List<ResPic> resPics = resPicService.getByIds(logos);
                    if (resPics != null && resPics.size() > 0) {
                        for (ResPic pic : resPics) {
                            for (CompanyInfoBO companyInfo : companyList) {
                                if (companyInfo.getLogo() != null && companyInfo.getLogo().longValue() == pic.getId().longValue()) {
                                    companyInfo.setLogoPath(pic.getPicPath());
                                    break;
                                }
                            }

                        }
                    }
                }
            }
            return companyList;
        }
        return null;
    }

    @Override
    public void updateUserRoles(Integer userId, List<Integer> oldRoleIds, List<Integer> currentRoleIds) {
        logger.info("套餐升级权限start,当前用户 =>{},当前权限 =>{},升级权限 =>{}" + userId + "," + "{" + oldRoleIds.toString() + "}" + "," + "{" + currentRoleIds.toString() + "}");
        if (userId == null) {
            throw new RuntimeException("传参错误");
        }

        if (oldRoleIds != null && !oldRoleIds.isEmpty()) {
            Set<Long> set = sysRoleService.getRolePlatformId(oldRoleIds);
            //删除平台权限
            userJurisdictionService.delByIds(userId);
            //删除旧套餐权限
            sysRoleService.delUserRole(userId, oldRoleIds);

            //删除用户角色缓存
            redisService.del(UserConstant.RBAC_USER_ROLE_PREFIX + userId);
        }
        List<SysUserRole> userRoles = null;
        Set<Long> nPlatformIds = new HashSet<>();
        if (currentRoleIds != null && !currentRoleIds.isEmpty()) {
            userRoles = currentRoleIds.stream().map(c -> {
                Date now = new Date();
                SysUserRole u = new SysUserRole();
                u.setUserId(new Long(userId));
                u.setGmtCreate(now);
                u.setGmtModified(now);
                u.setIsDeleted(0);
                u.setRoleId(new Long(c));
                u.setCreator("system");
                u.setModifier("system");
                u.setSysCode(Utils.getCurrentDateTime(Utils.DATETIMESSS) + "_" + Utils.generateRandomDigitString(6));
                return u;
            }).collect(Collectors.toList());
        }

        nPlatformIds = sysRoleService.getRolePlatformId(currentRoleIds);

        List<UserJurisdiction> ujs = nPlatformIds.stream().map(p -> {
            return buildUserJurisdiction(new Long(userId), p, "system");
        }).collect(Collectors.toList());

        userJurisdictionService.batchUserJurisdictions(ujs);

        if (userRoles != null && !userRoles.isEmpty()) {
            sysRoleService.batchUserRole(userRoles);
        }
    }

    @Override
    public UserInfoBO getUserInfoByNickName(String account) {
        return sysUserDao.getUserInfoByNickName(account);
    }

    @Override
    public SysUser getUserById(Long id) {
        return sysUserDao.selectUserById(id);
    }


    /**
     * add by wanghl start
     **/
    @Override
    public String getMaxCompanyInUserNameCode(String companyCodePrefixS, Long companyId) {
        return sysUserDao.getMaxCompanyInUserNameCode(companyCodePrefixS, companyId);
    }

    @Override
    public String getMaxFranchiserAccountCode(String companyCodePrefixS, Long companyId) {
        return sysUserDao.getMaxFranchiserAccountCode(companyCodePrefixS, companyId);
    }

    /**
     * add by wanghl end
     **/

    @Override
    public int updatePasswordByMerchantManage(Long id, String password, String oldPassword) {
        SysUser sysUser = sysUserDao.selectByPrimaryKey(id.intValue());

        if (!oldPassword.equals(sysUser.getPassword())) {
            throw new BizException(ExceptionCodes.CODE_10010043, "旧密码有误!");
        }
        //更新手机号
        SysUser update = new SysUser();
        //有手机号通过手机号批量修改，无则通过Id单个修改 update by xiaoxc-20190412
        update.setPassword(password);
        logger.error("---------------testmobile:{}", sysUser.getMobile());
        if (!StringUtils.isBlank(sysUser.getMobile())) {
            update.setMobile(sysUser.getMobile());
        } else {
            update.setId(id);
        }
        update.setModifier(sysUser.getNickName());
        return sysUserDao.updatePassword(update);
    }

    /**
     * wangHL
     * 套餐升级修改用户失效时间
     *
     * @param failureTime 新失效时间
     * @param nickName    用户昵称（修改人）
     * @param userId      用户Id
     * @return int 修改操作结果
     */
    @Override
    public int updateUserFailureTime(Date failureTime, String nickName, Long userId) {
        return sysUserDao.updateUserFailureTime(failureTime, nickName, userId);
    }

    @Override
    public String getMarkedMessage(Long userId, String platformCode) {
        //获取用户信息
        SysUser sysUser = sysUserDao.selectByPrimaryKey(userId.intValue());

        long incr = redisService.incr(MARK_MESSAGE + userId);
        if (incr != 1) {
            return "";
        }
        if (sysUser != null && Objects.equals(sysUser.getServicesFlag(), 1)) {
            //套餐用户 =>{} 查询套餐赠送度币和渲染数
            Map<String, Object> map = servicesPurchaseBizService.getFreeRenderDurationByUserid(userId);
            if (map.isEmpty()) {
                return "";
            }
            //构建提示信息
            Object sanduCurrency = map.get("sanduCurrency");
            Integer render = (Integer) map.get("freeRenderDuration");
            saveSysUserMessage(userId, 21, sanduCurrency);
            return this.buildMessage(render, sanduCurrency);
        }
       /* else {
            //非套餐用户 =>{}中介用户注册赠送3000度币
            if (sysUser.getUserType().intValue() == 11) {
                saveSysUserMessage(userId, 2, 3000);
                return "您可获得赠送3000度币";
            }

            PayModelGroupRef payModelGroupRef = payModelGroupRefService.getPayModelGroupRef(userId.intValue(), 2);
            if (payModelGroupRef != null) {
                if (incr == 1 && sysUser.getUserType() != 11) {
                    saveSysUserMessage(userId, 2, 0);
                }
                return "您可获得免费三个月渲染";
            }
            return "";
        }
*/
        return "";
    }

   /* @Override
    public String getMarkedMessage(Long userId, String platformCode) {
        Map<String, Object> map = servicesPurchaseBizService.getFreeRenderDurationByUserid(userId);
        BasePlatform basePlatform = basePlatformService.queryByPlatformCode(platformCode);
        Object sanduCurrency = null;
        if (!map.isEmpty()) {
            sanduCurrency = map.get("sanduCurrency");
        }
        int paymodelConfigId = 2;
        if (!map.isEmpty() && BasePlatform.PLATFORM_CODE_MOBILE_2B.equals(basePlatform.getPlatformCode())) {
            paymodelConfigId = 21;
        }
        long incr = redisService.incr(MARK_MESSAGE + userId);

        SysUser sysUser = sysUserDao.selectByPrimaryKey(userId.intValue());
        if (sysUser.getUserType() == 11) {
            if (incr == 1)
                saveSysUserMessage(userId, paymodelConfigId, 3000);
            return "您可获得赠送3000度币";
        }

        if (!map.isEmpty()) {
            //构建提示信息
            Integer render = (Integer) map.get("freeRenderDuration");
            return this.buildMessage(render, sanduCurrency);
        }

        PayModelGroupRef payModelGroupRef = payModelGroupRefService.getPayModelGroupRef(userId.intValue(), paymodelConfigId);
        if (payModelGroupRef != null) {
            if (incr == 1 && sysUser.getUserType() != 11) {
                saveSysUserMessage(userId, paymodelConfigId, sanduCurrency);
            }
            return "您可获得免费三个月渲染";
        }
        return "";
    }*/

    private void saveSysUserMessage(Long userId, Integer payModelConfigId, Object sanduCurrency) {
        //获取套餐详细信息
        PayModelGroupRef payModelGroupRef = payModelGroupRefService.getPayModelGroupRef(userId.intValue(), payModelConfigId);

        if (payModelGroupRef != null && sanduCurrency != null && (Integer) sanduCurrency != 0) {
            SysUserMessage message = new SysUserMessage();
            Date now = new Date();
            message.setTitle("赠送渲染");
            if (sanduCurrency != null) {
                message.setTitle(message.getTitle() + "及赠送" + sanduCurrency + "度币");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("渲染有效期:");
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            sb.append(df.format(payModelGroupRef.getEffectiveTime()));
            sb.append("  -  ");
            sb.append(df.format(payModelGroupRef.getExpiryTime()));
            message.setContent(sb.toString());
            message.setUserId(userId.intValue());
            message.setIsDeleted(0);
            message.setGmtCreate(now);
            message.setGmtModified(now);
            message.setPlatformId(1);
            int row = sysUserDao.insertSysUserMessage(message);
        } else if (payModelGroupRef != null && (sanduCurrency == null || (Integer) sanduCurrency == 0)) {
            SysUserMessage message = new SysUserMessage();
            Date now = new Date();
            message.setTitle("赠送渲染");
            StringBuilder sb = new StringBuilder();
            sb.append("渲染有效期:");
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            sb.append(df.format(payModelGroupRef.getEffectiveTime()));
            sb.append("  -  ");
            sb.append(df.format(payModelGroupRef.getExpiryTime()));
            message.setContent(sb.toString());
            message.setUserId(userId.intValue());
            message.setIsDeleted(0);
            message.setGmtCreate(now);
            message.setGmtModified(now);
            message.setPlatformId(1);
            int row = sysUserDao.insertSysUserMessage(message);
        } else if (payModelGroupRef == null && sanduCurrency != null && (Integer) sanduCurrency != 0) {
            SysUserMessage message = new SysUserMessage();
            Date now = new Date();
            message.setTitle("赠送" + sanduCurrency + "度币");
            message.setContent("赠送" + sanduCurrency + "度币");
            message.setUserId(userId.intValue());
            message.setIsDeleted(0);
            message.setGmtCreate(now);
            message.setGmtModified(now);
            message.setPlatformId(1);
            int row = sysUserDao.insertSysUserMessage(message);
        }
    }

    private String buildMessage(Integer render, Object sanduCurrency) {
        StringBuilder sb = new StringBuilder();
        if (render != null && render != 0) {
            sb.append("你的套餐可获得");
            sb.append(render);
            sb.append("个月免费渲染");
            if (sanduCurrency != null && (Integer) sanduCurrency != 0) {
                sb.append("及");
                sb.append(sanduCurrency);
                sb.append("度币");
            }
        } else {
            if (sanduCurrency != null && (Integer) sanduCurrency != 0) {
                sb.append("您的套餐赠送");
                sb.append(sanduCurrency);
                sb.append("度币");
            }
        }
        return sb.toString();
    }

    /**
     * 获取失效时间在当前时间和当前时间后days天的B端用户电话
     *
     * @param days         天数
     * @param platformType 平台类型 （C端是1,B端是2）
     * @return List
     */
    @Override
    public List<UserWillExpireVO> getUserWillExpire(Integer days, Integer platformType) {
        if (null != days && null != platformType) {
            //当前时间
            Date date = new Date();
            //计算当前时间days后的时间
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.DATE, days);
            Date afterDays = c.getTime();
            //查询账号
            List<UserWillExpireVO> userList = sysUserDao.getUserWillExpire(date, afterDays, platformType);
            if (null != userList && 0 < userList.size()) {
                //计算账号到期时间还剩几天
                for (UserWillExpireVO user : userList) {
                    Long remainDays = (user.getFailureTime().getTime() - date.getTime()) / (1000 * 3600 * 24);
                    logger.info("账号到期剩余时间:{}", remainDays);
                    logger.info("将到期的账号电话:{}", user.getMobile());
                    user.setRemainDays(remainDays.intValue());
                }
                return userList;
            }
        }
        return null;
    }

    @Override
    public boolean checkInvitation(String invitationCode) {
        SysUser user = sysUserDao.getUserByInvitationCode(invitationCode);
        if (null != user && invitationCode.equals(user.getMyInvitationCode())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int modifyPassword(Long id, String newPassword, String oldPassword) {

        SysUser sysUser = sysUserDao.selectByPrimaryKey(id.intValue());

        Md5 md5 = new Md5();
        String password = md5.getMd5Password(oldPassword);

        if (!sysUser.getPassword().equals(password)) {
            throw new BizException(ExceptionCodes.CODE_10010043, "旧密码有误!");
        }

        String md5Password = md5.getMd5Password(newPassword);
        //更新手机号
        SysUser update = new SysUser();
        update.setPassword(md5Password);
        update.setModifier(sysUser.getUserName());
        update.setId(id);
        return sysUserDao.updateByPrimaryKeySelective(update);
    }

    @Override
    public boolean checkUserSecondRender(Long userId) {
        return sysUserDao.checkUserSecondRender(userId) >= 1;
    }

    @Override
    public Map<String, Object> getUserNickNameANDMobile(String nickName, String mobile) {
        return sysUserDao.getUserNickNameANDMobile(nickName, mobile);
    }

    @Override
    public List<SysUser> getUsersByCompanyId(Long companyId) {
        return sysUserDao.getUsersByCompanyId(companyId);
    }

    @Override
    public int modifyUserInfo(MinProUserInfo minProUserInfo, Long userId) throws Exception {
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(minProUserInfo, sysUser);
        sysUser.setId(userId);
        sysUser.setGmtModified(new Date()); //避免传空报错
        if (StringUtils.isNotEmpty(minProUserInfo.getHeadPic())) {
            Long picId = this.uploadPic(minProUserInfo.getHeadPic(), userId);
            sysUser.setPicId(picId);
        }
        return sysUserDao.updateByPrimaryKeySelective(sysUser);
    }

    private Long uploadPic(String headPic, Long userId) throws IOException {
        SysUser user = sysUserDao.getUserById(userId);
        //用户保存头像,需要将头像信息保存到res_pic表中兼容老数据
        URL url = new URL(headPic);
        BufferedImage image = ImageIO.read(url);

        StringBuilder sb = new StringBuilder();
        sb.append(ROOT_PATH);
        sb.append(PIC_DEFAULT_PATH);
//        sb.append("C:\\Users\\Sandu\\Desktop\\测试图片");
        File dir = new File(sb.toString());
        if (!dir.exists()) {
            dir.mkdirs();
        }
        sb.append("/");
        sb.append(Utils.generateRandomDigitString(8));
        sb.append("_");
        sb.append(user.getOpenId());
        sb.append(".");
        sb.append(PIC_TYPE);
        String picPath = sb.toString();
        //上传图片的路径
        logger.info("上传图片的路径 =>{}" + picPath);

        //上传图片
        ImageIO.write(image, "png", new File(picPath));
        String userPath = picPath.substring(ROOT_PATH.length(), picPath.length());
        //保存地址到res_pic表中
        ResPic resPic = new ResPic();
        Date now = new Date();
        resPic.setCreator(user.getOpenId());
        resPic.setIsDeleted(0);
        resPic.setGmtCreate(now);
        resPic.setGmtModified(now);
        resPic.setModifier(user.getOpenId());
        resPic.setPicPath(userPath);
        return new Long(resPicService.save(resPic));
    }


    @Override
    public void saveMiniUserInfo(String appId, String openId) {
        // TODO Auto-generated method stub
        BaseCompanyMiniProgramConfig miniProgramConfig = miniProgramConfigService.getMiniProgramConfig(appId);
        if (miniProgramConfig == null) {
            logger.error("未找到appId对应企业:" + appId);
            throw new BizException(ExceptionCodes.CODE_10010029, "未找到appId对应企业");
        }
        //从数据字典中获取赠送户型数据
        SysDictionary sysDictionary = selectIntegral("minProGivingHouse");
        //生成账号UUID add by WangHaiLin
        String uuid = UUID.randomUUID().toString().replace("-", "");
        sysUserDao.addMiniProgramUser(openId, miniProgramConfig.getCompanyId(), sysDictionary.getValue(), appId, uuid);
    }

    private SysDictionary selectIntegral(String type) {
        logger.info("查询数据字段小程序登录积分赠送记录begin");
        SysDictionary sysDictionary = new SysDictionary();
        sysDictionary.setType(type);
        List<SysDictionary> list = sysDictionaryService.getList(sysDictionary);
        if (list == null || list.size() <= 0) {
            throw new BizException(ExceptionCodes.CODE_10010039, "小程序首次登录赠送失败,数据字典的赠送积分记录为空");
        }
        return list.get(0);
    }

    @Override
    public boolean isMiniUserExist(String openId) {
        UserQuery userQuery = new UserQuery();
        userQuery.setIsDeleted(0);
        userQuery.setOpenId(openId);
        List list = sysUserDao.selectList(userQuery);
        if (list != null && list.size() > 0) {
            return true;
        }
        return false;
    }


    @Override
    public SysUser getMiniUser(String openId) {
        // TODO Auto-generated method stub
        SysUser user = new SysUser();
        user.setOpenId(openId);
        user.setIsDeleted(0);
        List<SysUser> userList = sysUserDao.selectUserList(user);
        if (userList != null && userList.size() > 0) {
            return userList.get(0);
        }
        return null;
    }


    @Override
    public int applyFormalAccount(Long id,String platformCode) {

        //获取客服账号
        List<SysDictionary> listByType = sysDictionaryService.getListByType(CUSTOMER_ACCOUNT);

        if (Objects.nonNull(listByType) && !listByType.isEmpty()) {
            //获取客服用户信息
            Long customerUserId = Long.parseLong(listByType.get(0).getValue() + "");
            SysUser customerUser = sysUserDao.getUserById(customerUserId);

            //获取申请VIP的用户信息
            SysUser vipUser = sysUserDao.getUserById(id);

            //获取平台id
            BasePlatform basePlatform = basePlatformService.queryByPlatformCode(platformCode);

            //构造消息对象
            SysUserMessage sysUserMessage = this.buildSysuserMessage(customerUser.getId(), vipUser.getMobile(), vipUser.getUserName(),basePlatform == null ? 0 : basePlatform.getId().intValue());

            //保存信息
            return sysUserDao.insertSysUserMessage(sysUserMessage);
        }

        return 0;
    }

    private SysUserMessage buildSysuserMessage(Long customerUserId, String mobile, String userName,Integer platformId) {
            SysUserMessage message = new SysUserMessage();
            Date now = new Date();
            StringBuilder sb = new StringBuilder();
            sb.append(userName);
            sb.append(mobile);
//            "客户昵称+账号的手机号码申请开通VIP权限，请立即联系客户"
            sb.append("申请开通VIP权限，请立即联系客户");
            message.setIsDeleted(0);
            message.setContent(sb.toString());
            message.setIsDeleted(0);
            message.setCreator(userName);
            message.setUserId(customerUserId.intValue());
            message.setGmtCreate(now);
            message.setGmtModified(now);
            message.setIsRead(0);
            message.setPlatformId(platformId);
            message.setStatus(1);
            message.setMessageType(10); //使用账号申请消息
            message.setTitle("申请VIP");
            return message;
    }

    @Override
    public Map<String, Object> getUserPackInfo(Long userId) {
        return sysUserDao.getUserPackInfo(userId);
    }

    @Override
    public String getPackType(Integer servicesId) {
        return sysUserDao.getPackType(servicesId);
    }

    public static void main(String[] args) {
//        String s = Utils.generateRandomDigitString(8);
//        StringBuilder sb = new StringBuilder();
//        sb.append("/data001/resource");
//        sb.append("/AA/userPic/upload");
//        sb.append("/");
//        sb.append(s);
//        sb.append(".");
//        sb.append("png");
//        String picPath = sb.toString();
//        System.out.println(picPath);
//        String uploadPicPath = "/data001/resource";
//        System.out.println(picPath.substring(uploadPicPath.length(), picPath.length()));

        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        for (String s : list) {
            if ("1".equals(s)){
                continue;
            }
            System.out.println(s);
        }
    }

    @Override
    public int modifyServiceUserFirstLogin(Date firstLoginTime, Long userId, Date failureTime) {
        return sysUserDao.updateServiceUserFirstLogin(firstLoginTime, userId, failureTime);
    }



    @Override
    public PageInfo<UserManageDTO> selectUserList(UserManageSearch search) {
        PageHelper.startPage(search.getPage(), search.getLimit());
        List<UserManageDTO> users =  sysUserDao.selectUserListResult(search);
        return new PageInfo<>(users);
    }

    @Override
    public PageInfo<UserManageDTO> findAllUserList(UserManageSearch search) {
        PageHelper.startPage(search.getPage(), search.getLimit());
        List<UserManageDTO> users =  sysUserDao.findAllUserList(search);
        return new PageInfo<>(users);
    }

    @Override
    public List<Map<String,Object>> findUserPackageInfoByUserIds(List<Long> userIds) {
        return sysUserDao.findUserPackageInfoByUserIds(userIds);
    }

    @Override
    public void updateFreezeState(SysUser sysUser) {
        sysUserDao.updateFreezeState(sysUser);
    }

    @Override
    public boolean handlerUserDel(String ids, Long loginUserId) {

        //当前登录用户
        SysUser loginUser = sysUserDao.getUserById(loginUserId);

        //操作删除的用户id集合
        Set<Long> userIds = Arrays.asList(ids.split(",")).stream().map(userId -> Long.parseLong(userId)).collect(Collectors.toSet());

        // 取消主子账号关系
        delMasterSon(userIds);

        //执行删除操作
        sysUserDao.batchDelByPrimaryKey(userIds, SysUserUtil.getUserName(loginUser));

        //删除用户的时候，要把用户创建的店铺，博文，工程案列删除
        //step 1 获取用户下的店铺信息
        List<CompanyShop> companyShops = getUserCompanyShopInfo(userIds);

        if (Objects.nonNull(companyShops) && !companyShops.isEmpty()){
            //step 2 =>{} 提取店铺id
            List<Integer> shopIds = companyShops.stream().map(shop -> shop.getId()).collect(Collectors.toList());

            //step 3 =>{} 开始删除店铺下的博文信息
            companyShopArticleService.deleteArticleByShopId(shopIds,SysUserUtil.getUserName(loginUser));

            //step 4 =>{} 开始删除店铺下的工程案例
            projectCaseService.deleteCaseByShopId(shopIds,SysUserUtil.getUserName(loginUser));

            //step 5 =>{} 开始删除店铺下的方案信息
            companyShopDesignPlanService.deleteDesignPlanByShopId(shopIds);

            //step 6 =>{} 开始删除用户下的店铺信息
            companyShopService.delCompanyShopByUserIds(userIds,SysUserUtil.getUserName(loginUser));
        }
        return Boolean.TRUE;
    }

    // 取消主子账号关系
    private void delMasterSon(Set<Long> userIds){

        if (null != userIds && userIds.size() > 0) {

            List<Integer> userIdList = userIds.stream().map(s -> s.intValue()).collect(Collectors.toList());
            SysUser sysUser = null;
            for (Integer userId : userIdList) {

                // 查看该用户类型
                sysUser = this.get(userId);

                // 主账号
                if (2 == sysUser.getMasterSonType()) {

                    // 取消关联关系
                    userMasterSonRefDao.deleteMasterSonRef(userId,null);
                }

                // 子账号
                if (1 == sysUser.getMasterSonType()) {

                    // 取消关联关系
                    userMasterSonRefDao.deleteMasterSonRef(null,userId);
                }
            }
        }
    }

    private List<CompanyShop> getUserCompanyShopInfo(Set<Long> userIds) {
        return companyShopService.selectShopByUserIds(userIds);
    }

    @Override
    public Map<String, Object> getPackServicesName(Long id) {
        Map<String,Object> map;
        ServicesAccountRel servicesAccount = servicesAccountRelService.getAccountByUserId(id.intValue());
        if (Objects.nonNull(servicesAccount)) {
            ServicesBaseInfo baseInfo = servicesBaseInfoService.getById(servicesAccount.getServicesId());
            if (Objects.nonNull(baseInfo)) {
                map = new HashMap<>();
                map.put("servicesName",baseInfo.getServicesName());
                map.put("servicesId",baseInfo.getId());
                return map;
            }
        }
        return Collections.EMPTY_MAP;
    }

    @Override
    public List<BasePlatform> getLoginUserAdminPlatform(Long userId) {
        //查询用户拥有多少个管理平台权限
        List<UserJurisdiction> lists = userJurisdictionService.queryByUserIdANDplatformIds(userId,adminPlatforms);

        if (Objects.nonNull(lists) && !lists.isEmpty()){
            Set<Long> platformIds = lists.stream().map(plat -> plat.getPlatformId()).collect(Collectors.toSet());
            return basePlatformService.queryByIds(platformIds);
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    public List<SysUser> getRestoreUserData() {
        return sysUserDao.getRestoreData();
    }

    @Override
    public boolean checkPhone(String phone, String platformCode, String appId) {
        UserQuery user = new UserQuery();
        user.setMobile(phone);
        user.setIsDeleted(0);
        user.setAppId(appId);
        if(!StringUtils.isEmpty(platformCode)) {
            user.setPlatformType(basePlatformService.getIdByPlatformCode(platformCode));
        }
        //根据手机号查询是否已经被注册了
        List<UserInfoBO> usInfo = sysUserDao.selectList(user);
        if (!usInfo.isEmpty()) {
            return true;
        }
        return false;
    }

    @Override
    public MessageUtil verifySmsInfo(String mobile, String clientIp) {

        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(clientIp)) {
            logger.error(CLASS_LOG_PERFIX + "手机号或客户端Ip为空!mobile:{},clientIp:{}", mobile, clientIp);
            return new MessageUtil(true);
        }
        // 返回对象 把数量存储进来
        Map<String, Integer> map = new HashMap<>(2);
        // 获取当前时间
        long currentTime = System.currentTimeMillis();
        // 拦截手机号接收次数和频率
        String mobileVoStr = (String) redisTemplate.opsForHash().get(UserConstant.SESSION_SMS_CODE + ":" + Utils.getDate(), mobile);

        Integer receiveCount = 0;
        Integer sendCount = 0;
        if (null != mobileVoStr) {
            SmsVo.MobileReceiveVo mobileReceiveVo = gson.fromJson(mobileVoStr, SmsVo.MobileReceiveVo.class);
            if (null != mobileReceiveVo) {
                receiveCount = mobileReceiveVo.getReceiveCount();
                if (null != receiveCount && SmsConfig.PHONE_RECEIVE_MSOT_NUMBER <= receiveCount) {
                    logger.error(CLASS_LOG_PERFIX + "手机短信发送过于频繁,请明天尝试!!mobile:{},", mobile);
                    return new MessageUtil(false, "手机短信发送过于频繁,请明天尝试!");
                }
                Long receiveTime = mobileReceiveVo.getReceiveTime();
                if (null != receiveTime && ((currentTime - receiveTime)/1000) < SmsConfig.SEND_INTERVAL_TIME) {
                    logger.error(CLASS_LOG_PERFIX + "手机短信发送频率过快,请稍后尝试!mobile:{},", mobile);
                    return new MessageUtil(false, "手机短信发送频率过快,请稍后尝试!");
                }
            }
        }

        // 拦截客户端IP发送短信次数和频率,白名单ip除外
        String clientIpVo = (String) redisTemplate.opsForHash().get(UserConstant.SESSION_SMS_CODE + ":" + Utils.getDate(), clientIp);
        if (SmsConfig.WHITELIST_CILENTIPS.indexOf(clientIp) == -1 && null != clientIpVo) {
            SmsVo.ClientIpSendVo clientIpSendVo = gson.fromJson(clientIpVo, SmsVo.ClientIpSendVo.class);
            if (null != clientIpSendVo) {
                sendCount = clientIpSendVo.getSendCount();
                if (null != sendCount && SmsConfig.CILENTIP_SEND_SMS_MOST_NUMBER <= sendCount) {
                    logger.error(CLASS_LOG_PERFIX + "客户端操作发送手机短信过于频繁！,请明天尝试!clientIp:{},", clientIp);
                    return new MessageUtil(false, "客户端操作发送手机短信过于频繁,请明天尝试!");
                }
                Long sendTime = clientIpSendVo.getSendTime();
                if (null != sendTime && ((currentTime - sendTime)/1000) < SmsConfig.SEND_INTERVAL_TIME) {
                    logger.error(CLASS_LOG_PERFIX + "客户端操作发送手机短信频率过快,请稍后尝试!clientIp:{},", clientIp);
                    return new MessageUtil(false, "客户端操作发送手机短信频率过快,请稍后尝试!");
                }
            }
        }
        map.put(SmsVo.CLIENT_SEND_COUNT, sendCount);
        map.put(SmsVo.MOBILE_RECEIVE_COUNT, receiveCount);

        return new MessageUtil(map, true);
    }

    @Override
    public Boolean sendMessage(String mobile, String clientIp, Integer functionType, Map<String, Integer> map, String platformCode) {
        String message = "";
        String code = Utils.generateRandomDigitString(6);
        logger.info(CLASS_LOG_PERFIX + "发送短信验证码, 手机号:{}, 验证码:{}", mobile, code);
        try {
            if (SmsConstant.FUNCTION_USER_REGISTER == functionType) {
                // 注册手机号
                message = MessageFormat.format(SmsConfig.REGISTERCONTEXT, code,
                        Utils.VALID_TIME / 60000, Utils.SERVICE_PHONE);
            } else if (SmsConstant.FUNCTION_UPDATE_LOGIN_PASSWORD == functionType
                    || SmsConstant.FUNCTION_BINDING_PHONE == functionType) {
                // 修改、绑定手机号和修改手机号密码
                message = MessageFormat.format(SmsConfig.UPDATEMOBILECONTEXT, code,
                        Utils.VALID_TIME / 60000, Utils.SERVICE_PHONE);
            }
            message = URLEncoder.encode(message, "UTF-8");
            // 当前时间
            long seqId = System.currentTimeMillis();
            // 组装发送参数
            String params = "phone=" + mobile + "&message=" + message + "&addserial=&seqid=" + seqId;
            String result = Utils.sendSMS(Utils.SEND_MESSAGE, params);
            if ("1".equals(result)) {
                return false;
            }
            // 缓存短信相关信息
            this.cacheSmsRelatedInfo(mobile, clientIp, code, seqId, map, platformCode);

        } catch (UnsupportedEncodingException e) {
           logger.error(CLASS_LOG_PERFIX + "发送短信异常,Exception:{}", e);
           return false;
        }

        return true;
    }

    @Override
    public boolean batchModifyPackage(List<Integer> userIds, Integer servicesId) {
        if (CollectionUtils.isEmpty(userIds)){
            throw new IllegalArgumentException("请选择您要更换套餐的用户");
        }

        if (Objects.isNull(servicesId)){
            throw new IllegalArgumentException("请选择套餐");
        }

        //获取套餐信息
        ServicesBaseInfo info = servicesBaseInfoService.getById(Long.valueOf(servicesId + ""));

        String userScope = Optional.ofNullable(info).map(item -> item.getUserScope()).orElseThrow(() -> new RuntimeException("获取不到套餐信息"));

        //获取套餐价格信息
        List<ServicesPrice> list = servicesPriceService.findServicesPriceList(Long.valueOf(servicesId));

        if (CollectionUtils.isEmpty(list)){
            throw new RuntimeException("该套餐没有配置价格信息");
        }
        /**
         * findServicesPriceList 这个方法有点坑,没有添加isDeleted过滤
         */
        Integer priceId = this.filterVaildServicesPrice(list).getId().intValue();
        for (Integer userId : userIds){
            SysUser sysUser = get(userId);
            Integer userType = Optional.ofNullable(sysUser).map(user -> user.getUserType()).orElse(0);
            logger.info("current userType =>{}" + userType);
            if (!Objects.equals(Integer.parseInt(userScope),userType) && userType != 0){
                throw new RuntimeException("套餐不能满足所有的用户");
            }

            ServicesAccountRel oldAccount =  null;
            //step1 删除旧的权限信息
            if (Objects.equals(1,sysUser.getServicesFlag())){
                oldAccount = servicesAccountRelService.getAccountByUserId(userId);
                handlerPackageAccountRole(oldAccount);
            }else{
                handlerOldUserInfo(userId);
            }
            //step2 插入套餐信息
            handlerPackageInfo(Long.valueOf(servicesId),priceId,Long.valueOf(userId),sysUser);
            syncAccountInfo(sysUser,Long.valueOf(servicesId));


            if (Objects.nonNull(oldAccount) && Objects.equals(Integer.parseInt(oldAccount.getStatus()),1)){
                //套餐已使用 =>{} 激活套餐时间
                activePackageUserTime(servicesId,sysUser.getFailureTime(),sysUser.getId().intValue());
                continue;
            }

            if (Objects.equals(sysUser.getServicesFlag(),0)){
                //旧用户转套餐用户,立即启用
                if (Objects.nonNull(sysUser.getFailureTime())){
                    //已经登录过了 =>{}激活套餐时间
                    activePackageUserTime(servicesId,sysUser.getFailureTime(),sysUser.getId().intValue());
                    continue;
                }
            }
            saveNotActiveVaildTime(Long.valueOf(userId),priceId);
        }
        return true;
    }

    private ServicesPrice filterVaildServicesPrice(List<ServicesPrice> list) {
        return list.stream()
                .filter(
                        item -> Objects.equals(item.getIsDeleted(),0)
                )
                .findFirst()
                .get();
    }


    @Override
    public void saveOperatorLog(Long userId, Integer priceId) {
        this.saveNotActiveVaildTime(userId,priceId);
    }

    private void saveNotActiveVaildTime(Long userId, Integer priceId) {
        ServicesPrice price = servicesPriceService.findPriceById(Long.valueOf(priceId));
        SysUserOperatorLog log = new SysUserOperatorLog();
        log.setIsDeleted(0);
        log.setOperatorTime(new Date());
        log.setRemark("记录尚未激活的套餐用户有效时长");
        log.setUserId(userId);
        log.setOperator("system");
        log.setValue(fetchVaildTimeByPriceInfo(price));
        log.setEventCode(10);
        //先删除原有的时长
        sysUserOperatorLogService.updateByUserId(userId);
        sysUserOperatorLogService.addLog(log);
    }

    private String fetchVaildTimeByPriceInfo(ServicesPrice price) {
        StringBuilder sb = new StringBuilder();
        sb.append(price.getDuration());
        switch (price.getPriceUnit()) {
            case PRICE_UNIT_YEAR://年
                sb.append("Y");
                break;
            case ServicesPurchaseConstant.PRICE_UNIT_MONTH://月
                sb.append("M");
                break;
            case ServicesPurchaseConstant.PRICE_UNIT_DAY://日
                sb.append("D");
                break;
            default:
                sb.append("D");
                break;
        }
        return sb.toString();
    }

    @Override
    public List<SysUser> listByIds(List<Integer> userIds) {
        if (CollectionUtils.isEmpty(userIds)){
            return null;
        }
        return sysUserDao.selectListByIds(userIds);
    }

    private void handlerPackageAccountRole(ServicesAccountRel account) {
        if (Objects.nonNull(account)){
            logger.info("开始删除用户旧的套餐权限 =>{}" + account.getServicesId());
            //获取用户原来的套餐权限
            List<ServicesRoleRel> oldRoles = servicesRoleRelService.getByServiceId(account.getServicesId());

            if (!CollectionUtils.isEmpty(oldRoles)){
                //提取用户角色id
                List<Integer> oldRoleIds = oldRoles.stream().map(ServicesRoleRel::getRoleId).collect(Collectors.toList());
                //根据角色id获取旧套餐所配置的平台id
                Set<Long> oldPlatformIds = sysRoleService.getRolePlatformId(oldRoleIds);

                //删除用户的旧的权限
                sysRoleService.delUserRole(account.getUserId().intValue(),oldRoleIds);

                //删除用户的平台权限
                userJurisdictionService.batchDelUserJurisdiction(account.getUserId().intValue(),oldPlatformIds);

            }
            //删除套餐用户关联的信息
            ServicesAccountRel servicesAccountRel = new ServicesAccountRel();
            servicesAccountRel.setIsDeleted(1);
            servicesAccountRel.setId(account.getId());
            servicesAccountRelService.updateByPrimaryKeySelective(servicesAccountRel);
        }
    }

    private void handlerPackageInfo(Long servicesId,Integer priceId,Long addUserId,SysUser sysUser) {
        if (Objects.nonNull(servicesId)){
            //用户新增时有设置套餐
            sysUser.setId(addUserId);
            List<ServicesRoleRel> servicesRoleRels = null;
            servicesRoleRels = servicesPurchaseBizService.handlerCompanyUserPackageInfo(servicesId, sysUser,priceId);
            if (!CollectionUtils.isEmpty(servicesRoleRels)){
                //插入用户角色
                this.handlerPackageUserRoles(sysUser.getId(), servicesRoleRels);
                //插入平台权限
                this.handlerPackageUserJurisdiction(sysUser.getId(), sysUser.getMobile(), servicesRoleRels);
            }
        }
    }


    @Override
    public boolean activePackageUserTime(Integer servicesId, Date failureTime, Integer userId) {
        List<ServicesPrice> servicesPrices = servicesPriceService.findServicesPriceList(Long.valueOf(servicesId));
        if (!CollectionUtils.isEmpty(servicesPrices)){
            ServicesPrice servicesPrice =  this.filterVaildServicesPrice(servicesPrices);
            Date now = new Date();
            Date result = fetchDate(now, servicesPrice.getDuration(), Integer.valueOf(servicesPrice.getPriceUnit()));
            ServicesAccountRel accountRel = servicesAccountRelService.getAccountByUserId(userId);
            ServicesAccountRel updateAccountRel = new ServicesAccountRel();
            updateAccountRel.setId(accountRel.getId());
            updateAccountRel.setEffectiveBegin(now);
            updateAccountRel.setEffectiveEnd(result);
            updateAccountRel.setStatus("1");
            servicesAccountRelService.updateByPrimaryKeySelective(updateAccountRel);
            //计算用户使用了多少时长
            //同步SysUser表中失效时间
            dealUserFailureTimeAndValidTime(accountRel.getUserId(),new Date().compareTo(failureTime) >= 0 ? failureTime : new Date(),now,result);
        }
        return Boolean.TRUE;
    }

    /**
     * 处理套餐升级带来的失效时间和有效时长变更
     * @author wanghl
     * @param userId 用户Id
     * @param failureTime 失效时间
     * @return boolean (true:操作成功；false:操作失败)
     */
    private boolean dealUserFailureTimeAndValidTime(Long userId, Date oldAccountFailureTime,Date beginTime,Date failureTime) {
        try{
            if (null!=userId&&null!=failureTime){
                SysUser sysUser = getUserById(userId);
                //初次登录，同步账号失效时间和套餐失效时间(按照逻辑，此判断不会起作用，以防万一保留)
                if (null!=sysUser&&null==sysUser.getFirstLoginTime()){
                    int result = modifyServiceUserFirstLogin(new Date(), userId, failureTime);
                    if (result>0){
                        return true;
                    }
                    logger.error("初次登录同步账号到期时间失败");
                    return false;
                }


                logger.info("套餐升级修改用户失效时间和有效时长:通过Id查询用户结果：{}",sysUser);
                if (null!=sysUser&&null!=sysUser.getFirstLoginTime()&&null==sysUser.getFailureTime()){
                    logger.info("套餐升级修改用户失效时间和有效时长:用户没有失效时间处理...开始");
                    if (CollectionUtils.isEmpty(sysUserOperatorLogService.getLogByUserId(sysUser.getId()))){
                        Date firstLoginTime = sysUser.getFirstLoginTime();
                        String validTime = this.getYearMonthDayFromDateToDate(firstLoginTime, failureTime);
                        SysUserOperatorLog operatorLog=new SysUserOperatorLog();
                        operatorLog.setUserId(sysUser.getId());
                        operatorLog.setEventCode(100);
                        operatorLog.setIsDeleted(0);
                        operatorLog.setValue(validTime);
                        operatorLog.setOperatorTime(new Date());
                        operatorLog.setOperator(sysUser.getNickName());
                        operatorLog.setRemark("套餐购买账号套餐升级记录有效时间");
                        int result = sysUserOperatorLogService.addLog(operatorLog);
                        if (result<=0){
                            logger.error("向sys_user_operator_log表添加数据失败");
                            return false;
                        }
                        int result2 = updateUserFailureTime(failureTime, sysUser.getNickName(), sysUser.getId());
                        if (result2>0){
                            return true;
                        }
                    }
                    logger.error("修改失效时间失败失败");
                    logger.info("套餐升级修改用户失效时间和有效时长:用户没有失效时间处理...结束");
                    return false;
                }
                if (null!=sysUser&&null!=sysUser.getFirstLoginTime()&&null!=sysUser.getFailureTime()){
                    logger.info("套餐升级修改用户失效时间和有效时长:用户有失效时间处理...开始");
//                    Date formerFailureTime = sysUser.getFailureTime();
                    String oldUsedTime = null;
                    if (Objects.nonNull(oldAccountFailureTime)){
                        //用户上个套餐使用的时长
                        oldUsedTime = this.getYearMonthDayFromDateToDate(sysUser.getFirstLoginTime(), oldAccountFailureTime);
                    }
                    //得到新失效时间到原失效时间之间的差值：xxYxxMxxD
                    //String validTime = this.getYearMonthDayFromDateToDate(formerFailureTime, totalVaildTime);
                    //计算更换套餐使用时长
                    String validTime = this.getYearMonthDayFromDateToDate(beginTime,failureTime);
                    //将上个套餐使用的时长和新套餐的时长累加起来
                    String finalVaildTime =  sumOldUsedTimeAndCurVaildTime(oldUsedTime,validTime);
                    if (StringUtils.isNotEmpty(finalVaildTime)){
                        //删除旧的有效时间
                        int update = sysUserOperatorLogService.updateByUserId(sysUser.getId());
                    }
                    //构造新增operatorLog表入参
                    SysUserOperatorLog operatorLog=new SysUserOperatorLog();
                    operatorLog.setUserId(sysUser.getId());
                    operatorLog.setEventCode(100);
                    operatorLog.setIsDeleted(0);
                    operatorLog.setValue(finalVaildTime);
                    operatorLog.setOperatorTime(new Date());
                    operatorLog.setOperator(sysUser.getNickName());
                    operatorLog.setRemark("套餐升级");
                    int result = sysUserOperatorLogService.addLog(operatorLog);
                    if (result<=0){
                        logger.error("向sys_user_operator_log表添加数据失败");
                        return false;
                    }
                    int result2 = updateUserFailureTime(failureTime, sysUser.getNickName(), sysUser.getId());
                    if (result2>0){
                        return true;
                    }
                    logger.error("修改失效时间失败失败");
                    logger.info("套餐升级修改用户失效时间和有效时长:用户有失效时间处理...结束");
                    return false;
                }
                logger.error("该用户不满足套餐升级");
                return false;
            }
            logger.error("参数异常userId:{},failureTime:{}",userId,failureTime);
            return false;
        }catch (Exception e){
            logger.error("处理套餐升级失效时间处理失败",e);
            return false;
        }
    }

    /**
     * 计算两个时间点之间相隔xx年xx月xx天
     * @param fromDate 前一个时间点
     * @param toDate 后一个时间点
     * @return xx年xx月xx天
     */
    private static String getYearMonthDayFromDateToDate(Date fromDate,Date toDate){
        StringBuilder validTimeStr=new StringBuilder();
        Calendar  from  =  Calendar.getInstance();
        from.setTime(fromDate);
        Calendar  to  =  Calendar.getInstance();
        to.setTime(toDate);
        int fromYear = from.get(Calendar.YEAR);
        int fromMonth = from.get(Calendar.MONTH);
        int fromDay = from.get(Calendar.DAY_OF_MONTH);
        int toYear = to.get(Calendar.YEAR);
        int toMonth = to.get(Calendar.MONTH);
        int toDay = to.get(Calendar.DAY_OF_MONTH);
        int year = toYear-fromYear;
        int month = toMonth-fromMonth;
        int day = toDay-fromDay;
        if (day<0){
            month=month-1;
            if (toMonth==1||toMonth==3||toMonth==5||toMonth==7||toMonth==8||toMonth==11){//Calendar的月从0开始
                day=day+31;
            }else if(toMonth==4||toMonth==6||toMonth==9||toMonth==10||toMonth==12){
                day=day+30;
            }else{
                day=day+28;
            }
        }
        if (month<0){
            year=year-1;
            month=month+12;
        }
        if (year>0){
            validTimeStr.append(year);
            validTimeStr.append("Y");
        }if (month>0){
            validTimeStr.append(month);
            validTimeStr.append("M");
        }if (day>0){
            validTimeStr.append(day);
            validTimeStr.append("D");
        }
        return validTimeStr.toString();
    }

    private String sumOldUsedTimeAndCurVaildTime(String oldUsedTime, String validTime) {
        if (StringUtils.isEmpty(oldUsedTime)){
            return validTime;
        }

        StringBuilder sb = new StringBuilder();

        //计算年限
        this.totalYear(sb,oldUsedTime,validTime);

        //计算月份
        this.totalMonth(sb,oldUsedTime,validTime);

        //计算天数
        this.totalDate(sb,oldUsedTime,validTime);

        return sb.toString();
    }

    private void totalDate(StringBuilder sb, String oldUsedTime, String validTime) {
        this.getVaildTime(sb,oldUsedTime,validTime,"D","M");

    }

    private void totalYear(StringBuilder sb, String oldUsedTime, String validTime) {
        this.getVaildTime(sb,oldUsedTime,validTime,"Y",null);
    }

    private int getAgeLimit(String oldUsedTime,String type,String beforeType) {
        int yIndex = oldUsedTime.indexOf(type);
        int y = Integer.parseInt(oldUsedTime.substring(StringUtils.isEmpty(beforeType) ? 0 : oldUsedTime.indexOf(beforeType)+1, yIndex));
        return y;
    }


    private void getVaildTime(StringBuilder sb, String oldUsedTime, String validTime, String type,String beforeType) {
        int finalD = 0;
        if (oldUsedTime.indexOf(type) > 0){
            int year = this.getAgeLimit(oldUsedTime,type,beforeType);
            int vY = 0;
            if (validTime.indexOf(type) > 0){
                vY = this.getAgeLimit(validTime,type,beforeType);
            }
            finalD = year + vY;
        }else{
            if (validTime.indexOf(type) > 0){
                int vY = this.getAgeLimit(validTime,type,beforeType);
                finalD = vY;
            }
        }
        if (finalD > 0){
            sb.append(finalD);
            sb.append(type);
        }
    }

    private void totalMonth(StringBuilder sb, String oldUsedTime, String validTime) {
        this.getVaildTime(sb,oldUsedTime,validTime,"M","Y");
    }


    @Override
    public boolean syncAccountInfo(SysUser editUserInfo, Long servicesId) {
        ServicesBaseInfo baseInfo = servicesBaseInfoService.getById(servicesId);
        Integer useType = baseInfo.getServicesType() == 0 ? 1 : 0;
        SysUser update = new SysUser();
        update.setUseType(useType);
        update.setId(editUserInfo.getId());
        update.setServicesFlag(1);
        update(update);
        return true;
    }

    @Override
    public void batchModifyPassword(boolean random, String password, String loginName, String userIds) {
        if (random){
            password = getStringRandom(6);
        }

        String md5Password = new Md5().getMd5Password(password);

        sysUserDao.batchUpdatePasswordByUserIds(password,md5Password, Arrays.stream(userIds.split(",")).map(Integer::parseInt).collect(Collectors.toList()), loginName);
    }

    private Date fetchDate(Date currentDate, int mount, int timeUnit) {
        Date result = currentDate;
        switch (timeUnit + "") {
            case PRICE_UNIT_YEAR:
                result = DateUtils.addYears(currentDate, mount);
                break;
            case ServicesPurchaseConstant.PRICE_UNIT_MONTH:
                result = DateUtils.addMonths(currentDate, mount);
                break;
            case ServicesPurchaseConstant.PRICE_UNIT_DAY:
                result = DateUtils.addDays(currentDate, mount);
                break;
            default:
                break;
        }
        return result;
    }

    private void handlerPackageUserJurisdiction(Long userId, String mobile, List<ServicesRoleRel> servicesRoleRels) {
        List<Integer> roleids = servicesRoleRels.stream().map(item -> item.getRoleId()).collect(Collectors.toList());
        if (Objects.nonNull(roleids)){
            Set<Long> platformIds = sysRoleService.getRolePlatformId(roleids);
            logger.info("需要开通的平台权限 =>{}" + (platformIds == null ? 0 : platformIds.toString()));

            if (platformIds != null) {
                //构建UserJurisdiction
                List<UserJurisdiction> userJurisdictions = platformIds.stream()
                        .map(platformId -> {
                            return buildUserJurisdiction(userId, platformId, mobile);
                        })
                        .collect(Collectors.toList());

                userJurisdictionService.batchUserJurisdictions(userJurisdictions);
            }
        }
    }

    private void handlerPackageUserRoles(Long userId, List<ServicesRoleRel> servicesRoleRels) {

        List<SysUserRole> userRoles = servicesRoleRels.stream().map(m -> {
            Date now = new Date();
            SysUserRole u = new SysUserRole();
            u.setUserId(userId);
            u.setGmtCreate(now);
            u.setGmtModified(now);
            u.setIsDeleted(0);
            u.setRoleId(new Long(m.getRoleId()));
            u.setCreator("system");
            u.setModifier("system");
            u.setSysCode(Utils.getCurrentDateTime(Utils.DATETIMESSS) + "_" + Utils.generateRandomDigitString(6));
            return u;
        }).collect(Collectors.toList());
        //批量插入用户角色权限
        if (userRoles != null && !userRoles.isEmpty()) {
            sysRoleService.batchUserRole(userRoles);
        }

    }

    private void handlerOldUserInfo(Integer userId) {
            logger.info("开始删除老账号权限 =>{}" + userId);
            //获取用户的角色权限
            Set<SysUserRole> userRoles = sysRoleService.getUserRolesByUserId(Long.valueOf(userId + ""));
            //获取用户的角色组权限
            Set<SysUserRoleGroup> userGroupRole = sysRoleGroupService.getUserRoleGroupByUserId(Long.valueOf(userId + ""));
            //获取用户的平台权限
            List<UserJurisdiction> userJurisdictions = userJurisdictionService.queryByUserId(Long.valueOf(userId + ""));
            //开始删除权限
            if (!CollectionUtils.isEmpty(userRoles)){
                List<Integer> roleIds = userRoles.stream().map(id -> Integer.valueOf(id.getId() + "")).collect(Collectors.toList());
                sysRoleService.delUserRole(userId,roleIds);
            }

            if (!CollectionUtils.isEmpty(userGroupRole)){
                List<Integer> roleGroupIds = userGroupRole.stream().map(id -> Integer.valueOf(id.getId() + "")).collect(Collectors.toList());
                sysRoleGroupService.batchDel(Long.valueOf(userId + ""),roleGroupIds);
            }

            if (!CollectionUtils.isEmpty(userJurisdictions)){
                Set<Long> oldPlatformIds = userJurisdictions.stream().map(UserJurisdiction::getPlatformId).collect(Collectors.toSet());
                //删除用户的平台权限
                userJurisdictionService.batchDelUserJurisdiction(userId,oldPlatformIds);
            }
    }

    /**
     * 缓存发送短信相关信息
     * @author xiaoxc
     * @param mobile
     * @param clientIp
     * @param code
     * @param seqId
     * @param map
     * @date 2019-01-17
     */
    private void cacheSmsRelatedInfo(String mobile, String clientIp, String code, Long seqId, Map<String, Integer> map, String platformCode) {
        String h = UserConstant.SESSION_SMS_CODE;
        /*if(BasePlatform.PLATFORM_CODE_PC_2B.equals(platformCode) ){
            h = h + ":" + platformCode;
        }*/
        // 手机验证码添加到缓存中
        SmsVo smsVo = new SmsVo();
        smsVo.setCode(code);
        smsVo.setSendTime(seqId);
        smsVo.setVerifyCount(3);
        redisTemplate.opsForHash().put(h, mobile, gson.toJson(smsVo));

        // 获取客户端操作发送的次数和手机端接收的次数
        Integer sendCount = 0;
        Integer receiveCount = 0;
        if (null != map && map.size() > 0) {
            sendCount = map.get(SmsVo.CLIENT_SEND_COUNT);
            receiveCount = map.get(SmsVo.MOBILE_RECEIVE_COUNT);
        }
        String smsCacheKey = UserConstant.SESSION_SMS_CODE + ":" + Utils.getDate();
        // 记录IP操作时间及发送短信数量
        SmsVo.ClientIpSendVo clientIpSendVo = new SmsVo.ClientIpSendVo();
        clientIpSendVo.setSendTime(seqId);
        clientIpSendVo.setSendCount(null != sendCount ? ++sendCount : 1);
        redisTemplate.opsForHash().put(smsCacheKey, clientIp, gson.toJson(clientIpSendVo));

        // 记录手机哈操作时间及接受短信数量
        SmsVo.MobileReceiveVo mobileReceiveVo = new SmsVo.MobileReceiveVo();
        mobileReceiveVo.setReceiveTime(seqId);
        mobileReceiveVo.setReceiveCount(null != receiveCount ? ++receiveCount : 1);
        redisTemplate.opsForHash().put(smsCacheKey, mobile, gson.toJson(mobileReceiveVo));
        //设置超时时间一天
        redisTemplate.expire(smsCacheKey, 1, TimeUnit.DAYS);
    }

    @Override
    public Integer getMasterUserId(Integer userId){
        return sysUserDao.getMasterUserId(userId);
    }
}

