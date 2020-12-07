package com.ilog.teste.Model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "employee")
@EntityListeners(AuditingEntityListener.class)
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String phone;

    private String address;

    private Date admissionDate;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<CourseMembership> courseMemberships;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setphone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getAdmissionDate() {
        return this.admissionDate;
    }

    public void setAdmissionDate(String admissionDate) throws ParseException {
        if (admissionDate != null) {
            DateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
            Date date = new Date(fmt.parse(admissionDate).getTime());
            this.admissionDate = date;
        }
    }

    public void setAdmissionDate(Date admissionDate) throws ParseException {
        this.admissionDate = admissionDate;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}