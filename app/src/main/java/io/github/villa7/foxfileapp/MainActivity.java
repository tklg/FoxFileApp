package io.github.villa7.foxfileapp;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.goebl.david.Webb;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends Activity {

    private Webb webb;
    private static String phpsessid;
    private static SharedPreferences pm;
    private String user, pass;
    private ProgressBar progress;
    private EditText userField, passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Toolbar toolbar = findViewById (R.id.toolbar);
        //Toolbar will now take on default Action Bar characteristics
        SetActionBar(toolbar);
        //You can now use and reference the ActionBar
        ActionBar.setTitle("FoxFile");*/

        progress = (ProgressBar) findViewById(R.id.load_progress);
        progress.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.primary), android.graphics.PorterDuff.Mode.SRC_IN);

        //startSession();
        pm = PreferenceManager.getDefaultSharedPreferences(this);

        //SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        userField = (EditText) findViewById(R.id.editText_username);
        passwordField = (EditText) findViewById(R.id.editText_password);
        userField.setText(pm.getString("user", ""), TextView.BufferType.EDITABLE);
        passwordField.setText(pm.getString("pass", ""), TextView.BufferType.EDITABLE);

        FoxFileClient.initCookies(this);

        if (!userField.getText().equals("") && !passwordField.getText().equals("")) {
            checkUsernameAndPassword(findViewById(R.id.button_login));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void startSession() {
        webb = Webb.create();
        Request post = new Request(webb, "", "getsessionid");
        post.start();
        String res = post.getResponse().toString();
        //String res = post.send();
        phpsessid = res;
        System.out.println("Got session id: " + phpsessid);
    }
    public void checkUsernameAndPassword(View v) {
        user = userField.getText().toString();
        pass = passwordField.getText().toString();

        if (user.equals("")) {
            toast("Please enter a name.");
        } else if (pass.equals("")) {
            toast("Please enter a Password.");
        } else {
            /*if (*//*!login(user, pass)*//*!login("login", user, pass)) {
                toast("Username or password is incorrect.");
            } else {
                SharedPreferences.Editor edit = pm.edit();
                edit.putString("user", user);
                edit.putString("pass", pass);
                edit.commit();
                Intent intentBrowse = new Intent(this, Browse.class);
                intentBrowse.putExtra("phpsessid", phpsessid);
                intentBrowse.putExtra("username", user);
                startActivity(intentBrowse);
            }*/
            login("login", user, pass);
        }
    }
    public boolean login(String u, String p) {
        showSpinner();
        Request post = new Request(webb, phpsessid, "login", u, p);
        post.start();
        String res = post.getResponse().toString();
        //String res = post.send();
        //toast(res);
        hideSpinner();
        return res.equals("valid");
    }

    private Context context = this;
    private static boolean fff = false;
    public boolean login(String... params) {
        final Context context = this;
        showSpinner();
        userField.setEnabled(false);
        passwordField.setEnabled(false);
        Object[] bla = Params.getParams(params);
        String page = (String) bla[0];
        F.nl("Page: " + page);
        //F.nl("params:");
        //F.pa(params);
        RequestParams param = (RequestParams) bla[1];

        FoxFileClient.post(page, param, new TextHttpResponseHandler() { /*JsonHttpResponseHandler*/

            @Override
            public void onSuccess(int statusCode, Header[] headers, String res) {
                F.nl("Result: " + res);
                if (res.equals("valid")) {
                    //MainActivity.fff = true;
                    SharedPreferences.Editor edit = pm.edit();
                    edit.putString("user", user);
                    edit.putString("pass", pass);
                    edit.commit();
                    Intent intentBrowse = new Intent(context, Browse.class);
                    intentBrowse.putExtra("phpsessid", phpsessid);
                    intentBrowse.putExtra("username", user);
                    hideSpinner();
                    startActivity(intentBrowse);
                    userField.setEnabled(true);
                    passwordField.setEnabled(true);
                } else {
                    F.nl("not valid");
                    toast("Username or password is incorrect.");
                    hideSpinner();
                    userField.setEnabled(true);
                    passwordField.setEnabled(true);
                    //MainActivity.fff = false;
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable error) {
                hideSpinner();
                userField.setEnabled(true);
                passwordField.setEnabled(true);
                toast("Failed to connect to server");
                F.nl("failed");
            }
        });

        //F.nl("Returning: " + fff);
        return fff;

    }
    public void showSpinner() {
        F.nl("Showing spinner");
        //setProgressBarIndeterminateVisibility(true);
        progress.setVisibility(View.VISIBLE);
    }
    public void hideSpinner() {
        F.nl("Hiding spinner");
        //setProgressBarIndeterminateVisibility(false);
        progress.setVisibility(View.GONE);
    }
    public void toast(String o) {
        Toast.makeText(getApplicationContext(), o, Toast.LENGTH_SHORT).show();
    }
}
