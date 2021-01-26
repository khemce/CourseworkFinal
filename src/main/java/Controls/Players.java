package controllers;

import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

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
                row.put("UserName", results.getString(4));
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
    public String PlayersAdd(@CookieParam("PlayerID") Integer PlayerID, @FormDataParam("FirstName") String FirstName, @FormDataParam("LastName") String LastName, @FormDataParam("UserName") String UserName, @FormDataParam("Password") String Password) {
        System.out.println("Invoked Players.PlayersAdd()");
        try {
            PreparedStatement ps = server.Main.db.prepareStatement("INSERT INTO Players (FirstName, LastName, UserName, Password) VALUES (?, ?, ?, ?)");
//            ps.setInt(1, PlayerID);
            ps.setString(1, FirstName);
            ps.setString(2, LastName);
            ps.setString(3, UserName);
            ps.setString(4, Password);
            ps.execute();
            return "{\"OK\": \"Added player.\"}";
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to create new item, please see server console for more info.\"}";
        }

    }

    @POST
    @Path("login")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String loginPlayer(@FormDataParam("Username") String Username, @FormDataParam("Password") String Password) {
        System.out.println("Invoked loginPlayer() on path Players/login");
        try {
            PreparedStatement ps1 = server.Main.db.prepareStatement("SELECT PlayerID, Username, Password FROM Players WHERE Username = ?");
            ps1.setString(1, Username);
            ResultSet loginResults = ps1.executeQuery();
            if (loginResults.next()) {
                String correctPassword = loginResults.getString(3);
                if (Password.equals(correctPassword)) {
                    JSONObject playerDetails = new JSONObject();
                    playerDetails.put("PlayerID", loginResults.getInt(1));
                    System.out.println(playerDetails.toString());
                    return playerDetails.toString();
                } else {
                    return "{'Error: Incorrect password!'}";
                }
            } else {
                return "{'Error: Username is incorrect.}";
            }
        } catch (Exception exception) {
            System.out.println("Database error during /Player/login: " + exception.getMessage());
            return "{'Error: Server side error!'}";
        }
    }

}

