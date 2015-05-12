package io.github.villa7.foxfileapp;

import android.os.AsyncTask;

import com.goebl.david.Webb;

/**
 * Created by kluget on 5/11/2015.
 */
public class AsyncLoginTask extends AsyncTask<String, Integer, String> { //<Params, progress, result>

    private Exception exception;
    private static String self = "";
    /*private final String CHECK_USER = "check_username",
                      LOGIN = "login";*/

    //replace this with something more reliable - find out what other apps use
    protected String doInBackground(String... param) {
        //String type = param[0];
        String response = "";
        Webb webb = Webb.create();
        webb.setBaseUri("http://lucianoalberto.zapto.org/foxfile");
        webb.setDefaultHeader(Webb.HDR_USER_AGENT, "Const.UA");

        String u = param[0];
        String p = param[1];
        response = webb.post("/uauth.php")
                .param("login", "yes")
                .param("username", u)
                .param("password", p)
                .ensureSuccess()
                .asString()
                .getBody();
        return response;
    }
    protected void onPostExecute(String response) {
        // TODO: check this.exception
        // return the thing to mainactivity
        //System.out.println(response);
        this.self = response;
    }
    public String toString() {
        //System.out.println("returning self");
        return self;
    }

}
