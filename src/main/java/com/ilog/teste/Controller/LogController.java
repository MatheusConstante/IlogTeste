package com.ilog.teste.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import com.ilog.teste.Model.Log;
import com.ilog.teste.Repository.LogRepository;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class LogController {
    @Autowired
    private LogRepository logRepository;

    @GetMapping("/log")
    public List<Log> getAllLogs() {
        return logRepository.findAll();
    }
}
