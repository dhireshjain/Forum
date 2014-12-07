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

    public static class Login {

        public String usn;
        public String password;


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
        try {
            Subject.create("Operating Systems",0L);
            Subject.create("Java",0L);
            Subject.create("DBMS",0L);
            Subject.create("System Software",0L);
            Subject.create("Computer Networks",0L);
            Subject.create("Software Engineering",0L);
            Question.create("Cassandra","Struggling with cassandra",new java.sql.Date(new java.util.Date().getTime()),"1ms12cs028", "DBMS");
        }
        catch(Exception pe) {
            try {
                System.out.println(pe.toString()+"2");
                PrintWriter out = new PrintWriter(new FileWriter("a.txt", true));
                out.append("\n" + pe.toString());
                out.close();
            } catch (IOException e) {
                System.out.println(e.toString() + "S");
            }
        }

        List<Users> users = Users.getAllUsers();
        List<Question> questions = Question.getAllQuestion();
        List<Subject> subjects = Subject.getAllSubject();
        List<Question> usnQuestions = Question.getUsnQuestions("1ms12cs028");

        return ok(index.render(users,questions,subjects,usnQuestions));
    }

    public static Result login(){
        return ok(enter.render(loginForm,signupForm));
    }



    public static Result authenticateLogin(){
        Form<Login> loginForm = form(Login.class).bindFromRequest();

        if(loginForm.hasErrors())
            return badRequest(enter.render(loginForm,signupForm));
        else{
            session().clear();
            session("usn",loginForm.get().usn);
            return redirect(controllers.routes.Application.index());
        }

    }

    public static Result authenticateSignup() {
        Form<SignUp> signUpForm = signupForm.bindFromRequest();

        if(signUpForm.hasErrors())
        {
            flash("fail",signUpForm.globalError().message());
            return badRequest(enter.render(loginForm,signupForm));
        }
        else{
            SignUp obj = signUpForm.get();
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

}
