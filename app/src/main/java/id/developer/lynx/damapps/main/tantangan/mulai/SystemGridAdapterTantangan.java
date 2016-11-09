package id.developer.lynx.damapps.main.tantangan.mulai;

import android.content.Context;
import android.content.pm.LabeledIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import id.developer.lynx.damapps.R;
import id.developer.lynx.damapps.Utils;

/**
 * Created by Bend on 11/9/2016.
 */

public class SystemGridAdapterTantangan extends BaseAdapter {

    Context context;
    List<String> list;
    String typeTantangan;

    public SystemGridAdapterTantangan(Context context, List<String> list, String typeTantangan){
        this.context = context;
        this.list = list;
        this.typeTantangan = typeTantangan;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_grid_tantangan, null);
        }

        if(typeTantangan.equalsIgnoreCase(Utils.TANTANGAN_TYPE_TEXT)){
            ((TextView)convertView.findViewById(R.id.text_lay_grid_tantangan)).setText(list.get(position));
            ((ImageView)convertView.findViewById(R.id.image_lay_grid_tantangan)).setVisibility(View.GONE);
        }else{
            ((TextView)convertView.findViewById(R.id.text_lay_grid_tantangan)).setText(list.get(position));
            ((ImageView)convertView.findViewById(R.id.image_lay_grid_tantangan)).setVisibility(View.GONE);
        }

        return convertView;
    }
}
