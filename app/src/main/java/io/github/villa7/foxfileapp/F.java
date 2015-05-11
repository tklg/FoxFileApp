package io.github.villa7.foxfileapp;

import android.widget.Toast;

/**
 * Created by kluget on 5/11/2015.
 */
public class F {

    public static boolean toast(Object o) {
        Toast.makeText(F.this, o, Toast.LENGTH_SHORT).show();
    }

}
