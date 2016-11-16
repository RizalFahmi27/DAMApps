package id.developer.lynx.damapps.main.tantangan.history;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import id.developer.lynx.damapps.R;
import id.developer.lynx.damapps.main.tantangan.ObjectHistory;

/**
 * Created by Bend on 11/9/2016.
 */

public class SystemListAdapterHistory extends BaseAdapter {

    Context context;
    List<ObjectHistory> listHistory;

    public SystemListAdapterHistory(Context context, List<ObjectHistory> listHistory){
        this.context = context;
        this.listHistory = listHistory;
    }

    @Override
    public int getCount() {
        return listHistory.size();
    }

    @Override
    public Object getItem(int position) {
        return listHistory.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listHistory.indexOf(listHistory.get(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_list_history_tantangan, null);
        }

        ((TextView)convertView.findViewById(R.id.text_lay_list_history_id)).setText("" +(position+1));
        ((TextView)convertView.findViewById(R.id.text_lay_list_history_date)).setText(listHistory.get(position).getTanggal());
        int score = listHistory.get(position).getHasil();
        ((TextView)convertView.findViewById(R.id.text_lay_list_history_jawaban_benar))
                .setText(score+ "/10");
        CardView cardView = (CardView) convertView.findViewById(R.id.cardViewScoreHistory);
        if(score>=0 && score<=4){
            cardView.setCardBackgroundColor(Color.parseColor("#f7465e"));
        }
        else if(score>4 && score<=7){
            cardView.setCardBackgroundColor(Color.parseColor("#fff167"));
        }
        else cardView.setCardBackgroundColor(Color.parseColor("#7aea78"));

        return convertView;
    }
}
