package io.github.villa7.foxfileapp;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.support.v4.content.LocalBroadcastManager;

import com.goebl.david.Webb;

public class RequestService extends IntentService {

    private String type = "";
    private String[] params;
    private Object response;
    private Webb webb;
    private boolean done;
    private static String phpsessid;
    private static String user;

    public static void send(Context context, String phpsessid, String... p) {
        Intent intent = new Intent(context, RequestService.class);
        intent.putExtra("phpsessid", phpsessid);
        intent.putExtra("params", p);
        //context.startService(intent);
        //LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public RequestService() {
        super("RequestService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            webb = Webb.create();
            phpsessid = intent.getStringExtra("phpsessid");
            params = intent.getStringArrayExtra("params");
            type = params[0];

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
        }
    }
}
