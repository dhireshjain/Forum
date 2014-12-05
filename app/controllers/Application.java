package controllers;

import models.Question;
import models.Subject;
import models.Users;
import play.data.Form;
import play.data.validation.Constraints;
import play.mvc.*;

import views.html.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class Application extends Controller {

    public static class Login {

        public String usn;
        public String password;

        public String validate() {
            if(Users.authenticate(usn, password) == null) {
                return "Invalid user or password";
            }
            return null;
        }
    }

    public static Form<Login> login = Form.form(Login.class);

    public static class SignUp {

        @Constraints.Required
        public String usn;
        public String username;
        public String password;
        public String firstName;
        public String lastName;
        public String email;

        public String validate() {

            if(Users.signUpAuthenticator(usn,username,password,firstName,lastName,email)==null){
                return "Invalid SignUp Details";
            }
            return null;
        }
    }

    public static Form<SignUp> signup = Form.form(SignUp.class);

    public static Result index() {
        try {
            Subject.create("Operating Systems",0L);
            Subject.create("Java",0L);
            Subject.create("DBMS",0L);
            Subject.create("System Software",0L);
            Subject.create("Computer Networks",0L);
            Subject.create("Software Engineering",0L);

            Users.create("1ms12cs028", "dhiresh", "N","Dhiresh","Jain","dhiresh@gmail.com");
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
        return ok(enter.render(login));
    }

    public static Result authenticate(){
        Form<Login> loginForm = login.bindFromRequest();
        if(loginForm.hasErrors())
            return badRequest(enter.render(loginForm));
        else{
            session().clear();
            session("usn",loginForm.get().usn);
            return redirect(routes.Application.index());
        }

    }

    public static Result logout() {
        session().clear();
        flash("success", "You've been logged out");
        return redirect(
                routes.Application.index()
        );
    }

}
