package controllers;

import play.mvc.Http.*;
import play.mvc.Result;
import play.mvc.Security;


/**
 * Created by dhiresh on 6/12/14.
 */
public class Secured extends Security.Authenticator {

    @Override
    public String getUsername(Context ctx){
        return ctx.session().get("usn");
    }

    @Override
    public Result onUnauthorized(Context ctx) {
        return redirect(routes.Application.index(1));
    }

}
