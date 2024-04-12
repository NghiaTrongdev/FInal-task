package com.example.baitap1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class myAdapter extends BaseAdapter {
    private ArrayList<Thisinh> lst;
    private Context context;
    private LayoutInflater inflater;
    public myAdapter(Context context ,ArrayList<Thisinh> data ){
        this.context = context;
        this.lst = data;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return lst.size();
    }

    @Override
    public Object getItem(int position) {
        return lst.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_container, parent, false);
            holder = new ViewHolder();
            holder.txtSbd = convertView.findViewById(R.id.txtSbd);
            holder.txtName = convertView.findViewById(R.id.txtname);
            holder.txtPoint = convertView.findViewById(R.id.txtPoint);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Thisinh item = lst.get(position);
        holder.txtSbd.setText(item.getSbd());
        holder.txtName.setText(item.getName());
        holder.txtPoint.setText(String.format("%.1f", calculaterAvg(item)));

        return convertView;
    }
    private double calculaterAvg(Thisinh thisinh){
        return (thisinh.getToan() + thisinh.getLy() + thisinh.getHoa())/3.0;
    }
    static class ViewHolder {
        TextView txtSbd;
        TextView txtName;
        TextView txtPoint;
    }
}
