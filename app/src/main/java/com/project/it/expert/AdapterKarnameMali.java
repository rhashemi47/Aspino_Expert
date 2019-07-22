package com.project.it.expert;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

@SuppressLint("NewApi")
public class AdapterKarnameMali extends BaseAdapter {


    private String karbarCode;
    private int check_tab;
    public ArrayList<HashMap<String, String>> list;
    Activity activity;

    public AdapterKarnameMali(Activity activity, ArrayList<HashMap<String, String>> list) {
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
        TextView tvCurrency;
        TextView tvOrderCode;
        TextView tvDateHesab;
        TextView tvStatusHesab;
        LinearLayout LinearStatusService;
        LinearLayout LinearMain;
    }

    // @Override
    @SuppressLint("ResourceAsColor")
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();
        HashMap<String, String> map = list.get(position);
//        if (convertView == null) {
//            Typeface faceh = Typeface.createFromAsset(activity.getAssets(), "font/vazir.ttf");
            convertView = inflater.inflate(R.layout.list_item_karname_mali, null);
            holder = new ViewHolder();
            holder.tvCurrency = (TextView) convertView.findViewById(R.id.tvCurrency);
            holder.tvOrderCode = (TextView) convertView.findViewById(R.id.tvOrderCode);
            holder.tvDateHesab = (TextView) convertView.findViewById(R.id.tvDateHesab);
            holder.tvStatusHesab = (TextView) convertView.findViewById(R.id.tvStatusHesab);
            holder.LinearStatusService = (LinearLayout) convertView.findViewById(R.id.LinearStatusService);
            holder.LinearMain = (LinearLayout) convertView.findViewById(R.id.LinearMain);
            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
        String Code = map.get("Code");
        String Currency = map.get("Currency");
        String Status = map.get("Status");
        String OrderCode = map.get("OrderCode");
        String DateHesab = map.get("DateHesab");
        if(Status.compareTo("بستانکار")==0)
        {
//            holder.LinearStatusService.setBackgroundColor(Color.parseColor("#FF09B94B"));
            holder.LinearStatusService.setBackgroundResource(R.drawable.rounded_head_karname_bestankar);
            holder.tvStatusHesab.setText(Status);
//            if(Status.compareTo("5")==0)
//            holder.LinearMain.setOnClickListener(ItemOnclickPardakht);
        }
        else
        {
//            holder.LinearStatusService.setBackgroundColor(Color.parseColor("#FFE70202"));
            holder.LinearStatusService.setBackgroundResource(R.drawable.rounded_head_karname_bedehkar);
            holder.tvStatusHesab.setText(Status);
//            holder.LinearMain.setOnClickListener(ItemOnclickSelectHamyar);
        }
        holder.LinearMain.setTag(Code);
        holder.tvCurrency.setText(Currency);
        holder.tvOrderCode.setText(OrderCode);
        holder.tvDateHesab.setText(DateHesab);
        return convertView;
    }


//    private OnClickListener ItemOnclickSelectHamyar = new OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            String code = ((LinearLayout)v).getTag().toString();
////            Toast.makeText(activity.getApplicationContext(),"Select Hamyar",Toast.LENGTH_LONG).show();
//            Intent intent = new Intent(activity.getApplicationContext(),Select_Hamyar.class);//todo SelectHamyar
//            intent.putExtra("OrderCode",code);
//            activity.startActivity(intent);
//        }
//    };
//    private OnClickListener ItemOnclickPardakht = new OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            String code = ((LinearLayout)v).getTag().toString();
////            Toast.makeText(activity.getApplicationContext(),"Pardakht Factor",Toast.LENGTH_LONG).show();
//            Intent intent = new Intent(activity.getApplicationContext(),Pardakh_Factor_Sefaresh.class);//todo pardakh_factor_sefaresh.xml
//            intent.putExtra("OrderCode",code);
//            activity.startActivity(intent);
//        }
//    };

}

