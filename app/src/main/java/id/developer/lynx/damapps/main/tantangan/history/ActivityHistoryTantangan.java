package id.developer.lynx.damapps.main.tantangan.history;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import id.developer.lynx.damapps.R;
import id.developer.lynx.damapps.SystemDBHandler;
import id.developer.lynx.damapps.Utils;
import id.developer.lynx.damapps.main.tantangan.ObjectHistory;
import id.developer.lynx.damapps.main.tantangan.ObjectSoal;
import id.developer.lynx.damapps.main.tantangan.result.ActivityResultTantangan;

public class ActivityHistoryTantangan extends AppCompatActivity {

    SystemDBHandler dbHandler;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_tantangan);

        dbHandler = new SystemDBHandler(this);

        try{
            initView();
        }catch (Exception e){
            Log.d(Utils.LOG_TAG, Utils.CATCH_ERROR+ e.getMessage());
        }
    }

    private void initView(){
        listView = (ListView)findViewById(R.id.list_act_history_tantangan);
        listView.setAdapter(new SystemListAdapterHistory(this, dbHandler.getSemuaHistory()));

        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ObjectHistory historyKe = (ObjectHistory)listView.getAdapter().getItem(position);
                        ArrayList<ObjectSoal> listSoal = dbHandler.getAllSoalByIdHistory(historyKe.getId());

                        Intent intent = new Intent(ActivityHistoryTantangan.this, ActivityResultTantangan.class);
                        intent.putParcelableArrayListExtra(Utils.PARAM_TANTANGAN_LIST, listSoal);
                        startActivity(intent);
                    }
                }
        );
    }
}
