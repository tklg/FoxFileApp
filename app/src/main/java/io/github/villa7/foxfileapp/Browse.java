package io.github.villa7.foxfileapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;


import com.goebl.david.Webb;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;


public class Browse extends Activity implements OnItemClickListener, OnItemLongClickListener, OnItemSelectedListener {

    private Webb webb;
    private String phpsessid;
    private String user;
    private ArrayList<String> folderBeingViewed = new ArrayList<String>(); //store folder hashes in here
    private ArrayList<FileItem> files;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        webb = Webb.create();

        Intent intent = getIntent();
        phpsessid = intent.getStringExtra("phpsessid");
        user = intent.getStringExtra("username");
        F.nl("user:\t" + user + "\nsessid:\t" + phpsessid);

        open(user, "folder"); //open the root directory to start
    }

    private void open(String fileHash, String type) {
        F.nl("opening " + fileHash);
        if (type.equals("folder")) {
            Request post = new Request(webb, phpsessid, "dir", fileHash, type);
            //Request post = new Request(webb, phpsessid, "phpsession");
            F.nl("POST ?dir=" + fileHash + "&type=" + type);
            post.start();
            JSONArray res = (JSONArray) post.getResponse();

            files = FileItem.fromJSON(res);
            //System.out.println(files);
            FileItemAdapter adapter = new FileItemAdapter(this, files);

            ListView listView = (ListView) findViewById(R.id.menubar);

            listView.setAdapter(adapter);
            listView.setOnItemClickListener(this);
            listView.setOnItemLongClickListener(this);
            listView.setOnItemSelectedListener(this);
        } else {
            F.nl("Type of opened file not \"folder\", was \"" + type + "\"");
            F.nl("POST ?preview=" + fileHash); //not how the preview query works (returns an image)
            //maybe have FileViewer activity here
            //or have a slidy bar from the right like Google Drive with preview and options
        }

        if (!folderBeingViewed.contains(fileHash)) {
            folderBeingViewed.add(fileHash);
        } else {
            F.nl("arraylist already has folder, was the back button pressed?");
        }
    }

    public void onItemClick(AdapterView<?> l, View v, int position, long id) {
        F.nl("Pressed: " + position);

        //View view = LayoutInflater.from().inflate(R.layout.layout_file, parent, false);
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.layout_file);
        TextView name = (TextView) layout.findViewById(R.id.fileName);
        String fileName = name.getText().toString();

        String hash = null;
        String type = "folder";
        /*for (FileItem f : files) {
            if (f.getName().equals(fileName)) {
                hash = f.getHash();
                type = f.getType();
            }
        }*/
        FileItem f = files.get(position);
        hash = f.getHash();
        type = f.getType();
        
        if (hash == null) hash = user; //default to home dir

        F.nl("Name: " + fileName);
        F.nl("Hash: " + hash);
        open(hash, type);
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
    boolean bbPressed = false;
    @Override
    public void onBackPressed() {
        F.nl("back button pressed");
        if (folderBeingViewed.size() == 1) {
            if (!bbPressed) {
                bbPressed = true;
                toast("Press again to log out");
            } else if (bbPressed) {
                finish(); //ends the activity
            }
        } else {
            bbPressed = false;
            F.nl("going back");
            folderBeingViewed.remove(folderBeingViewed.size() - 1);
            open(folderBeingViewed.get(folderBeingViewed.size() - 1), "folder");
        }
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
    public void toast(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }
}
