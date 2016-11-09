package id.developer.lynx.damapps;

/**
 * @author bend
 * @version 1.0
 * @date 11/7/2016
 * Kelas untuk mengatur seluruh transaksi dengan database
 */

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import id.developer.lynx.damapps.main.tantangan.ObjectHistory;
import id.developer.lynx.damapps.main.tantangan.ObjectSoal;

public class SystemDBHandler extends SQLiteOpenHelper{

    /** Nama database dan versinya */
    private static final String DB_NAME = "id.developer.lynx.damapss";
    private static final Integer DB_VERSION = 1;

    /** Tabel kata dan kolom */
    private static final String TABLE_KATA = "kata";
    private static final String TABLE_KATA_ID = "kata_id";
    private static final String TABLE_KATA_TEXT = "kata_text";
    private static final String TABLE_KATA_GAMBAR = "kata_gambar";

    /** Tabel pertanyaan dan kolom */
    private static final String TABLE_PERTANYAAN = "pertanyaan";
    private static final String TABLE_PERTANYAAN_ID = "pertanyaan_id";
    private static final String TABLE_PERTANYAAN_SOAL = "pertanyaan_soal";
    private static final String TABLE_PERTANYAAN_JAWABAN_1 = "pertanyaan_jawaban_1";
    private static final String TABLE_PERTANYAAN_JAWABAN_2 = "pertanyaan_jawaban_2";
    private static final String TABLE_PERTANYAAN_JAWABAN_3 = "pertanyaan_jawaban_3";
    private static final String TABLE_PERTANYAAN_JAWABAN_4 = "pertanyaan_jawaban_4";
    private static final String TABLE_PERTANYAAN_JAWABAN_BENAR = "pertanyaan_jawaban_benar";
    private static final String TABLE_PERTANYAAN_TYPE = "pertanyaan_type";

    /** Tabel history dan kolom */
    private static final String TABLE_HISTORY = "history";
    private static final String TABLE_HISTORY_ID = "history_id";
    private static final String TABLE_HISTORY_DATE = "history_date";
    private static final String TABLE_HISTORY_HASIL = "history_hasil";

    /** Tabel relasi history dan pertanyaan */
    private static final String TABLE_REL_HISTORY_PERTANYAAN = "rel_history_pertanyaan";
    private static final String TABLE_REL_HISTORY_PERTANYAAN_ID = "rel_history_pertanyaan_id";
    private static final String TABLE_REL_HISTORY_PERTANYAAN_ID_HISTORY = "rel_history_pertanyaan_id_history";
    private static final String TABLE_REL_HISTORY_PERTANYAAN_ID_PERTANYAAN = "rel_history_pertanyaan_id_pertanyaan";
    private static final String TABLE_REL_HISTORY_PERTANYAAN_JAWABAN_USER = "rel_history_pertanyaan_jawaban_user";

