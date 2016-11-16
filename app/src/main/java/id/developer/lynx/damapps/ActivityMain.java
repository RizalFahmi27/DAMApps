package id.developer.lynx.damapps;

/**
 * @author bend
 * @version 1.0
 * @date 11/6/2016
 * Kelas untuk menampilkan halaman utama
 * Halaman utama terdiri dari 4 tombol
 * Tombol Latihan untuk menuju ke ActivityLatihan
 * Tombol Tantangan untuk menuju ke ActivityTantangan
 * Tombol About untuk menuju ke ActivityAbout
 * Tombol Keluar untuk menutup aplikasi
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import id.developer.lynx.damapps.main.ActivityAbout;
import id.developer.lynx.damapps.main.ActivityLatihan;
import id.developer.lynx.damapps.main.ActivityTantangan;

public class ActivityMain extends AppCompatActivity {

    /** Variable untuk komponen yang ada di layout Main */
    TextView textLatihan, textTantangan;
    ImageView imageAbout, imageExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /** Menentukan layout yang akan ditampilkan ketika class ini pertama kali dipanggil */
        setContentView(R.layout.activity_main);

        /** Memanggil method initView */
        initView();
    }

    /** Method untuk menginisialisasi komponen yang ada di layout */
    private void initView(){

        /** Inisialisasi komponen di layout ke dalam variable */
        textLatihan = (TextView)findViewById(R.id.text_act_main_latihan);
        textTantangan = (TextView)findViewById(R.id.text_act_main_tantangan);
        imageAbout = (ImageView)findViewById(R.id.image_act_main_about);
        imageExit = (ImageView)findViewById(R.id.image_act_main_exit);

        /**
         * Ketika button Latihan, Tantangan, dan About diclick akan mengarah ke satu perintah yang sama
         * Yaitu perintah untuk berpindah Activity
         * Namun dengan tujuan yang berbeda
         * Maka dijadikan satu method textButtonClicked dengan parameter yang diterima sama hanya nilainya yang berbeda
         */
        textButtonClicked(textLatihan, ActivityLatihan.class);
        textButtonClicked(textTantangan, ActivityTantangan.class);
        textButtonClicked(imageAbout, ActivityAbout.class);

        /** Ketika button Keluar diclick maka akan menghentikan aplikasi */
        imageExit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                }
        );
    }

    /** Method untuk menginisialisasi perintah yang akan dilakukan ketika salah satu button diclick */
    private void textButtonClicked(View text, final Class goTo){
        text.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        /** Memindah tampilan dari main ke tujuan sesuai button yang diclick*/
                        startActivity(new Intent(ActivityMain.this, goTo));
                    }
                }
        );
    }
}
