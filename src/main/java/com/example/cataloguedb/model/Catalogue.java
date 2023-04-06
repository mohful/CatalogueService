package com.example.cataloguedb.model;

public class Catalogue {

    private String name;
    private String description;
    private String image;

    public String getFilename() {
        return filename;
    }

    private String filename;

    public Catalogue(String name, String description, String image) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.filename =  "";
    }

    public Catalogue(String name, String description) {
        this(name, description, "");
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
