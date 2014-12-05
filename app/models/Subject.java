package models;

import com.avaje.ebean.Ebean;
import play.data.format.Formats;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;

/**
 * Created by dhiresh on 30/11/14.
 */
@Entity

public class Subject extends Model {

    @Id
    public String name;

    @Formats.NonEmpty
    @Constraints.Required
    public long number;

    public static Model.Finder<String,Subject> find = new Model.Finder<String,Subject>(String.class,Subject.class);

    public Subject(String name, Long no){
        this.name = name;
        this.number = no;
    }

    public static void create(String name , Long number){
        Subject subject = new Subject(name,number);
        subject.save();
    }

    public static List<Subject> getAllSubject(){
        List<Subject> list = Ebean.find(Subject.class).findList();
        return list;
    }

}
