package com.sandu.web.statisticsCompany.controller;

import com.github.pagehelper.PageInfo;
import com.sandu.api.statisticsCompany.model.CompanyStatisticsDay;
import com.sandu.api.statisticsCompany.service.CompanyStatisticsDayService;
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

@Api(value = "CompanyStatistics", tags = "statisticsCompany", description = "企业数据统计模块")
@RestController
@RequestMapping(value = "/v1/companyStatistics")
@Slf4j
public class CompanyStatisticsController extends BaseController {
    @Autowired
    private CompanyStatisticsDayService companyStatisticsDayService;

    @PostMapping("/hotCompanyList")
    @ApiOperation(value = "热门企业使用统计", response = CompanyStatisticsDay.class)
    public ReturnData hotCompanyList(CompanyStatisticsDay companyStatisticsDay) {
        List<CompanyStatisticsDay> list = new ArrayList<>();
        list = companyStatisticsDayService.selectHotCompany(companyStatisticsDay);
        if (list == null || list.size() == 0) {
            return ReturnData.builder().success(true).code(ResponseEnum.NOT_CONTENT).message("数据不存在").data(new CompanyStatisticsDay());
        }
        return ReturnData.builder().code(ResponseEnum.SUCCESS).data(list);
    }

    @PostMapping("/companyDetailList")
    @ApiOperation(value = "热门企业使用数据明细", response = CompanyStatisticsDay.class)
    public ReturnData companyDetailList(CompanyStatisticsDay companyStatisticsDay) {
        PageInfo<CompanyStatisticsDay> pageInfo = new PageInfo<>();
        List<CompanyStatisticsDay> list = new ArrayList<>();
        pageInfo = companyStatisticsDayService.selectDetailList(companyStatisticsDay);
        list = pageInfo.getList();

        if (list == null || list.size() == 0) {
            CompanyStatisticsDay statisticsDay = new CompanyStatisticsDay();
            statisticsDay.setCompanyType(0);
            return ReturnData.builder().success(true).code(ResponseEnum.NOT_CONTENT).message("数据不存在").data(statisticsDay).total(0);
        }
        return ReturnData.builder().list(list).total(pageInfo.getTotal()).code(SUCCESS);
    }


    @GetMapping("/companyDetailExport")
    @ApiOperation(value = "热门企业数据明细导出", response = CompanyStatisticsDay.class)
    public void hotCompanyDetailExport(HttpServletResponse response, CompanyStatisticsDay query) throws Exception {
        log.info("热门企业数据明细-入参,CompanyStatisticsDay:{}", query);
        //查询导出的数据
        PageInfo<CompanyStatisticsDay> pageInfo = new PageInfo<>();
        List<CompanyStatisticsDay> datas = new ArrayList<>();

        pageInfo = companyStatisticsDayService.selectDetailList(query);
        datas = pageInfo.getList();
        if (datas == null || datas.size() == 0) {
            return;
        }
        try {
            List<Map<String, Object>> infoLs = this.createExcelRecord(datas);
            // 导出表格名称
            String filename = "热门企业使用数据明细";
            String columns[] = {"序号", "企业名称", "企业类型", "品牌", "新增用户数", "活跃用户数", "开通人数", "使用率"};
            String keys[] = {
                    "id",
                    "companyName",
                    "companyType",
                    "brandName",
                    "newUserCount",
                    "activeUserCount",
                    "userAccountCount",
                    "userEffectiveRate"
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

    private List<Map<String, Object>> createExcelRecord(List<CompanyStatisticsDay> list) {
        List<Map<String, Object>> listmap = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("sheetName", "sheet1");
        listmap.add(map);
        CompanyStatisticsDay project = null;
        int index = 1;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (int j = 0; j < list.size(); j++) {
            project = list.get(j);
            Map<String, Object> mapValue = new HashMap<String, Object>();
            mapValue.put("id", index++);
            mapValue.put("companyName", project.getCompanyName());
            //1:厂商、2:经销商、3:门店、4:设计公司、5:装修公司、6:设计师、7:工长（施工单位）
            if (project.getCompanyType() == 1) {
                mapValue.put("companyType", "厂商");
            } else if (project.getCompanyType() == 2) {
                mapValue.put("companyType", "经销商");
            } else if (project.getCompanyType() == 3) {
                mapValue.put("companyType", "门店");
            } else if (project.getCompanyType() == 4) {
                mapValue.put("companyType", "设计公司");
            } else if (project.getCompanyType() == 5) {
                mapValue.put("companyType", "装修公司");
            } else if (project.getCompanyType() == 6) {
                mapValue.put("companyType", "设计师");
            } else if (project.getCompanyType() == 7) {
                mapValue.put("companyType", "工长（施工单位）");
            } else if (project.getCompanyType() == 8) {
                mapValue.put("companyType", "中介");
            } else if (project.getCompanyType() == 9) {
                mapValue.put("companyType", "独立经销商");
            } else if (project.getCompanyType() == 10) {
                mapValue.put("companyType", "定制柜");
            } else {
                mapValue.put("companyType", "其他");
            }
            mapValue.put("brandName", project.getBrandName());
            mapValue.put("newUserCount", project.getNewUserCount());
            mapValue.put("activeUserCount", project.getActiveUserCount());
            mapValue.put("userAccountCount", project.getUserAccountCount());
            mapValue.put("userEffectiveRate", project.getUserEffectiveRate());
            listmap.add(mapValue);
        }
        return listmap;
    }
}
