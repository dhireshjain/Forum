package controllers;

import com.avaje.ebean.Ebean;
import models.*;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import scala.App;
import views.html.display;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dhiresh on 2/12/14.
 */
public class QuestionView extends Controller {

    public static class UserQuestion {
        public String title;
        public String body;
        public String validate() {
            if(body.length()==0 || title.length()==0) {
                return "Field cannot be empty";
            }
            return null;
        }
    }
    private static Form<UserQuestion> questionForm = Form.form(UserQuestion.class);

    public static class UserAnswer {
        public String body;
        public String validate() {
            if(body.length()==0) {
                return "Field cannot be empty";
            }
            return null;
        }
    }
    private static Form<UserAnswer> answerForm = Form.form(UserAnswer.class);

    public static class UserComment {
        public String body;
        public String validate() {
            if(body.length()==0) {
                return "Field cannot be empty";
            }
            return null;
        }
    }
    private static Form<UserComment> commentForm = Form.form(UserComment.class);

    public static class AnswerWithComments {
        public Answer answer;
        public List<Comment> comments;

        public AnswerWithComments() {
            answer = new Answer();
            comments = new ArrayList<>();
        }

        public AnswerWithComments(Answer answer, List<Comment> comments) {
            this.answer = answer;
            this.comments = comments;
        }
    }

    @Security.Authenticated(Secured.class)
    public static Result viewQuestionById(long id) {

        ArrayList<AnswerWithComments> answerWithComments = new ArrayList<AnswerWithComments>();

        Question question = Question.find.ref(id);

            /*
            Gets all answers for this question
             */
        List<Answer> answers = Answer.getAnswers(id);
        for (Answer answer : answers) {
            List<Comment> comments = Comment.getCommentsByAnswerId(answer.id);
            answerWithComments.add(new AnswerWithComments(answer, comments));
        }

        return ok(views.html.displayquestion.render(question, answerWithComments, answerForm));
    }

    @Security.Authenticated(Secured.class)
    public static Result addAnswer(long id){

        Form<UserAnswer> currentForm = answerForm.bindFromRequest();

        ArrayList<AnswerWithComments> answerWithComments = new ArrayList<AnswerWithComments>();

        if(currentForm.hasErrors()) {
            flash("error","Answer cannot be empty");
            return redirect(routes.QuestionView.addAnswer(id));
        }
        Answer.create(currentForm.get().body, id, session("usn"), new java.sql.Date(new java.util.Date().getTime()));

        return redirect(routes.QuestionView.viewQuestionById(id));
    }

    @Security.Authenticated(Secured.class)
    public static Result addComment(long id){

        Form<UserComment> currentForm = commentForm.bindFromRequest();

        ArrayList<AnswerWithComments> answerWithComments = new ArrayList<>();

        Question question = Answer.find.ref(id).question;

        if(currentForm.hasErrors()) {
            /*
            Gets all answers for this question
             */
            List<Answer> answers = Answer.getAnswers(question.id);
            for (Answer answer : answers) {
                List<Comment> comments = Comment.getCommentsByAnswerId(answer.id);
                answerWithComments.add(new AnswerWithComments(answer, comments));
            }

            return ok(views.html.displayquestion.render(question, answerWithComments, answerForm));
        }
        Comment.create(currentForm.get().body,new java.sql.Date(new java.util.Date().getTime()), session("usn"),id);

        return redirect(routes.QuestionView.viewQuestionById(question.id));
    }

    @Security.Authenticated(Secured.class)
    public static Result addQuestion(String subject){

        Form<UserQuestion> currentForm = questionForm.bindFromRequest();

        if(currentForm.hasErrors()) {
            List<Question> list = Question.getSubjectQuestions(subject);
            flash("error","Field(s) cannot be empty");
            return badRequest(display.render(list,subject,questionForm));
        }
        Question.create(currentForm.get().title,currentForm.get().body,new java.sql.Date(new java.util.Date().getTime()),session("usn"),subject);

        return redirect(routes.QuestionView.viewAllQuestions(subject));
    }

    @Security.Authenticated(Secured.class)
    public static Result viewAllQuestions(String name){
        List<Question> list = Question.getSubjectQuestions(name);
        return ok(display.render(list,name,questionForm));
    }

    public static Result deleteCommentById(long id) {
        Comment comment = Comment.getCommentById(id);
        long questionId = comment.answer.question.id;
        comment.delete();

        return redirect(routes.QuestionView.viewQuestionById(questionId));
    }

    public static Result deleteAnswerById(long id) {
        Answer answer = Answer.getThisAnswer(id);
        long answerId = answer.question.id;
        answer.delete();
        return redirect(routes.QuestionView.viewQuestionById(answerId));
    }
}
