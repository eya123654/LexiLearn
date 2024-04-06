package com.example.demo1.Controllers;

import com.example.demo1.Entities.Cours;
import com.example.demo1.Entities.Lecon;
import com.example.demo1.Services.CoursService;
import com.example.demo1.Services.LeconService;

import java.sql.SQLException;
import java.util.List;

public class LeconController {
    private LeconService leconService;
    public  LeconController() throws SQLException {
        leconService =new LeconService();
    }
    // Create
    public void addLecon(int id,Cours course, String titre, String description, String contenu, boolean completed) {
        Lecon lesson = new Lecon(id,course, titre, description, contenu,completed);
        try {
            leconService.ajouter(lesson);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Read
    public List<Lecon> getCustomers() {
        return leconService.readAll();
    }
}
