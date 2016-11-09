package id.developer.lynx.damapps.main.latihan;

/**
 * @author bend
 * @version 1.0
 * @date 11/6/2016
 * Kelas untuk menampilkan teori latihan sesuai dengan latihan yang dipilih
 * Di kelas ini terdapat sebuah grid yang berfungsi untuk menampilkan teori latihan
 */

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import id.developer.lynx.damapps.R;
import id.developer.lynx.damapps.Utils;

public class ActivityShowLatihan extends AppCompatActivity {

    /** Variable grid untuk menampilkan semua data */
    GridView gridView;

    /**
     * Variable type latihan akan menentukan latihan mana yang akan ditampilkan
     * Jika berisi angka, maka grid akan menampilkan angka
     * Jika berisi huruf, maka grid akan menampilkan huruf
     * Jika berisi kata, maka grid akan menampilkan kata
     * Isi dari variable ini diambil dari data yang dikirim dari ActivityLatihan sebelumnya
     */
    String typeLatihan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /** Menentukan layout yang akan ditampilkan ketika class ini pertama kali dipanggil */
        setContentView(R.layout.activity_show_latihan);

        /** Memanggil method initBundle */
        initBundle();

        /** Memanggil method initView */
        initView();
    }

    /**
     * Method ini digunakan untuk mengambil data yang dikirim dari ActivityLatihan sebelumnya
     * Data yang diambil akan disimpan di variable typeLatihan
     */
    private void initBundle(){

        /** Mengambil data */
        Bundle extras = getIntent().getExtras();

        /** Menyimpan data ke variable */
        typeLatihan = extras.getString(Utils.PARAM_LATIHAN);
    }

    private void initView(){
        /** Inisialisasi grid */
        gridView = (GridView)findViewById(R.id.grid_act_show_latihan);

        /** Inisialisasi adapter untuk grid */
        gridView.setAdapter(new SystemGridAdapterLatihan(this, getListLatihan()));
    }

    /**
     * Method untuk mendapatkan data yang akan ditampilkan di grid
     * Data yang ditampilkan sesuai dengan tipe latihan
     * Jika angka, maka data yang diambil adalah latihan angka
     * Jika huruf, maka data yang diambil adalah latihan huruf
     * Jika kata, maka data yang diambil adalah latihan kata
     * @return list yang berisi data latihan
     */
    private List<ObjectLatihan> getListLatihan(){

        /** Inisialisasi variable list dan data kosong */
        List<ObjectLatihan> list = new ArrayList<>();
        String[] data = {};

        /** Pengecekan tipe latihan */
        if(typeLatihan.equalsIgnoreCase(Utils.PARAM_LATIHAN_ANGKA)){

            /** Mengambil data angka */
            data = Utils.LIST_LATIHAN_ANGKA;
        }else if(typeLatihan.equalsIgnoreCase(Utils.PARAM_LATIHAN_HURUF)){

            /** Mengambil data huruf */
            data = Utils.LIST_LATIHAN_HURUF;
        }

        /** Looping untuk memasukkan data ke dalam list */
        for (int i = 0; i < data.length; i++) {

            /** Memasukkan data ke dalam list */
            list.add(new ObjectLatihan(data[i], ""));
        }

        return list;
    }
}
