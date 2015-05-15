package io.github.villa7.foxfileapp;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.goebl.david.Webb;


public class MainActivity extends Activity {

    private Webb webb;
    private static String phpsessid;
    private static SharedPreferences pm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Toolbar toolbar = findViewById (R.id.toolbar);
        //Toolbar will now take on default Action Bar characteristics
        SetActionBar(toolbar);
        //You can now use and reference the ActionBar
        ActionBar.setTitle("FoxFile");*/

        startSession();
        pm = PreferenceManager.getDefaultSharedPreferences(this);

        //SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        EditText etu = (EditText) findViewById(R.id.editText_username);
        EditText etp = (EditText) findViewById(R.id.editText_password);
        etu.setText(pm.getString("user", ""), TextView.BufferType.EDITABLE);
        etp.setText(pm.getString("pass", ""), TextView.BufferType.EDITABLE);
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
                SharedPreferences.Editor edit = pm.edit();
                edit.putString("user", user);
                edit.putString("pass", pass);
                edit.commit();
                Intent intentBrowse = new Intent(this, Browse.class);
                intentBrowse.putExtra("phpsessid", phpsessid);
                intentBrowse.putExtra("username", user);
                startActivity(intentBrowse);
            }
        }
    }
    public boolean login(String u, String p) {
        Request post = new Request(webb, phpsessid, "login", u, p);
        post.start();
        String res = post.getResponse().toString();
        //String res = post.send();
        //toast(res);
        return res.equals("valid");
    }
    public void toast(String o) {
        Toast.makeText(getApplicationContext(), o, Toast.LENGTH_SHORT).show();
    }
}
