package controllers;

import models.Subject;
import models.Users;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.enter;
import views.html.index;

import java.util.List;

import static play.data.Form.form;

public class Application extends Controller {

    static int sem = 1;
    static List<Subject> subjects = Subject.getSubjectsOfSem(sem);

    public static Result index(int semester) {
        sem = semester;
        setSubjects();
        return ok(index.render(subjects));
    }

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

    public static Result login(){
        if(!session().containsKey("usn"))
            return ok(enter.render(loginForm,signupForm));
        else
            return redirect(controllers.routes.Application.index(sem));
    }

    public static Result authenticateLogin(){
        Form<Login> login = form(Login.class).bindFromRequest();

        if(login.hasErrors()) {
            return badRequest(enter.render(login, signupForm));
        }
        else{
            session().clear();
            session("usn", login.get().usn);
            return redirect(controllers.routes.Application.index(sem));
        }

    }

    public static Result authenticateSignup() {
        Form<SignUp> signUp = signupForm.bindFromRequest();

        if(signUp.hasErrors()) {
            return badRequest(enter.render(loginForm, signUp));
        }
        else{
            SignUp obj = signUp.get();
            Users.create(obj.usn,obj.username,obj.password,obj.firstName,obj.lastName,obj.email);
            flash("success","You've signed up successfully");
            return redirect(controllers.routes.Application.index(sem));
        }
    }

    public static Result logout() {
        session().clear();
        flash("success", "You've been logged out");
        return redirect(
                controllers.routes.Application.index(sem)
        );
    }

    public static Result profile(String usn){
        return ok(views.html.profile.render());
    }

    public static int getSem() { return sem; }

    public static void setSubjects(){
        subjects = Subject.getSubjectsOfSem(sem);
    }
}
