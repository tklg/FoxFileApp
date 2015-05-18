package io.github.villa7.foxfileapp;

import com.loopj.android.http.RequestParams;

/**
 * Created by kluget on 5/16/2015.
 */
public class Params {

    private static RequestParams requestParams;
    private static String page;

    public static Object[] getParams(String... params) {
        page = "";
        requestParams = new RequestParams();
        String type = params[0];
        F.nl("making " + type + " params");
        if (type.equals("login")) {
            page = "uauth.php";
            String u = params[1];
            String p = params[2];
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
            F.nl("making params for read_file");
            page = "dbquery.php";
            String hash = params[1];
            F.nl("page: " + page + " hash: " + hash);
            requestParams.put("read_file", hash);
        } else if (type.equals("preview")) {
            F.nl("making params for preview");
            page = "dbquery.php";
            String hash = params[1];
            F.nl("page: " + page + " hash: " + hash);
            requestParams.put("preview", hash);
        } else if (type.equals("rename")) {
            F.nl("making params for rename");
            page = "dbquery.php";
            String hash = params[1];
            String newName = params[2];
            F.nl("page: " + page + " hash: " + hash);
            requestParams.put("rename", "plsyes");
            requestParams.put("file_id", hash);
            requestParams.put("name", newName);
        } else if (type.equals("delete")) {
            F.nl("making params for delete");
            page = "dbquery.php";
            String hash = params[1];
            F.nl("page: " + page + " hash: " + hash);
            requestParams.put("delete", "do et");
            requestParams.put("file_id", hash);
        } else if (type.equals("download")) {
            F.nl("making params for download");
            page = "dbquery.php";
            String hash = params[1];
            String fileName = params[2];
            F.nl("page: " + page + " hash: " + hash);
            requestParams.put("download", "yesss");
            requestParams.put("file_id", hash);
            requestParams.put("file_name", fileName);
        } else if (type.equals("upload")) {
            F.nl("making params for upload");
            page = "dbquery.php";
            String hash = params[1];
            F.nl("page: " + page + " hash: " + hash);
            requestParams.put("upload_target", hash);
        }

        Object[] o = {page, requestParams};

        return o;

    }

}
