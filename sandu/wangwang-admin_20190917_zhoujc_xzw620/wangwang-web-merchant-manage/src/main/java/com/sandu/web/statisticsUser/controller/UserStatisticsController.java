package com.sandu.web.statisticsUser.controller;

import com.github.pagehelper.PageInfo;
import com.sandu.api.dictionary.input.SysDictionaryQuery;
import com.sandu.api.dictionary.model.Dictionary;
import com.sandu.api.dictionary.service.DictionaryService;
import com.sandu.api.statisticsUser.model.UserStatistics;
import com.sandu.api.statisticsUser.service.UserStatisticsDayService;
import com.sandu.api.statisticsUser.service.UserStatisticsHourService;
import com.sandu.common.BaseController;
import com.sandu.common.ReturnData;
import com.sandu.constant.ResponseEnum;
import com.sandu.util.excel.ExportExcel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.sandu.constant.ResponseEnum.SUCCESS;

@Api(value = "UserStatistics", tags = "statisticsUser", description = "用户数据统计模块")
@RestController
@RequestMapping(value = "/v1/userStatistics")
@Slf4j
public class UserStatisticsController extends BaseController {

    @Autowired
    private UserStatisticsDayService userStatisticsDayService;
    @Autowired
    private UserStatisticsHourService userStatisticsHourService;
    @Autowired
    private DictionaryService dictionaryService;

    @PostMapping("/userList")
    @ApiOperation(value = "活跃用户和用户新增数据明细", response = UserStatistics.class)
    public ReturnData userList(UserStatistics userStatistics) {
        PageInfo<UserStatistics> pageInfo = new PageInfo<>();
        List<UserStatistics> list = new ArrayList<>();
        Integer time = userStatistics.getTime();

        //每小时数据
        if (time == 0) {
            //请求分页列表数据
            pageInfo = userStatisticsHourService.selectList(userStatistics);
        }
        //每天数据
        if (time == 1 || time == 2 || time == 3) {
            //请求分页列表数据
            pageInfo = userStatisticsDayService.selectList(userStatistics);
        }
        list = pageInfo.getList();
        if (list == null || list.size() == 0) {
            return ReturnData.builder().success(true).code(ResponseEnum.NOT_CONTENT).message("数据不存在").data(new UserStatistics()).total(0);
        }
        return ReturnData.builder().list(list).total(pageInfo.getTotal()).code(SUCCESS);
    }

    @PostMapping("/userCountList")
    @ApiOperation(value = "活跃用户和用户新增数据汇总", response = UserStatistics.class)
    public ReturnData userCountList(UserStatistics query) {
        UserStatistics userTotal = new UserStatistics();
        userTotal = userStatisticsDayService.selectUserTotal(query);

        //累积用户数和未激活用户数据
        UserStatistics user = userStatisticsDayService.selectAccountAndNonactivatedTotal(query);
        if (user == null && userTotal == null) {
            userTotal = new UserStatistics();
            userTotal.setAccountTotal(0);
            userTotal.setNonactivatedTotal(0);
        } else if (user != null && userTotal == null) {
            userTotal = new UserStatistics();
            userTotal.setAccountTotal(user.getAccountTotal());
            userTotal.setNonactivatedTotal(user.getNonactivatedTotal());
        } else if (user != null && userTotal != null) {
            userTotal.setAccountTotal(user.getAccountTotal());
            userTotal.setNonactivatedTotal(user.getNonactivatedTotal());
        }

        if (userTotal == null) {
            return ReturnData.builder().success(true).code(ResponseEnum.NOT_CONTENT).message("数据不存在").data(new UserStatistics());
        }
        return ReturnData.builder().code(ResponseEnum.SUCCESS).data(userTotal);
    }


