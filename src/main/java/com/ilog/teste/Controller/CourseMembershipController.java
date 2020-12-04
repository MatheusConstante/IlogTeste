package com.ilog.teste.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import com.ilog.teste.Model.CourseMembership;
import com.ilog.teste.Repository.CourseMembershipRepository;

@RestController
@RequestMapping("/api")
public class CourseMembershipController {
    @Autowired
    CourseMembershipRepository courseMembershipRepository;

    @GetMapping("/memberships")
    public List<CourseMembership> getAllMemberships() {
        return courseMembershipRepository.findAll();
    }

    @PostMapping("/memberships")
    public CourseMembership createMembership(@RequestBody CourseMembership courseMembership) {
        return courseMembershipRepository.save(courseMembership);
    }

    @DeleteMapping("/memberships")
    public void deleteMembership(@RequestBody CourseMembership courseMembership) {
        courseMembershipRepository.delete(courseMembership);
    }
}