    public SystemDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB_NAME, factory, DB_VERSION);

        /** Membuka database */
        SQLiteDatabase db = this.getReadableDatabase();

        /**
         * Cek apakah tabel sudah ada atau belum
         * Jika belum ada maka tabel akan dibuat lalu akan diisi dengan data
         * Pembuatan dan pengisian tabel hanya dilakukan ketika aplikasi pertama kali dijalankan
         */
        if(cekTableExist(db) == 0){
            insertPertanyaan(context);
        }

        /** Menutup database */
        db.close();
    }

    public SystemDBHandler(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    /**
     * Method untuk mengambil banyaknya data yang ada dalam tabel pertanyaan
     * @param db
     * @return jumlah data yang didapat dari query
     */
    private int cekTableExist(SQLiteDatabase db){

        /** Query select semua data pada tabel pertanyaan */
        Cursor c = db.rawQuery("SELECT * FROM " +TABLE_PERTANYAAN, null);

        /** Diambil banyaknya data yang didapat dari query */
        int count = c.getCount();

        /** Tutup cursor */
        c.close();

        return count;
    }

    /**
     * Method untuk membuat tabel
     * Method ini hanya dipanggil ketika tabel belum dibuat
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        /** Query untuk membuat tabel kata */
        String query = "CREATE TABLE " +TABLE_KATA+ "(" +
                TABLE_KATA_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TABLE_KATA_TEXT+ " TEXT, " +
                TABLE_KATA_GAMBAR+ " TEXT " +
                ");";
        db.execSQL(query);

        /** Query untuk membuat tabel pertanyaan */
        query = "CREATE TABLE " +TABLE_PERTANYAAN+ "(" +
                TABLE_PERTANYAAN_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TABLE_PERTANYAAN_SOAL+ " TEXT, " +
                TABLE_PERTANYAAN_JAWABAN_1+ " TEXT, " +
                TABLE_PERTANYAAN_JAWABAN_2+ " TEXT, " +
                TABLE_PERTANYAAN_JAWABAN_3+ " TEXT, " +
                TABLE_PERTANYAAN_JAWABAN_4+ " TEXT, " +
                TABLE_PERTANYAAN_JAWABAN_BENAR+ " TEXT, " +
                TABLE_PERTANYAAN_TYPE+ " TEXT " +
                ");";
        db.execSQL(query);

        /** Query untuk membuat tabel history */
        query = "CREATE TABLE " +TABLE_HISTORY+ "(" +
                TABLE_HISTORY_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TABLE_HISTORY_DATE+ " TEXT, " +
                TABLE_HISTORY_HASIL+ " INTEGER " +
                ");";
        db.execSQL(query);

        /** Query untuk membuat tabel relasi history dan pertanyaan */
        query = "CREATE TABLE " +TABLE_REL_HISTORY_PERTANYAAN+ "(" +
                TABLE_REL_HISTORY_PERTANYAAN_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TABLE_REL_HISTORY_PERTANYAAN_ID_HISTORY+ " INTEGER, " +
                TABLE_REL_HISTORY_PERTANYAAN_ID_PERTANYAAN+ " INTEGER, " +
                TABLE_REL_HISTORY_PERTANYAAN_JAWABAN_USER+ " TEXT " +
                ");";
        db.execSQL(query);
    }

    /**
     * Method untuk menghapus lalu membuat kembali tabel ketika versi database diupgrade
     * @param db
     * @param oldVersion versi database lama
     * @param newVersion versi database baru
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PERTANYAAN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
        onCreate(db);
    }

    private void insertPertanyaan(Context context){
        SQLiteDatabase db = this.getWritableDatabase();

        String mCSVfile = "dummy.csv";
        AssetManager manager = context.getAssets();
        InputStream inStream = null;

        try {
            inStream = manager.open(mCSVfile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader buffer = new BufferedReader(new InputStreamReader(inStream));
        String line = "";
        db.beginTransaction();
        try {
            while ((line = buffer.readLine()) != null) {
                String[] colums = line.split(",");
                Log.i(Utils.LOG_TAG, "this is loop for inserting data : " +line);

                ContentValues cv = new ContentValues();
                cv.put(TABLE_PERTANYAAN_ID, colums[0].trim());
                cv.put(TABLE_PERTANYAAN_SOAL, colums[1].trim());
                cv.put(TABLE_PERTANYAAN_JAWABAN_1, colums[2].trim());
                cv.put(TABLE_PERTANYAAN_JAWABAN_2, colums[3].trim());
                cv.put(TABLE_PERTANYAAN_JAWABAN_3, colums[4].trim());
                cv.put(TABLE_PERTANYAAN_JAWABAN_4, colums[5].trim());
                cv.put(TABLE_PERTANYAAN_JAWABAN_BENAR, colums[6].trim());
                cv.put(TABLE_PERTANYAAN_TYPE, colums[7].trim());

                db.insert(TABLE_PERTANYAAN, null, cv);
            }
        } catch (IOException e) {
            Log.i(Utils.LOG_TAG, "this is error from inserting data to database : " +e.getMessage());
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        try{
            buffer.close();
        }catch (Exception e){}
    }

    public ObjectSoal getSatuPertanyaanRandom(List<ObjectSoal> list){
        ObjectSoal satuPertanyaan = new ObjectSoal();
        String query = "SELECT * FROM " +TABLE_PERTANYAAN+ " WHERE 1";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        boolean same;
        do{
            same = false;
            Integer id = c.getInt(c.getColumnIndex(TABLE_PERTANYAAN_ID));

            for (int i = 0; i < list.size(); i++) {
                if(id == list.get(i).id){
                    same = true;
                    break;
                }
            }

            if(!same){
                String soal = c.getString(c.getColumnIndex(TABLE_PERTANYAAN_SOAL));
                String jwb_1 = c.getString(c.getColumnIndex(TABLE_PERTANYAAN_JAWABAN_1));
                String jwb_2 = c.getString(c.getColumnIndex(TABLE_PERTANYAAN_JAWABAN_2));
                String jwb_3 = c.getString(c.getColumnIndex(TABLE_PERTANYAAN_JAWABAN_3));
                String jwb_4 = c.getString(c.getColumnIndex(TABLE_PERTANYAAN_JAWABAN_4));
                String jwb_benar = c.getString(c.getColumnIndex(TABLE_PERTANYAAN_JAWABAN_BENAR));
                String type = c.getString(c.getColumnIndex(TABLE_PERTANYAAN_TYPE));

                satuPertanyaan = new ObjectSoal(id, soal, jwb_1, jwb_2, jwb_3, jwb_4, jwb_benar, type);
                break;
            }
        }while(c.moveToNext());

        return satuPertanyaan;
    }

    public void updateHistory(List<ObjectSoal> listSoal, String date, Integer result){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(TABLE_HISTORY_DATE, date);
        cv.put(TABLE_HISTORY_HASIL, result);

        db.insert(TABLE_HISTORY, null, cv);
        db.close();

        updateRelHistoryPertanyaan(listSoal);
    }

    private void updateRelHistoryPertanyaan(List<ObjectSoal> listSoal){
        String query = "SELECT * FROM " +TABLE_HISTORY+ " WHERE 1";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToLast();

        Log.d(Utils.LOG_TAG, Utils.DB_LENGTH_CURSOR+ cursor.getCount());
        Integer id = cursor.getInt(cursor.getColumnIndex(TABLE_HISTORY_ID));

        for (int i = 0; i < listSoal.size(); i++) {
            ContentValues cv = new ContentValues();
            cv.put(TABLE_REL_HISTORY_PERTANYAAN_ID_HISTORY, id);
            cv.put(TABLE_REL_HISTORY_PERTANYAAN_ID_PERTANYAAN, listSoal.get(i).id);
            cv.put(TABLE_REL_HISTORY_PERTANYAAN_JAWABAN_USER, listSoal.get(i).jawaban_user);

            db.insert(TABLE_REL_HISTORY_PERTANYAAN, null, cv);
        }

        cursor.close();
        db.close();
    }

    public List<ObjectHistory> getSemuaHistory(){
        String query = "SELECT * FROM " +TABLE_HISTORY+ " WHERE 1";
        List<ObjectHistory> listHistory = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        do{
            Integer id = cursor.getInt(cursor.getColumnIndex(TABLE_HISTORY_ID));
            String date = cursor.getString(cursor.getColumnIndex(TABLE_HISTORY_DATE));
            Integer hasil = cursor.getInt(cursor.getColumnIndex(TABLE_HISTORY_HASIL));

            listHistory.add(new ObjectHistory(id, date, hasil));
        }while (cursor.moveToNext());

        cursor.close();
        db.close();

        return listHistory;
    }

    public ArrayList<ObjectSoal> getAllSoalByIdHistory(Integer idHistory){
        ArrayList<ObjectSoal> listSoal = new ArrayList<>();
        String query = "SELECT p.*, rel." +TABLE_REL_HISTORY_PERTANYAAN_JAWABAN_USER+ " " +
                "FROM " +TABLE_PERTANYAAN+ " as p " +
                "INNER JOIN " +TABLE_REL_HISTORY_PERTANYAAN+ " as rel " +
                "ON p." +TABLE_PERTANYAAN_ID+ " = rel." +TABLE_REL_HISTORY_PERTANYAAN_ID_PERTANYAAN+ " " +
                "WHERE rel." +TABLE_REL_HISTORY_PERTANYAAN_ID_HISTORY+ " = " +idHistory;

        Log.d(Utils.LOG_TAG, Utils.DB_QUERY+ query);

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        Log.d(Utils.LOG_TAG, Utils.DB_LENGTH_CURSOR+ cursor.getCount());

        do{
            Integer id = cursor.getInt(cursor.getColumnIndex(TABLE_PERTANYAAN_ID));
            String soal = cursor.getString(cursor.getColumnIndex(TABLE_PERTANYAAN_SOAL));
            String jwb_1 = cursor.getString(cursor.getColumnIndex(TABLE_PERTANYAAN_JAWABAN_1));
            String jwb_2 = cursor.getString(cursor.getColumnIndex(TABLE_PERTANYAAN_JAWABAN_2));
            String jwb_3 = cursor.getString(cursor.getColumnIndex(TABLE_PERTANYAAN_JAWABAN_3));
            String jwb_4 = cursor.getString(cursor.getColumnIndex(TABLE_PERTANYAAN_JAWABAN_4));
            String jwb_benar = cursor.getString(cursor.getColumnIndex(TABLE_PERTANYAAN_JAWABAN_BENAR));
            String type = cursor.getString(cursor.getColumnIndex(TABLE_PERTANYAAN_TYPE));
            String jwb_user = cursor.getString(cursor.getColumnIndex(TABLE_REL_HISTORY_PERTANYAAN_JAWABAN_USER));

            listSoal.add(new ObjectSoal(id, soal, jwb_1, jwb_2, jwb_3, jwb_4, jwb_benar, type));
            listSoal.get(listSoal.size()-1).setJawaban_user(jwb_user);
        }while(cursor.moveToNext());

        cursor.close();
        db.close();

        return listSoal;
    }
}
