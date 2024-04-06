package com.example.demo1.Entities;

public class Cours {
    private int id;
    private String nomCours;
    private String description;
    private int avancement;
    private String image; // Assuming this is an ID linked to an image resource
    private String price;
    public Cours(int id,String nom_cours, String description, int avancement, String image, String price) {
        this.id=id;
        this.nomCours = nom_cours;
        this.description = description;
        this.avancement = avancement;
        this.image = image;
        this.price = price;
    }
    public Cours(String nom_cours, String description, int avancement, String image, String price) {
        this.nomCours = nom_cours;
        this.description = description;
        this.avancement = avancement;
        this.image = image;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Cours{" +
                "id=" + id +
                ", nomCours='" + nomCours + '\'' +
                ", description='" + description + '\'' +
                ", avancement=" + avancement +
                ", image='" + image + '\'' +
                ", price='" + price + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomCours() {
        return nomCours;
    }

    public void setNomCours(String nomCours) {
        this.nomCours = nomCours;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAvancement() {
        return avancement;
    }

    public void setAvancement(int avancement) {
        this.avancement = avancement;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
