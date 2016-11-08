package id.developer.lynx.damapps;

/**
 * @author bend
 * @version 1.0
 * @date 11/7/2016
 * Kelas untuk mengatur seluruh transaksi dengan database
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
    private static final String TABLE_HISTORY_ID_SOAL = "history_id_soal";
    private static final String TABLE_HISTORY_JAWABAN = "history_jawaban";
    private static final String TABLE_HISTORY_HASIL = "history_hasil";

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
                TABLE_HISTORY_ID_SOAL+ " TEXT, " +
                TABLE_HISTORY_JAWABAN+ " TEXT, " +
                TABLE_HISTORY_HASIL+ " TEXT " +
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
}
