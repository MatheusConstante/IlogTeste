package com.ilog.teste.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

import com.ilog.teste.Model.Course;
import com.ilog.teste.Model.Log;
import com.ilog.teste.Repository.CourseRepository;
import com.ilog.teste.Repository.LogRepository;

@RestController
@RequestMapping("/api")
public class CourseController {
    @Autowired
    CourseRepository courseRepository;
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

    @DeleteMapping("/courses")
    public void deleteCourse(@RequestBody Course course) {
        Course tempCourse = getCourse(course.getId());
        Date now = new Date();
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
}
