package io.github.villa7.foxfileapp;

import android.content.Context;
import android.graphics.Typeface;

import java.util.Hashtable;

/**
 * Created by kluget on 5/17/2015.
 * This is a class to prevent fonts from being loaded multiple times.
 * This saves memory :D
 */
public class Typefaces {

    private static final Hashtable<String, Typeface> fontCache = new Hashtable<String, Typeface>();

    public static Typeface get(Context c, String assetPath) {
        synchronized (fontCache) {
            if (!fontCache.containsKey(assetPath)) {
                try {
                    Typeface t = Typeface.createFromAsset(c.getAssets(), assetPath);
                    fontCache.put(assetPath, t);
                } catch (Exception e) {
                    F.nl("Failed to load typeface - " + e.getMessage());
                    return null;
                }
            }
            return fontCache.get(assetPath);
        }
    }
}
