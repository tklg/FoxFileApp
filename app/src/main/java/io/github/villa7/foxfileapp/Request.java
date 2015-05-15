package io.github.villa7.foxfileapp;

import com.goebl.david.Webb;

/**
 * Created by kluget on 5/12/2015.
 */
public class Request extends Thread {

    private String type = "";
    private String[] params;
    private Object response;
    private Webb webb;
    private boolean done;
    private static String phpsessid;
    private static String user;

    public Request(Webb webb, String phpsessid, String... params) {
        this.webb = webb;
        this.type = params[0];
        this.params = params;
        this.response = "";
        this.done = false;
        this.phpsessid = phpsessid;
    }

    public void run() {
        //Webb webb = Webb.create();
        webb.setBaseUri("http://lucianoalberto.zapto.org/foxfile");
        webb.setDefaultHeader(Webb.HDR_USER_AGENT, System.getProperty("http.agent"));
        Object res = "";
        if (type.equals("login")) {
            String u = params[1];
            user = u;
            String p = params[2];
            res = webb.post("/uauth.php")
                    .param("SETSESSIONID", phpsessid)
                    .param("login", "yes")
                    .param("username", u)
                    .param("password", p)
                    .ensureSuccess()
                    .asString()
                    .getBody();
        } else if (type.equals("check_username")) {
            String u = params[1];
            res = webb.post("/uauth.php")
                    .param("SETSESSIONID", phpsessid)
                    .param("check_username", "yes")
                    .param("username", u)
                    .ensureSuccess()
                    .asString()
                    .getBody();
        } else if (type.equals("getsessionid")) {
            res = webb.post("/uauth.php")
                    .param("GETSESSIONID", "yespls")
                    .ensureSuccess()
                    .asString()
                    .getBody();
        } else if (type.equals("dir")) {
            String dir = params[1];
            String fileType = params[2];
            //webb.post("/dbquery.php").param("SETSESSIONID", phpsessid);
            System.out.println("Setting phpsessid to " + phpsessid);
            res = webb.post("/dbquery.php")
                    .param("SETSESSIONID", phpsessid)
                    .param("dir", dir)
                    .param("type", fileType)
                    .ensureSuccess()
                    .asJsonArray()
                    .getBody();
        } else if (type.equals("phpsession")) {
            System.out.println("[Request]: phpsessid = " + phpsessid);
            res = webb.post("/dbquery.php")
                    .param("SETSESSIONID", phpsessid)
                    .ensureSuccess()
                    .asString()
                    .getBody();
        } else if (type.equals("read_file")) {
            String hash = params[1];
            res = webb.post("/dbquery.php")
                    .param("SETSESSIONID", phpsessid)
                    .param("read_file", hash)
                    .ensureSuccess()
                    .asString()
                    .getBody();
        }
        System.out.println("response: " + res);
        response = res;
        done = true;
        //return res;
    }
    public String send() {
        return null;
    }
    public Object getResponse() {
        while (!done) {
            System.out.println("waiting for response");
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
