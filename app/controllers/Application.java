package controllers;

import models.Question;
import models.Subject;
import models.Users;
import play.mvc.*;

import views.html.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class Application extends Controller {

    public static Result index() {

    	//Users.create("e","f","g");

        //UserProfile.create("a","b","c","d",new Users("e","f","g"));

        //UserProfile.deleter();
        //Users.deleter();

        try {
            Subject.create("Operating Systems",0L);
            Subject.create("Java",0L);
            Subject.create("DBMS",0L);
            Subject.create("System Software",0L);
            Subject.create("Computer Networks",0L);
            Subject.create("Software Engineering",0L);

            Users.create("1ms12cs028", "dhiresh", "N","Dhiresh","Jain","dhiresh@gmail.com");
            Question.create("Cassandra","Struggling with cassandra","2-2-1024","1ms12cs028", "DBMS");
//          Answer.create("Egork is the man",1L,"1ms12cs029","10101");
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        //Users.create("1ms12cs029","dhiresh a","abc");
            //UserProfile.create("");
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

        List<Users> list = Users.getAllUsers();
        List<Question> list2 = Question.getAllQuestion();
        List<Subject> list3 = Subject.getAllSubject();
        List<Question> list4 = Question.questionsUsn("1ms12cs028");

        return ok(index.render(list,list2,list3,list4));
    }

    public static Result questions(String subject){

        List<Question> list = Question.getSubjectQuestions(subject);
        System.out.println(list.size());
        return ok(display.render(list));
    }

}
