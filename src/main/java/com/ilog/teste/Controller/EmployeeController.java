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

import java.util.List;

import com.ilog.teste.Model.Employee;
import com.ilog.teste.Repository.EmployeeRepository;

@RestController
@RequestMapping("/api")
public class EmployeeController {
    @Autowired
    EmployeeRepository employeeRepository;

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
        return employeeRepository.save(employee);
    }

    @DeleteMapping("/employees")
    public void deleteCourse(@RequestBody Employee employee) {
        employeeRepository.delete(employee);
    }

    @PatchMapping("/employees/{id}")
    public void updateEmployees(@PathVariable("id") long id, @RequestBody Employee employee) {
        Employee tempEmployee = getEmployee(id);
        if (employee.getName() != null){
            tempEmployee.setName(employee.getName());
        }
        employeeRepository.save(tempEmployee);
    }
}
