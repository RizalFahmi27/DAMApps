package id.developer.lynx.damapps.main.tantangan.result;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.List;

import id.developer.lynx.damapps.R;
import id.developer.lynx.damapps.Utils;
import id.developer.lynx.damapps.main.tantangan.ObjectSoal;

public class ActivityResultTantangan extends AppCompatActivity {

    ListView listView;
    List<ObjectSoal> listSoal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_tantangan);

        initBundle();
        initView();
    }

    private void initBundle(){
        Bundle extras = getIntent().getExtras();

        listSoal = extras.getParcelableArrayList(Utils.PARAM_TANTANGAN_LIST);
    }

    private void initView(){
        listView = (ListView)findViewById(R.id.list_act_result_tantangan);

        listView.setAdapter(new SystemListAdapterResult(this, listSoal));
    }
}
