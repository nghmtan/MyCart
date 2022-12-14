package hanu.a2_1901040237.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import hanu.a2_1901040237.model.Product;

public class ProductInCartManager {
    private SQLiteDatabase db;
    public ProductInCartManager(Context context){
        DbHelper dbHelper = new DbHelper(context);
        db=dbHelper.getWritableDatabase();
    }
    public List<Product> getAll_products(){
        List<Product> products = new ArrayList<>();
        Cursor results =db.rawQuery("SELECT id,thumbnail,name,price,quantity FROM cart  WHERE quantity > 0",null);
        int idIndex = results.getColumnIndex("id");
        int thumbnailIndex=results.getColumnIndex("thumbnail");
        int nameIndex=results.getColumnIndex("name");
        int priceIndex = results.getColumnIndex("price");
        int quantityIndex = results.getColumnIndex("quantity");
        while(results.moveToNext()){
            int id =results.getInt(idIndex);
            String thumbnail = results.getString(thumbnailIndex);
            String name = results.getString(nameIndex);
            double price = results.getDouble(priceIndex);
            int quantity = results.getInt(quantityIndex);
            Product product = new Product(id,thumbnail,name,price,quantity);
            products.add(product);
        }
        results.close();
        return products;
    }
}
