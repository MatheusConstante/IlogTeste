package com.ilog.teste.Repository;

import com.ilog.teste.Model.Course;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long>{
    
}
