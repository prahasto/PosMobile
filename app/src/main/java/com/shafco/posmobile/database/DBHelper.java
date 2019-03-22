package com.shafco.posmobile.database;

//android:icon="@drawable/ic_menu_manage"o
//deklarasi import package
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
    /**
     * deklarasi konstanta-konstanta yang digunakan pada database, seperti nama tabel,
     * nama-nama kolom, nama database, dan versi dari database
     **/
    private static final String db_name = "stockopname_master.db";
    private static final int db_version = 18;
    public static final String COLUMN_ID = "id";

    public static final String TABLE_NAME_1 = "m_toko";
    public static final String COLUMN_KODE_TOKO = "kode_toko";
    public static final String COLUMN_NAMA_TOKO = "nama_toko";

    public static final String TABLE_NAME_2 = "m_lokator";
    public static final String COLUMN_KODE_LOKATOR = "kode_lokator";
    public static final String COLUMN_NAMA_LOKATOR = "nama_lokator";

    public static final String TABLE_NAME_3 = "m_produk";
    public static final String COLUMN_KODE_PRODUK = "kode_produk";
    public static final String COLUMN_NAMA_PRODUK = "nama_produk";
    public static final String COLUMN_HPJ = "hpj";
    public static final String COLUMN_R_PRODUK_ID = "r_produk_id";

    public static final String TABLE_NAME_4 = "opname";
    public static final String COLUMN_TEAM = "team";
    public static final String COLUMN_TANGGAL = "tanggal";
    public static final String COLUMN_JAM = "jam";
    public static final String COLUMN_QTY = "qty";
    public static final String COLUMN_STATUS = "status";

    private static final String TABLE_T_TRANSPOS = "t_transpos";
    private static final String COLUMN_T_TRANSPOS_ID = "t_transpos_id";
    private static final String COLUMN_NOTRANS = "notrans";

    private static final String COLUMN_TOTAL_TRANS = "total_trans";
    private static final String COLUMN_NO_FAKTUR_PAJAK = "nofaktur_pajak";
    private static final String COLUMN_R_TIPEDOKUMEN_ID= "r_tipedokumen_id";
    private static final String COLUMN_KDSTASTUSDOKUMEN= "kdstatusdokumen";
    private static final String COLUMN_R_ORGANISASI_ID= "r_organisasi_id";
    private static final String COLUMN_IS_UPLOAD= "is_upload";



    private static final String TABLE_T_TRANSPOS_DETAIL = "t_transpos_detail";
    private static final String COLUMN_T_TRANSPOS_DETAIL_ID = "t_transpos_detail_id";
    private static final String COLUMN_NOTRANS_DETAIL = "notrans";
    private static final String COLUMN_KDPRODUK = "kdproduk";

    private static final String COLUMN_HARGA = "harga";
    private static final String COLUMN_PRSN_DISC = "prsn_disc";
    private static final String COLUMN_NOMINAL_DISC = "nominal_disc";
    private static final String COLUMN_NETTO = "netto";
    private static final String COLUMN_PPN = "ppn";
    private static final String COLUMN_R_PRODUK_ID_TRANPOS_DETAIL = "r_produk_id";
    private static final String COLUMN_M_KATEGORI_DISC_ID = "m_kategori_disc_id";



    private static final String TABLE_T_BAYAR_POS = "t_bayarpos";
    private static final String COLUMN_NOTRANS_BAYAR_POS = "notrans";
    private static final String COLUMN_NOTRANS_BAYAR_POS_id= "t_bayar_pos_id";
    private static final String COLUMN_KODE_BAYAR_POS = "kode_cara_bayar";
    private static final String COLUMN_M_EDC_ID = "m_edc_id";
    private static final String COLUMN_NOMINAL_BAYAR = "nominal_bayar";
    private static final String COLUMN_BAYAR = "bayar";



    private static final String TABLE_M_CARABAYAR = "m_carabayar";
    private static final String COLUMN_CARABAYAR_ID= "kode_cara_bayar_id";
    private static final String COLUMN_CARABAYAR = "kode_cara_bayar";
    private static final String COLUMN_NAMA_CARABAYAR = "nama_cara_bayar";
    private static final String COLUMN_ISEDC = "isedc";


    private static final String TABLE_M_EDC = "m_edc";
    private static final String COLUMN_NAMAMERCHAND = "nama_merchand";
    private static final String COLUMN_NAMAMERCHAND_id = "m_edc_id";
    private static final String COLUMN_TIPEBANK = "tipe_bank";
    private static final String COLUMN_R_ORGANISASI_ID_EDC = "r_organisasi_id";

    private static final String TABLE_M_PRODUK_PROMO = "m_produk_promo";
    private static final String COLUMN_PRODUK_PROMO_id = "m_produk_promo_id";
    private static final String COLUMN_KDPRODUK_PROMO = "kdproduk";
    private static final String COLUMN_DISC= "disc";
    //public static final String COLUMN_ = "":

    // Perintah SQL untuk membuat tabel database baru
    private static final String db_create_table_1 = "create table "
            + TABLE_NAME_1 + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_KODE_TOKO + " varchar(100) not null, "
            + COLUMN_NAMA_TOKO + " varchar(100) not null)";

    private static final String db_create_table_2 = "create table "
            + TABLE_NAME_2 + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_KODE_LOKATOR + " varchar(100) not null, "
            + COLUMN_NAMA_LOKATOR + " varchar(100) not null)";

    private static final String db_create_table_3 = "create table "
            + TABLE_NAME_3 + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_KODE_PRODUK + " varchar(100) not null, "
            + COLUMN_NAMA_PRODUK + " varchar(100) not null,"
            + COLUMN_HPJ + " integer, "
            + COLUMN_R_PRODUK_ID + " integer )";

    private static final String db_create_table_4 = "create table "
            + TABLE_NAME_4 + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_TEAM + " integer,"
            + COLUMN_TANGGAL + " DATE,"
            + COLUMN_JAM + " TIME,"
            + COLUMN_KODE_TOKO+ " varchar(50) not null,"
            + COLUMN_KODE_LOKATOR + " varchar(100) not null,"
            + COLUMN_KODE_PRODUK + " varvhar(100) not null,"
            + COLUMN_QTY + " integer,"
            + COLUMN_STATUS + " varchar(2) default 0 not null)";

    private static final String db_create_tbl_t_transpos = "create table "
            + TABLE_T_TRANSPOS + "("
            + COLUMN_T_TRANSPOS_ID + " integer primary key autoincrement, "
            + COLUMN_NOTRANS + " varchar(30) , "
            + COLUMN_TANGGAL + " date,"
            + COLUMN_TOTAL_TRANS + " integer,"
            + COLUMN_NO_FAKTUR_PAJAK + " varchar(30) , "
            + COLUMN_R_TIPEDOKUMEN_ID + " integer , "
            + COLUMN_KDSTASTUSDOKUMEN + " varchar(5) , "
            + COLUMN_R_ORGANISASI_ID + " integer,"
            + COLUMN_IS_UPLOAD + " integer,"
            + COLUMN_JAM + " time)";

    private static final String db_create_tbl_t_transpos_detail = "create table "
            + TABLE_T_TRANSPOS_DETAIL + "("
            + COLUMN_T_TRANSPOS_DETAIL_ID + " integer primary key autoincrement, "
            + COLUMN_NOTRANS_DETAIL + " varchar(30) , "
            + COLUMN_KDPRODUK + " varchar(30) , "
            + COLUMN_QTY + " integer,"
            + COLUMN_HARGA + " integer,"
            + COLUMN_PRSN_DISC + " double , "
            + COLUMN_NOMINAL_DISC + " integer , "
            + COLUMN_NETTO + " integer , "
            + COLUMN_PPN + " double , "
            + COLUMN_R_PRODUK_ID_TRANPOS_DETAIL + " integer,"
            + COLUMN_M_KATEGORI_DISC_ID + " integer)";

    private static final String db_create_tbl_t_transpos_bayar = "create table "
            + TABLE_T_BAYAR_POS + "("
            + COLUMN_NOTRANS_BAYAR_POS_id + " integer primary key autoincrement, "
            + COLUMN_NOTRANS_BAYAR_POS + " varchar(30) , "
            + COLUMN_KODE_BAYAR_POS + " varchar(30) , "
            + COLUMN_M_EDC_ID + " integer,"
            + COLUMN_NOMINAL_BAYAR + " integer,"
            + COLUMN_BAYAR + " integer)";


    private static final  String db_create_tbl_m_carabyar = "create table "
            + TABLE_M_CARABAYAR + "("
            + COLUMN_CARABAYAR_ID + " integer primary key autoincrement, "
            + COLUMN_CARABAYAR + " varchar(20) , "
            + COLUMN_NAMA_CARABAYAR + "  varchar(20),"
            + COLUMN_ISEDC + " varchar(100))";

    private  String db_create_tbl_m_edc = "create table "
            + TABLE_M_EDC + "("
            + COLUMN_NAMAMERCHAND_id + " integer primary key autoincrement, "
            + COLUMN_NAMAMERCHAND + " varchar(20) , "
            + COLUMN_TIPEBANK + "  varchar(50),"
            + COLUMN_R_ORGANISASI_ID_EDC + " integer)";

    private static final String db_create_tbl_m_produk_promo = "create table "
            + TABLE_M_PRODUK_PROMO + "("
            + COLUMN_PRODUK_PROMO_id + " integer primary key autoincrement, "
            + COLUMN_KDPRODUK_PROMO + " varchar(20) , "
            + COLUMN_DISC + " float)";


    public DBHelper(Context context) {
        super(context, db_name, null, db_version);
        // Auto generated
    }

    //mengeksekusi perintah SQL di atas untuk membuat tabel database baru
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(db_create_table_1);
        db.execSQL(db_create_table_2);
        db.execSQL(db_create_table_3);
        db.execSQL(db_create_table_4);
        db.execSQL(db_create_tbl_t_transpos);
        db.execSQL(db_create_tbl_t_transpos_detail);
        db.execSQL(db_create_tbl_t_transpos_bayar);
        db.execSQL(db_create_tbl_m_carabyar);
        db.execSQL(db_create_tbl_m_edc);
        db.execSQL(db_create_tbl_m_produk_promo);


    }

    // dijalankan apabila ingin mengupgrade database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DBHelper.class.getName(), "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_1);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_2);
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_3);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_4);
        onCreate(db);

    }
}

