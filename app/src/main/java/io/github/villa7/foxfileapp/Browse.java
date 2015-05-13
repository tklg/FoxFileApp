package io.github.villa7.foxfileapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;


import com.goebl.david.Webb;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;


public class Browse extends Activity implements OnItemClickListener, OnItemLongClickListener, OnItemSelectedListener {

    private Webb webb;
    private String phpsessid;
    private String user;
    private ArrayList folderBeingViewed = new ArrayList(); //store folder hashes in here

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        webb = Webb.create();

        Intent intent = getIntent();
        phpsessid = intent.getStringExtra("phpsessid");
        user = intent.getStringExtra("username");
        System.out.println("user: " + user + "\nsessid: " + phpsessid);

        Request post = new Request(webb, phpsessid, "dir", user, "folder");
        //Request post = new Request(webb, phpsessid, "phpsession");
        System.out.println("GET with dir=" + user + " and type=folder");
        post.start();
        JSONArray res = (JSONArray) post.getResponse();

        ArrayList<FileItem> files = FileItem.fromJSON(res);
        //System.out.println(files);
        FileItemAdapter adapter = new FileItemAdapter(this, files);

        ListView listView = (ListView) findViewById(R.id.menubar);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        listView.setOnItemSelectedListener(this);

        //setContentView(listView);
    }

    public void onItemClick(AdapterView<?> l, View v, int position, long id) {
        System.out.println("clicked pos: " + position);
    }
    public boolean onItemLongClick(AdapterView<?> l, View v, int position, long id) {
        System.out.println("held pos: " + position);
        return true;
    }
    public void onItemSelected(AdapterView<?> l, View v, int position, long id) {
        System.out.println("selected pos: " + position);
    }
    public void onNothingSelected(AdapterView<?> l) {
        System.out.println("nothing selected");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_browse, menu);
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
}
