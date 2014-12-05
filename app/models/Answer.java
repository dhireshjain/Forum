package models;

import com.avaje.ebean.Ebean;
import play.data.format.Formats;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;

/**
 * Created by dhiresh on 25/11/14.
 */
@Entity
public class Answer extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    @NotNull
    @Formats.NonEmpty
    @Constraints.Required
    @Column(nullable = false,length=1000)
    public String body;

    @ManyToOne(fetch= FetchType.LAZY)
    public Question question;

    public long upvotes, downvotes;

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getDownvotes() {
        return downvotes;
    }

    public void setDownvotes(long downvotes) {
        this.downvotes = downvotes;
    }

    public long getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(long upvotes) {
        this.upvotes = upvotes;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String time;

    @ManyToOne(fetch = FetchType.LAZY)
    //public UserProfile userprofile;
    public Users user;


    public static Model.Finder<Long,Answer> find = new Model.Finder<Long,Answer>(Long.class,Answer.class);

    public static List<Answer> getAllAnswers(){

        List<Answer> list = Answer.find.findList();
        return list;

    }

    public static List<Answer> getAnswers(Long id) {
        return Answer.find.where()
                .eq("question_id", id)
                .findList();
    }

    public Answer(Users usn , String body, Question question, String time){

        setUser(usn);
        setBody(body);
        setQuestion(question);
        setTime(time);
        setDownvotes(0);
        setUpvotes(0);
        System.out.println("---/-/-/-/-/"+getUser());
 }


    public static void create(String body, Long id, String usn, String time) {

            Question question = Question.find.ref(id);
            Users user = Ebean.find(Users.class, usn);
            System.out.println("-/-/-/-/-/"+user+usn+id);

            Answer answer = new Answer(user,body,question,time);
            answer.save();
    }

}