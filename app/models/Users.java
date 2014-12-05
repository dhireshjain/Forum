package models;

import com.avaje.ebean.Ebean;
import play.data.format.Formats;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;
import javax.validation.Constraint;
import javax.validation.constraints.NotNull;
import java.io.IOException;
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
    public String usn;

    @NotNull
    @Constraints.Required
    @Formats.NonEmpty
    @Column(unique = true,nullable = false)
    public String username;

    @NotNull
    @Constraints.Required
    @Formats.NonEmpty
    @Column(unique = true,nullable = false)
    public String password;

    @NotNull
    @Constraints.Required
    @Formats.NonEmpty
    @Column(unique = true,nullable = false)
    public String firstName;

    @NotNull
    @Constraints.Required
    @Formats.NonEmpty
    @Column(unique = true,nullable = false)
    public String lastName;

    @NotNull
    @Constraints.Required
    @Formats.NonEmpty
    @Column(unique = true,nullable = false)
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

    public static void create(String usn, String username, String password, String firstName, String lastName,String email) {
           Users users = new Users(usn, username, password, firstName, lastName, email);
           users.save();
    }

    public static List<Users> getAllUsers() {
        List<Users> users = Ebean.find(Users.class)
                .findList();
        return users;
    }

    public Users getThisUsn(String usn1){

        Users user = Users.find.where().eq(usn1,this.usn).findUnique();
        return user;
    }

    public static void deleter(){

        List<Users> users = Ebean.find(Users.class)
                .findList();

        Iterator<Users> it = users.iterator();

        while(it.hasNext()){
            it.next().delete();
        }

    }
}