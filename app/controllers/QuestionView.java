package controllers;

import com.avaje.ebean.Ebean;
import models.Answer;
import models.Comment;
import models.Question;
import models.Users;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
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

        ArrayList<AnswerWithComments> answerWithComments = new ArrayList<>();

        Question question = Question.find.ref(id);

            /*
            Gets all answers for this question
             */
        List<Answer> answers = Answer.getAnswers(id);
        for (Answer answer : answers) {
            List<Comment> comments = Comment.getCommentsByAnswerId(answer.id);
            answerWithComments.add(new AnswerWithComments(answer, comments));
        }

        return badRequest(views.html.displayquestion.render(question, answerWithComments, answerForm));
    }

    @Security.Authenticated(Secured.class)
    public static Result addAnswer(long id){

        Form<UserAnswer> currentForm = answerForm.bindFromRequest();

        ArrayList<AnswerWithComments> answerWithComments = new ArrayList<>();

        if(currentForm.hasErrors()) {

            Question question = Question.find.ref(id);

            /*
            Gets all answers for this question
             */
            List<Answer> answers = Answer.getAnswers(id);
            for (Answer answer : answers) {
                List<Comment> comments = Comment.getCommentsByAnswerId(answer.id);
                answerWithComments.add(new AnswerWithComments(answer, comments));
            }

            return badRequest(views.html.displayquestion.render(question, answerWithComments, answerForm));
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

            return badRequest(views.html.displayquestion.render(question, answerWithComments, answerForm));
        }
        Comment.create(currentForm.get().body,new java.sql.Date(new java.util.Date().getTime()), session("usn"),id);

        return redirect(routes.QuestionView.viewQuestionById(question.id));
    }

    @Security.Authenticated(Secured.class)
    public static Result addQuestion(String subject){

        Form<UserQuestion> currentForm = questionForm.bindFromRequest();

        if(questionForm.hasErrors()) {
            List<Question> list = Question.getSubjectQuestions(subject);
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

}
