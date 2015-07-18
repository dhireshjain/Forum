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
    public String code;

    public int sem;

    public static Model.Finder<String,Subject> find = new Model.Finder<String,Subject>(String.class,Subject.class);

    public Subject(String name, String code, int sem){
        this.name = name;
        this.code = code;
        this.sem = sem;
    }

    public static void create(String name , String code, int sem){
        Subject subject = new Subject(name, code, sem);
        subject.save();
    }

    public static List<Subject> getSubjectsOfSem(int sem){
        List<Subject> list = Ebean.find(Subject.class).where().eq("sem",sem).findList();
        return list;
    }

}
