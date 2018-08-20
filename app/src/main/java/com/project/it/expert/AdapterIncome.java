package com.project.it.expert;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

@SuppressLint("NewApi")
public class AdapterIncome extends BaseAdapter {


    private ArrayList<HashMap<String, String>> list;
    private Activity activity;
    private String guid;
    private String hamyarcode;

    public AdapterIncome(Activity activity, ArrayList<HashMap<String, String>> list) {
        super();
        this.activity = activity;
        this.list = list;
    }

    // @Override
    public int getCount() {
        return list.size();
    }

    // @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    // @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder {
        TextView tvDateIncom;
        TextView tvPrice;
        TextView tvCodeOrder;
    }

    // @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();
        HashMap<String, String> map = list.get(position);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_income, null);
            holder = new ViewHolder();
            holder.tvDateIncom = (TextView) convertView.findViewById(R.id.tvDateIncom);
            holder.tvPrice = (TextView) convertView.findViewById(R.id.tvPrice);
            holder.tvCodeOrder = (TextView) convertView.findViewById(R.id.tvCodeOrder);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String ConfirmDate = map.get("ConfirmDate");
        String Price = map.get("Price");
        String Code = map.get("Code");
        holder.tvCodeOrder.setText(Code);
        holder.tvPrice.setTag(Price);
        holder.tvDateIncom.setTag(ConfirmDate);
        return convertView;
    }
}

