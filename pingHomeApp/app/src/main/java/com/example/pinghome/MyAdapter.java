package com.example.pinghome;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {

    public ArrayList<Menu> source;
    public Context context;

    public MyAdapter(Context context, ArrayList<Menu> source){
        this.source = source;
        this.context = context;
    }
    @Override
    public int getCount() {
        return this.source.size();
    }

    @Override
    public Object getItem(int position) {
        return this.source.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(this.context).inflate(R.layout.my_adaptor_layout,
                    parent, false);
            TextView title = convertView.findViewById(R.id.tvTitle);
            title.setText(source.get(position).title);
        }
        return convertView;
    }
}
