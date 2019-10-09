package com.sandu.web.statisticsHouse.controller;

import com.github.pagehelper.PageInfo;
import com.sandu.api.house.service.HouseService;
import com.sandu.api.statisticsHouse.model.HouseStatistics;
import com.sandu.api.statisticsHouse.model.HouseUsageAmountDay;
import com.sandu.api.statisticsHouse.service.HouseStatisticsDayService;
import com.sandu.api.statisticsHouse.service.HouseStatisticsHourService;
import com.sandu.api.statisticsHouse.service.HouseUsageAmountDayService;
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

@Api(value = "HouseStatistics", tags = "statisticsHouse", description = "户型数据统计模块")
@RestController
@RequestMapping(value = "/v1/houseStatistics")
@Slf4j
public class HouseStatisticsController extends BaseController {

    @Autowired
    private HouseStatisticsDayService houseStatisticsDayService;
    @Autowired
    private HouseStatisticsHourService houseStatisticsHourService;
    @Autowired
    private HouseService houseService;
    @Autowired
    private HouseUsageAmountDayService houseUsageAmountDayService;


    @PostMapping("/houseList")
    @ApiOperation(value = "户型使用和户型新增数据明细", response = HouseStatistics.class)
    public ReturnData houseList(HouseStatistics houseStatistics) {
        PageInfo<HouseStatistics> pageInfo = new PageInfo<>();
        List<HouseStatistics> list = new ArrayList<>();
        Integer time = houseStatistics.getTime();

        //每小时数据
        if (time == 0) {
            //请求分页列表数据
            pageInfo = houseStatisticsHourService.selectList(houseStatistics);
            list = pageInfo.getList();
        }
        //每天数据
        if (time == 1 || time == 2 || time == 3) {
            //请求分页列表数据
            pageInfo = houseStatisticsDayService.selectList(houseStatistics);
            list = pageInfo.getList();
        }
        if (list == null || list.size() == 0) {
            return ReturnData.builder().success(true).code(ResponseEnum.NOT_CONTENT).message("数据不存在").data(new HouseStatistics()).total(0);
        }
        return ReturnData.builder().list(list).total(pageInfo.getTotal()).code(SUCCESS);
    }


    @PostMapping("/houseCountList")
    @ApiOperation(value = "户型使用和户型新增汇总", response = HouseStatistics.class)
    public ReturnData houseCountList(HouseStatistics query) {
        Integer time = query.getTime();
        //新增户型和使用户型总数
        Integer newHouseTotal = 0;
        Integer useHouseTotal = 0;
        //每小时数据
        if (time == 0) {
            HouseStatistics houseTotal = houseStatisticsHourService.selectHouseTotal(query);
            if (houseTotal != null) {
                newHouseTotal = houseTotal.getNewHouseTotal();
                useHouseTotal = houseTotal.getUseHouseTotal();
            }
        }
        //每天数据
        if (time == 1 || time == 2 || time == 3) {
            HouseStatistics houseTotal = houseStatisticsDayService.selectHouseTotal(query);
            if (houseTotal != null) {
                newHouseTotal = houseTotal.getNewHouseTotal();
                useHouseTotal = houseTotal.getUseHouseTotal();
            }
        }
        //累计总户型数
        int houseCount = houseService.selectHouseCount(query);

        HouseStatistics house = new HouseStatistics();
        house.setUseHouseTotal(useHouseTotal);
        house.setNewHouseTotal(newHouseTotal);
        house.setHouseCount(houseCount);
        if (house == null) {
            return ReturnData.builder().success(true).code(ResponseEnum.NOT_CONTENT).message("数据不存在").data(new HouseStatistics());
        }
        return ReturnData.builder().code(ResponseEnum.SUCCESS).data(house);
    }

    @PostMapping("/hotHouseList")
    @ApiOperation(value = "热门户型统计", response = HouseUsageAmountDay.class)
    public ReturnData hotHouseList(HouseUsageAmountDay houseUsageAmountDay) {
        List<HouseUsageAmountDay> list = new ArrayList<>();
        list = houseUsageAmountDayService.selectHotHouse(houseUsageAmountDay);
        if (list == null || list.size() == 0) {
            return ReturnData.builder().success(true).code(ResponseEnum.NOT_CONTENT).message("数据不存在").data(new HouseUsageAmountDay());
        }
        return ReturnData.builder().list(list).code(SUCCESS);
    }

