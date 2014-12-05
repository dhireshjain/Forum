package controllers;

import models.Answer;
import models.Question;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

/**
 * Created by dhiresh on 2/12/14.
 */
public class QuestionView extends Controller {

    public static class UserAnswer {
        public String body;
    }

   private static Form<UserAnswer> form = Form.form(UserAnswer.class);

    public static Result viewQuestion(long id) {
        Question question = Question.find.ref(id);
        List<Answer> answer = Answer.getAnswers(id);
        return ok(views.html.displayquestion.render(question, answer, form));
    }

    public static Result addAnswer(long id){
        Form<UserAnswer> currentForm = form.bindFromRequest();
        try {
            Answer.create(currentForm.get().body, id, "1ms12cs028", "1-1-1");
        }
        catch(Exception e){
            System.out.println("\\/\\/\\/\\/\\");
        }
        return redirect(routes.QuestionView.viewQuestion(id));
    }
}
