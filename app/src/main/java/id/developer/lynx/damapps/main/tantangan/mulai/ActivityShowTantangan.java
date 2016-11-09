package id.developer.lynx.damapps.main.tantangan.mulai;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import id.developer.lynx.damapps.R;
import id.developer.lynx.damapps.SystemDBHandler;
import id.developer.lynx.damapps.Utils;
import id.developer.lynx.damapps.main.tantangan.ObjectSoal;
import id.developer.lynx.damapps.main.tantangan.result.ActivityResultTantangan;

public class ActivityShowTantangan extends AppCompatActivity {

    ImageView imageSoal;
    TextView textSoal;
    GridView gridView;

    SystemDBHandler dbHandler;
    ArrayList<ObjectSoal> listSoal;
    ObjectSoal soalSelanjutnya;

    Integer iniSoalKe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_tantangan);

        dbHandler = new SystemDBHandler(this);

        initBundle();
        initView();
    }

    private void initBundle(){
        Bundle extras = getIntent().getExtras();

        listSoal = extras.getParcelableArrayList(Utils.PARAM_TANTANGAN_LIST);
        iniSoalKe = extras.getInt(Utils.PARAM_TANTANGAN_COUNT_SOAL);
    }

    private void initView(){
        imageSoal = (ImageView)findViewById(R.id.image_act_show_tantangan_soal);
        textSoal = (TextView)findViewById(R.id.text_act_show_tantangan_soal);

        textSoal.setText("Soal ke-" +(iniSoalKe+1));

        gridView = (GridView)findViewById(R.id.grid_act_show_tantangan);
        gridView.setAdapter(new SystemGridAdapterTantangan(this, getData(), listSoal.get(iniSoalKe).type));

        gridView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String jawabanYangDiClick = "";

                        switch (position){
                            case 0:
                                jawabanYangDiClick = "A";
                                break;

                            case 1:
                                jawabanYangDiClick = "B";
                                break;

                            case 2:
                                jawabanYangDiClick = "C";
                                break;

                            case 3:
                                jawabanYangDiClick = "D";
                                break;
                        }

                        listSoal.get(iniSoalKe).setJawaban_user(jawabanYangDiClick);

                        if(iniSoalKe < 9){
                            if(iniSoalKe == listSoal.size()-1){
                                soalSelanjutnya = dbHandler.getSatuPertanyaanRandom(listSoal);
                                listSoal.add(soalSelanjutnya);
                            }

                            Intent intent = new Intent(ActivityShowTantangan.this, ActivityShowTantangan.class);
                            intent.putParcelableArrayListExtra(Utils.PARAM_TANTANGAN_LIST, listSoal);
                            intent.putExtra(Utils.PARAM_TANTANGAN_COUNT_SOAL, iniSoalKe+1);
                            startActivity(intent);
                        }else{
                            Integer hasil = 0;

                            for (int i = 0; i < listSoal.size(); i++) {
                                if(listSoal.get(i).jawaban_benar.equalsIgnoreCase(listSoal.get(i).jawaban_user)){
                                    hasil++;
                                }
                            }

                            Calendar c = Calendar.getInstance();
                            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                            String formattedDate = df.format(c.getTime());

                            dbHandler.updateHistory(listSoal, formattedDate, hasil);

                            Intent intent = new Intent(ActivityShowTantangan.this, ActivityResultTantangan.class);
                            intent.putParcelableArrayListExtra(Utils.PARAM_TANTANGAN_LIST, listSoal);
                            startActivity(intent);
                        }
                    }
                }
        );
    }

    private List<String> getData(){
        List<String> list = new ArrayList<>();
        list.add(listSoal.get(iniSoalKe).jawaban_a);
        list.add(listSoal.get(iniSoalKe).jawaban_b);
        list.add(listSoal.get(iniSoalKe).jawaban_c);
        list.add(listSoal.get(iniSoalKe).jawaban_d);

        return list;
    }

    @Override
    public void onBackPressed() {
        if(iniSoalKe == 0){
            super.onBackPressed();
        }else{
            Intent intent = new Intent(ActivityShowTantangan.this, ActivityShowTantangan.class);
            intent.putParcelableArrayListExtra(Utils.PARAM_TANTANGAN_LIST, listSoal);
            intent.putExtra(Utils.PARAM_TANTANGAN_COUNT_SOAL, iniSoalKe-1);
            startActivity(intent);
        }
    }
}
