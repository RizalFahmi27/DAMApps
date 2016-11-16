package id.developer.lynx.damapps.main;

/**
 * @author bend
 * @version 1.0
 * @date 11/8/2016
 * Kelas untuk menampilkan halaman tantangan
 * Halaman latihan terdiri dari 2 tombol
 * Tombol Mulai untuk menuju mulai menampilkan soal di ActivityShowTantangan
 * Tombol History untuk menampilkan tantangan yang pernah dilakukan di ActivityHistoryTantangan
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

import java.util.ArrayList;
import java.util.List;

import id.developer.lynx.damapps.R;
import id.developer.lynx.damapps.SystemDBHandler;
import id.developer.lynx.damapps.Utils;
import id.developer.lynx.damapps.main.tantangan.ObjectSoal;
import id.developer.lynx.damapps.main.tantangan.history.ActivityHistoryTantangan;
import id.developer.lynx.damapps.main.tantangan.mulai.ActivityShowTantangan;

public class ActivityTantangan extends AppCompatActivity {

    /** Variable untuk komponen yang ada di layout Tantangan */
    TextView textMulai, textHistory;
    ImageView imageKembali;

    /** Variable dbHandler */
    SystemDBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tantangan);

        /** Inisialisasi awal dbHandler */
        dbHandler = new SystemDBHandler(this);

        /** Memanggil method initView */
        initView();
    }

    /** Method untuk menginisialisasi komponen yang ada di layout */
    private void initView(){

        /** Inisialisasi awal komponen di layout */
        textMulai = (TextView)findViewById(R.id.text_act_tantangan_start);
        textHistory = (TextView)findViewById(R.id.text_act_tantangan_history);
        imageKembali = (ImageView)findViewById(R.id.image_act_tantangan_kembali);

        /**
         * Inisialisasi ketika tombol mulai diclick
         * Ketika tombol diclick, system akan mengambil satu pertanyaan terlebih dahulu baru dikirim ke Activity selanjutnya
         */
        textMulai.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        /** Insialisasi awal list soal dan satu soal yang akan ditampilkan pertama */
                        ArrayList<ObjectSoal> soal = new ArrayList<ObjectSoal>();
                        ObjectSoal soalSelanjutnya = dbHandler.getSatuPertanyaanRandom(soal);

                        /** Memasukkan soal kedalam list */
                        soal.add(soalSelanjutnya);

                        /**
                         * Inisialisasi intent
                         * Di intent diberi tambahan list soal dan angka 0
                         * Angka 0 berfungsi sebagai penanda bahwa user sekarang sedang mengerjakan soal ke n+1 (0+1 = 1)
                         */
                        Intent intent = new Intent(ActivityTantangan.this, ActivityShowTantangan.class);
                        intent.putParcelableArrayListExtra(Utils.PARAM_TANTANGAN_LIST, soal);
                        intent.putExtra(Utils.PARAM_TANTANGAN_COUNT_SOAL, 0);
                        startActivity(intent);
                    }
                }
        );

        /**
         * Inisialisasi ketika tombol history diclick
         * Ketika tombol diclick, system akan mengarahkan user ke ActivityHistoryTantangan
         */
        textHistory.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(ActivityTantangan.this, ActivityHistoryTantangan.class));
                    }
                }
        );

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
}
