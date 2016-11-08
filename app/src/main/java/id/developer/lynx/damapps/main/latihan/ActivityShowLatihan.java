package id.developer.lynx.damapps.main.latihan;

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

    GridView gridView;

    String typeLatihan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_latihan);

        initBundle();
        initView();
    }

    private void initBundle(){
        Bundle extras = getIntent().getExtras();

        typeLatihan = extras.getString(Utils.PARAM_LATIHAN);
    }

    private void initView(){
        gridView = (GridView)findViewById(R.id.grid_act_show_latihan);
        gridView.setAdapter(new SystemGridAdapterLatihan(this, getListLatihan()));
    }

    private List<ObjectLatihan> getListLatihan(){
        List<ObjectLatihan> list = new ArrayList<>();
        String[] data = {};

        if(typeLatihan.equalsIgnoreCase(Utils.PARAM_LATIHAN_ANGKA)){
            data = Utils.LIST_LATIHAN_ANGKA;
        }else if(typeLatihan.equalsIgnoreCase(Utils.PARAM_LATIHAN_HURUF)){
            data = Utils.LIST_LATIHAN_HURUF;
        }

        for (int i = 0; i < data.length; i++) {
            list.add(new ObjectLatihan(data[i], ""));
        }

        return list;
    }
}
