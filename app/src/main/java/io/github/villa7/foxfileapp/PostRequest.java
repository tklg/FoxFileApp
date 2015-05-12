package io.github.villa7.foxfileapp;

import com.goebl.david.Webb;

/**
 * Created by kluget on 5/12/2015.
 */
public class PostRequest extends Thread {

    private String type = "";
    private String[] params;
    private String response;
    private Webb webb;
    private boolean done;

    public PostRequest(Webb webb, String... params) {
        this.webb = webb;
        this.type = params[0];
        this.params = params;
        this.response = "";
        this.done = false;
    }

    public void run() {
        //Webb webb = Webb.create();
        webb.setBaseUri("http://lucianoalberto.zapto.org/foxfile");
        webb.setDefaultHeader(Webb.HDR_USER_AGENT, "Const.UA");
        String res = "";

        if (type.equals("login")) {
            String u = params[1];
            String p = params[2];
            res = webb.post("/uauth.php")
                    .param("login", "yes")
                    .param("username", u)
                    .param("password", p)
                    .ensureSuccess()
                    .asString()
                    .getBody();
        } else if (type.equals("check_username")) {
            String u = params[1];
            res = webb.post("/uauth.php")
                    .param("check_username", "yes")
                    .param("username", u)
                    .ensureSuccess()
                    .asString()
                    .getBody();
        }
        System.out.println("response: " + res);
        response = res;
        done = true;
    }
    public String getResponse() {
        while (!done) {
            System.out.println("sleep");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("response2: " + response);
        return response;
    }

}
