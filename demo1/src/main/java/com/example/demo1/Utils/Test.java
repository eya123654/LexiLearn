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
        Cours cours1= new Cours("public 1","hahahahaha",2,"https://unsplash.com/fr/photos/une-image-dune-fleur-rose-et-blanche-XdhgsNXe7Hg","250");
        CoursService service= new CoursService();
        service.ajouter(cours1);
        /*Lecon lesson= new Lecon(1,"hahahahaha","zzjjzakakp","https://unsplash.com/fr/photos/une-image-dune-fleur-rose-et-blanche-XdhgsNXe7Hg",true);
        LeconService lessonService= new LeconService();
        lessonService.ajouter(lesson);*/
    }
}