package com.example.cataloguedb.model;

public class Catalogue {

    private String name;
    private String description;
    private String image;

    public Catalogue(String name, String description, String image) {
        this.name = name;
        this.description = description;
        this.image = image;
    }

    public Catalogue() {
    }


    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
