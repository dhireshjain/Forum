package controllers;

import models.Question;
import models.Subject;
import models.Users;
import play.data.Form;
import play.mvc.*;

import static play.data.Form.*;

import views.html.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class Application extends Controller {

    static List<Subject> subjects = Subject.getAllSubject();

    public static class Login {

        public String usn;
        public String password;

        public String validate() {return Users.logInAuthenticator(usn,password); }

    }

    public static Form<Login> loginForm = form(Login.class);

    public static class SignUp {


        public String usn;
        public String username;
        public String password;
        public String firstName;
        public String lastName;
        public String email;

        public String validate() {
            return Users.signUpAuthenticator(usn,username,password,firstName,lastName,email);
        }
    }

    public static Form<SignUp> signupForm = form(SignUp.class);

    public static Result index() {
        List<Users> users = Users.getAllUsers();
        List<Question> questions = Question.getAllQuestion();
        List<Question> usnQuestions = Question.getUsnQuestions("1ms12cs028");

        return ok(index.render(users,questions,subjects,usnQuestions));
    }

    public static Result login(){
        return ok(enter.render(loginForm,signupForm));
    }



    public static Result authenticateLogin(){
        Form<Login> login = form(Login.class).bindFromRequest();

        if(login.hasErrors()) {
            flash("notFound", login.globalError().message());
            return badRequest(enter.render(loginForm, signupForm));
        }
        else{
            session().clear();
            session("usn", login.get().usn);
            return redirect(controllers.routes.Application.index());
        }

    }

    public static Result authenticateSignup() {
        Form<SignUp> signUp = signupForm.bindFromRequest();

        if(signUp.hasErrors())
        {
            flash("fail",signUp.globalError().message());
            return badRequest(enter.render(loginForm,signupForm));
        }
        else{
            SignUp obj = signUp.get();
            Users.create(obj.usn,obj.username,obj.password,obj.firstName,obj.lastName,obj.email);
            flash("success","You've signed up successfully");
            return redirect(controllers.routes.Application.index());
        }
    }

    public static Result logout() {
        session().clear();
        flash("success", "You've been logged out");
        return redirect(
                controllers.routes.Application.index()
        );
    }

    public static Result profile(String usn){


        return ok(views.html.profile.render(subjects));
    }

}
