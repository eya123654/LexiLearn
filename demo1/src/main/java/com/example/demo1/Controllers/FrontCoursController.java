package com.example.demo1.Controllers;

import com.example.demo1.Entities.Cours;
import com.example.demo1.Services.CoursService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Pagination;

import java.io.IOException;

public class FrontCoursController {
    @FXML
    private Pagination pagination;


    @FXML
    private FlowPane coursesContainer;
    @FXML
    private StackPane mainStackPane;
    public DashboardController dashboardController;

    @FXML
    public void setDashboardController(DashboardController dashboardController) {
        if (dashboardController == null) {
            System.out.println("Attempting to set a null dashboardController!");
        }
        this.dashboardController = dashboardController;
        System.out.println("DashboardController is set successfully.");
        loadCourses();
    }

   /* @FXML
    public void initialize() {
        setDashboardController(dashboardController);

        loadCourses();
    }*/

    @FXML
    private void loadCourses() {
        CoursService coursService = new CoursService();
        ObservableList<Cours> courses = coursService.readAll();
        int itemsPerPage = 5; // Nombre de cours par page
        int pageCount = (int) Math.ceil(courses.size() / (double) itemsPerPage);
        pagination.setPageCount(pageCount);
        pagination.setPageFactory(this::createPage);
        for (Cours course : courses) {

            coursesContainer.getChildren().add(createCourseCard(course));
        }
    }
    private Node createPage(int pageIndex) {
        int itemsPerPage = 5;
        CoursService coursService = new CoursService();
        ObservableList<Cours> allCourses = coursService.readAll();
        int fromIndex = pageIndex * itemsPerPage;
        int toIndex = Math.min(fromIndex + itemsPerPage, allCourses.size());
        ObservableList<Cours> coursesOnPage = FXCollections.observableArrayList(allCourses.subList(fromIndex, toIndex));

        FlowPane pageContent = new FlowPane();
        for (Cours course : coursesOnPage) {
            pageContent.getChildren().add(createCourseCard(course));
        }
        return pageContent;
    }

    private Node createCourseCard(Cours course) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo1/views/CoursFront/CourseCard.fxml"));
            Node card = loader.load();
            CourseCardController cardController = loader.getController();
            cardController.setCourseDetailsCallback(this.dashboardController);
            System.out.println("Callback set for course card: " + course.getNomCours());


            cardController.initData(course);
            return card;
        } catch (IOException e) {
            // Handle exceptions
            e.printStackTrace();
            return new VBox(new Label("Error loading course card."));
        }
    }
 

}
