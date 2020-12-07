package com.ilog.teste.Controller;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.List;

import com.ilog.teste.Model.Log;
import com.ilog.teste.Repository.LogRepository;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class LogController {
    @Autowired
    private LogRepository logRepository;

    @GetMapping("/log")
    public List<Log> getAllLogs() {
        return logRepository.findAll();
    }

    @GetMapping("/log/download")
    public ResponseEntity<ByteArrayResource> GenerateExcel() {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            List<Log> logList = logRepository.findAll();
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("mySheet");

            XSSFRow row = sheet.createRow(0);
            row.createCell(0).setCellValue("ID");
            row.createCell(1).setCellValue("TIPO");
            row.createCell(2).setCellValue("CADASTRO");
            row.createCell(3).setCellValue("DATA");
            row.createCell(4).setCellValue("OPERACAO");

            for (int i = 0; i < logList.size(); i++) {
                XSSFRow rows = sheet.createRow(i + 1);
                rows.createCell(0).setCellValue(logList.get(i).id);
                rows.createCell(1).setCellValue(logList.get(i).type);
                rows.createCell(2).setCellValue(logList.get(i).title);
                rows.createCell(3).setCellValue(logList.get(i).dateTime);
                rows.createCell(4).setCellValue(logList.get(i).operation);
            }
            
            HttpHeaders header = new HttpHeaders();
            header.setContentType(MediaType.parseMediaType("application/octet-stream"));
            header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Log-" + Calendar.getInstance().getTime() + ".xlsx");
            workbook.write(out);
            workbook.close();
            return new ResponseEntity<>(new ByteArrayResource(out.toByteArray()),
                    header, HttpStatus.CREATED);

        } catch (Exception e) {
            System.out.println("<br> GenerateExcel - Message: " + e);
        }
        return null;
    }
}
