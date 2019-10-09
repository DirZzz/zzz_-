package com.sandu.web.statisticsPlan.controller;

import com.github.pagehelper.PageInfo;
import com.google.common.base.Splitter;
import com.sandu.api.statisticsPlan.model.PlanStatistics;
import com.sandu.api.statisticsPlan.model.PlanUsageAmountDay;
import com.sandu.api.statisticsPlan.service.PlanStatisticsDayService;
import com.sandu.api.statisticsPlan.service.PlanStatisticsHourService;
import com.sandu.api.statisticsPlan.service.PlanUsageAmountDayService;
import com.sandu.api.solution.model.PlanRecommendedQuery;
import com.sandu.api.solution.service.DesignPlanRecommendedService;
import com.sandu.common.BaseController;
import com.sandu.common.ReturnData;
import com.sandu.constant.ResponseEnum;
import com.sandu.util.excel.ExportExcel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.sandu.constant.ResponseEnum.SUCCESS;

@Api(value = "PlanStatistics", tags = "statisticsPlan", description = "方案数据统计模块")
@RestController
@RequestMapping(value = "/v1/planStatistics")
@Slf4j
public class PlanStatisticsController extends BaseController {
    @Autowired
    private PlanStatisticsDayService planStatisticsDayService;
    @Autowired
    private PlanStatisticsHourService planStatisticsHourService;
    @Autowired
    private DesignPlanRecommendedService designPlanRecommendedService;
    @Autowired
    private PlanUsageAmountDayService planUsageAmountDayService;


    @PostMapping("/planDetailList")
    @ApiOperation(value = "方案使用和方案新增数据明细", response = PlanStatistics.class)
    public ReturnData planDetailList(PlanStatistics planStatistics) {
        PageInfo<PlanStatistics> pageInfo = new PageInfo<>();
        List<PlanStatistics> list = new ArrayList<>();
        Integer time = planStatistics.getTime();
        //每小时数据
        if (time == 0) {
            // 方案风格
            if (!StringUtils.isEmpty(planStatistics.getPlanGroupStyleId())) {
                planStatistics.setListGroupStyleId(Splitter.on(",").omitEmptyStrings().trimResults().splitToList(planStatistics.getPlanGroupStyleId())
                        .stream().map(Integer::valueOf).collect(Collectors.toList()));
            }
            //请求分页列表数据
            pageInfo = planStatisticsHourService.selectList(planStatistics);
            list = pageInfo.getList();
        }
        //每天数据
        if (time == 1 || time == 2 || time == 3) {
            // 方案风格
            if (!StringUtils.isEmpty(planStatistics.getPlanGroupStyleId())) {
                planStatistics.setListGroupStyleId(Splitter.on(",").omitEmptyStrings().trimResults().splitToList(planStatistics.getPlanGroupStyleId())
                        .stream().map(Integer::valueOf).collect(Collectors.toList()));
            }
            //请求分页列表数据
            pageInfo = planStatisticsDayService.selectList(planStatistics);
            list = pageInfo.getList();
        }
        if (list == null || list.size() == 0) {
            return ReturnData.builder().success(true).code(ResponseEnum.NOT_CONTENT).message("数据不存在").data(new PlanStatistics()).total(0);
        }
        return ReturnData.builder().list(list).total(pageInfo.getTotal()).code(SUCCESS);
    }


