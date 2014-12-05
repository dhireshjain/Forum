/*package models;

import com.avaje.ebean.Ebean;
import play.data.format.Formats;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//@Entity
public class UserProfile extends Model {


    @Formats.NonEmpty
    @Constraints.Required
    public String firstName;

    @Formats.NonEmpty
    @Constraints.Required
    public String lastName;

    @Formats.NonEmpty
    @Constraints.Required
    public String username;

    @Id
    public long id;

    @OneToOne
    @JoinColumn(name="usn", referencedColumnName = "usn")
    public Users user;

    @Formats.NonEmpty
    @Constraints.Required
    public String email;

    public String getFirstName(){
        return firstName;
    }
    public void setFirstName(String firstName){
        this.firstName = firstName;
    }

    public String getLastname(){
        return lastName;
    }
    public void setLastname(String lastName){
        this.lastName = lastName;
    }
    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email = email;
    }

    public String getUsername(){
        return username;
    }
    public void setUsername(String username){
        this.username = username;
    }

    public Users getUsers() {
        return user;
    }
    public void setCompany(Users user) {
        this.user = user;
    }

    public static Model.Finder<Long,UserProfile> find = new Model.Finder<Long,UserProfile>(Long.class, UserProfile.class);

    public static List<UserProfile> getAllUsers() {
        List<UserProfile> users = Ebean.find(UserProfile.class)
                .findList();
        return users;
    }


    public UserProfile(String username, String firstName, String lastName, String email, Users user) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.user = user;
    }

    public static void create(String username, String firstName, String lastName, String email, Users user){
        UserProfile u = new UserProfile(username, firstName, lastName, email, user);
        u.save();

    }

    public static void deleter(){

        List<UserProfile> users = Ebean.find(UserProfile.class)
                .findList();

        Iterator<UserProfile> it = users.iterator();

        while(it.hasNext()){
            it.next().delete();
        }

    }

}

*/