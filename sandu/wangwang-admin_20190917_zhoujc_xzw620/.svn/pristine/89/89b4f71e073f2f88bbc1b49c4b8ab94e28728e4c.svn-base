package com.sandu.web.user.controller;

import com.github.pagehelper.PageInfo;
import com.sandu.api.solution.input.UserAppointmentQuery;
import com.sandu.api.solution.model.MiniProObtainMobileDetail;
import com.sandu.api.solution.model.MiniProUserAppointmentDetail;
import com.sandu.api.solution.service.MiniProObtainMobileDetailService;
import com.sandu.api.solution.service.MiniProUserAppointmentDetailService;
import com.sandu.common.ReturnData;
import com.sandu.util.excel.ExportExcel;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/v1/appointment")
public class MiniProUserAppointmentController {

    @Autowired
    private MiniProUserAppointmentDetailService miniProUserAppointmentDetailService;

    @Autowired
    private MiniProObtainMobileDetailService miniProObtainMobileDetailService;

    @PostMapping("/list")
    public ReturnData appointmentList(@RequestBody UserAppointmentQuery query){

        if (null == query.getCompanyId()){
            return ReturnData.builder().success(false).message("companyId is null!");
        }

        try {
            PageInfo<MiniProUserAppointmentDetail> pageInfo = miniProUserAppointmentDetailService.getPageList(query);
            return ReturnData.builder().success(true).list(pageInfo.getList()).total(pageInfo.getTotal());
        } catch (Exception e) {
            log.error("获取列表失败",e);
            return ReturnData.builder().success(false).message("获取列表异常");
        }
    }

    @GetMapping("/detail")
    public ReturnData appointmentLDetail(Long id){

        if (null == id){
            return ReturnData.builder().success(false).message("param is error");
        }

        MiniProUserAppointmentDetail detail = miniProUserAppointmentDetailService.get(id);
        return ReturnData.builder().success(true).data(detail);
    }

    @PostMapping("/update")
    public ReturnData appointmentUpdate(String remake,Integer status,Long id){

        if (null == id){
            return ReturnData.builder().success(false).message("param is error");
        }

        int update = miniProUserAppointmentDetailService.updateRemakeAndStatusById(remake,status,id);

        if (update > 0){
            return ReturnData.builder().success(true).message("更新成功");
        }

        return ReturnData.builder().success(false).message("更新失败");
    }

    @PostMapping("export")
    public void appointmentExport(@RequestBody UserAppointmentQuery query, HttpServletResponse response) {

        List<MiniProUserAppointmentDetail> datas = miniProUserAppointmentDetailService.findList(query);
        if(datas == null ||datas.isEmpty()) {
            return;
        }
        try {
            List<Map<String, Object>> infoLs = this.createExcelRecord(datas);
            // 导出表格名称
            String filename = "用户预约信息导出";
            String columns[] = {"序号","客户姓名","手机号","预约时间","标题","状态","备注"};
            String keys[] = {
                    "id",
                    "userName",
                    "mobile",
                    "appointmentTime",
                    "appointmentTitle",
                    "state",
                    "remake"
            };
            Workbook wookbook = ExportExcel.createWorkBook(infoLs, keys, columns);
            try {
                response.setContentType("application/vnd.ms-excel");
                response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(filename+".xls", "utf-8"));
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

    @GetMapping(value = "/remove")
    public ReturnData remove(Long id) {
        if (null == id) {
            return ReturnData.builder().success(false).message("param is error");
        }

        try {
            int remove = miniProUserAppointmentDetailService.remove(id);

            if (remove > 0) {
                return ReturnData.builder().success(true).message("删除成功!!");
            }
        } catch (Exception e) {
            log.error("删除异常", e);
            return ReturnData.builder().success(false).message("删除异常!!");
        }
        return ReturnData.builder().success(false).message("删除失败!!");
    }

    private List<Map<String, Object>> createExcelRecord(List<MiniProUserAppointmentDetail> list) {
        List<Map<String, Object>> listmap = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("sheetName", "sheet1");
        listmap.add(map);
        MiniProUserAppointmentDetail project = null;
        int index = 1;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (int j = 0; j < list.size(); j++) {
            project = list.get(j);
            Map<String, Object> mapValue = new HashMap<String, Object>();
            mapValue.put("id", index++);
            mapValue.put("userName", project.getUserName()); // 户型编码
            mapValue.put("mobile", project.getMobile());
            mapValue.put("appointmentTime", format.format(project.getAppointmentTime()));
            mapValue.put("appointmentTitle", project.getAppointmentTitle());
            mapValue.put("state", project.getStatus() == 0 ? "有效" : "无效");
            mapValue.put("remake", project.getRemake());
            listmap.add(mapValue);
        }
        return listmap;
    }

    @GetMapping(value = "/refresh")
    public void refreshData(){
        log.info("########### 更改企业获取手机号预约次数开始 =>{}", LocalDateTime.now());
        List<MiniProObtainMobileDetail> list =  miniProObtainMobileDetailService.findListLimit1();
        List<Long> ids = list.stream().map(MiniProObtainMobileDetail::getId).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(ids)) miniProObtainMobileDetailService.updateCurRemainNumByIds(ids);
        log.info("########### 更改企业获取手机号预约次数end =>{}",list == null ? 0 : list.size());

    }
}