    @PostMapping("/planCountList")
    @ApiOperation(value = "方案使用和方案新增汇总", response = PlanRecommendedQuery.class)
    public ReturnData planCountList(PlanRecommendedQuery query) {

        Integer time = query.getTime();
        PlanStatistics planStatistics = new PlanStatistics();
        planStatistics.setCompanyId(query.getCompanyId());
        planStatistics.setDesignStyleId(query.getDesignRecommendedStyleId());
        planStatistics.setPlanSource(query.getPlanSource());
        planStatistics.setPlanType(query.getRecommendedType());
        planStatistics.setSpaceCommonType(query.getSpaceFunctionId());
        planStatistics.setTime(time);
        planStatistics.setPlanGroupStyleId(query.getPlanGroupStyleId());
        //新增方案和使用方案总数
        Integer newPlanTotal = 0;
        Integer usePlanTotal = 0;
        //每小时数据
        if (time == 0) {
            // 方案风格
            if (!StringUtils.isEmpty(planStatistics.getPlanGroupStyleId())) {
                planStatistics.setListGroupStyleId(Splitter.on(",").omitEmptyStrings().trimResults().splitToList(planStatistics.getPlanGroupStyleId())
                        .stream().map(Integer::valueOf).collect(Collectors.toList()));
            }
            PlanStatistics planTotal = planStatisticsHourService.selectPlanTotal(planStatistics);
            if (planTotal != null) {
                newPlanTotal = planTotal.getNewPlanTotal();
                usePlanTotal = planTotal.getUsePlanTotal();
            }
        }
        //每天数据
        if (time == 1 || time == 2 || time == 3) {
            // 方案风格
            if (!StringUtils.isEmpty(planStatistics.getPlanGroupStyleId())) {
                planStatistics.setListGroupStyleId(Splitter.on(",").omitEmptyStrings().trimResults().splitToList(planStatistics.getPlanGroupStyleId())
                        .stream().map(Integer::valueOf).collect(Collectors.toList()));
            }
            PlanStatistics planTotal = planStatisticsDayService.selectPlanTotal(planStatistics);
            if (planTotal != null) {
                newPlanTotal = planTotal.getNewPlanTotal();
                usePlanTotal = planTotal.getUsePlanTotal();
            }
        }
        // 方案风格
        if (!StringUtils.isEmpty(query.getPlanGroupStyleId())) {
            query.setListGroupStyleId(Splitter.on(",").omitEmptyStrings().trimResults().splitToList(query.getPlanGroupStyleId())
                    .stream().map(Integer::valueOf).collect(Collectors.toList()));
        }
        //累计上架方案数
        Integer shelfPlanCount = designPlanRecommendedService.selectShelfPlanCount(query);
        //累计总方案数
        Integer planCount = designPlanRecommendedService.selectPlanCount(query);

        PlanStatistics plan = new PlanStatistics();
        plan.setUsePlanTotal(usePlanTotal);
        plan.setNewPlanTotal(newPlanTotal);
        plan.setShelfPlanCount(shelfPlanCount);
        plan.setPlanCount(planCount);

        if (plan == null) {
            return ReturnData.builder().success(true).code(ResponseEnum.NOT_CONTENT).message("数据不存在").data(new PlanStatistics());
        }

        return ReturnData.builder().code(ResponseEnum.SUCCESS).data(plan);
    }


    @PostMapping("/hotPlanList")
    @ApiOperation(value = "热门方案统计", response = PlanUsageAmountDay.class)
    public ReturnData hotPlanList(PlanUsageAmountDay planUsageAmountDay) {
        List<PlanUsageAmountDay> list = new ArrayList<>();

        // 方案风格
        if (!StringUtils.isEmpty(planUsageAmountDay.getPlanGroupStyleId())) {
            planUsageAmountDay.setListGroupStyleId(Splitter.on(",").omitEmptyStrings().trimResults().splitToList(planUsageAmountDay.getPlanGroupStyleId())
                    .stream().map(Integer::valueOf).collect(Collectors.toList()));
        }
        list = planUsageAmountDayService.selectHotPlan(planUsageAmountDay);

        if (list == null || list.size() == 0) {
            PlanUsageAmountDay usageAmountDay = new PlanUsageAmountDay();
            usageAmountDay.setPlanType(0);
            usageAmountDay.setPlanSource("");
            usageAmountDay.setDesignStyleId(0);
            return ReturnData.builder().success(true).code(ResponseEnum.NOT_CONTENT).message("数据不存在").data(usageAmountDay);
        }
        return ReturnData.builder().code(ResponseEnum.SUCCESS).data(list);
    }

