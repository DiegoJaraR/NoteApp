package com.example.alumnoicin.noteapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.alumnoicin.noteapp.R;
import com.example.alumnoicin.noteapp.models.Nota;


import java.util.List;

public class MyAdapter extends BaseAdapter {

    private List<Nota> list;
    private int layout;
    private Context context;

    public MyAdapter(List<Nota> list, int layout, Context context) {
        this.list = list;
        this.layout = layout;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Nota getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(layout, null);
            vh = new ViewHolder();
            vh.nota = (TextView) convertView.findViewById(R.id.textViewNota);
            vh.id = (TextView) convertView.findViewById(R.id.textViewID);
            vh.color = (LinearLayout) convertView.findViewById(R.id.layout_color);
            convertView.setTag( vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        Nota n = list.get(position);
        vh.id.setText("Nota: "+ n.getid() + "");
        vh.nota.setText(n.getNota());

        if (n.getColor() == 0){
            vh.color.setBackgroundColor(Color.WHITE);
        }
        else if(n.getColor()==1) {
            vh.color.setBackgroundColor(Color.YELLOW);
        }
        else if(n.getColor()==2) {
            vh.color.setBackgroundColor(Color.CYAN);
        }
        else if(n.getColor()==3) {
            vh.color.setBackgroundColor(Color.RED);
        }
        else if(n.getColor()==4) {
            vh.color.setBackgroundColor(Color.GREEN);
        }

        return convertView;
    }
    public class ViewHolder {
        TextView nota;
        TextView id;
        LinearLayout color;
    }


}