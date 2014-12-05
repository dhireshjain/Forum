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


    //@OneToOne(mappedBy = "user")
   //public UserProfile profile;


    public String getUsn(){
        return usn;
    }
    public void setUsn(String usn)  {
        this.usn = usn;
    }

    public String getUsername(){
        return username;
    }
    public void setUsername(String username){
        this.username = username;
    }

    public String getPassword(){
        return password;
    }
    public void setPassword(String password){
        this.password = password;
    }

    public String getFirstName(){
        return firstName;
    }
    public void setFirstName(String firstName){
        this.firstName = firstName;
    }

    public String getLastName(){
        return lastName;
    }
    public void setLastName(String lastName){
        this.lastName = lastName;
    }

    public String getEmail(){
        return email;
    }
    public void setEmail(String email){this.email = email;
    }

    public Users(String usn, String username, String password,String firstName, String lastName, String email) {

            setUsn(usn);
            setUsername(username);
            setPassword(password);
            setFirstName(firstName);
            setLastName(lastName);
            setEmail(email);

    }

    public static void create(String usn, String username, String password, String firstName, String lastName,String email) throws IOException {
       try {
           Users users = new Users(usn, username, password, firstName, lastName, email);
           users.save();
       }
       catch(Exception e){System.out.println("asd");
           throw new IOException(e.toString());
       }

    }

    public static Model.Finder<String,Users> find = new Model.Finder<String,Users>(String.class, Users.class);

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