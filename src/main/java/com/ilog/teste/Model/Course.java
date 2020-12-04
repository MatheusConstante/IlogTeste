package com.ilog.teste.Model;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "course")
@EntityListeners(AuditingEntityListener.class)
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @OneToMany
    private List<CourseMembership> courseMemberships;

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}