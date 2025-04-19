package com.globits.da.rest;

import com.globits.da.dto.MyFirstApiDto;
import com.globits.da.service.MyFirstApiService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

@RestController
@RequestMapping("/api/da")
public class theFirstApi {
    private final MyFirstApiService myFirstApiService;

    public theFirstApi(MyFirstApiService myFirstApiService) {
        this.myFirstApiService = myFirstApiService;
    }

    @GetMapping
    public String firstGet() {
        return myFirstApiService.myFirstApi();
    }


    @PostMapping("creat")
    public MyFirstApiDto firstApiDto(@RequestParam Integer code,
                                     @RequestParam String name,
                                     @RequestParam int age) {
        return new MyFirstApiDto(code, name, age);
    }

    @PostMapping("/uploadFile")
    public String uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        StringBuilder result = new StringBuilder();

        String fileName = file.getOriginalFilename();
        if (fileName.endsWith(".txt")) {

            String content = new String(file.getBytes());
            result.append("Dòng trong file text: \n");
            for (String line : content.split("\n")) {
                System.out.println(line);
                result.append(line).append("\n");
            }
        } else if (fileName.endsWith(".xlsx")) {
            InputStream inputStream = file.getInputStream();
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0); // Lấy sheet đầu tiên
            Iterator<Row> rowIterator = sheet.iterator();

            result.append("Dòng trong file Excel: \n");
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();

                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    System.out.print(cell.toString() + "\t");
                    result.append(cell.toString()).append("\t");
                }
                System.out.println();
                result.append("\n");
            }
            workbook.close();
        } else {
            return "Chỉ chấp nhận file .txt hoặc .xlsx";
        }

        return result.toString();
    }
}

