package controllers;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Path("Results/")   //These annotations are interpreted by the Jersey Library and turn this method into a HTTP request handler.
@Consumes(MediaType.MULTIPART_FORM_DATA)    //I used @GET because I am using a SELECT query. The following method is a HTTP GET request.
@Produces(MediaType.APPLICATION_JSON)

public class Results{
    @GET
    @Path("list")
    public String ResultsList() {
        System.out.println("Invoked Results.ResultsList()");
        JSONArray response = new JSONArray(); //This creates a new JSON array. JSON objects are made from each row of the results and these objects are added to the JSON array.
        try {
            PreparedStatement ps = server.Main.db.prepareStatement("SELECT Wins, Draws, Losses, PlayerID FROM Results");
            ResultSet results = ps.executeQuery();
            while (results.next()==true) { //Loops throw my Results table as long as there is another record after.
                JSONObject row = new JSONObject();
                row.put("Wins", results.getString(1));
                row.put("Draws", results.getString(2));
                row.put("Losses", results.getString(3));
                row.put("ResultID", results.getInt(4));
                response.add(row);
            }
            return response.toString();
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to list items.  Error code xx.\"}";
        }
    }
}
