package com.example.demo1.Controllers;

import com.example.demo1.Entities.Cours;
import com.example.demo1.Services.CoursService;
import javafx.event.ActionEvent;

import java.sql.SQLException;
import java.util.List;

public class CoursController {
    private CoursService coursService;
    public  CoursController() throws SQLException{
        coursService =new CoursService();
    }
    // Create
    public void addCours( int id,String nom_cours, String description, int avancement, String image, String price) {
        Cours cours = new Cours(id,nom_cours, description, avancement, image,price);
        try {
            coursService.ajouter(cours);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Read
    public List<Cours> getCustomers() {
        return coursService.readAll();
    }


}
