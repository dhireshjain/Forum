package models;

import play.data.format.Formats;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.util.List;

/**
 * Created by dhiresh on 6/12/14.
 */
@Entity
public class Comment extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @NotNull
    @Constraints.Required
    @Formats.NonEmpty
    @Column(unique = true, nullable = false, length = 1000)
    public String body;

    long upvotes, downvotes;

    public Date time;

    @ManyToOne(fetch = FetchType.LAZY)
    public Answer answer;

    @ManyToOne(fetch = FetchType.LAZY)
    public Users user;

    public static Model.Finder<Long, Comment> find = new Model.Finder<Long, Comment>(Long.class, Comment.class);

    public Comment() {

    }

    public Comment(String body, Date time, Users user, Answer answer) {

        this.body = body;
        this.time = time;
        this.user = user;
        this.answer = answer;

    }

    public static void create(String body, Date time, String usn, long answerId) {

        Users user = Users.find.ref(usn);
        Answer answer = Answer.find.ref(answerId);
        Comment comment = new Comment(body, time, user, answer);
        comment.save();
    }

    public static List<Comment> getCommentsByAnswerId(long id){
        List<Comment> list = Comment.find.where().eq("answer_id",id).findList();
        return list;
    }

    public static Comment getCommentById(long id) {
        return Comment.find.byId(id);
    }
}