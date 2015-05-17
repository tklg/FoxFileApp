package io.github.villa7.foxfileapp;

import android.content.Context;

import com.loopj.android.http.*;

/**
 * Created by kluget on 5/16/2015.
 * http://loopj.com/android-async-http/
 */
public class FoxFileClient {

    private static final String BASE_URL = "http://lucianoalberto.zapto.org/foxfile/";

    private static AsyncHttpClient client = new AsyncHttpClient();
    private static PersistentCookieStore cookies;

    public static void initCookies(Context context) {
        cookies = new PersistentCookieStore(context);
        client.setCookieStore(cookies);
    }

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

}
