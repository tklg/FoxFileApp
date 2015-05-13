package io.github.villa7.foxfileapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by kluget on 5/12/2015.
 */

public class FileItemAdapter extends ArrayAdapter<FileItem> {

    private static class ViewHolder {
        TextView name;
        TextView type;
        TextView size;
    }

    public FileItemAdapter(Context context, ArrayList<FileItem> files) {
        super(context, 0, files);
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

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //fill template
        viewHolder.name.setText(file.name);
        viewHolder.type.setText(file.getDetType());
        viewHolder.size.setText(file.getSize());

        return convertView;
    }
}