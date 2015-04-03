package models;

import com.avaje.ebean.Ebean;
import play.data.format.Formats;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Iterator;
import java.util.List;

/**
 * Created by dhiresh on 21/11/14.
 */

@Entity
public class Users extends Model {

    @Id
    @Formats.NonEmpty
    @Constraints.Required
    @Column(nullable = false)
    public String usn;

    @NotNull
    @Constraints.Required
    @Formats.NonEmpty
    @Column(nullable=false, unique=true)
    public String username;

    @NotNull
    @Constraints.Required
    @Formats.NonEmpty
    @Column(nullable = false)
    public String password;

    @NotNull
    @Constraints.Required
    @Formats.NonEmpty
    @Column(nullable = false)
    public String firstName;

    @NotNull
    @Constraints.Required
    @Formats.NonEmpty
    @Column(nullable = false)
    public String lastName;

    @NotNull
    @Constraints.Required
    @Formats.NonEmpty
    @Column(nullable = false, unique=true)
    public String email;

    public Users(String usn, String username, String password,String firstName, String lastName, String email) {

            this.usn = usn;
            this.username = username;
            this.password = password;
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
    }

    public static Model.Finder<String,Users> find = new Model.Finder<String,Users>(String.class, Users.class);

    public static boolean create(String usn, String username, String password, String firstName, String lastName,String email) {
           try {
               Users users = new Users(usn, username, password, firstName, lastName, email);
               users.save();
               return true;
           }
           catch(Exception e){
               return false;
           }
    }

    public static List<Users> getAllUsers() {
        return Ebean.find(Users.class)
                .findList();
    }
    public static Users getUserByUsn(String usn){
        return Users.find.byId(usn);
    }

   

    public static String logInAuthenticator(String usn, String password) {
        if(Users.find.where().eq("usn",usn).eq("password", password).findUnique() == null)
           return "Invalid Usn or Password" ;
        return null;
    }

    public static String signUpAuthenticator(String usn, String username, String password, String firstName, String lastName, String email){

        if ((!usn.startsWith("1MS") && !usn.startsWith("1ms")) || usn.length() != 10 || find.where().eq("usn",usn).findUnique() != null)
            return "Invalid USN";

        if (find.where().eq("username",username).findUnique() != null)
            return "Username exists";

        if (username.length() == 0)
            return "Username required";

        if (password.length() < 4)
            return "Password must be 4 or more characters";

        if (firstName.length() == 0 || lastName.length() == 0)
            return "You have a name";

        if (email.length() == 0 || !email.contains("@"))
            return "Invalid email";

        return null;
    }

    private static void deleter(){

        List<Users> users = Ebean.find(Users.class)
                .findList();

        Iterator<Users> it = users.iterator();

        while(it.hasNext()){
            it.next().delete();
        }

    }
    private static boolean deleteByUsn(String usn){
        try {
            Users user = Users.find.byId(usn);
            user.delete();
            return true;
        }
        catch(Exception e){
            return false;
        }
    }
}