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

import com.goebl.david.Webb;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends Activity {

    private Webb webb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webb = Webb.create();
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
            if (!login(user, pass)) {
                toast("Username or password is incorrect.");
            } else {
                Intent intentBrowse = new Intent(this, Browse.class);
                intentBrowse.putExtra("PHPSESSID", user);
                startActivity(intentBrowse);
            }
        }
    }
    public boolean login(String u, String p) {
        PostRequest post = new PostRequest(webb, "login", u, p);
        post.start();
        String res = post.getResponse();
        toast(res);
        return res.equals("valid");
    }
    public void toast(String o) {
        Toast.makeText(getApplicationContext(), o, Toast.LENGTH_SHORT).show();
    }
}
