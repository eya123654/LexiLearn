package com.example.demo1.Utils;



import com.example.demo1.Entities.Cours;
import com.example.demo1.Entities.Lecon;
import com.example.demo1.Services.CoursService;
import com.example.demo1.Services.LeconService;

import java.sql.Connection;
import java.sql.SQLException;

public class Test {
    public static void main(String[] args) throws SQLException {
        Connection con1=DataSource.getInstance().getCon();

        Connection con2=DataSource.getInstance().getCon();

        System.out.println(con1);
        System.out.println(con2);

    }
}