package id.developer.lynx.damapps.main.latihan;

/**
 * @author bend
 * @version 1.0
 * @date 11/6/2016
 * Kelas untuk menampilkan teori latihan sesuai dengan latihan yang dipilih
 * Di kelas ini terdapat sebuah grid yang berfungsi untuk menampilkan teori latihan
 */

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import id.developer.lynx.damapps.R;
import id.developer.lynx.damapps.SystemDBHandler;
import id.developer.lynx.damapps.Utils;

public class ActivityShowLatihan extends AppCompatActivity {

    /** Variable grid untuk menampilkan semua data */
    GridView gridView;

    SystemDBHandler dbHandler;

    /**
     * Variable type latihan akan menentukan latihan mana yang akan ditampilkan
     * Jika berisi angka, maka grid akan menampilkan angka
     * Jika berisi huruf, maka grid akan menampilkan huruf
     * Jika berisi kata, maka grid akan menampilkan kata
     * Isi dari variable ini diambil dari data yang dikirim dari ActivityLatihan sebelumnya
     */
    String typeLatihan;

    private Animator mCurrentAnimator;
    private int mShortAnimationDuration;

    private boolean isZoomed = false;
    Rect startBounds;
    float startScaleFinal;

    View thumbView;
    ImageView imageExpanded;
    RelativeLayout relativeContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /** Menentukan layout yang akan ditampilkan ketika class ini pertama kali dipanggil */
        setContentView(R.layout.activity_show_latihan);

        dbHandler = new SystemDBHandler(this);

        /** Memanggil method initBundle */
        initBundle();

        initZoomableImage();

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

    private void initZoomableImage(){
        mShortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);
        imageExpanded = (ImageView)findViewById(R.id.image_act_show_latihan_expanded_image);
        relativeContainer = (RelativeLayout)findViewById(R.id.relative_act_show_latihan_container);
    }

    private void initView(){
        /** Inisialisasi grid */
        gridView = (GridView)findViewById(R.id.grid_act_show_latihan);

        /** Inisialisasi adapter untuk grid */
        gridView.setAdapter(new SystemGridAdapterLatihan(this, getListLatihan()));

        gridView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ObjectLatihan clickedItem = (ObjectLatihan)gridView.getAdapter().getItem(position);

                        thumbView = (ImageView)view.findViewById(R.id.image_lay_grid_latihan);
                        zoomImageFromThumb(clickedItem.getGambar());
                    }
                }
        );
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
        String type = "";

        /** Pengecekan tipe latihan */
        if(typeLatihan.equalsIgnoreCase(Utils.PARAM_LATIHAN_ANGKA)){

            /** Mengambil data angka */
            data = Utils.LIST_LATIHAN_ANGKA;
            type = "angka_";
        }else if(typeLatihan.equalsIgnoreCase(Utils.PARAM_LATIHAN_HURUF)){

            /** Mengambil data huruf */
            data = Utils.LIST_LATIHAN_HURUF;
            type = "huruf_";
        }else{
            return dbHandler.getSemuaKata();
        }

        /** Looping untuk memasukkan data ke dalam list */
        for (int i = 0; i < data.length; i++) {

            /** Memasukkan data ke dalam list */
            list.add(new ObjectLatihan(data[i], type+ data[i]));
        }

        return list;
    }

    private void zoomImageFromThumb(String url) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        // Load the high-resolution "zoomed-in" image.
        isZoomed = true;
        int imageId = getResources().getIdentifier("drawable/"+url, "drawable", getPackageName());
        Picasso
                .with(ActivityShowLatihan.this)
                .load(imageId)
                .into(imageExpanded);

        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        thumbView.getGlobalVisibleRect(startBounds);
        findViewById(R.id.relative_act_show_latihan_container)
                .getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        relativeContainer.setVisibility(View.VISIBLE);
        thumbView.setAlpha(0f);
        imageExpanded.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        imageExpanded.setPivotX(0f);
        imageExpanded.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(imageExpanded, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(imageExpanded, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(imageExpanded, View.SCALE_X,
                        startScale, 1f)).with(ObjectAnimator.ofFloat(imageExpanded,
                View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        startScaleFinal = startScale;
        relativeContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unZoomImageFromThumb();
            }
        });
    }

    private void unZoomImageFromThumb(){
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        // Animate the four positioning/sizing properties in parallel,
        // back to their original values.
        AnimatorSet set = new AnimatorSet();
        set.play(ObjectAnimator
                .ofFloat(imageExpanded, View.X, startBounds.left))
                .with(ObjectAnimator
                        .ofFloat(imageExpanded,
                                View.Y, startBounds.top))
                .with(ObjectAnimator
                        .ofFloat(imageExpanded,
                                View.SCALE_X, startScaleFinal))
                .with(ObjectAnimator
                        .ofFloat(imageExpanded,
                                View.SCALE_Y, startScaleFinal));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                thumbView.setAlpha(1f);
                relativeContainer.setVisibility(View.GONE);
                imageExpanded.setVisibility(View.GONE);
                mCurrentAnimator = null;
                isZoomed = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                thumbView.setAlpha(1f);
                relativeContainer.setVisibility(View.GONE);
                imageExpanded.setVisibility(View.GONE);
                mCurrentAnimator = null;
                isZoomed = false;
            }
        });
        set.start();
        mCurrentAnimator = set;
    }

    @Override
    public void onBackPressed() {

        if(isZoomed){
            isZoomed = false;
            unZoomImageFromThumb();
            return;
        }

        super.onBackPressed();
    }
}
