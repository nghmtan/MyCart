package hanu.a2_1901040237.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME="cart.db";
    private static final int DB_VERSION=20;

    public DbHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        //create table
        db.execSQL("CREATE TABLE cart("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "thumbnail TEXT NOT NULL,"+
                "name TEXT NOT NULL,"+
                "quantity INT,"+
                "price DOUBLE NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE cart");
        onCreate(db);
    }
}
