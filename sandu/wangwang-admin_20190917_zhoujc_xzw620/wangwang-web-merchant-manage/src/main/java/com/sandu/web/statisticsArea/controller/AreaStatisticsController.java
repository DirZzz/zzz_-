package com.sandu.web.statisticsArea.controller;

import com.github.pagehelper.PageInfo;
import com.sandu.api.statisticsArea.model.AreaStatisticsDay;
import com.sandu.api.statisticsArea.service.AreaStatisticsDayService;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sandu.constant.ResponseEnum.SUCCESS;

@Api(value = "AreaStatistics", tags = "statisticsArea", description = "区域数据统计模块")
@RestController
@RequestMapping(value = "/v1/areaStatistics")
@Slf4j
public class AreaStatisticsController extends BaseController {
    @Autowired
    private AreaStatisticsDayService areaStatisticsDayService;

    @PostMapping("/activeUserAreaList")
    @ApiOperation(value = "活跃用户区域数据排行", response = AreaStatisticsDay.class)
    public ReturnData activeUserAreaList(AreaStatisticsDay areaStatisticsDay) {
        List<AreaStatisticsDay> list = new ArrayList<>();
        list = areaStatisticsDayService.selectActiveHotArea(areaStatisticsDay);
        if (list == null || list.size() == 0) {
            return ReturnData.builder().success(true).code(ResponseEnum.NOT_CONTENT).message("数据不存在").data(new AreaStatisticsDay());
        }
        return ReturnData.builder().code(ResponseEnum.SUCCESS).data(list);
    }

    @PostMapping("/newUserAreaList")
    @ApiOperation(value = "新增用户区域数据排行", response = AreaStatisticsDay.class)
    public ReturnData newUserAreaList(AreaStatisticsDay areaStatisticsDay) {
        List<AreaStatisticsDay> list = new ArrayList<>();
        list = areaStatisticsDayService.selectNewHotArea(areaStatisticsDay);
        if (list == null || list.size() == 0) {
            return ReturnData.builder().success(true).code(ResponseEnum.NOT_CONTENT).message("数据不存在").data(new AreaStatisticsDay());
        }
        return ReturnData.builder().code(ResponseEnum.SUCCESS).data(list);
    }

    @PostMapping("/activeUserAreaDetailList")
    @ApiOperation(value = "活跃用户区域数据明细", response = AreaStatisticsDay.class)
    public ReturnData activeUserAreaDetailList(AreaStatisticsDay areaStatisticsDay) {
        PageInfo<AreaStatisticsDay> pageInfo = new PageInfo<>();
        List<AreaStatisticsDay> list = new ArrayList<>();
        pageInfo = areaStatisticsDayService.selectActiveDetailList(areaStatisticsDay);
        list = pageInfo.getList();
        if (list == null || list.size() == 0) {
            return ReturnData.builder().success(true).code(ResponseEnum.NOT_CONTENT).message("数据不存在").data(new AreaStatisticsDay()).total(0);
        }
        return ReturnData.builder().list(list).total(pageInfo.getTotal()).code(SUCCESS);
    }

    @PostMapping("/newUserAreaDetailList")
    @ApiOperation(value = "新增用户区域数据明细", response = AreaStatisticsDay.class)
    public ReturnData newUserAreaDetailList(AreaStatisticsDay areaStatisticsDay) {
        PageInfo<AreaStatisticsDay> pageInfo = new PageInfo<>();
        List<AreaStatisticsDay> list = new ArrayList<>();
        pageInfo = areaStatisticsDayService.selectNewDetailList(areaStatisticsDay);
        list = pageInfo.getList();
        if (list == null || list.size() == 0) {
            return ReturnData.builder().success(true).code(ResponseEnum.NOT_CONTENT).message("数据不存在").data(new AreaStatisticsDay()).total(0);
        }
        return ReturnData.builder().list(list).total(pageInfo.getTotal()).code(SUCCESS);
    }


    @GetMapping("/activeUserAreaDetailExport")
    @ApiOperation(value = "活跃用户区域数据明细导出", response = AreaStatisticsDay.class)
    public void activeUserAreaDetailExport(HttpServletResponse response, AreaStatisticsDay query) throws Exception {
        log.info("活跃用户区域数据明细-入参,AreaStatisticsDay:{}", query);
        //查询导出的数据
        PageInfo<AreaStatisticsDay> pageInfo = new PageInfo<>();
        List<AreaStatisticsDay> datas = new ArrayList<>();
        pageInfo = areaStatisticsDayService.selectActiveDetailList(query);
        datas = pageInfo.getList();
        if (datas == null || datas.size() == 0) {
            return;
        }
        try {
            List<Map<String, Object>> infoLs = this.createExcelRecord(datas, query.getAreaType());
            // 导出表格名称
            String filename = "活跃用户区域数据明细";
            String columns[] = {"序号", "区域", "活跃用户数", "新增用户数"};
            String keys[] = {
                    "id",
                    "areaName",
                    "activeUserCount",
                    "newUserCount"
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

    private List<Map<String, Object>> createExcelRecord(List<AreaStatisticsDay> list, Integer areaType) {
        List<Map<String, Object>> listmap = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("sheetName", "sheet1");
        listmap.add(map);
        AreaStatisticsDay project = null;
        int index = 1;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (int j = 0; j < list.size(); j++) {
            project = list.get(j);
            Map<String, Object> mapValue = new HashMap<String, Object>();
            mapValue.put("id", index++);
            mapValue.put("areaName", project.getAreaName());
            mapValue.put("activeUserCount", project.getActiveUserCount());
            mapValue.put("newUserCount", project.getNewUserCount());
            listmap.add(mapValue);
        }
        return listmap;
    }

    @GetMapping("/newUserAreaDetailExport")
    @ApiOperation(value = "新增用户区域数据明细导出", response = AreaStatisticsDay.class)
    public void newUserAreaDetailExport(HttpServletResponse response, AreaStatisticsDay query) throws Exception {
        log.info("新增用户区域数据明细-入参,AreaStatisticsDay:{}", query);
        //查询导出的数据
        PageInfo<AreaStatisticsDay> pageInfo = new PageInfo<>();
        List<AreaStatisticsDay> datas = new ArrayList<>();
        pageInfo = areaStatisticsDayService.selectNewDetailList(query);
        datas = pageInfo.getList();
        if (datas == null || datas.size() == 0) {
            return ;
        }
        try {
            List<Map<String, Object>> infoLs = this.createExcelRecordTwo(datas, query.getAreaType());
            // 导出表格名称
            String filename = "新增用户区域数据明细";
            String columns[] = {"序号", "区域", "新增用户数", "活跃用户数"};
            String keys[] = {
                    "id",
                    "areaName",
                    "newUserCount",
                    "activeUserCount"
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

    private List<Map<String, Object>> createExcelRecordTwo(List<AreaStatisticsDay> list, Integer areaType) {
        List<Map<String, Object>> listmap = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("sheetName", "sheet1");
        listmap.add(map);
        AreaStatisticsDay project = null;
        int index = 1;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (int j = 0; j < list.size(); j++) {
            project = list.get(j);
            Map<String, Object> mapValue = new HashMap<String, Object>();
            mapValue.put("id", index++);
            mapValue.put("areaName", project.getAreaName());
            mapValue.put("newUserCount", project.getNewUserCount());
            mapValue.put("activeUserCount", project.getActiveUserCount());
            listmap.add(mapValue);
        }
        return listmap;
    }
}