    @PostMapping("/hotHouseDetailList")
    @ApiOperation(value = "热门户型数据明细", response = HouseUsageAmountDay.class)
    public ReturnData hotHouseDetailList(HouseUsageAmountDay houseUsageAmountDay) {
        PageInfo<HouseUsageAmountDay> pageInfo = new PageInfo<>();
        List<HouseUsageAmountDay> list = new ArrayList<>();
        pageInfo = houseUsageAmountDayService.selectDetailList(houseUsageAmountDay);
        list = pageInfo.getList();
        if (list == null || list.size() == 0) {
            return ReturnData.builder().success(true).code(ResponseEnum.NOT_CONTENT).message("数据不存在").data(new HouseUsageAmountDay()).total(0);
        }
        return ReturnData.builder().list(list).total(pageInfo.getTotal()).code(SUCCESS);
    }


    @GetMapping("/houseUseDetailExport")
    @ApiOperation(value = "户型使用数据明细导出", response = HouseStatistics.class)
    public void houseUseDetailExport(HttpServletResponse response, HouseStatistics houseStatistics) throws Exception {
        log.info("户型使用数据明细-入参,houseStatistics:{}", houseStatistics);
        //查询导出的数据
        PageInfo<HouseStatistics> pageInfo = new PageInfo<>();
        List<HouseStatistics> datas = new ArrayList<HouseStatistics>();
        Integer time = houseStatistics.getTime();

        //每小时数据
        if (time == 0) {
            //请求分页列表数据
            pageInfo = houseStatisticsHourService.selectList(houseStatistics);
            datas = pageInfo.getList();
        }
        //每天数据
        if (time == 1 || time == 2 || time == 3) {
            //请求分页列表数据
            pageInfo = houseStatisticsDayService.selectList(houseStatistics);
            datas = pageInfo.getList();
        }
        if (datas == null || datas.size() == 0) {
            return;
        }
        try {
            List<Map<String, Object>> infoLs = this.createExcelRecord(datas);
            // 导出表格名称
            String filename = "户型使用数据明细导出";
            String columns[] = {"序号", "日期", "户型使用量"};
            String keys[] = {
                    "id",
                    "startTime",
                    "useHouseCount"
            };
            Workbook wookbook = ExportExcel.createWorkBook(infoLs, keys, columns);
            try {
                response.setContentType("application/vnd.ms-excel");
                response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(filename + ".xls", "utf-8"));
                OutputStream outputStream = response.getOutputStream();
                wookbook.write(outputStream);
                outputStream.flush();
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Map<String, Object>> createExcelRecord(List<HouseStatistics> list) {
        List<Map<String, Object>> listmap = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("sheetName", "sheet1");
        listmap.add(map);
        HouseStatistics project = null;
        int index = 1;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            for (int j = 0; j < list.size(); j++) {
                project = list.get(j);
                Map<String, Object> mapValue = new HashMap<String, Object>();
                mapValue.put("id", index++);
                Date date = sdf.parse(project.getStartTime());
                mapValue.put("startTime", sdf.format(date));
                mapValue.put("useHouseCount", project.getUseHouseCount());
                listmap.add(mapValue);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return listmap;
    }


    @GetMapping("/houseNewDetailExport")
    @ApiOperation(value = "户型新增数据明细导出", response = HouseStatistics.class)
    public void houseNewDetailExport(HttpServletResponse response, HouseStatistics houseStatistics) throws Exception {
        log.info("户型新增数据明细-入参,houseStatistics:{}", houseStatistics);
        //查询导出的数据
        PageInfo<HouseStatistics> pageInfo = new PageInfo<>();
        List<HouseStatistics> datas = new ArrayList<HouseStatistics>();
        Integer time = houseStatistics.getTime();

        //每小时数据
        if (time == 0) {
            //请求分页列表数据
            pageInfo = houseStatisticsHourService.selectList(houseStatistics);
            datas = pageInfo.getList();
        }
        //每天数据
        if (time == 1 || time == 2 || time == 3) {
            //请求分页列表数据
            pageInfo = houseStatisticsDayService.selectList(houseStatistics);
            datas = pageInfo.getList();
        }
        if (datas == null || datas.size() == 0) {
            return;
        }
        try {
            List<Map<String, Object>> infoLs = this.createExcelRecordTwo(datas);
            // 导出表格名称
            String filename = "户型新增数据明细导出";
            String columns[] = {"序号", "日期", "户型新增数据"};
            String keys[] = {
                    "id",
                    "startTime",
                    "newHouseCount"
            };
            Workbook wookbook = ExportExcel.createWorkBook(infoLs, keys, columns);
            try {
                response.setContentType("application/vnd.ms-excel");
                response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(filename + ".xls", "utf-8"));
                OutputStream outputStream = response.getOutputStream();
                wookbook.write(outputStream);
                outputStream.flush();
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Map<String, Object>> createExcelRecordTwo(List<HouseStatistics> list) {
        List<Map<String, Object>> listmap = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("sheetName", "sheet1");
        listmap.add(map);
        HouseStatistics project = null;
        int index = 1;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            for (int j = 0; j < list.size(); j++) {
                project = list.get(j);
                Map<String, Object> mapValue = new HashMap<String, Object>();
                mapValue.put("id", index++);
                Date date = sdf.parse(project.getStartTime());
                mapValue.put("startTime", sdf.format(date));
                mapValue.put("newHouseCount", project.getNewHouseCount());
                listmap.add(mapValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listmap;
    }


    @GetMapping("/hotHouseDetailExport")
    @ApiOperation(value = "热门户型数据明细导出", response = HouseUsageAmountDay.class)
    public void hotHouseDetailExport(HttpServletResponse response, HouseUsageAmountDay query) throws Exception {
        log.info("热门户型数据明细-入参,HouseUsageAmountDay:{}", query);
        //查询导出的数据
        PageInfo<HouseUsageAmountDay> pageInfo = new PageInfo<>();
        List<HouseUsageAmountDay> datas = new ArrayList<>();

        pageInfo = houseUsageAmountDayService.selectDetailList(query);
        datas = pageInfo.getList();
        if (datas == null || datas.size() == 0) {
            return;
        }
        try {
            List<Map<String, Object>> infoLs = this.createExcelRecordThree(datas);
            // 导出表格名称
            String filename = "热门户型使用数据明细导出";
            String columns[] = {"序号", "户型编码", "户型名称", "小区名称"
            };
            String keys[] = {
                    "id",
                    "houseCode",
                    "houseName",
                    "livingName"
            };
            Workbook wookbook = ExportExcel.createWorkBook(infoLs, keys, columns);
            try {
                response.setContentType("application/vnd.ms-excel");
                response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(filename + ".xls", "utf-8"));
                OutputStream outputStream = response.getOutputStream();
                wookbook.write(outputStream);
                outputStream.flush();
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Map<String, Object>> createExcelRecordThree(List<HouseUsageAmountDay> list) {
        List<Map<String, Object>> listmap = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("sheetName", "sheet1");
        listmap.add(map);
        HouseUsageAmountDay project = null;
        int index = 1;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (int j = 0; j < list.size(); j++) {
            project = list.get(j);
            Map<String, Object> mapValue = new HashMap<String, Object>();
            mapValue.put("id", index++);
            mapValue.put("houseCode", project.getHouseCode());
            mapValue.put("houseName", project.getHouseName());
            mapValue.put("livingName", project.getLivingName());
            listmap.add(mapValue);
        }
        return listmap;
    }


    @PostMapping("/houseChartList")
    @ApiOperation(value = "户型使用和户型新增数据图表", response = HouseStatistics.class)
    public ReturnData houseChartList(HouseStatistics houseStatistics) {
        Integer time = houseStatistics.getTime();
        List<HouseStatistics> list = new ArrayList<>();
        Calendar cal=Calendar.getInstance();
        cal.add(Calendar.MONTH, -1); // 时间减去一天 上个月
        int days=cal.getActualMaximum(Calendar.DAY_OF_MONTH); //调取当月的天数
        houseStatistics.setDay(days);
        //每小时数据
        if (time == 0) {
            list = houseStatisticsHourService.selectHourHouseChart(houseStatistics);
        }
        //每天数据
        if (time == 1 || time == 2 || time == 3) {
            list = houseStatisticsDayService.selectDayHouseChart(houseStatistics);
        }
        if (list == null || list.size() == 0) {
            return ReturnData.builder().success(true).code(ResponseEnum.NOT_CONTENT).message("数据不存在").data(new HouseStatistics());
        }
        return ReturnData.builder().list(list).code(SUCCESS);
    }

}
