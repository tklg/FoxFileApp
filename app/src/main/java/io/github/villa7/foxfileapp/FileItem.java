package io.github.villa7.foxfileapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by kluget on 5/12/2015.
 */
public class FileItem {

    private int PID;
    public String size = "0",
            owner = "",
            name = "placeholder",
            type = "folder",
            detType = "",
            icon = "",
            hash = "test_hash",
            parent = "test_parent",
            lastmod = "00 0, 0000";
    //from json object
    public static FileItem fromJSON(JSONObject jsonObject) {
        FileItem fi = new FileItem();
        try {
            fi.PID = jsonObject.getInt("PID");
            fi.owner = jsonObject.getString("owner");
            fi.name = jsonObject.getString("file_name");
            fi.type = jsonObject.getString("file_type");
            fi.hash = jsonObject.getString("file_self");
            fi.parent = jsonObject.getString("file_parent");
            fi.lastmod = jsonObject.getString("last_modified");
        } catch (JSONException e) {
            /*e.printStackTrace();
            return null;*/
        }
        try { //because the folders do not have a size and the thing throwing an exception breaks naming folders
            fi.size = jsonObject.getInt("file_size") + "";
        } catch (JSONException e) {
            /*e.printStackTrace();
            return null;*/
        }

        FileInfo fo = new FileInfo(fi);
        fo.getInfo();
        fi.size = fo.getSize();
        fi.type = fo.getBft();
        fi.detType = fo.getDft();
        fi.icon = fo.getIcon();

        return fi;
    }
    //from json array, returned from server
    public static ArrayList<FileItem> fromJSON(JSONArray arr) {
        ArrayList<FileItem> files = new ArrayList<FileItem>();
        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj;
            try {
                obj = arr.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            FileItem file = FileItem.fromJSON(obj);
            if (file != null) {
                files.add(file);
            }
        }
        return files;
    }

    public int getPID() {
        return PID;
    }
    public String getSize() {
        return size;
    }
    public String getOwner() {
        return owner;
    }
    public String getName() {
        return name;
    }
    public String getType() {
        return type;
    }
    public String getDetType() {
        return detType;
    }
    public String getHash() {
        return hash;
    }
    public String getParent() {
        return parent;
    }
    public String getLastmod() {
        return lastmod;
    }

}
