package com.ilog.teste.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import com.ilog.teste.Model.Course;
import com.ilog.teste.Model.CourseMembership;
import com.ilog.teste.Model.Employee;
import com.ilog.teste.Repository.CourseMembershipRepository;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class CourseMembershipController {
    @Autowired
    CourseMembershipRepository courseMembershipRepository;

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
        return courseMembershipRepository.save(courseMembership);
    }

    @DeleteMapping("/memberships")
    public void deleteMembership(@RequestBody CourseMembership courseMembership) {
        courseMembershipRepository.delete(courseMembership);
    }

    public void deleteMembershipByEmployee(Employee employee) {
        courseMembershipRepository.deleteByEmployee(employee);
    }
}
