package id.developer.lynx.damapps.main.latihan;

/**
 * @author bend
 * @version 1.0
 * @date 11/6/2016
 * Adapter untuk grid latihan
 */

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import id.developer.lynx.damapps.ActivityMain;
import id.developer.lynx.damapps.R;
import id.developer.lynx.damapps.Utils;

public class SystemGridAdapterLatihan extends BaseAdapter {

    Context context;
    List<ObjectLatihan> list;

    public SystemGridAdapterLatihan(Context context, List<ObjectLatihan> list){
        this.context = context;
        this.list = list;
    }

    /**
     * Method untuk mendapatkan banyaknya data di list
     * @return jumlah data list
     */
    @Override
    public int getCount() {
        return list.size();
    }

    /**
     * Method untuk mendapatkan object pada index tertentu
     * @param position
     * @return object pada index position
     */
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    /**
     * Method untuk mendapatkan id dari object pada index tertentu
     * @param position
     * @return id object pada index position
     */
    @Override
    public long getItemId(int position) {
        return list.indexOf(list.get(position));
    }

    /**
     * Method untuk inisialisasi layout dan semua transaksi pada setiap index
     * @param position
     * @param convertView
     * @param parent
     * @return view yang akan ditampilkan pada index position
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        /**
         * Pengecekan apakah convertView belum di-inisialisasi
         * Inisialisasi hanya dilakukan sekali
         * Setelah itu pengecekan akan dilewati
         */
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_grid_latihan, null);
        }

        String text = list.get(position).getText();
        int imageId = context.getResources().getIdentifier("drawable/"+list.get(position).getGambar(), "drawable", context.getPackageName());

        Log.d(Utils.LOG_TAG, Utils.CATCH_ERROR+ list.get(position).getGambar());

        /** Set text sesuai dengan text pada list */
        ((TextView)convertView.findViewById(R.id.text_lay_grid_latihan)).setText(text.toUpperCase());
        ((ImageView)convertView.findViewById(R.id.image_lay_grid_latihan)).setImageResource(imageId);

        return convertView;
    }
}
