package com.example.cataloguedb.controller;

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
            record.put("image", rs.getString("image_data"));
            array.add(record);
        }
        jsonObject.put("Catalogue", array);
        connection.close();
        return jsonObject;
    }

    @PostMapping(path = "/post", consumes = "application/json")
    public String postToDatabase(@RequestBody Catalogue catalogue) throws SQLException {
        createConnection();
        String insertStatement = "INSERT INTO Catalogue (name, description, image_data, filename) VALUES (?, ?, ?, ?);";
        PreparedStatement stmnt = connection.prepareStatement(insertStatement, Statement.RETURN_GENERATED_KEYS);
        stmnt.setString(1, catalogue.getName());
        stmnt.setString(2, catalogue.getDescription());
        stmnt.setString(3, catalogue.getImage());
        stmnt.setString(4, catalogue.getFilename());
        stmnt.executeUpdate();

        int itemId = 0;
        ResultSet generatedKeys = stmnt.getGeneratedKeys();
        if (generatedKeys.next()) {
            itemId = generatedKeys.getInt(1);
        }
        connection.close();
        return """
                {"itemId": %s}
                """.formatted(itemId);
    }

    @PutMapping(path="/put/{id}")
    public String putToDatabase(@PathVariable Integer id, @RequestBody Catalogue catalogue) throws SQLException {
        createConnection();
        String updateStatement = "UPDATE Catalogue SET name=?, description=?, image_data=? WHERE id=?;";
        PreparedStatement statement = connection.prepareStatement(updateStatement, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, catalogue.getName());
        statement.setString(2, catalogue.getDescription());
        statement.setString(3, catalogue.getImage());
        statement.setInt(4, id);
        statement.executeUpdate();
        connection.close();
        return "Item " + id + " has been updated";
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
