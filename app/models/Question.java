package models;

import com.avaje.ebean.*;
import play.data.format.Formats;
import play.data.validation.Constraints;
import play.db.ebean.Model;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.sql.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by dhiresh on 24/11/14.
 */
@Entity
public class Question extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @NotNull
    @Constraints.Required
    @Formats.NonEmpty
    @Column(nullable = false,length=200, unique=true)
    public String title;

    @NotNull
    @Constraints.Required
    @Formats.NonEmpty
    @Column(nullable = false, unique=true, length=700)
    public String body;

    public Date time;

    @ManyToOne(fetch = FetchType.LAZY)
    public Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    public Subject subject;

    public static Model.Finder<Long,Question> find = new Model.Finder<Long,Question>(Long.class, Question.class);

    public Question(String title, String body,Date time, Users user, Subject subject) {

        this.title = title;
        this.body = body;
        this.time= time;
        this.user = user;
        this.subject = subject;

    }

    public static void create(String title, String body,Date time, String usn, String sub)  {
            Users user1 = Users.find.ref(usn);
            Subject subject = Subject.find.ref(sub);
            Question question = new Question(title,body,time,user1,subject);
            question.save();

    }

    public static List<Question> getAllQuestion() {
        List<Question> question = Ebean.find(Question.class)
                .findList();
        return question;
    }

    public static List<Question> getSubjectQuestions(String id) {
        List<Question> question = Question.find.where().eq("subject_name",id).findList();
        return question;
    }

    public static List<Question> getUsnQuestions(String user) {
        return Question.find.where()
                .eq("user_usn", user)
                .findList();
    }


    public static void deleter(){

        List<Question> question = Ebean.find(Question.class)
                .findList();

        Iterator<Question> it = question.iterator();

        while(it.hasNext()){
            it.next().delete();
        }

    }


}