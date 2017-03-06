package com.hm.animationdemo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ListViewAdapter extends ArrayAdapter<String> {

    private Context context;
    private List<String> stringList;
    private int resource;

    public ListViewAdapter(Context context, int resource, List<String> stringList) {
        super(context, resource, stringList);
        this.context = context;
        this.resource = resource;
        this.stringList = stringList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, parent, false);
            holder = new ViewHolder();
            holder.textView = (TextView) convertView.findViewById(android.R.id.text1);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.textView.setText(stringList.get(position));
        return convertView;
    }

    class ViewHolder {
        TextView textView;
    }
}