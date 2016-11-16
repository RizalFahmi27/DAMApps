package id.developer.lynx.damapps.main.tantangan.result;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import id.developer.lynx.damapps.R;
import id.developer.lynx.damapps.main.tantangan.ObjectSoal;

/**
 * Created by Bend on 11/9/2016.
 */

public class SystemListAdapterResult extends BaseAdapter {

    Context context;
    List<ObjectSoal> listSoal;

    public SystemListAdapterResult(Context context, List<ObjectSoal> listSoal){
        this.context = context;
        this.listSoal = listSoal;
    }

    @Override
    public int getCount() {
        return listSoal.size();
    }

    @Override
    public Object getItem(int position) {
        return listSoal.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listSoal.indexOf(listSoal.get(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_list_result_tantangan, null);
        }

        String jawaban_benar = listSoal.get(position).jawaban_benar;
        String jawaban_user = listSoal.get(position).jawaban_user;

        ((TextView)convertView.findViewById(R.id.text_lay_list_result_id)).setText("" +(position+1));
        ((TextView)convertView.findViewById(R.id.text_lay_list_result_jawaban_benar)).setText(jawaban_benar);
        ((TextView)convertView.findViewById(R.id.text_lay_list_result_jawaban_user)).setText(jawaban_user);

        if(jawaban_benar.equalsIgnoreCase(jawaban_user)){
            ((TextView)convertView.findViewById(R.id.text_lay_list_result_jawaban_user)).setTextColor(Color.parseColor("#57ed59"));
        }else{
            ((TextView)convertView.findViewById(R.id.text_lay_list_result_jawaban_user)).setTextColor(Color.parseColor("#f26464"));
        }

        return convertView;
    }
}
