package com.example.demo1.Services;
import com.example.demo1.Entities.Cours;
import com.example.demo1.Entities.Lecon;
import com.example.demo1.Utils.DataSource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LeconService implements IService<Lecon> {
    private Connection connection;

    public LeconService() {
        connection = DataSource.getInstance().getCon();
    }
    @Override
    public void ajouter(Lecon lecon) throws SQLException {
        String query = "INSERT INTO lecon (cours_id, titre, description, contenu,completed) VALUES (?, ?, ?, ?,?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, lecon.getCourse().getId());
            preparedStatement.setString(2, lecon.getTitre());
            preparedStatement.setString(3, lecon.getDescription());
            preparedStatement.setString(4, lecon.getContenu());
            preparedStatement.setBoolean(5, lecon.isCompleted());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Cours readSingle(int id) throws SQLException {
        return null;
    }


    @Override
    public void supprimer(int id) throws SQLException {
        String query = "DELETE FROM lecon WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("No lessons were deleted.");
            }
        }
    }


    @Override
    public void update(Lecon lecon) throws SQLException {
        String query = "UPDATE lecon SET cours_id = ?, titre = ?, description = ?, contenu = ?, completed = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, lecon.getCourse().getId());
            preparedStatement.setString(2, lecon.getTitre());
            preparedStatement.setString(3, lecon.getDescription());
            preparedStatement.setString(4, lecon.getContenu());
            preparedStatement.setBoolean(5, lecon.isCompleted());
            preparedStatement.setInt(6, lecon.getId());

            preparedStatement.executeUpdate();
        }
    }
    @Override
    public ObservableList<Lecon> readAll() {
        ObservableList<Lecon> lecons = FXCollections.observableArrayList();
        String query = "SELECT * FROM lecon";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Cours course = fetchCourseById(resultSet.getInt("cours_id"));

                Lecon musee = new Lecon(
                        resultSet.getInt("id"),
                        course,
                        resultSet.getString("titre"),
                        resultSet.getString("description"),
                        resultSet.getString("contenu"),
                        resultSet.getBoolean("completed")


                );
                lecons.add(musee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lecons;
    }

    @Override
    public Lecon findbyId(int id) throws SQLException {
        String query = "SELECT * FROM lecon WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Cours course = fetchCourseById(resultSet.getInt("cours_id"));

                    return new Lecon(
                            resultSet.getInt("id"),
                            course,
                            resultSet.getString("titre"),
                            resultSet.getString("description"),
                            resultSet.getString("contenu"),
                            resultSet.getBoolean("completed")
                    );
                }
            }
        }
        return null;

    }

    private Cours fetchCourseById(int coursId) throws SQLException {
        String query = "SELECT * FROM cours WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, coursId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new Cours(
                            resultSet.getInt("id"),
                            resultSet.getString("nom_cours"),
                            resultSet.getString("description"),
                            resultSet.getInt("avancement"),
                            resultSet.getString("image"),
                            resultSet.getString("price")
                    );
                }
            }
        }
        throw new SQLException("Course with ID " + coursId + " not found.");
    }
    public boolean checkCourseNameUnique(String lessonName) {
        String query = "SELECT count(*) FROM lecon WHERE titre = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, lessonName);
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
}