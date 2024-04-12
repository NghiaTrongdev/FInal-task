package com.example.baitap1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DatabaseManager extends SQLiteOpenHelper {
    private static final String dbname = "Thisinhinformation.db";
    private static final int version = 1;
    public static final String TableName = "ThisinhTable";
    public static final String Id = "Id";
    public static final String Name = "Name";
    public static final String Toan = "Toan";
    public static final String Ly = "Ly";
    public static final String Hoa = "Hoa";
    private Context context;
    public DatabaseManager(@Nullable Context context) {
        super(context, dbname, null, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Tạo câu SQL để tạo bảng ThisinhTable
        String sqlCreate = "CREATE TABLE IF NOT EXISTS " + TableName + "("
                + Id + " Text PRIMARY KEY , "
                + Name + " TEXT, "
                + Toan + " Double, "
                + Ly + " Double, "
                + Hoa + " Double)";
        // Chạy câu truy vấn SQL để tạo bảng
        sqLiteDatabase.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // Xóa bảng ThisinhTable đã có
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TableName);
        // Tạo lại
        onCreate(sqLiteDatabase);
    }

    // Lấy tất cả các dòng của bảng ThisinhTable trả về dạng ArrayList
    public ArrayList<Thisinh> getAllThisinh() {
        ArrayList<Thisinh> list = new ArrayList<>();
        // Câu truy vấn
        String sql = "SELECT * FROM " + TableName;
        // Lấy đối tượng csdl sqlite
        SQLiteDatabase db = this.getReadableDatabase();
        // Chạy câu truy vấn trả về dạng Cursor
        Cursor cursor = db.rawQuery(sql, null);
        // Tạo ArrayList<Thisinh> để trả về
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String sbd = cursor.getString(0);
                String name = cursor.getString(1);
                Double toan = cursor.getDouble(2);
                Double ly = cursor.getDouble(3);
                Double hoa = cursor.getDouble(4);
                Thisinh thisinh = new Thisinh(sbd,name,toan,ly,hoa);
                list.add(thisinh);
            }
            cursor.close();
        }
        db.close();
        return list;
    }


    public void addThisinh(Thisinh thisinh) {
       SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Id,thisinh.getSbd());
        contentValues.put(Name,thisinh.getName());
        contentValues.put(Toan,thisinh.getToan());
        contentValues.put(Ly,thisinh.getLy());
        contentValues.put(Hoa,thisinh.getHoa());
        database.insert(TableName,null,contentValues);
        database.close();
    }

    // Cập nhật thông tin của một thí sinh trong bảng ThisinhTable
    public void updateThisinh(Thisinh thisinh) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Name, thisinh.getName());
        values.put(Toan, thisinh.getToan());
        values.put(Ly, thisinh.getLy());
        values.put(Hoa, thisinh.getHoa());
        db.update(TableName, values, Id + " = ?", new String[]{String.valueOf(thisinh.getSbd())});
        db.close();
    }

    // Xóa thông tin của một thí sinh trong bảng ThisinhTable
    public void deleteThisinhbyId(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TableName, Id + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }
    public void deleteThiSinhbyObj(Thisinh thisinh){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TableName, Id + " = ?", new String[]{String.valueOf(thisinh.getSbd())});
        db.close();
    }
}
