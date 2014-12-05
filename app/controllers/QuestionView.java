package controllers;

import models.Answer;
import models.Question;
import models.Users;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.display;

import java.util.List;

/**
 * Created by dhiresh on 2/12/14.
 */
public class QuestionView extends Controller {

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

    @Security.Authenticated(Secured.class)
    public static Result viewQuestionById(long id) {
        Question question = Question.find.ref(id);
        List<Answer> answer = Answer.getAnswers(id);
        return ok(views.html.displayquestion.render(question, answer, answerForm));
    }

    @Security.Authenticated(Secured.class)
    public static Result addAnswer(long id){

        Form<UserAnswer> currentForm = answerForm.bindFromRequest();

        if(currentForm.hasErrors()) {
            Question question = Question.find.ref(id);
            List<Answer> answer = Answer.getAnswers(id);
            return badRequest(views.html.displayquestion.render(question, answer, answerForm));
        }
        Answer.create(currentForm.get().body, id, session("usn"), new java.sql.Date(new java.util.Date().getTime()));

        return redirect(routes.QuestionView.viewQuestionById(id));
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
