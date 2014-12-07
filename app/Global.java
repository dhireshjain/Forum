import play.*;
import play.libs.*;

import java.util.*;

import com.avaje.ebean.*;

import models.*;

public class Global extends GlobalSettings {

    public void onStart(Application app) {
        System.out.println("STARTED1");
        InitialData.insert(app);
    }

    static class InitialData {

        public static void insert(Application app) {
            try{
            if (Ebean.find(Users.class).findRowCount() == 0 && false) {
                @SuppressWarnings("unchecked")
                Map<String, List<Object>> all = (Map<String, List<Object>>) Yaml.load("intial-data.yml");

                // Insert users first
                Ebean.save(all.get("users"));
                Ebean.save(all.get("subject"));
            }}
            catch(Exception e){

            }
        }
    }
}