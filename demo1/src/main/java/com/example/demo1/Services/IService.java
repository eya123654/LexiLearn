package com.example.demo1.Services;

import java.sql.SQLException;
import java.util.List;

public interface IService <T>{
    void ajouter(T t) throws SQLException;
    void supprimer(T t) throws SQLException;

    void update(T t) throws SQLException;

    List<T> readAll() throws SQLException;

    T findbyId(int id) throws SQLException;

}