package io.github.villa7.foxfileapp;

import com.loopj.android.http.RequestParams;

/**
 * Created by kluget on 5/16/2015.
 */
public class Params {

    private static RequestParams requestParams;
    private static String page;

    public static Object[] getParams(String... params) {
        String type = params[0];
        F.nl("making " + type + " params");
        if (type.equals("login")) {
            page = "uauth.php";
            String u = params[1];
            String p = params[2];
            requestParams = new RequestParams();
            requestParams.put("login", "yes");
            requestParams.put("username", u);
            requestParams.put("password", p);
        } else if (type.equals("check_username")) {
            page = "uauth.php";
            String u = params[1];
            requestParams.put("check_username", "yes");
            requestParams.put("username", u);
        } else if (type.equals("getsessionid")) {
            page = "dbquery.php";
            requestParams.put("GETSESSIONID", "yespls");
        } else if (type.equals("dir")) {
            F.nl("got request for dir");
            page = "dbquery.php";
            String dir = params[1];
            String fileType = params[2];
            F.nl("dir: " + dir);
            F.nl("type: " + fileType);
            requestParams.put("dir", dir);
            requestParams.put("type", fileType);
        } else if (type.equals("phpsession")) {
            page = "dbquery.php";
            requestParams.put("SETSESSIONID", 0);
        } else if (type.equals("read_file")) {
            page = "dbquery.php";
            String hash = params[1];
            requestParams.put("read_file", hash);
        }

        Object[] o = {page, requestParams};

        return o;

    }

}
