package io.github.villa7.foxfileapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by kluget on 5/12/2015.
 */

public class FileItemAdapter extends ArrayAdapter<FileItem> {

    private static class ViewHolder {
        TextView name;
        TextView type;
        TextView size;
        Button clickmenu;
        TextView icon;
    }
    private static Context context;

    public FileItemAdapter(Context context, ArrayList<FileItem> files) {
        super(context, 0, files);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FileItem file = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_file, parent, false);

            viewHolder.name = (TextView) convertView.findViewById(R.id.fileName);
            viewHolder.type = (TextView) convertView.findViewById(R.id.fileType);
            viewHolder.size = (TextView) convertView.findViewById(R.id.fileSize);
            viewHolder.clickmenu = (Button) convertView.findViewById(R.id.clickmenu);
            viewHolder.icon = (TextView) convertView.findViewById(R.id.file_icon);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //fill template
        viewHolder.name.setText(file.name);
        viewHolder.type.setText(file.getDetType());
        viewHolder.size.setText(file.getSize());
        viewHolder.clickmenu.setTypeface(Typefaces.get(context, "fontawesome-webfont.ttf"));
        int stringID = context.getResources().getIdentifier("icon_" + file.getType(), "string", context.getApplicationInfo().packageName);
        if (stringID == 0) {
            throw new IllegalArgumentException("No resource string found with name " + "icon_" + file.getType());
        } else {
            viewHolder.icon.setText(context.getString(stringID));
        }
        viewHolder.icon.setTypeface(Typefaces.get(context, "fontawesome-webfont.ttf"));

        return convertView;
    }
}