package models;

import com.avaje.ebean.*;
import play.data.format.Formats;
import play.data.validation.Constraints;
import play.db.ebean.Model;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * Created by dhiresh on 24/11/14.
 */
@Entity
public class Question extends Model {

    public long getId(){
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @NotNull
    @Constraints.Required
    @Formats.NonEmpty
    @Column(nullable = false,length=1000)
    public String title;

    @NotNull
    @Constraints.Required
    @Formats.NonEmpty
    @Column(unique = true,nullable = false,length=1000)
    public String body;

    public String time;

    @ManyToOne(fetch = FetchType.LAZY)
    public Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    public Subject subject;

    public Question(String title, String body,String time, Users user, Subject subject) {

        setTitle(title);
        setBody(body);
        setTime(time);
        setUser(user);
        setSubject(subject);

    }

    public static void create(String title, String body,String time, String usn, String sub) throws IOException {
        try {
            if(title.isEmpty() || body.isEmpty())
                throw new IOException("Empty field");

            Users user1 = Users.find.ref(usn);
            Subject subject = Subject.find.ref(sub);
            Question question = new Question(title,body,time,user1,subject);
            question.save();
        }
        catch(Exception e){
            System.out.println(e.toString() + "1");
            throw new IOException(e.toString());
        }

    }


    public static Model.Finder<Long,Question> find = new Model.Finder<Long,Question>(Long.class, Question.class);

    public static List<Question> getAllQuestion() {
        List<Question> question = Ebean.find(Question.class)
                .findList();
        return question;
    }

    public static List<Question> getSubjectQuestions(String subject) {
        List<Question> question = Question.find.where().eq("name",subject).findList();
        return question;
    }

    public Question(String usn){
        this.body = usn;
    }

    public static List<Question> questionsUsn(String user) {
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