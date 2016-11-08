package id.developer.lynx.damapps.main.latihan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import id.developer.lynx.damapps.R;

/**
 * Created by Bend on 11/9/2016.
 */

public class SystemGridAdapterLatihan extends BaseAdapter {

    Context context;
    List<ObjectLatihan> list;

    public SystemGridAdapterLatihan(Context context, List<ObjectLatihan> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.indexOf(list.get(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_grid_latihan, null);
        }

        ((TextView)convertView.findViewById(R.id.text_lay_grid_latihan)).setText(list.get(position).getText());

        return convertView;
    }
}