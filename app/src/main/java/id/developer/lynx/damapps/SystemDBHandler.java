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

import id.developer.lynx.damapps.main.latihan.ObjectLatihan;
import id.developer.lynx.damapps.main.tantangan.ObjectHistory;
import id.developer.lynx.damapps.main.tantangan.ObjectSoal;

public class SystemDBHandler extends SQLiteOpenHelper{

    /** Nama database dan versinya */
    private static final String DB_NAME = "id.developer.lynx.damapss";
    private static final Integer DB_VERSION = 2;

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
            insertKata(context);
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

    /**
     * Method untuk memasukkan daftar kata ke database
     * Method ini dipanggil hanya sekali ketika aplikasi pertama kali dibuka
     * @param context
     */
    private void insertKata(Context context){

        /** Membuka database */
        SQLiteDatabase db = this.getWritableDatabase();

        /** Nama file CSV yang berisi daftar pertanyaan */
        String mCSVfile = "obj_kata.csv";

        /** Inisialisasi Asset Manager dan Input Stream */
        AssetManager manager = context.getAssets();
        InputStream inStream = null;

        try {
            /** Membuka file CSV dengan Input Stream */
            inStream = manager.open(mCSVfile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        /** Inisialisasi Buffered Reader */
        BufferedReader buffer = new BufferedReader(new InputStreamReader(inStream));
        String line = "";

        /** Memulai transaksi untuk insert data ke database */
        db.beginTransaction();

        try {
            /**
             * Looping untuk mengambil data dari file CSV
             * Data diambil per baris
             */
            while ((line = buffer.readLine()) != null) {

                /** Data yang diambil per baris dijadikan array dengan di split comma (,) */
                String[] colums = line.split(",");
                Log.i(Utils.LOG_TAG, "this is loop for inserting data : " +line);

                /** Inisialisasi Content Values */
                ContentValues cv = new ContentValues();

                /**
                 * Memasukkan data ke dalam Content Values
                 * Key di Content Values merupakan nama kolom dari tabel
                 */
                cv.put(TABLE_KATA_ID, colums[0].trim());
                cv.put(TABLE_KATA_TEXT, colums[1].trim());
                cv.put(TABLE_KATA_GAMBAR, colums[2].trim());

                /** Memasukkan data yang ada di Content Values kedalam database */
                db.insert(TABLE_KATA, null, cv);
            }
        } catch (IOException e) {
            Log.i(Utils.LOG_TAG, "this is error from inserting data to database : " +e.getMessage());
        }

        /** Mengakhiri transaksi database */
        db.setTransactionSuccessful();
        db.endTransaction();

        /** Menutup database */
        db.close();

        try{
            /** Menutup Buffered Reader */
            buffer.close();
        }catch (Exception e){}
    }

    /**
     * Method untuk memasukkan daftar pertanyaan ke database
     * Method ini dipanggil hanya sekali ketika aplikasi pertama kali dibuka
     * @param context
     */
    private void insertPertanyaan(Context context){

        /** Membuka database */
        SQLiteDatabase db = this.getWritableDatabase();

        /** Nama file CSV yang berisi daftar pertanyaan */
        String mCSVfile = "pertanyaan.csv";

        /** Inisialisasi Asset Manager dan Input Stream */
        AssetManager manager = context.getAssets();
        InputStream inStream = null;

        try {
            /** Membuka file CSV dengan Input Stream */
            inStream = manager.open(mCSVfile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        /** Inisialisasi Buffered Reader */
        BufferedReader buffer = new BufferedReader(new InputStreamReader(inStream));
        String line = "";

        /** Memulai transaksi untuk insert data ke database */
        db.beginTransaction();

        try {
            /**
             * Looping untuk mengambil data dari file CSV
             * Data diambil per baris
             */
            while ((line = buffer.readLine()) != null) {

                /** Data yang diambil per baris dijadikan array dengan di split comma (,) */
                String[] colums = line.split(",");
                Log.i(Utils.LOG_TAG, "this is loop for inserting data : " +line);

                /** Inisialisasi Content Values */
                ContentValues cv = new ContentValues();

                /**
                 * Memasukkan data ke dalam Content Values
                 * Key di Content Values merupakan nama kolom dari tabel
                 */
                cv.put(TABLE_PERTANYAAN_ID, colums[0].trim());
                cv.put(TABLE_PERTANYAAN_SOAL, colums[1].trim());
                cv.put(TABLE_PERTANYAAN_JAWABAN_1, colums[2].trim());
                cv.put(TABLE_PERTANYAAN_JAWABAN_2, colums[3].trim());
                cv.put(TABLE_PERTANYAAN_JAWABAN_3, colums[4].trim());
                cv.put(TABLE_PERTANYAAN_JAWABAN_4, colums[5].trim());
                cv.put(TABLE_PERTANYAAN_JAWABAN_BENAR, colums[6].trim());
                cv.put(TABLE_PERTANYAAN_TYPE, colums[7].trim());

                /** Memasukkan data yang ada di Content Values kedalam database */
                db.insert(TABLE_PERTANYAAN, null, cv);
            }
        } catch (IOException e) {
            Log.i(Utils.LOG_TAG, "this is error from inserting data to database : " +e.getMessage());
        }

        /** Mengakhiri transaksi database */
        db.setTransactionSuccessful();
        db.endTransaction();

        /** Menutup database */
        db.close();

        try{
            /** Menutup Buffered Reader */
            buffer.close();
        }catch (Exception e){}
    }

    public List<ObjectLatihan> getSemuaKata(){
        String query = "SELECT * FROM " +TABLE_KATA+ " WHERE 1";
        List<ObjectLatihan> listKata = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        do {
            String text = cursor.getString(cursor.getColumnIndex(TABLE_KATA_TEXT));
            String gambar = cursor.getString(cursor.getColumnIndex(TABLE_KATA_GAMBAR));

            listKata.add(new ObjectLatihan(text, gambar));
        }while (cursor.moveToNext());

        cursor.close();
        db.close();

        return listKata;
    }

    /**
     * Method untuk mengambil satu pertanyaan secara acak yang akan ditampilkan di soal
     * @param list
     * @return satu pertanyaan yang akan ditampilkan
     */
    public ObjectSoal getSatuPertanyaanRandom(List<ObjectSoal> list){

        /** Inisialisasi variable */
        ObjectSoal satuPertanyaan = new ObjectSoal();

        /** Query untuk mengambil semua data pada table pertanyaan */
        String query = "SELECT * FROM " +TABLE_PERTANYAAN+ " WHERE 1 ORDER BY RANDOM()";

        /** Membuka database */
        SQLiteDatabase db = this.getReadableDatabase();

        /** Melakukan querying */
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        /**
         * Inisialisasi variable boolean
         * Variable ini digunakan untuk mengecek apakah pertanyaan yang di query pernah ditampilkan sebelumnya
         * Jika iya maka bernilai true
         * Jika tidak maka bernilai false
         */
        boolean same;

        /** Looping untuk mengambil data pada table pertanyaan per baris */
        do{

            /** Nilai awal variable boolean same selalu false */
            same = false;

            /** Mengambil data id pada kolom pertanyaan_id dalam table pertanyaan baris ke-n */
            Integer id = c.getInt(c.getColumnIndex(TABLE_PERTANYAAN_ID));

            /**
             * Looping untuk mengecek apakah id yang diambil diatas ada pada list pertanyaan yang pernah ditampilkan
             */
            for (int i = 0; i < list.size(); i++) {
                /**
                 * Jika id ditemukan sama maka variable boolean bernilai true dan looping dihentikan
                 */
                if(id == list.get(i).id){
                    same = true;
                    break;
                }
            }

            /**
             * Jika variable boolean same bernilai false, maka pertanyaan ini belum pernah ditampilkan
             * Oleh karena itu, data pada table pertanyaan baris ke-n akan diambil lalu looping akan dihentikan
             * Setelah data diambil, data akan dirubah menjadi object lalu dikirim kembali ke activity
             */
            if(!same){

                /** Mengambil semua data pada table pertanyaan baris ke-n */
                String soal = c.getString(c.getColumnIndex(TABLE_PERTANYAAN_SOAL));
                String jwb_1 = c.getString(c.getColumnIndex(TABLE_PERTANYAAN_JAWABAN_1));
                String jwb_2 = c.getString(c.getColumnIndex(TABLE_PERTANYAAN_JAWABAN_2));
                String jwb_3 = c.getString(c.getColumnIndex(TABLE_PERTANYAAN_JAWABAN_3));
                String jwb_4 = c.getString(c.getColumnIndex(TABLE_PERTANYAAN_JAWABAN_4));
                String jwb_benar = c.getString(c.getColumnIndex(TABLE_PERTANYAAN_JAWABAN_BENAR));
                String type = c.getString(c.getColumnIndex(TABLE_PERTANYAAN_TYPE));

                /** Merubah data yang diambil menjadi object pertanyaan */
                satuPertanyaan = new ObjectSoal(id, soal, jwb_1, jwb_2, jwb_3, jwb_4, jwb_benar, type);
                break;
            }
        }while(c.moveToNext());

        return satuPertanyaan;
    }

    /**
     * Method untuk mengupadate history
     * Method ini dipanggil setiap kali user menyelesaikan 10 pertanyaan
     * Di table history akan disimpan tanggal dan berapa banyak soal yang dijawab dengan benar oleh user
     * Untuk daftar pertanyaan yang dijawab, disimpan pada table rel_history_pertanyaan yang akan diproses di method updateRelHistoryPertanyaan
     * @param listSoal
     * @param date
     * @param result
     */
    public void updateHistory(List<ObjectSoal> listSoal, String date, Integer result){

        /** Membuka database */
        SQLiteDatabase db = this.getWritableDatabase();

        /** Inisialisasi Content Values */
        ContentValues cv = new ContentValues();

        /** Menyimpan data ke dalam Content Values */
        cv.put(TABLE_HISTORY_DATE, date);
        cv.put(TABLE_HISTORY_HASIL, result);

        /** Insert data Content Values ke dalam database */
        db.insert(TABLE_HISTORY, null, cv);

        /** Menutup database */
        db.close();

        /** Memanggil method selanjutnya untuk menyimpan daftar pertanyaan yang dijawab user */
        updateRelHistoryPertanyaan(listSoal);
    }

    /**
     * Method ini berfungsi untuk menyimpan daftar pertanyaan yang dijawab oleh user
     * Daftar pertanyaan tersebut akan direlasikan dengan id history ke-n
     * Sehingga kita dapat mengambil semua pertanyaan yang dijawab sesuai dengan history ke-n
     * @param listSoal
     */
    private void updateRelHistoryPertanyaan(List<ObjectSoal> listSoal){

        /** Query untuk mengambil semua data pada table history */
        String query = "SELECT * FROM " +TABLE_HISTORY+ " WHERE 1";

        /** Membuka database */
        SQLiteDatabase db = this.getWritableDatabase();

        /**
         * Melakukan querying
         * Query ini dimaksudkan untuk mendapatkan id history terakhir yang akan menjadi acuan untuk menyimpan daftar pertanyaan yang dijawab
         */
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToLast();

        Log.d(Utils.LOG_TAG, Utils.DB_LENGTH_CURSOR+ cursor.getCount());

        /** Mengambil id history terakhir */
        Integer id = cursor.getInt(cursor.getColumnIndex(TABLE_HISTORY_ID));

        /** Looping untuk menyimpan daftar soal ke dalam table rel_history_pertanyaan */
        for (int i = 0; i < listSoal.size(); i++) {

            /** Inisialisasi Content Values */
            ContentValues cv = new ContentValues();

            /** Menyimpan data daftar pertanyaan, id history, dan jawaban user ke dalam Content Values */
            cv.put(TABLE_REL_HISTORY_PERTANYAAN_ID_HISTORY, id);
            cv.put(TABLE_REL_HISTORY_PERTANYAAN_ID_PERTANYAAN, listSoal.get(i).id);
            cv.put(TABLE_REL_HISTORY_PERTANYAAN_JAWABAN_USER, listSoal.get(i).jawaban_user);

            /** Menyimpan data yang ada pada Content Values ke dalam database */
            db.insert(TABLE_REL_HISTORY_PERTANYAAN, null, cv);
        }

        /** Menutup cursor dan database */
        cursor.close();
        db.close();
    }

    /**
     * Method untuk mengambil semua history pada database
     * @return list semua history yang ada
     */
    public List<ObjectHistory> getSemuaHistory(){

        /** Query untuk mengambil semua history yang ada */
        String query = "SELECT * FROM " +TABLE_HISTORY+ " WHERE 1";

        /** Inisialisasi array list untuk tempat menyimpan data history */
        List<ObjectHistory> listHistory = new ArrayList<>();

        /** Membuka database */
        SQLiteDatabase db = this.getReadableDatabase();

        /** Melakukan querying */
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        /**
         * Looping untuk mengambil data yang ada pada table history
         * Pengambilan data dilakukan per baris
         */
        do{
            /** Pengambilan dan penyimpanan nilai data kedalam variable */
            Integer id = cursor.getInt(cursor.getColumnIndex(TABLE_HISTORY_ID));
            String date = cursor.getString(cursor.getColumnIndex(TABLE_HISTORY_DATE));
            Integer hasil = cursor.getInt(cursor.getColumnIndex(TABLE_HISTORY_HASIL));

            /** Merubah data menjadi object lalu disimpan ke dalam list */
            listHistory.add(new ObjectHistory(id, date, hasil));
        }while (cursor.moveToNext());

        /** Menutup curson dan database */
        cursor.close();
        db.close();

        return listHistory;
    }

    /**
     * Method yang digunakan untuk mengambil daftar pertanyaan yang dijawab user sesuai dengan history ke-n
     * @param idHistory
     * @return list pertanyaan yang dijawab user beserta jawaban user
     */
    public ArrayList<ObjectSoal> getAllSoalByIdHistory(Integer idHistory){

        /** Inisialisasi list untuk menyimpan data pertanyaan yang dijawab */
        ArrayList<ObjectSoal> listSoal = new ArrayList<>();

        /** Query untuk mengambil data pertanyaan yang dijawab user berdasarkan history ke-n */
        String query = "SELECT p.*, rel." +TABLE_REL_HISTORY_PERTANYAAN_JAWABAN_USER+ " " +
                "FROM " +TABLE_PERTANYAAN+ " as p " +
                "INNER JOIN " +TABLE_REL_HISTORY_PERTANYAAN+ " as rel " +
                "ON p." +TABLE_PERTANYAAN_ID+ " = rel." +TABLE_REL_HISTORY_PERTANYAAN_ID_PERTANYAAN+ " " +
                "WHERE rel." +TABLE_REL_HISTORY_PERTANYAAN_ID_HISTORY+ " = " +idHistory;

        Log.d(Utils.LOG_TAG, Utils.DB_QUERY+ query);

        /** Membuka database */
        SQLiteDatabase db = this.getReadableDatabase();

        /** Melakukan querying */
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        Log.d(Utils.LOG_TAG, Utils.DB_LENGTH_CURSOR+ cursor.getCount());

        /**
         * Looping untuk mengambil data dan menyimpannya ke dalam list
         * Data diambil per baris
         */
        do{

            /** Pengambilan data yang disimpan kedalam variable */
            Integer id = cursor.getInt(cursor.getColumnIndex(TABLE_PERTANYAAN_ID));
            String soal = cursor.getString(cursor.getColumnIndex(TABLE_PERTANYAAN_SOAL));
            String jwb_1 = cursor.getString(cursor.getColumnIndex(TABLE_PERTANYAAN_JAWABAN_1));
            String jwb_2 = cursor.getString(cursor.getColumnIndex(TABLE_PERTANYAAN_JAWABAN_2));
            String jwb_3 = cursor.getString(cursor.getColumnIndex(TABLE_PERTANYAAN_JAWABAN_3));
            String jwb_4 = cursor.getString(cursor.getColumnIndex(TABLE_PERTANYAAN_JAWABAN_4));
            String jwb_benar = cursor.getString(cursor.getColumnIndex(TABLE_PERTANYAAN_JAWABAN_BENAR));
            String type = cursor.getString(cursor.getColumnIndex(TABLE_PERTANYAAN_TYPE));
            String jwb_user = cursor.getString(cursor.getColumnIndex(TABLE_REL_HISTORY_PERTANYAAN_JAWABAN_USER));

            /** Merubah data pada variable menjadi object yang disimpan kedalam list */
            listSoal.add(new ObjectSoal(id, soal, jwb_1, jwb_2, jwb_3, jwb_4, jwb_benar, type));
            listSoal.get(listSoal.size()-1).setJawaban_user(jwb_user);
        }while(cursor.moveToNext());

        /** Menutup cursor dan database */
        cursor.close();
        db.close();

        return listSoal;
    }
}
