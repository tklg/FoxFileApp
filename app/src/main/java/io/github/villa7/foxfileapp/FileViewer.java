package io.github.villa7.foxfileapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.goebl.david.Webb;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

public class FileViewer extends Activity {

    private Webb webb;
    private String phpsessid;
    private String user;
    private String fileHash;
    private String type;
    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_viewer);

        webb = Webb.create();

        progress = (ProgressBar) findViewById(R.id.load_progress);
        progress.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.primary), android.graphics.PorterDuff.Mode.SRC_IN);

        Intent intent = getIntent();
        phpsessid = intent.getStringExtra("phpsessid");
        user = intent.getStringExtra("username");
        fileHash = intent.getStringExtra("filehash");
        type = intent.getStringExtra("filetype");
        F.nl("user:\t\t" + user + "\nsessid:\t" + phpsessid + "\nhash:\t" + fileHash + "\ntype:\t" + type);

        getPreview("read_file", fileHash);

    }

    private void getPreview(String... params) {
        showSpinner();
        switch (type) {
            case "text":
            case "code":
                /*Request post = new Request(webb, phpsessid, "read_file", fileHash);
                post.start();
                String res = post.getResponse().toString();*/

                getText(params);

                /*TextView textPreview = (TextView) findViewById(R.id.text_preview);
                textPreview.setText(res);*/
                break;
            default:
                F.nl("file type not supported yet");
                toast("File type not supported yet");
        }
    }
    public void getText(String... params) {
        final Context context = this;

        Object[] bla = Params.getParams(params);
        String page = (String) bla[0];
        F.nl("Page: " + page);
        F.nl("params:");
        F.pa(params);
        RequestParams param = (RequestParams) bla[1];

        FoxFileClient.post(page, param, new TextHttpResponseHandler() { /*JsonHttpResponseHandler*/

            @Override
            public void onSuccess(int statusCode, Header[] headers, String res) {
                F.nl("Result: " + res);
                TextView textPreview = (TextView) findViewById(R.id.text_preview);
                textPreview.setText(res);
                hideSpinner();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable error) {
                F.nl("failed");
            }
        });

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
}
