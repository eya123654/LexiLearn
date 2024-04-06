package com.example.demo1.Services;

import com.example.demo1.Entities.Cours;
import com.example.demo1.Utils.DataSource;

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
    public List<Cours> readAll() {
        List<Cours> cours = new ArrayList<>();
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
                cours.add(cours1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cours;
    }

    @Override
    public Cours findbyId(int id) throws SQLException {
        return null;
    }

}