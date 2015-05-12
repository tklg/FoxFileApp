package io.github.villa7.foxfileapp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
    private boolean isFirstTimeChecking = true; //because the first time only works on the 3rd try
    public void checkUsernameAndPassword(View v) {
        String user = ((EditText) findViewById(R.id.editText_username)).getText().toString();
        String pass = ((EditText) findViewById(R.id.editText_password)).getText().toString();

        if (user.equals("")) {
            toast("Please enter a name.");
        } else if (pass.equals("")) {
            toast("Please enter a Password.");
        } else {
            if (isFirstTimeChecking) {
                for (int i = 0; i < 3; i++) {
                    login(user, pass); //spam this a few times to make sure it works the first time :D
                }
                isFirstTimeChecking = false;
            }
            if (!login(user, pass)) {
                toast("Username or password is incorrect.");
            } else {
                Intent intentBrowse = new Intent(this, Browse.class);
                intentBrowse.putExtra("PHPSESSID", user);
                ast.cancel(true);
                startActivity(intentBrowse);
            }
        }
    }
    private AsyncTask ast;
    public boolean login(String u, String p) {

        //AsyncTask ast = new AsyncLoginTask().execute("check_username", u);
        //AsyncTask ast2;

        //if (ast.toString().equals("0")) { //if returns '0', user exists
            //ast2 = new AsyncLoginTask().execute("login", u, p);
            //System.out.println("[MainActivity]: " + ast2.toString());
        //} else {
        //    System.out.println("[MainActivity]: " + "User " + u + " does not exist.");
        //}
        ast = new AsyncLoginTask().execute(u, p);
        String res = ((AsyncTask) ast).toString();
        System.out.println("[MainActivity:login]: " + res);
        //ast.cancel(false);
        return res.equals("valid");
    }
    public void toast(String o) {
        Toast.makeText(getApplicationContext(), o.toString(), Toast.LENGTH_SHORT).show();
    }
}