    @PostMapping("/hotPlanDetailList")
    @ApiOperation(value = "热门方案数据明细", response = PlanUsageAmountDay.class)
    public ReturnData hotPlanDetailList(PlanUsageAmountDay planUsageAmountDay) {
        PageInfo<PlanUsageAmountDay> pageInfo = new PageInfo<>();
        List<PlanUsageAmountDay> list = new ArrayList<>();

        // 方案风格
        if (!StringUtils.isEmpty(planUsageAmountDay.getPlanGroupStyleId())) {
            planUsageAmountDay.setListGroupStyleId(Splitter.on(",").omitEmptyStrings().trimResults().splitToList(planUsageAmountDay.getPlanGroupStyleId())
                    .stream().map(Integer::valueOf).collect(Collectors.toList()));
        }
        pageInfo = planUsageAmountDayService.selectDetailList(planUsageAmountDay);
        list = pageInfo.getList();

        if (list == null || list.size() == 0) {
            PlanUsageAmountDay usageAmountDay = new PlanUsageAmountDay();
            usageAmountDay.setPlanType(0);
            usageAmountDay.setPlanSource("");
            usageAmountDay.setDesignStyleId(0);
            return ReturnData.builder().success(true).code(ResponseEnum.NOT_CONTENT).message("数据不存在").data(usageAmountDay).total(0);
        }
        return ReturnData.builder().list(list).total(pageInfo.getTotal()).code(SUCCESS);
    }


