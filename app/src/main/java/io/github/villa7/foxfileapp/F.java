package io.github.villa7.foxfileapp;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by kluget on 5/11/2015.
 */
class F {
    public static void hi() {
        nl("Hello world!");
    }
    public static void nl(Object o) {
        System.out.println(o);
    }
    public static void l(Object o) {
        System.out.print(o);
    }
    public static void nl() {
        System.out.println();
    }
    public static void pa(Object[] o) {
        int i = 0;
        for(Object obj : o) {
            nl(i + ": " + obj.toString());
            i++;
        }
    }
    public static void pa2d(Object[][] o) {
        int i = 0, c = 0;
        for (Object[] obj1 : o) {
            l(i + ": ");
            for (Object obj2 : obj1) {
                l(obj2.toString() + "\t\t");
            }
            F.nl();
        }
    }
    public static void pm(Map m) {
        if (m != null) {
            int i = 0;
            Iterator it = ((Map) m).entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                nl(i + ": " + pair.getKey() + " = " + pair.getValue());
                it.remove();
                i++;
            }
        } else {
            throw new NullPointerException("F.pm(Map m) cannot be null");
        }
    }

}
