package io.github.villa7.foxfileapp;

import android.widget.Toast;

/**
 * Created by kluget on 5/11/2015.
 */
class F {

    public static void nl(Object o) {
        System.out.println(o);
    }
    public static void nl() {
        System.out.println();
    }
    public static void pa(Object[] o) {
        int i = 0;
        for(Object obj : o) {
            F.nl(i + ": " + obj);
            i++;
        }
    }

}
