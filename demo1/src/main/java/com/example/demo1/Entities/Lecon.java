package com.example.demo1.Entities;

public class Lecon {
    private int id;
    private Cours course;

    private String titre;
    private String description;
    private String contenu;
    private boolean completed;

    // Constructor
    public Lecon( int id,Cours course, String titre, String description, String contenu, boolean completed) {
        this.id=id;
        this.course = course;
        this.titre = titre;
        this.description = description;
        this.contenu = contenu;
        this.completed = completed;
    }
    public Lecon( Cours course, String titre, String description, String contenu, boolean completed) {
        this.course = course;
        this.titre = titre;
        this.description = description;
        this.contenu = contenu;
        this.completed = completed;
    }


    @Override
    public String toString() {
        return "Lecon{" +
                "id=" + id +
                ", course=" + course +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", contenu='" + contenu + '\'' +
                ", completed=" + completed +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cours getCourse() {
        return course;
    }

    public void setCourse(Cours course) {
        this.course = course;
        if (!course.getLessons().contains(this)) {
            course.getLessons().add(this);
        }
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
