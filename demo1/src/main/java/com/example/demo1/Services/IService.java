package com.example.demo1.Services;

import com.example.demo1.Entities.Cours;

import java.sql.SQLException;
import java.util.List;

public interface IService <T>{
    void ajouter(T t) throws SQLException;
    Cours readSingle(int id) throws SQLException;

    void supprimer(int id) throws SQLException;

    void update(T t) throws SQLException;

    List<T> readAll() throws SQLException;

    T findbyId(int id) throws SQLException;

}