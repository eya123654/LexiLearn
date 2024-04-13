package com.example.demo1.Services;

import com.example.demo1.Entities.Cours;
import com.example.demo1.Entities.Lecon;
import com.example.demo1.Utils.DataSource;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;



public class CoursService implements IService<Cours> {
    private Connection connection;

    public CoursService() {
        connection = DataSource.getInstance().getCon();
    }

    @Override
    public void ajouter(Cours cours) throws SQLException {
        String query = "INSERT INTO cours (nom_cours, description, avancement, image,price) VALUES (?, ?, ?, ?,?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, cours.getNomCours());
            preparedStatement.setString(2, cours.getDescription());
            preparedStatement.setInt(3, cours.getAvancement());
            preparedStatement.setString(4, cours.getImage());
            preparedStatement.setString(5, cours.getPrice());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM cours WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    @Override
    public void update(Cours cours) throws SQLException {
        String sql = "UPDATE cours SET nom_cours = ?, description = ?, avancement = ?, image = ?, price = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, cours.getNomCours());
            pstmt.setString(2, cours.getDescription());
            pstmt.setDouble(3, cours.getAvancement());
            pstmt.setString(4, cours.getImage());
            pstmt.setString(5, cours.getPrice());
            pstmt.setInt(6, cours.getId());
            pstmt.executeUpdate();
        }
    }

@Override
public Cours readSingle(int id) throws SQLException {
    String sql = "SELECT * FROM cours WHERE id = ?";
    try ( PreparedStatement pstmt = connection.prepareStatement(sql)) {
        pstmt.setInt(1, id);
        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return new Cours(
                        rs.getInt("id"),
                        rs.getString("nom_cours"),
                        rs.getString("description"),
                        rs.getInt("avancement"),
                        rs.getString("image"),
                        rs.getString("price")
                );
            }
        }}
    return null;}

    @Override
    public ObservableList<Cours> readAll() {
        ObservableList<Cours> cours =  FXCollections.observableArrayList();
        String query = "SELECT * FROM cours";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Cours cours1 = new Cours(
                        resultSet.getInt("id"),
                        resultSet.getString("nom_cours"),
                        resultSet.getString("description"),
                        resultSet.getInt("avancement"),
                        resultSet.getString("image"),
                        resultSet.getString("price")


                );
                cours1.setLessons(fetchLessonsForCourse(cours1));

                cours.add(cours1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cours;
    }
    public ObservableList<Lecon> fetchLessonsForCourse(Cours course) {
        ObservableList<Lecon> lessons = FXCollections.observableArrayList();
        String query = "SELECT * FROM lecon WHERE cours_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, course.getId());
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    lessons.add(new Lecon(
                            rs.getInt("id"),
                            course,
                            rs.getString("titre"),
                            rs.getString("description"),
                            rs.getString("contenu"),
                            rs.getBoolean("completed")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lessons;
    }

    @Override
    public Cours findbyId(int id) throws SQLException {
        return null;
    }
    public boolean checkCourseNameUnique(String courseName) {
        String query = "SELECT count(*) FROM cours WHERE nom_cours = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, courseName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) == 0; // Retourne vrai si aucun enregistrement n'est trouvé
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public void updateCourseProgress(Cours course) {
        int totalLessons = course.getLessons().size();
        int completedLessons = (int) course.getLessons().stream().filter(Lecon::isCompleted).count();
        int newProgress = (completedLessons * 100) / totalLessons; // Toujours en pourcentage
        course.setAvancement(newProgress); // stocke la valeur entière, par exemple, 100 pour 100%

        String sql = "UPDATE Cours SET avancement = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, newProgress); // envoie l'entier, par exemple 100
            stmt.setInt(2, course.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        // Convertit l'avancement en un double pour la ProgressBar
        double progressForBar = newProgress / 100.0; // Convertit en double, par exemple, 1.0 pour 100%
        
    }

}