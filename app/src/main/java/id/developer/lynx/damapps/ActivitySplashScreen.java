package id.developer.lynx.damapps;

/**
 * @author bend
 * @version 1.0
 * @date 11/6/2016
 * Kelas untuk menampilkan splash screen
 * Splash screen ditampilkan selama 1 detik
 * Setelah selesai akan lanjut ke class ActivityMain
 * Class ini tidak disimpan dalam stack history
 */

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class ActivitySplashScreen extends AppCompatActivity {

    /** Variable untuk database handler */
    SystemDBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /** Menentukan layout yang akan ditampilkan ketika class ini pertama kali dipanggil */
        setContentView(R.layout.activity_splash_screen);

        /** Inisialisasi database handler */
        dbHandler = new SystemDBHandler(this, "", null, 0);

        /**
         * Membuat thread
         * Thread bekerja di background
         */
        Thread thread = new Thread(){
            @Override
            public void run() {
                try{

                    /** Menghentikan aplikasi selama 1000 milidetik */
                    sleep(1000);
                }catch (Exception e){

                    /** Menampilkan error di log */
                    Log.d(Utils.LOG_TAG, Utils.CATCH_ERROR+ e.getMessage());
                }finally {

                    /** Memindah tampilan dari splash screen ke main */
                    startActivity(new Intent(ActivitySplashScreen.this, ActivityMain.class));
                }
            }
        };

        /** Memulai thread */
        thread.start();
    }
}