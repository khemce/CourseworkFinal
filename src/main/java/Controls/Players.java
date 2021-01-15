package controllers;

import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Path("Players/")
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.APPLICATION_JSON)


public class Players {
    @GET
    @Path("list")
    public String PlayersList() {
        System.out.println("Invoked Players.PlayersList()");
        JSONArray response = new JSONArray();
        try {
            PreparedStatement ps = server.Main.db.prepareStatement("SELECT PlayerID, UserName FROM Players");
            ResultSet results = ps.executeQuery();
            while (results.next() == true) {
                JSONObject row = new JSONObject();
                row.put("PlayerID", results.getInt(1));
                row.put("UserName", results.getString(2));
                response.add(row);
            }
            return response.toString();
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to list items.  Error code xx.\"}";
        }
    }


    @POST
    @Path("add")
    public String PlayersAdd(@FormDataParam("PlayerID") Integer PlayerID, @FormDataParam("FirstName") String FirstName, @FormDataParam("LastName") String LastName, @FormDataParam("UserName") String UserName, @FormDataParam("Password") String Password) {
        System.out.println("Invoked Players.PlayersAdd()");
        try {
            PreparedStatement ps = server.Main.db.prepareStatement("INSERT INTO Players (PlayerID, FirstName, LastName, UserName, Password) VALUES (?, ?, ?, ?, ?)");
            ps.setInt(1, PlayerID);
            ps.setString(2, FirstName);
            ps.setString(3, LastName);
            ps.setString(4, UserName);
            ps.setString(5, Password);
            ps.execute();
            return "{\"OK\": \"Added player.\"}";
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to create new item, please see server console for more info.\"}";
        }

    }

}

