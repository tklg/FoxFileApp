package io.github.villa7.foxfileapp;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;


import com.goebl.david.Webb;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Browse extends Activity implements OnItemClickListener, OnItemLongClickListener, OnItemSelectedListener{

    private Webb webb;
    private String phpsessid;
    private String user;
    private String fileName;
    private String hash, type;
    private static ArrayList<String> folderBeingViewed = new ArrayList<String>(); //store folder hashes in here
    private static ArrayList<FileItem> files;
    private ProgressBar progress;
    private ListView listView;
    private FileItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        //getLoaderManager().initLoader(0, null, this);

        setContentView(R.layout.activity_browse);

        webb = Webb.create();

        Intent intent = getIntent();
        phpsessid = intent.getStringExtra("phpsessid");
        user = intent.getStringExtra("username");
        F.nl("user:\t" + user + "\nsessid:\t" + phpsessid);
        progress = (ProgressBar) findViewById(R.id.load_progress);
        progress.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.primary), android.graphics.PorterDuff.Mode.SRC_IN);

        listView = (ListView) findViewById(R.id.menubar);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        listView.setOnItemSelectedListener(this);
        open(user, "folder"); //open the root directory to start
        //setTitle("FoxFile");
    }

    private void open(String fileHash, String type) {
        showSpinner();
        F.nl("opening " + fileHash);
        if (type.equals("folder")) {

            try {
                getResult("dir", fileHash, type);
            } catch (Exception e) {
                F.nl("Failed to get result from getResult()");
                e.printStackTrace();
            }

            //Request post = new Request(webb, phpsessid, "dir", fileHash, type);
            //Request post = new Request(webb, phpsessid, "phpsession");
            F.nl("POST ?dir=" + fileHash + "&type=" + type);

            //post.start();
            //JSONArray res = (JSONArray) post.getResponse();

            //files = FileItem.fromJSON(res);
            //System.out.println(files);
            //adapter = new FileItemAdapter(this, files);

            /*listView = (ListView) findViewById(R.id.menubar);

            listView.setAdapter(adapter);
            listView.setOnItemClickListener(this);
            listView.setOnItemLongClickListener(this);
            listView.setOnItemSelectedListener(this);*/

            /*hideSpinner();
            if (fileName != null && fileName.equals(user)) fileName = "My Files";
            setTitle(fileName);*/
            if (!folderBeingViewed.contains(fileHash)) {
                folderBeingViewed.add(fileHash);
            } else {
                F.nl("arraylist already has folder, was the back button pressed?");
            }
        } else {
            F.nl("Type of opened file not \"folder\", was \"" + type + "\"");
            F.nl("GET ?preview=" + fileHash); //not how the preview query works (returns an image)

            //if new page doesnt keep this page state, use a modal
            Intent intentView = new Intent(this, FileViewer.class);
            intentView.putExtra("phpsessid", phpsessid);
            intentView.putExtra("username", user);
            intentView.putExtra("filename", fileName);
            intentView.putExtra("filehash", fileHash);
            intentView.putExtra("filetype", type);
            //intentView.putExtra("siblings", files); //send the other files in the folder along with it for swipe left/right (does not work)
            hideSpinner();
            startActivity(intentView);
        }
    }

    public void onItemClick(AdapterView<?> l, View v, int position, long id) {
        F.nl("Pressed: " + position);

        //View view = LayoutInflater.from().inflate(R.layout.layout_file, parent, false);
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.layout_file);
        TextView name = (TextView) layout.findViewById(R.id.fileName);
        String fileName = name.getText().toString();
        //this.fileName = fileName;

        hash = null;
        type = "folder";
        /*for (FileItem f : files) {
            if (f.getName().equals(fileName)) {
                hash = f.getHash();
                type = f.getType();
            }
        }*/
        FileItem f = files.get(position);
        hash = f.getHash();
        type = f.getType();
        //fileName = f.getName();
        this.fileName = fileName;
        
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
    public void getResult(String... params) throws JSONException {
        final Context context = this;

        Object[] bla = Params.getParams(params);
        String page = (String) bla[0];
        RequestParams param = (RequestParams) bla[1];
        F.nl("Page: " + page);
        F.nl("params:");
        F.pa(params);

        FoxFileClient.post(page, param, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject res) {
                // If the response is JSONObject instead of expected JSONArray
                F.nl("Recieved a single result");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray res) {
                F.nl("Got result JSONArray");
                F.nl(res.toString());
                // Do something with the response
                //System.out.println(res);

                files = FileItem.fromJSON(res);
                //System.out.println(files);
                FileItemAdapter adapter = new FileItemAdapter(context, files);

                ListView listView = (ListView) findViewById(R.id.menubar);

                listView.setAdapter(adapter);
                hideSpinner();

                /*for (FileItem f : files) {
                    if (f.getHash().equals(params[1])) {
                        fileName = f.getName();
                    }
                }*/

                if (fileName != null && fileName.equals(user)) fileName = "My Files";
                F.nl("Setting title to " + fileName);
                //setTitle(fileName);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable error, JSONArray res) {
                hideSpinner();
                toast("Failed to connect to server");
                F.nl("failed");
            }
        });
    }
    public void toast(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }
}
