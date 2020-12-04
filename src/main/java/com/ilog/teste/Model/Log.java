package com.ilog.teste.Model;

import org.springframework.data.annotation.Id;

public class Log {
    @Id
    public String id;

    public String type;
    public String title;
    public String dateTime;
    public String operation;

    public Log(String type, String title, String dateTime, String operation) {
        this.title = title;
        this.type = type;
        this.dateTime = dateTime;
        this.operation = operation;
    }
}
