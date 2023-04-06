package com.example.cataloguedb.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.*;

import com.example.cataloguedb.model.Catalogue;
import com.example.cataloguedb.repository.JDBCConnection;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/PaymentServiceApp/catalogue")
public class CatalogueController {


    private Connection connection;

    public void createConnection() throws SQLException {
        connection = JDBCConnection.connect();
    }

    @GetMapping(path="/get")
    public JSONObject getFromDatabase(@RequestParam(value = "id", required = false) Integer itemId) throws SQLException {
        createConnection();
        Statement statement = connection.createStatement();
        String query = "Select * from CatalogueDB.Catalogue";
        if (itemId!=null) {
            query += " WHERE id=%s".formatted(itemId);
        }
        ResultSet rs = statement.executeQuery(query);

        JSONObject jsonObject = new JSONObject();
        JSONArray array = new JSONArray();
        while(rs.next()) {
            JSONObject record = new JSONObject();
            //Inserting key-value pairs into the json object
            record.put("id", rs.getInt("id"));
            record.put("name", rs.getString("name"));
            record.put("description", rs.getString("description"));
            record.put("image_data", rs.getBytes("image_data"));
            array.add(record);
        }
        jsonObject.put("Catalogue", array);
        connection.close();
        return jsonObject;
    }

    @PostMapping(path = "/post", consumes = "application/json")
    public String postToDatabase(@RequestBody Catalogue catalogue) throws SQLException {
        createConnection();
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO Catalogue (name, description, image_data) VALUES (?, ?, ?)")) {

            File imageFile = new File(catalogue.getImage());
            InputStream imageStream = new FileInputStream(imageFile);

            statement.setString(1, catalogue.getName());
            statement.setString(2, catalogue.getDescription());
            statement.setBinaryStream(3, imageStream);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Image stored successfully!");
            } else {
                System.out.println("Failed to store image.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Item " + catalogue.getName() + " has been added to the database";
    }

    @PutMapping(path="/put/{id}")
    public String putToDatabase(@PathVariable Integer id, @RequestBody Catalogue catalogue) throws SQLException {
        createConnection();
        System.out.println(catalogue);
        String query = "UPDATE Catalogue SET name=?, description=?, image_data=? WHERE id=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            File imageFile = new File(catalogue.getImage());
            InputStream imageStream = new FileInputStream(imageFile);

            statement.setString(1, catalogue.getName());
            statement.setString(2, catalogue.getDescription());
            statement.setBinaryStream(3, imageStream);
            statement.setInt(4, id);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Image updated successfully!");
            } else {
                System.out.println("Failed to update image.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Item " + catalogue.getName() + " has been updated to the database whose id is: " + id;
    }

    @DeleteMapping(path="/delete/{id}")
    public String deleteFormDatabase(@PathVariable Integer id) throws SQLException {
        createConnection();
        Statement statement = connection.createStatement();
        String delete = "DELETE FROM Catalogue WHERE id=" + id;
        statement.executeUpdate(delete);
        connection.close();
        return "Item " + id + " has been deleted from the database";
    }

}