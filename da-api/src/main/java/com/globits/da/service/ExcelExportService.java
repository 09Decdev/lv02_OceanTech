package com.globits.da.service;

import com.globits.da.domain.entity.Employee;
import com.globits.da.repository.EmployeeRepository;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import java.util.List;

@Service
public class ExcelExportService {
    private EmployeeRepository employeeRepository;
    public ExcelExportService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public void exportToExcel(HttpServletResponse response)throws Exception {
        List<Employee> getEmployees = employeeRepository.findAll();

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Employee");
        HSSFRow row = sheet.createRow(0);
//        STT, Tên , Mã , Email, Phone, Age
        row.createCell(0).setCellValue("STT");
        row.createCell(1).setCellValue("Tên");
        row.createCell(2).setCellValue("Email");
        row.createCell(3).setCellValue("Phone");
        row.createCell(4).setCellValue("Age");

        int dataRow = 1;
        for (Employee employee : getEmployees) {
            HSSFRow dataRow1 = sheet.createRow(dataRow);
            dataRow1.createCell(0).setCellValue(dataRow);
            dataRow1.createCell(1).setCellValue(employee.getName());
            dataRow1.createCell(2).setCellValue(employee.getEmail());
            dataRow1.createCell(3).setCellValue(employee.getPhone());
            dataRow1.createCell(4).setCellValue(employee.getAge());
            dataRow++;
        }
        ServletOutputStream servletOutputStream=response.getOutputStream();
        workbook.write(servletOutputStream);
        servletOutputStream.flush();
        servletOutputStream.close();
    }

}
