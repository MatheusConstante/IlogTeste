package com.ilog.teste.Repository;

import java.util.List;

import com.ilog.teste.Model.Log;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface LogRepository extends MongoRepository<Log, String> {

  public List<Log> findByTitle(String title);
  public List<Log> findByType(String type);

}