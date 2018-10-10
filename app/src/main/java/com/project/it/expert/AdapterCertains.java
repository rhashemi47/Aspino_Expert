package com.project.it.expert;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

@SuppressLint("NewApi")
public class AdapterCertains extends BaseAdapter {


    private ArrayList<HashMap<String, String>> list;
    private Activity activity;
    private String guid;
    private String hamyarcode;
    private String Table;

    public AdapterCertains(Activity activity, ArrayList<HashMap<String, String>> list, String guid, String hamyarcode) {
        super();
        this.activity = activity;
        this.list = list;
        this.guid = guid;
        this.hamyarcode = hamyarcode;
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
        TextView tvTitleOrder;
        TextView tvOrderDate;
        TextView tvAddres;
        LinearLayout LinearItemOrder;
    }

    // @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();
        HashMap<String, String> map = list.get(position);
        convertView = inflater.inflate(R.layout.list_item_order, null);
        holder = new ViewHolder();
        holder.tvTitleOrder = (TextView) convertView.findViewById(R.id.tvTitleOrder);
        holder.tvOrderDate = (TextView) convertView.findViewById(R.id.tvOrderDate);
        holder.tvAddres = (TextView) convertView.findViewById(R.id.tvAddres);
        holder.LinearItemOrder = (LinearLayout) convertView.findViewById(R.id.LinearItemOrder);
        convertView.setTag(holder);
        String TitleOrder = map.get("TitleOrder");
        String OrderDate = map.get("OrderDate");
        String Addres = map.get("Addres");
        String Code = map.get("Code");
        this.Table = map.get("Table");
        holder.tvTitleOrder.setText(TitleOrder);
        holder.tvAddres.setText(Addres);
        holder.tvOrderDate.setText(OrderDate);
        holder.LinearItemOrder.setTag(Code);
        holder.LinearItemOrder.setOnClickListener(TextViewItemOnclick);
        return convertView;
    }


    private OnClickListener TextViewItemOnclick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            String BsUserServicesID="";
            BsUserServicesID = ((LinearLayout)v).getTag().toString();
            Intent intent = new Intent(activity.getApplicationContext(),Joziat_Sefaresh.class);
            intent.putExtra("guid",guid);
            intent.putExtra("hamyarcode",hamyarcode);
            intent.putExtra("BsUserServicesID",BsUserServicesID);
            intent.putExtra("Table",Table);
            activity.startActivity(intent);
        }
    };
}

