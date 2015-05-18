package io.github.villa7.foxfileapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.goebl.david.Webb;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

public class FileViewer extends Activity {

    private Webb webb;
    private String phpsessid;
    private String user;
    private String fileHash;
    private String fileName;
    private String type;
    private ProgressBar progress;
    private String[] allowedTypes = {".*"};

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
        fileName = intent.getStringExtra("filename");
        type = intent.getStringExtra("filetype");
        F.nl("user:\t\t" + user + "\nsessid:\t" + phpsessid + "\nhash:\t" + fileHash + "\ntype:\t" + type);

        setTitle(fileName);
        getPreview(getQuery(), fileHash);

    }
    private String getQuery() {
        switch(type) {
            case "text":
            case "code":
                return "read_file";
            case "image":
            case "audio":
            case "video":
                return "preview";
            case "zip":
                return "zip";
            case "pdf":
                return "preview";
            default:
                F.nl("file query type not supported yet");
                return "";
        }
    }
    private void getPreview(String... params) {
        showSpinner();
        switch (type) {
            case "text":
            case "code":
                /*Request post = new Request(webb, phpsessid, "read_file", fileHash);
                post.start();
                String res = post.getResponse().toString();*/
                /*TextView textPreview = (TextView) findViewById(R.id.text_preview);
                textPreview.setText(res);*/
                getText(params);
                break;
            case "image":
                getImagePreview(params);
                //toast("Is image");
                break;
            case "audio":
                toast("Is audio");
                break;
            case "video":
                toast("Is video");
                break;
            case "zip":
                toast("Is archive");
                break;
            case "pdf":
                toast("Is PDF");
                break;
            default:
                F.nl("file type not supported yet");
                toast("File type not supported yet");
        }
    }
    public void getText(String... params) {

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
                textPreview.setVisibility(View.VISIBLE);
                hideSpinner();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable error) {
                F.nl("failed");
            }
        });
    }
    public void getImagePreview(String... params) {

        Object[] bla = Params.getParams(params);
        String page = (String) bla[0];
        F.nl("Page: " + page);
        F.nl("params:");
        F.pa(params);
        RequestParams param = (RequestParams) bla[1];

        FoxFileClient.get(page, param, new BinaryHttpResponseHandler(allowedTypes) { /*JsonHttpResponseHandler*/
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] res) {
                F.nl("Result: " + new String(res));
                F.nl("res is: " + res.getClass().getName());
                ImageView imagePreview = (ImageView) findViewById(R.id.image_preview);
                Bitmap bmp = BitmapFactory.decodeByteArray(res, 0, res.length);
                imagePreview.setImageBitmap(bmp);
                imagePreview.setVisibility(View.VISIBLE);
                hideSpinner();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] res, Throwable error) {
                hideSpinner();
                F.nl("failed");
                toast("Failed to connect to server");
                error.printStackTrace();
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
