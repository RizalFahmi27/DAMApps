package id.developer.lynx.damapps.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
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

    TextView textMulai, textHistory;

    SystemDBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tantangan);

        dbHandler = new SystemDBHandler(this);

        initView();
    }

    private void initView(){
        textMulai = (TextView)findViewById(R.id.text_act_tantangan_start);
        textHistory = (TextView)findViewById(R.id.text_act_tantangan_history);

        textMulai.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<ObjectSoal> soal = new ArrayList<ObjectSoal>();
                        ObjectSoal soalSelanjutnya = dbHandler.getSatuPertanyaanRandom(soal);

                        soal.add(soalSelanjutnya);

                        Intent intent = new Intent(ActivityTantangan.this, ActivityShowTantangan.class);
                        intent.putParcelableArrayListExtra(Utils.PARAM_TANTANGAN_LIST, soal);
                        intent.putExtra(Utils.PARAM_TANTANGAN_COUNT_SOAL, 0);
                        startActivity(intent);
                    }
                }
        );

        textHistory.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(ActivityTantangan.this, ActivityHistoryTantangan.class));
                    }
                }
        );
    }
}
