package com.example.demo1.Controllers;


import com.example.demo1.Entities.Cours;
import com.example.demo1.Services.CoursService;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.scene.control.Label;
import javafx.geometry.Insets;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

public class DashboardController implements Initializable {

    @FXML
    private VBox pnItems = null;
    @FXML
    private Button btnOverview;

    @FXML
    private Button btnOrders;

    @FXML
    private Button btnCustomers;

    @FXML
    private Button btnMenus;

    @FXML
    private Button btnPackages;

    @FXML
    private Button btnSettings;

    @FXML
    private Button btnSignout;

    @FXML
    private Pane pnlCustomer;

    @FXML
    private Pane pnlOrders;

    @FXML
    private Pane pnlOverview;

    @FXML
    private Pane pnlMenus;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pnItems.setFillWidth(true);

        Node nodas;
// Create image objects
        Image image1 = null;
        try {
            image1 = new Image(new FileInputStream("/home/tx/perso/Dinosors/src/main/resources/images/Login/fast-food-resto.jpg"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        CoursService serviceCours = new CoursService();
        List<Cours> cos = serviceCours.readAll();

        System.out.println(cos);
//creating ImageView for adding images
        for (Cours data : cos) {

            ImageView imageView1 = new ImageView();
            imageView1.setImage(image1);
            imageView1.setFitWidth(20);
            imageView1.setFitHeight(20);
            imageView1.setPreserveRatio(true);
            imageView1.setSmooth(true);
            imageView1.setCache(true);
            TextField labelk1 = new TextField();
            labelk1.setText(data.getNomCours());
            labelk1.setEditable(true);
            TextField labelk2 = new TextField();
            labelk2.setText(data.getDescription());
//            labelk2.setTextFill(Paint.valueOf("#b7c3d7"));
            TextField labelk3 = new TextField();
            labelk3.setText(String.valueOf(data.getAvancement()));
//            labelk3.setTextFill(Paint.valueOf("#b7c3d7"));
            TextField labelk4 = new TextField();
            labelk4.setText(data.getImage());
            TextField labelk5 = new TextField();
            labelk4.setText(data.getPrice());
//            labelk4.setTextFill(Paint.valueOf("#b7c3d7"));
            Button but = new Button();
            but.setPrefSize(100, 100); // Sets the preferred width to 100 and the preferred height to 50
            FontAwesomeIconView iconView = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
            but.setGraphic(iconView);
            but.setStyle("-fx-background-color: #ff0000;-fx-background-radius: 15px;"); // Sets the background color to red

            Button butt = new Button();
            butt.setPrefSize(100, 100); // Sets the preferred width to 100 and the preferred height to 50

            iconView = new FontAwesomeIconView(FontAwesomeIcon.EDIT);
            butt.setGraphic(iconView);
            butt.setStyle("-fx-background-color: #2f3636;-fx-background-radius: 15px;"); // Sets the background color to red

            Button buttt = new Button();
            buttt.setPrefSize(100, 100); // Sets the preferred width to 100 and the preferred height to 50

            iconView = new FontAwesomeIconView(FontAwesomeIcon.PLUS);
            buttt.setGraphic(iconView);
            buttt.setStyle("-fx-background-color: #008600;-fx-background-radius: 15px;"); // Sets the background color to red
            buttt.setVisible(false);
//            butt.setFont(new Font(""));

//creating HBox to add image views
            HBox hBox = new HBox();
            hBox.getChildren().add(imageView1);
            hBox.getChildren().add(labelk1);
            hBox.getChildren().add(labelk2);
            hBox.getChildren().add(labelk3);
            hBox.getChildren().add(labelk4);
            hBox.getChildren().add(butt);
            hBox.getChildren().add(but);
            hBox.getChildren().add(buttt);
            HBox.setMargin(imageView1, new Insets(10, 10, 10, 10)); // Example margin for imageView1
            HBox.setMargin(labelk1, new Insets(10, 10, 10, 10)); // Example margin for labelk1
            HBox.setMargin(labelk2, new Insets(10, 10, 10, 10)); // Example margin for labelk2
            HBox.setMargin(labelk3, new Insets(10, 10, 10, 10)); // Example margin for labelk2
            HBox.setMargin(labelk4, new Insets(10, 10, 10, 10)); // Example margin for labelk2
            HBox.setMargin(but, new Insets(10, 10, 10, 10)); // Example margin for but
            HBox.setMargin(butt, new Insets(10, 10, 10, 10)); // Example margin for but
            HBox.setMargin(buttt, new Insets(10, 10, 10, 10)); // Example margin for but

            nodas = hBox;
            Node finalNodas = nodas;
            nodas.setOnMouseEntered(event -> {
                finalNodas.setStyle("-fx-background-color : #0A0E3F");
            });
            nodas.setOnMouseExited(event -> {
                finalNodas.setStyle("-fx-background-color : #02030A");
            });
            pnItems.getChildren().add(finalNodas);

        }


//        for (int i = 0; i < nodas.length; i++) {
//
//            final int j = i;
//            // nodes[i] = FXMLLoader.load(getClass().getResource("/views/Admin/Item.fxml"));//give the items some effect
//            nodas[i] = hBox;
//            nodas[i].setOnMouseEntered(event -> {
//                nodas[j].setStyle("-fx-background-color : #0A0E3F");
//            });
//            nodas[i].setOnMouseExited(event -> {
//                nodas[j].setStyle("-fx-background-color : #02030A");
//            });
//            pnItems.getChildren().add(hBox);
//        }

    }


    public void handleClicks(ActionEvent actionEvent) {
        if (actionEvent.getSource() == btnCustomers) {
            pnlCustomer.setStyle("-fx-background-color : #1620A1");
            pnlCustomer.toFront();
        }
        if (actionEvent.getSource() == btnMenus) {
            pnlMenus.setStyle("-fx-background-color : #53639F");
            pnlMenus.toFront();
        }
        if (actionEvent.getSource() == btnOverview) {
            pnlOverview.setStyle("-fx-background-color : #02030A");
            pnlOverview.toFront();
        }
        if (actionEvent.getSource() == btnOrders) {
            pnlOrders.setStyle("-fx-background-color : #464F67");
            pnlOrders.toFront();
        }
    }
}