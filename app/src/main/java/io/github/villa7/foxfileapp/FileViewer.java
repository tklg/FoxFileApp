package io.github.villa7.foxfileapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.goebl.david.Webb;

public class FileViewer extends Activity {

    private Webb webb;
    private String phpsessid;
    private String user;
    private String fileHash;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_viewer);

        webb = Webb.create();

        Intent intent = getIntent();
        phpsessid = intent.getStringExtra("phpsessid");
        user = intent.getStringExtra("username");
        fileHash = intent.getStringExtra("filehash");
        type = intent.getStringExtra("filetype");
        F.nl("user:\t\t" + user + "\nsessid:\t" + phpsessid + "\nhash:\t" + fileHash + "\ntype:\t" + type);

        getPreview(fileHash, type);

    }

    private void getPreview(String fileHash, String type) {
        switch (type) {
            case "text":
            case "code":
                Request post = new Request(webb, phpsessid, "read_file", fileHash);
                post.start();
                String res = post.getResponse().toString();

                TextView textPreview = (TextView) findViewById(R.id.text_preview);
                textPreview.setText(res);
                break;
            default:
                F.nl("file type not supported yet");
                toast("File type not supported yet");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_file_viewer, menu);
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
