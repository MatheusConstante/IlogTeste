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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.ilog.teste.Model.Course;
import com.ilog.teste.Model.CourseMembership;
import com.ilog.teste.Model.Employee;
import com.ilog.teste.Model.Log;
import com.ilog.teste.Repository.CourseMembershipRepository;
import com.ilog.teste.Repository.LogRepository;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class CourseMembershipController {
    @Autowired
    CourseMembershipRepository courseMembershipRepository;
    @Autowired
    private LogRepository logRepository;

    @GetMapping("/memberships")
    public List<CourseMembership> getAllMemberships() {
        return courseMembershipRepository.findAll();
    }

    @GetMapping("/memberships/employee/{employeeId}")
    public List<CourseMembership> getMembershipsByEmployeeId(@PathVariable Long employeeId) {
        Employee employee = new Employee();
        employee.setId(employeeId);
        return courseMembershipRepository.findByEmployee(employee);
    }

    @GetMapping("/memberships/course/{courseId}")
    public List<CourseMembership> getMembershipsByCourseId(@PathVariable Long courseId) {
        Course course = new Course();
        course.setId(courseId);
        return courseMembershipRepository.findByCourse(course);
    }

    @PostMapping("/memberships")
    public CourseMembership createMembership(@RequestBody CourseMembership courseMembership) {
        Date now = new Date();
        Log log = new Log("membership",
                "courseId: " + courseMembership.getCourse().getId() + " / employeeId: " + courseMembership.getEmployee().getId(),
                now.toString(), "created");
        logRepository.save(log);
        return courseMembershipRepository.save(courseMembership);
    }

    @DeleteMapping("/memberships")
    public void deleteMembership(@RequestBody CourseMembership courseMembership) {
        Date now = new Date();
        CourseMembership tempCourseMembership = courseMembershipRepository.findById(courseMembership.getId()).get();
        Log log = new Log("membership",
                "courseId: " + tempCourseMembership.getCourse().getId() + " / employeeId: " + tempCourseMembership.getEmployee().getId(),
                now.toString(), "deleted");
        logRepository.save(log);
        courseMembershipRepository.delete(courseMembership);
    }

    public void deleteMembershipByEmployee(Employee employee) {
        courseMembershipRepository.deleteByEmployee(employee);
    }

    @GetMapping("/memberships/download")
    public ResponseEntity<ByteArrayResource> GenerateExcel() {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            List<CourseMembership> memberList = courseMembershipRepository.findAll();
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("mySheet");

            XSSFRow row = sheet.createRow(0);
            row.createCell(0).setCellValue("ID-MATRICULA");
            row.createCell(1).setCellValue("CURSO");
            row.createCell(2).setCellValue("ID-CURSO");
            row.createCell(3).setCellValue("FUNCIONARIO");
            row.createCell(4).setCellValue("ID-FUNCIONARIO");

            for (int i = 0; i < memberList.size(); i++) {
                XSSFRow rows = sheet.createRow(i + 1);
                rows.createCell(0).setCellValue(memberList.get(i).getId());
                rows.createCell(1).setCellValue(memberList.get(i).getCourse().getTitle());
                rows.createCell(2).setCellValue(memberList.get(i).getCourse().getId());
                rows.createCell(3).setCellValue(memberList.get(i).getEmployee().getName());
                rows.createCell(4).setCellValue(memberList.get(i).getEmployee().getId());
            }

            HttpHeaders header = new HttpHeaders();
            header.setContentType(MediaType.parseMediaType("application/octet-stream"));
            header.set(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=Matrículas-" + Calendar.getInstance().getTime() + ".xlsx");
            workbook.write(out);
            workbook.close();
            return new ResponseEntity<>(new ByteArrayResource(out.toByteArray()), header, HttpStatus.CREATED);

        } catch (Exception e) {
            System.out.println("<br> GenerateExcel - Message: " + e);
        }
        return null;
    }
}
