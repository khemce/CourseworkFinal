package Controls;
import org.glassfish.jersey.media.multipart.FormDataParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.sql.PreparedStatement;

public class Players {
    @POST
    @Path("add")
    public String UsersAdd(@FormDataParam("FirstName") String FirstName, @FormDataParam("LastName") String LastName, @FormDataParam("UserName") String UserName, @FormDataParam("Password") String Password) {
        System.out.println("Invoked Users.UsersAdd()");
        try {
            PreparedStatement ps = server.Main.db.prepareStatement("INSERT INTO Player (FirstName, LastName, UserName, Password) VALUES (?, ?, ?, ?)");
            ps.setString(1, FirstName);
            ps.setString(2, LastName);
            ps.setString(3, UserName);
            ps.setString(4, Password);
            ps.execute();
            return "{\"OK\": \"Added user.\"}";
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to create new item, please see server console for more info.\"}";
        }

    }

}
