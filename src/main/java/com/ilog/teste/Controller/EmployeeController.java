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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import com.ilog.teste.Model.CourseMembership;
import com.ilog.teste.Model.Employee;
import com.ilog.teste.Model.Log;
import com.ilog.teste.Repository.CourseMembershipRepository;
import com.ilog.teste.Repository.EmployeeRepository;
import com.ilog.teste.Repository.LogRepository;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class EmployeeController {
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    CourseMembershipRepository courseMembershipRepository;
    @Autowired
    private LogRepository logRepository;

    @GetMapping("/employees")
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @GetMapping("/employees/{id}")
    public Employee getEmployee(@PathVariable Long id) {
        return employeeRepository.findById(id).map(employee -> ResponseEntity.ok().body(employee))
                .orElse(ResponseEntity.notFound().build()).getBody();
    }

    @PostMapping("/employees")
    public Employee createEmployee(@RequestBody Employee employee) {
        Date now = new Date();
        Log log = new Log("employee", employee.getName(), now.toString(), "created");
        logRepository.save(log);
        return employeeRepository.save(employee);
    }

    @Transactional
    @DeleteMapping("/employees")
    public void deleteCourse(@RequestBody Employee employee) {
        List<CourseMembership> deletedList = courseMembershipRepository.deleteByEmployee(employee);
        Date now = new Date();
        for(CourseMembership courseMembership:deletedList){
            Log log = new Log("membership",
                    "courseId: " + courseMembership.getCourse().getId() + " / employeeId: " + courseMembership.getEmployee().getId(),
                    now.toString(), "deleted");
            logRepository.save(log);
        }
        Employee tempEmployee = getEmployee(employee.getId());
        Log log = new Log("employee", tempEmployee.getName(), now.toString(), "deleted");
        logRepository.save(log);
        employeeRepository.delete(employee);
    }

    @PatchMapping("/employees/{id}")
    public Employee updateEmployees(@PathVariable("id") long id, @RequestBody Employee employee) {
        Employee tempEmployee = getEmployee(id);
        if (employee.getName() != null) {
            tempEmployee.setName(employee.getName());
        }
        if (employee.getPhone() != null) {
            tempEmployee.setphone(employee.getPhone());
        }
        if (employee.getAddress() != null) {
            tempEmployee.setAddress(employee.getAddress());
        }
        if (employee.getAdmissionDate() != null) {
            try {
                tempEmployee.setAdmissionDate(employee.getAdmissionDate());
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        Date now = new Date();
        Log log = new Log("employee", employee.getName(), now.toString(), "updated");
        logRepository.save(log);
        return employeeRepository.save(tempEmployee);
    }

    @GetMapping("/employees/download")
    public ResponseEntity<ByteArrayResource> GenerateExcel() {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            List<Employee> empList = employeeRepository.findAll();
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("mySheet");

            XSSFRow row = sheet.createRow(0);
            row.createCell(0).setCellValue("ID");
            row.createCell(1).setCellValue("NOME");
            row.createCell(2).setCellValue("TELEFONE");
            row.createCell(3).setCellValue("ENDERECO");
            row.createCell(4).setCellValue("DATA DE ADMISSAO");

            for (int i = 0; i < empList.size(); i++) {
                XSSFRow rows = sheet.createRow(i + 1);
                rows.createCell(0).setCellValue(empList.get(i).getId());
                rows.createCell(1).setCellValue(empList.get(i).getName());
                rows.createCell(2).setCellValue(empList.get(i).getPhone());
                rows.createCell(3).setCellValue(empList.get(i).getAddress());
                rows.createCell(4).setCellValue(empList.get(i).getAdmissionDate());
            }
            
            HttpHeaders header = new HttpHeaders();
            header.setContentType(MediaType.parseMediaType("application/octet-stream"));
            header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Funcionários-" + Calendar.getInstance().getTime() + ".xlsx");
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
