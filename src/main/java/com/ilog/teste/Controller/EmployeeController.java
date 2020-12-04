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

import com.ilog.teste.Model.Employee;
import com.ilog.teste.Model.Log;
import com.ilog.teste.Repository.EmployeeRepository;
import com.ilog.teste.Repository.LogRepository;

@RestController
@RequestMapping("/api")
public class EmployeeController {
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    private LogRepository logRepository;

    @GetMapping("/employees")
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @GetMapping("/employees/{id}")
    public Employee getEmployee(@PathVariable Long id) {
        return employeeRepository.findById(id).map(employee -> ResponseEntity.ok().body(employee))
                .orElse(ResponseEntity.notFound().build()).getBody();
    }

    @PostMapping("/employees")
    public Employee createEmployee(@RequestBody Employee employee) {
        Date now = new Date();
        Log log = new Log("employee", employee.getName(), now.toString(), "created");
        logRepository.save(log);
        return employeeRepository.save(employee);
    }

    @DeleteMapping("/employees")
    public void deleteCourse(@RequestBody Employee employee) {
        Employee tempEmployee = getEmployee(employee.getId());
        Date now = new Date();
        Log log = new Log("employee", tempEmployee.getName(), now.toString(), "deleted");
        logRepository.save(log);
        employeeRepository.delete(employee);
    }

    @PatchMapping("/employees/{id}")
    public void updateEmployees(@PathVariable("id") long id, @RequestBody Employee employee) {
        Employee tempEmployee = getEmployee(id);
        if (employee.getName() != null) {
            tempEmployee.setName(employee.getName());
        }
        if (employee.getPhone() != null) {
            tempEmployee.setphone(employee.getPhone());
        }
        if (employee.getAddress() != null) {
            tempEmployee.setAddress(employee.getAddress());
        }
        if (employee.getAdmissionDate() != null) {
            try {
                tempEmployee.setAdmissionDate(employee.getAdmissionDate());
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        Date now = new Date();
        Log log = new Log("employee", employee.getName(), now.toString(), "updated");
        logRepository.save(log);
        employeeRepository.save(tempEmployee);
    }
}
