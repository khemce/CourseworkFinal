package controllers;

import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Path("Results/")   //These annotations are interpreted by the Jersey Library and turn this method into a HTTP request handler.
@Consumes(MediaType.MULTIPART_FORM_DATA)    //I used @GET because I am using a SELECT query. The following method is a HTTP GET request.
@Produces(MediaType.APPLICATION_JSON)

public class Results {
    @GET
    @Path("list")
    public String ResultsList() {
        System.out.println("Invoked Results.ResultsList()");
        JSONArray response = new JSONArray(); //This creates a new JSON array. JSON objects are made from each row of the results and these objects are added to the JSON array.
        try {
            PreparedStatement ps = server.Main.db.prepareStatement("SELECT Wins, Draws, Losses, PlayerID FROM Results");
            ResultSet results = ps.executeQuery();
            while (results.next() == true) { //Loops throw my Results table as long as there is another record after.
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

    @GET
    @Path("get_wins/{PlayerID}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String GetWins(@PathParam("PlayerID") Integer PlayerID) {
        System.out.println("Invoked Players.GetPlayers() with PlayerID " + PlayerID);
        try {
            PreparedStatement ps = server.Main.db.prepareStatement("SELECT Wins FROM Results WHERE PlayerID = ?");
            ps.setInt(1, PlayerID);
            ResultSet results = ps.executeQuery();
            JSONObject response = new JSONObject();
            if (results.next()) {
                response.put("PlayerID", PlayerID);
                response.put("Wins", results.getString(1));
            }
            return response.toString();
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{Error: Unable to get item, please see server console for more info.}";
        }
    }

    @GET
    @Path("get_losses/{PlayerID}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String GetLosses(@CookieParam("PlayerID") Integer PlayerID) {
        System.out.println("Invoked Players.GetPlayers() with PlayerID " + PlayerID);
        try {
            PreparedStatement ps = server.Main.db.prepareStatement("SELECT Losses FROM Results WHERE PlayerID = ?");
            ps.setInt(1, PlayerID);
            ResultSet results = ps.executeQuery();
            JSONObject response = new JSONObject();
            if (results.next()) {
                response.put("PlayerID", PlayerID);
                response.put("Losses", results.getString(1));
            }
            return response.toString();
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{Error: Unable to get item, please see server console for more info.}";
        }
    }


    @POST
    @Path("update_wins")
    public String updateWins(@CookieParam("PlayerID") Integer PlayerID, @FormDataParam("Wins") Integer Wins){
        try {
            System.out.println("Invoked Results.UpdateResults/update PlayerID=" + PlayerID);
            PreparedStatement ps = server.Main.db.prepareStatement("UPDATE Results SET Wins = ? WHERE PlayerID = ?");
            ps.setInt(1, PlayerID);
            ps.setInt(2, Wins);
            ps.execute();
            return "{\"OK\": \"Results updated\"}";
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to update item, please see server console for more info.\"}";
        }


    }

    @POST
    @Path("update_draws")
    public String updateDraws(@CookieParam("PlayerID") Integer PlayerID, @FormDataParam("Draws") Integer Draws){
        try {
            System.out.println("Invoked Results.UpdateResults/update PlayerID=" + PlayerID);
            PreparedStatement ps = server.Main.db.prepareStatement("UPDATE Results SET Draws = ? WHERE PlayerID = ?");
            ps.setInt(1, PlayerID);
            ps.setInt(2, Draws);
            ps.execute();
            return "{\"OK\": \"Results updated\"}";
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to update item, please see server console for more info.\"}";
        }


    }

    @POST
    @Path("update_losses")
    public String updateLosses(@CookieParam("PlayerID") Integer PlayerID, @FormDataParam("Losses") Integer Losses){
        try {
            System.out.println("Invoked Results.UpdateResults/update PlayerID=" + PlayerID);
            PreparedStatement ps = server.Main.db.prepareStatement("UPDATE Results SET Losses = ? WHERE PlayerID = ?");
            ps.setInt(1, PlayerID);
            ps.setInt(2, Losses);
            ps.execute();
            return "{\"OK\": \"Results updated\"}";
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to update item, please see server console for more info.\"}";
        }


    }


    @POST
    @Path("add")
    public String ResultsAdd(@FormDataParam("Wins") Integer Wins, @FormDataParam("Draws") Integer Draws, @FormDataParam("Losses") Integer Losses, @FormDataParam("PlayerID") Integer PlayerID) {
        System.out.println("Invoked Results.ResultsAdd()");
        try {
            PreparedStatement ps = server.Main.db.prepareStatement("INSERT INTO Results (Wins, Draws, Losses, PlayerID) VALUES (?, ?, ?, ?)");
            ps.setInt(1, Wins);
            ps.setInt(2, Draws);
            ps.setInt(3, Losses);
            ps.setInt(4, PlayerID);
            ps.execute();
            return "{\"OK\": \"Added results.\"}";
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to create new item, please see server console for more info.\"}";
        }

    }

}


