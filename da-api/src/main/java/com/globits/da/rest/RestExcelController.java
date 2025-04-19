package com.globits.da.rest;

import com.globits.da.service.ExcelExportService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/excel")
public class RestExcelController {
    private final ExcelExportService excelExportService;

    public RestExcelController(ExcelExportService excelExportService) {
        this.excelExportService = excelExportService;
    }

    @GetMapping("/export")
    public void exportExcel(HttpServletResponse response) throws Exception {
        response.setContentType("application/vnd.ms-excel");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment,filename=employees.xls";
        response.setHeader(headerKey, headerValue);
        excelExportService.exportToExcel(response);
    }
}
