package id.developer.lynx.damapps.main;

/**
 * @author bend
 * @version 1.0
 * @date 11/6/2016
 * Kelas untuk menampilkan halaman latihan
 * Halaman latihan terdiri dari 4 tombol
 * Tombol Angka untuk menuju ke ActivityLatihanAngka
 * Tombol Huruf untuk menuju ke ActivityLatihanHuruf
 * Tombol Kata untuk menuju ke ActivityLatihanKata
 * Tombol Kembali untuk kembali ke tampilan utama
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import id.developer.lynx.damapps.ActivityMain;
import id.developer.lynx.damapps.R;
import id.developer.lynx.damapps.Utils;
import id.developer.lynx.damapps.main.latihan.ActivityShowLatihan;

public class ActivityLatihan extends AppCompatActivity {

    /** Variable untuk komponen yang ada di layout Latihan */
    TextView textLatihanAngka, textLatihanHuruf, textLatihanKata;
    ImageView imageKembali;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /** Menentukan layout yang akan ditampilkan ketika class ini pertama kali dipanggil */
        setContentView(R.layout.activity_latihan);

        /** Memanggil method initView */
        initView();
    }

    /** Method untuk menginisialisasi komponen yang ada di layout */
    private void initView(){

        /** Inisialisasi komponen di layout ke dalam variable */
        textLatihanAngka = (TextView)findViewById(R.id.text_act_latihan_angka);
        textLatihanHuruf = (TextView)findViewById(R.id.text_act_latihan_huruf);
        textLatihanKata = (TextView)findViewById(R.id.text_act_latihan_kata);
        imageKembali = (ImageView)findViewById(R.id.image_act_latihan_kembali);

        /**
         * Ketika button Latihan Angka, Latihan Huruf, dan Latihan Kata diclick akan mengarah ke satu perintah yang sama
         * Yaitu perintah untuk berpindah Activity
         * Namun dengan parameter yang berbeda
         * Maka dijadikan satu method textButtonClicked dengan parameter yang diterima sama hanya nilainya yang berbeda
         */
        textButtonClicked(textLatihanAngka, Utils.PARAM_LATIHAN_ANGKA);
        textButtonClicked(textLatihanHuruf, Utils.PARAM_LATIHAN_HURUF);
        textButtonClicked(textLatihanKata, Utils.PARAM_LATIHAN_KATA);

        /** Ketika button Kembali diclick maka akan menutup activity saat ini dan menampilkan activity sebelumnya */
        imageKembali.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                }
        );
    }

    /** Method untuk menginisialisasi perintah yang akan dilakukan ketika salah satu button diclick */
    private void textButtonClicked(TextView text, final String param){
        text.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        /** Inisialisasi intent untuk menuju ke Activity Show Latihan */
                        Intent intent = new Intent(ActivityLatihan.this, ActivityShowLatihan.class);

                        /** Mengirim data untuk diproses di Activity Show Latihan */
                        intent.putExtra(Utils.PARAM_LATIHAN, param);

                        /** Pindah activity dari Activity Latihan ke Activity Show Latihan */
                        startActivity(intent);
                    }
                }
        );
    }
}
