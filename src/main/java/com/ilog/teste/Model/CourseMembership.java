package com.ilog.teste.Model;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Table(name = "course_membership")
@EntityListeners(AuditingEntityListener.class)
public class CourseMembership {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne()
    @JoinColumn(name = "course_id")
    private Course course;

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployeeId(Employee employee) {
        this.employee = employee;
    }

    public Course getCourse() {
        return this.course;
    }

    public void setCourseId(Course course) {
        this.course = course;
    }

    public Long getId() {
        return this.id;
    }

}