package com.ilog.teste.Repository;

import java.util.List;

import com.ilog.teste.Model.Course;
import com.ilog.teste.Model.CourseMembership;
import com.ilog.teste.Model.Employee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseMembershipRepository extends JpaRepository<CourseMembership, Long> {

    List<CourseMembership> findByEmployee(Employee employee);
    List<CourseMembership> deleteByEmployee(Employee employee);
    List<CourseMembership> findByCourse(Course course);
    List<CourseMembership> deleteByCourse(Course course);

}
