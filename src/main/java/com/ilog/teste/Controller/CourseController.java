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

import com.ilog.teste.Model.Course;
import com.ilog.teste.Model.CourseMembership;
import com.ilog.teste.Model.Log;
import com.ilog.teste.Repository.CourseMembershipRepository;
import com.ilog.teste.Repository.CourseRepository;
import com.ilog.teste.Repository.LogRepository;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class CourseController {
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    CourseMembershipRepository courseMembershipRepository;
    @Autowired
    private LogRepository logRepository;

    @GetMapping("/courses")
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @GetMapping("/courses/{id}")
    public Course getCourse(@PathVariable Long id) {
        return courseRepository.findById(id).map(course -> ResponseEntity.ok().body(course))
                .orElse(ResponseEntity.notFound().build()).getBody();
    }

    @PostMapping("/courses")
    public Course createCourse(@RequestBody Course course) {
        Date now = new Date();
        Log log = new Log("course", course.getTitle(), now.toString(), "created");
        logRepository.save(log);
        return courseRepository.save(course);
    }

    @Transactional
    @DeleteMapping("/courses")
    public void deleteCourse(@RequestBody Course course) {
        List<CourseMembership> deletedList = courseMembershipRepository.deleteByCourse(course);
        Date now = new Date();
        for (CourseMembership courseMembership : deletedList) {
            Log log = new Log("membership",
                    "courseId: " + courseMembership.getCourse().getId() + " / employeeId: " + courseMembership.getEmployee().getId(),
                    now.toString(), "deleted");
            logRepository.save(log);
        }
        Course tempCourse = getCourse(course.getId());
        Log log = new Log("course", tempCourse.getTitle(), now.toString(), "deleted");
        logRepository.save(log);
        courseRepository.delete(course);
    }

    @PatchMapping("/courses/{id}")
    public void updateCourse(@PathVariable("id") long id, @RequestBody Course course) {
        Course tempCourse = getCourse(id);
        Date now = new Date();
        Log log = new Log("course", tempCourse.getTitle(), now.toString(), "updated");
        logRepository.save(log);
        if (course.getTitle() != null) {
            tempCourse.setTitle(course.getTitle());
        }
        if (course.getDescription() != null) {
            tempCourse.setDescription(course.getDescription());
        }
        if (course.getCourseLength() != null) {
            tempCourse.setCourseLength(course.getCourseLength());
        }
        if (course.getCost() != null) {
            tempCourse.setCost(course.getCost());
        }
        courseRepository.save(tempCourse);
    }

    @GetMapping("/courses/download")
    public ResponseEntity<ByteArrayResource> GenerateExcel() {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            List<Course> courseList = courseRepository.findAll();
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("mySheet");

            XSSFRow row = sheet.createRow(0);
            row.createCell(0).setCellValue("ID");
            row.createCell(1).setCellValue("CURSO");
            row.createCell(2).setCellValue("DESCRICAO");
            row.createCell(3).setCellValue("CARGA HORARIA");
            row.createCell(4).setCellValue("VALOR");

            for (int i = 0; i < courseList.size(); i++) {
                XSSFRow rows = sheet.createRow(i + 1);
                rows.createCell(0).setCellValue(courseList.get(i).getId());
                rows.createCell(1).setCellValue(courseList.get(i).getTitle());
                rows.createCell(2).setCellValue(courseList.get(i).getDescription());
                if (courseList.get(i).getCourseLength() != null) {
                    rows.createCell(3).setCellValue(courseList.get(i).getCourseLength());
                } else {
                    rows.createCell(3).setCellValue("");
                }
                if (courseList.get(i).getCost() != null) {
                    rows.createCell(4).setCellValue(courseList.get(i).getCost());
                } else {
                    rows.createCell(4).setCellValue("");
                }
            }

            HttpHeaders header = new HttpHeaders();
            header.setContentType(MediaType.parseMediaType("application/octet-stream"));
            header.set(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=Cursos-" + Calendar.getInstance().getTime() + ".xlsx");
            workbook.write(out);
            workbook.close();
            return new ResponseEntity<>(new ByteArrayResource(out.toByteArray()), header, HttpStatus.CREATED);

        } catch (Exception e) {
            System.out.println("<br> GenerateExcel - Message: " + e);
        }
        return null;
    }
}