    @GetMapping("/activeUserDetailExport")
    @ApiOperation(value = "活跃用户数据明细导出", response = UserStatistics.class)
    public void activeUserDetailExport(HttpServletResponse response, UserStatistics userStatistics) throws Exception {
        log.info("活跃用户数据明细-入参,userStatistics:{}", userStatistics);
        //查询导出的数据
        PageInfo<UserStatistics> pageInfo = new PageInfo<>();
        List<UserStatistics> datas = new ArrayList<UserStatistics>();
        Integer time = userStatistics.getTime();

        //每小时数据
        if (time == 0) {
            //请求分页列表数据
            pageInfo = userStatisticsHourService.selectList(userStatistics);
        }
        //每天数据
        if (time == 1 || time == 2 || time == 3) {
            //请求分页列表数据
            pageInfo = userStatisticsDayService.selectList(userStatistics);
        }
        datas = pageInfo.getList();
        if (datas == null || datas.size() == 0) {
            return;
        }
        try {
            List<Map<String, Object>> infoLs = this.createExcelRecord(datas);
            // 导出表格名称
            String filename = "活跃用户数据明细";
            String columns[] = {"序号", "日期", "总活跃用户数", "登录PC端的用户", "登录移动端的用户", "登录商家后台用户"};
            String keys[] = {
                    "id",
                    "startTime",
                    "activeUserCount",
                    "loginUserCountPc2b",
                    "loginUserCountMobile2b",
                    "loginUserCountMerchantManage"
            };
            Workbook wookbook = ExportExcel.createWorkBook(infoLs, keys, columns);
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(filename + ".xls", "utf-8"));
            OutputStream outputStream = response.getOutputStream();
            wookbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Map<String, Object>> createExcelRecord(List<UserStatistics> list) {
        List<Map<String, Object>> listmap = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("sheetName", "sheet1");
        listmap.add(map);
        UserStatistics project = null;
        int index = 1;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            for (int j = 0; j < list.size(); j++) {
                project = list.get(j);
                Map<String, Object> mapValue = new HashMap<String, Object>();
                mapValue.put("id", index++);
                Date date = sdf.parse(project.getStartTime());
                mapValue.put("startTime", sdf.format(date));
                mapValue.put("activeUserCount", project.getActiveUserCount());
                mapValue.put("loginUserCountPc2b", project.getLoginUserCountPC2B());
                mapValue.put("loginUserCountMobile2b", project.getLoginUserCountMobile2B());
                mapValue.put("loginUserCountMerchantManage", project.getLoginUserCountMerchantManage());
                listmap.add(mapValue);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return listmap;
    }


    @GetMapping("/newUserDetailExport")
    @ApiOperation(value = "新增用户数据明细导出", response = UserStatistics.class)
    public void newUserDetailExport(HttpServletResponse response, UserStatistics userStatistics) throws Exception {
        log.info("新增用户数据明细-入参,userStatistics:{}", userStatistics);
        //查询导出的数据
        PageInfo<UserStatistics> pageInfo = new PageInfo<>();
        List<UserStatistics> datas = new ArrayList<UserStatistics>();
        Integer time = userStatistics.getTime();

        //每小时数据
        if (time == 0) {
            //请求分页列表数据
            pageInfo = userStatisticsHourService.selectList(userStatistics);
        }
        //每天数据
        if (time == 1 || time == 2 || time == 3) {
            //请求分页列表数据
            pageInfo = userStatisticsDayService.selectList(userStatistics);
        }
        datas = pageInfo.getList();
        if (datas == null || datas.size() == 0) {
            return;
        }
        try {
            List<Map<String, Object>> infoLs = this.createExcelRecordTwo(datas);
            // 导出表格名称
            String filename = "新增用户数据明细";
            String columns[] = {"序号", "日期", "新增用户数"};
            String keys[] = {
                    "id",
                    "startTime",
                    "newUserCount"
            };
            Workbook wookbook = ExportExcel.createWorkBook(infoLs, keys, columns);
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(filename + ".xls", "utf-8"));
            OutputStream outputStream = response.getOutputStream();
            wookbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Map<String, Object>> createExcelRecordTwo(List<UserStatistics> list) {
        List<Map<String, Object>> listmap = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("sheetName", "sheet1");
        listmap.add(map);
        UserStatistics project = null;
        int index = 1;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            for (int j = 0; j < list.size(); j++) {
                project = list.get(j);
                Map<String, Object> mapValue = new HashMap<String, Object>();
                mapValue.put("id", index++);
                Date date = sdf.parse(project.getStartTime());
                mapValue.put("startTime", sdf.format(date));
                mapValue.put("newUserCount", project.getNewUserCount());
                listmap.add(mapValue);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return listmap;
    }

    @ApiOperation(value = "用户类型列表", response = Dictionary.class)
    @GetMapping("/userTypeList")
    public ReturnData userTypeList(String att1) {
        String userType = "userType";
        
        // update by huangsongbo 2019.09.02
        SysDictionaryQuery query = SysDictionaryQuery.builder().att1(att1).type(userType).isDeleted(0).build();
        
        List<Dictionary> list = dictionaryService.getUserTypeList(query);
        /*List<Dictionary> list = dictionaryService.getUserTypeList(userType, att1);*/
        
        if (list == null) {
            return ReturnData.builder().code(ResponseEnum.ERROR).message("数据不存在");
        }
        
        return ReturnData.builder().list(list).code(SUCCESS);
    }


    @PostMapping("/userChartList")
    @ApiOperation(value = "用户活跃和用户新增数据图表", response = UserStatistics.class)
    public ReturnData userChartList(UserStatistics userStatistics) {
        Integer time = userStatistics.getTime();
        List<UserStatistics> list = new ArrayList<>();
        Calendar cal=Calendar.getInstance();
        cal.add(Calendar.MONTH, -1); // 时间减去一天 上个月
        int days=cal.getActualMaximum(Calendar.DAY_OF_MONTH); //调取当月的天数
        userStatistics.setDay(days);
        //每小时数据
        if (time == 0) {
            list = userStatisticsHourService.selectHourUserChart(userStatistics);
        }
        //每天数据
        if (time == 1 || time == 2 || time == 3) {
            list = userStatisticsDayService.selectDayUserChart(userStatistics);
        }
        if (list == null || list.size() == 0) {
            return ReturnData.builder().success(true).code(ResponseEnum.NOT_CONTENT).message("数据不存在").data(new UserStatistics());
        }
        return ReturnData.builder().list(list).code(SUCCESS);
    }

}
