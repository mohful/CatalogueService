package com.example.cataloguedb.controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

    @GetMapping(path="/get/{id}")
    public JSONObject getFromDatabase(@PathVariable Integer id) throws SQLException {
        createConnection();
        Statement statement = connection.createStatement();
        String query = "Select * from CatalogueDB.Catalogue Where id=" + id;
        ResultSet rs = statement.executeQuery(query);

        JSONObject jsonObject = new JSONObject();
        JSONArray array = new JSONArray();
        while(rs.next()) {
            JSONObject record = new JSONObject();
            //Inserting key-value pairs into the json object
            record.put("id", rs.getInt("id"));
            record.put("name", rs.getString("name"));
            record.put("description", rs.getString("description"));
            record.put("image", rs.getString("image"));
            array.add(record);
        }
        jsonObject.put("Catalogue", array);
        return jsonObject;
    }

    @PostMapping(path = "/post", consumes = "application/json")
    public String postToDatabase(@RequestBody Catalogue catalogue) throws SQLException {
        createConnection();
        Statement statement = connection.createStatement();
        String insert = "INSERT INTO Catalogue (name, description, image) VALUES (" + "'" + catalogue.getName() + "'" + ", " + "'" + catalogue.getDescription() + "'" + ", " + "'" + catalogue.getImage() + "'" + ");";
        statement.executeUpdate(insert);
        return "Item " + catalogue.getName() + " has been added to the database";
    }

    @PutMapping(path="/put/{id}")
    public String putToDatabase(@PathVariable Integer id, @RequestBody Catalogue catalogue) throws SQLException {
        createConnection();
        Statement statement = connection.createStatement();
        String update = "UPDATE Catalogue SET name=" + "'" + catalogue.getName() + "'" + ", description=" + "'" + catalogue.getDescription() + "'" + ", image=" + "'" + catalogue.getImage() + "'" + " WHERE id=" + id + ";";
        statement.executeUpdate(update);
        return "Item " + id + " has been updated";
    }

    @DeleteMapping(path="/delete/{id}")
    public String deleteFormDatabase(@PathVariable Integer id) throws SQLException {
        createConnection();
        Statement statement = connection.createStatement();
        String delete = "DELETE FROM Catalogue WHERE id=" + id;
        statement.executeUpdate(delete);
        return "Item " + id + " has been deleted from the database";
    }

}
