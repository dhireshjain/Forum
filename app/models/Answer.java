package models;

import com.avaje.ebean.Ebean;
import play.data.format.Formats;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.sql.Date;
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

    public Date time;

    @ManyToOne(fetch = FetchType.LAZY)
    public Users user;

    public static Model.Finder<Long,Answer> find = new Model.Finder<Long,Answer>(Long.class,Answer.class);

    public Answer() {

    }

    public static List<Answer> getAllAnswers(){
        List<Answer> list = Answer.find.findList();
        return list;
    }

    public static List<Answer> getAnswers(Long id) {
        return Answer.find.where()
                .eq("question_id", id)
                .findList();
    }

    public Answer(Users user , String body, Question question, Date time){
        this.user = user;
        this.body = body;
        this.question = question;
        this.time = time;
        this.downvotes = 0;
        this.upvotes = 0 ;
 }


    public static void create(String body, Long id, String usn, Date time) {

            Question question = Question.find.ref(id);
            Users user = Ebean.find(Users.class, usn);
            System.out.println("-/-/-/-/-/"+user+usn+id);

            Answer answer = new Answer(user,body,question,time);
            answer.save();
    }

}