    @GetMapping("/planUseDetailExport")
    @ApiOperation(value = "方案使用数据明细导出", response = PlanStatistics.class)
    public void planUseDetailExport(HttpServletResponse response, PlanStatistics planStatistics) throws Exception {
        log.info("方案使用数据明细-入参,statisticsPlan:{}", planStatistics);
        //查询导出的数据
        PageInfo<PlanStatistics> pageInfo = new PageInfo<>();
        List<PlanStatistics> datas = new ArrayList<PlanStatistics>();
        Integer time = planStatistics.getTime();

        //每小时数据
        if (time == 0) {
            // 方案风格
            if (!StringUtils.isEmpty(planStatistics.getPlanGroupStyleId())) {
                planStatistics.setListGroupStyleId(Splitter.on(",").omitEmptyStrings().trimResults().splitToList(planStatistics.getPlanGroupStyleId())
                        .stream().map(Integer::valueOf).collect(Collectors.toList()));
            }
            //请求分页列表数据
            pageInfo = planStatisticsHourService.selectList(planStatistics);
            datas = pageInfo.getList();
        }

        //每天数据
        if (time == 1 || time == 2 || time == 3) {
            // 方案风格
            if (!StringUtils.isEmpty(planStatistics.getPlanGroupStyleId())) {
                planStatistics.setListGroupStyleId(Splitter.on(",").omitEmptyStrings().trimResults().splitToList(planStatistics.getPlanGroupStyleId())
                        .stream().map(Integer::valueOf).collect(Collectors.toList()));
            }
            //请求分页列表数据
            pageInfo = planStatisticsDayService.selectList(planStatistics);
            datas = pageInfo.getList();
        }
        if (datas == null || datas.size() == 0) {
            return;
        }
        try {
            List<Map<String, Object>> infoLs = this.createExcelRecord(datas);
            // 导出表格名称
            String filename = "方案使用数据明细导出";
            String columns[] = {"序号", "开始时间", "方案使用数据"};
            String keys[] = {
                    "id",
                    "startTime",
                    "usePlanCount"
            };
            Workbook wookbook = ExportExcel.createWorkBook(infoLs, keys, columns);
            try {
                response.setContentType("application/vnd.ms-excel");
//	        	response.setContentType("application/octet-stream");
                response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(filename + ".xls", "utf-8"));
                OutputStream outputStream = response.getOutputStream();
//		        FileOutputStream fileOutputStream = new FileOutputStream(new File("D:\\excle_test\\测试.xls"));
                wookbook.write(outputStream);
//		        wookbook.write(fileOutputStream);
                outputStream.flush();
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private List<Map<String, Object>> createExcelRecord(List<PlanStatistics> list) {
        List<Map<String, Object>> listmap = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("sheetName", "sheet1");
        listmap.add(map);
        PlanStatistics project = null;
        int index = 1;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            for (int j = 0; j < list.size(); j++) {
                project = list.get(j);
                Map<String, Object> mapValue = new HashMap<String, Object>();
                mapValue.put("id", index++);
                Date date = sdf.parse(project.getStartTime());
                mapValue.put("startTime", sdf.format(date));
                mapValue.put("usePlanCount", project.getUsePlanCount());
                listmap.add(mapValue);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return listmap;
    }


    @GetMapping("/planNewDetailExport")
    @ApiOperation(value = "方案新增数据明细导出", response = PlanStatistics.class)
    public void planNewDetailExport(HttpServletResponse response, PlanStatistics planStatistics) throws Exception {
        log.info("方案新增数据明细-入参,statisticsPlan:{}", planStatistics);
        //查询导出的数据
        PageInfo<PlanStatistics> pageInfo = new PageInfo<>();
        List<PlanStatistics> datas = new ArrayList<PlanStatistics>();
        Integer time = planStatistics.getTime();

        //每小时数据
        if (time == 0) {
            // 方案风格
            if (!StringUtils.isEmpty(planStatistics.getPlanGroupStyleId())) {
                planStatistics.setListGroupStyleId(Splitter.on(",").omitEmptyStrings().trimResults().splitToList(planStatistics.getPlanGroupStyleId())
                        .stream().map(Integer::valueOf).collect(Collectors.toList()));
            }
            //请求分页列表数据
            pageInfo = planStatisticsHourService.selectList(planStatistics);
            datas = pageInfo.getList();
        }
        //每天数据
        if (time == 1 || time == 2 || time == 3) {
            // 方案风格
            if (!StringUtils.isEmpty(planStatistics.getPlanGroupStyleId())) {
                planStatistics.setListGroupStyleId(Splitter.on(",").omitEmptyStrings().trimResults().splitToList(planStatistics.getPlanGroupStyleId())
                        .stream().map(Integer::valueOf).collect(Collectors.toList()));
            }
            //请求分页列表数据
            pageInfo = planStatisticsDayService.selectList(planStatistics);
            datas = pageInfo.getList();
        }
        if (datas == null || datas.size() == 0) {
            return;
        }
        try {
            List<Map<String, Object>> infoLs = this.createExcelRecordTwo(datas);
            // 导出表格名称
            String filename = "方案新增数据明细导出";
            String columns[] = {"序号", "开始时间", "方案新增数据"};
            String keys[] = {
                    "id",
                    "startTime",
                    "newPlanCount"
            };
            Workbook wookbook = ExportExcel.createWorkBook(infoLs, keys, columns);
            try {
                response.setContentType("application/vnd.ms-excel");
//	        	response.setContentType("application/octet-stream");
                response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(filename + ".xls", "utf-8"));
                OutputStream outputStream = response.getOutputStream();
//		        FileOutputStream fileOutputStream = new FileOutputStream(new File("D:\\excle_test\\测试.xls"));
                wookbook.write(outputStream);
//		        wookbook.write(fileOutputStream);
                outputStream.flush();
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Map<String, Object>> createExcelRecordTwo(List<PlanStatistics> list) {
        List<Map<String, Object>> listmap = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("sheetName", "sheet1");
        listmap.add(map);
        PlanStatistics project = null;
        int index = 1;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            for (int j = 0; j < list.size(); j++) {
                project = list.get(j);
                Map<String, Object> mapValue = new HashMap<String, Object>();
                mapValue.put("id", index++);
                Date date = sdf.parse(project.getStartTime());
                mapValue.put("startTime", sdf.format(date));
                mapValue.put("newPlanCount", project.getNewPlanCount());
                listmap.add(mapValue);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return listmap;
    }

    @GetMapping("/hotPlanDetailExport")
    @ApiOperation(value = "热门方案数据明细导出", response = PlanUsageAmountDay.class)
    public void hotPlanDetailExport(HttpServletResponse response, PlanUsageAmountDay query) throws Exception {
        log.info("热门方案数据明细-入参,query:{}", query);
        //查询导出的数据
        PageInfo<PlanUsageAmountDay> pageInfo = new PageInfo<>();
        List<PlanUsageAmountDay> datas = new ArrayList<>();

        // 方案风格
        if (!StringUtils.isEmpty(query.getPlanGroupStyleId())) {
            query.setListGroupStyleId(Splitter.on(",").omitEmptyStrings().trimResults().splitToList(query.getPlanGroupStyleId())
                    .stream().map(Integer::valueOf).collect(Collectors.toList()));
        }
        pageInfo = planUsageAmountDayService.selectDetailList(query);
        datas = pageInfo.getList();
        if (datas == null || datas.size() == 0) {
            return;
        }
        try {
            List<Map<String, Object>> infoLs = this.createExcelRecordThree(datas);
            // 导出表格名称
            String filename = "方案使用数据明细导出";
            String columns[] = {"序号", "方案名称", "方案编码", "方案类型", "方案来源", "方案风格"
            };
            String keys[] = {
                    "id",
                    "planName",
                    "planCode",
                    "planType",
                    "planSource",
                    "designStyle"
            };
            Workbook wookbook = ExportExcel.createWorkBook(infoLs, keys, columns);
            try {
                response.setContentType("application/vnd.ms-excel");
//                response.setContentType("application/octet-stream");
                response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(filename + ".xls", "utf-8"));
                OutputStream outputStream = response.getOutputStream();
//                FileOutputStream fileOutputStream = new FileOutputStream(new File("D:\\excle\\测试2.xls"));
                wookbook.write(outputStream);
//                wookbook.write(fileOutputStream);
                outputStream.flush();
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Map<String, Object>> createExcelRecordThree(List<PlanUsageAmountDay> list) {
        List<Map<String, Object>> listmap = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("sheetName", "sheet1");
        listmap.add(map);
        PlanUsageAmountDay project = null;
        int index = 1;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (int j = 0; j < list.size(); j++) {
            project = list.get(j);
            Map<String, Object> mapValue = new HashMap<String, Object>();
            mapValue.put("id", index++);
            mapValue.put("planName", project.getPlanName());
            mapValue.put("planCode", project.getPlanCode());
            if (project.getPlanType() == 1) {
                mapValue.put("planType", "普通方案");
            } else if (project.getPlanType() == 2) {
                mapValue.put("planType", "智能方案");
            } else {
                mapValue.put("planType", project.getPlanType());
            }
            if (project.getPlanSource().equals("diy")) {
                mapValue.put("planSource", "内部制作");
            } else if (project.getPlanSource().equals("deliver")) {
                mapValue.put("planSource", "企业交付");
            } else if (project.getPlanSource().equals("huxing")) {
                mapValue.put("planSource", "户型方案");
            } else if (project.getPlanSource().equals("share")) {
                mapValue.put("planSource", "分享方案");
            } else {
                mapValue.put("planSource", project.getPlanSource());
            }
            mapValue.put("designStyle", project.getDesignStyle());
            listmap.add(mapValue);
        }
        return listmap;
    }

    @PostMapping("/planChartList")
    @ApiOperation(value = "方案使用和方案新增数据图表", response = PlanStatistics.class)
    public ReturnData planChartList(PlanStatistics planStatistics) {
        Integer time = planStatistics.getTime();
        List<PlanStatistics> list = new ArrayList<>();
        Calendar cal=Calendar.getInstance();
        cal.add(Calendar.MONTH, -1); // 时间减去一天 上个月
        int days=cal.getActualMaximum(Calendar.DAY_OF_MONTH); //调取当月的天数
        planStatistics.setDay(days);
        //每小时数据
        if (time == 0) {
            list = planStatisticsHourService.selectHourPlanChart(planStatistics);
        }
        //每天数据
        if (time == 1 || time == 2 || time == 3) {
            list = planStatisticsDayService.selectDayPlanChart(planStatistics);
        }
        if (list == null || list.size() == 0) {
            return ReturnData.builder().success(true).code(ResponseEnum.NOT_CONTENT).message("数据不存在").data(new PlanStatistics());
        }
        return ReturnData.builder().list(list).code(SUCCESS);
    }

}
