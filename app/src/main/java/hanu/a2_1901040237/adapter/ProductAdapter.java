package hanu.a2_1901040237.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.os.HandlerCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import hanu.a2_1901040237.R;
import hanu.a2_1901040237.database.DbHelper;
import hanu.a2_1901040237.model.Constants;
import hanu.a2_1901040237.model.Product;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder>{
    private Context context;
    private List<Product> products;

    public ProductAdapter(List<Product> products, Context context) {
        this.context = context;
        this.products = products;
    }
    public void addToCart(ProductAdapter.ProductHolder holder, int position){
        DbHelper dbHelper = new DbHelper(this.context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String insert_data = "INSERT INTO cart(id, thumbnail, name, price, quantity) VALUES(?, ?, ?, ?, ?)";
        String update_data = "UPDATE cart" +
                " SET quantity = quantity+1" +
                " WHERE id="+ products.get(position).getId()+";";
        String check_duplicate = "SELECT id,thumbnail,name,price,quantity from cart where id = " + products.get(position).getId();
        Cursor cursor = db.rawQuery(check_duplicate, null);
        SQLiteStatement statement = db.compileStatement(insert_data);
        holder.add_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cursor.getCount() <= 0){
                    cursor.close();
                    statement.bindLong(1,products.get(position).getId());
                    statement.bindString(2,products.get(position).getThumbnail());
                    statement.bindString(3,products.get(position).getName());
                    statement.bindDouble(4,products.get(position).getUnitPrice());
                    statement.bindLong(5,1);
                    statement.executeInsert();
                }
                else{db.execSQL(update_data);}
                notifyItemChanged(position);
            }
        });



    }
    @NonNull
    @Override
    public ProductAdapter.ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.product_view,parent,false);
        ProductHolder productHolder=new ProductHolder(view);
        return productHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ProductHolder holder, @SuppressLint("RecyclerView") int position) {
        bind(holder, position);
        addToCart(holder, position);
    }
    @Override
    public int getItemCount() {
        return products.size();
    }
public void bind(@NonNull ProductAdapter.ProductHolder holder, @SuppressLint("RecyclerView") int position){
    Product product = products.get(position);
    holder.name.setText(product.getName());
    holder.price.setText(Double.toString(product.getUnitPrice())+" VND");
    Handler handler = HandlerCompat.createAsync(Looper.getMainLooper());
    Constants.executor.execute(new Runnable() {
        @Override
        public void run() {
            Bitmap bitmap = Constants.downloadImage(products.get(position).getThumbnail());
            if (bitmap != null) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        holder.imageProduct.setImageBitmap(bitmap);
                    }
                });
            }
        }
    });
}
    public void setProducts(List<Product> products) {
        this.products = products;
    }
    public class ProductHolder extends RecyclerView.ViewHolder{
        TextView name;
        ImageView imageProduct;
        TextView price;
        ImageButton add_cart;
        public ProductHolder(@NonNull View itemView) {
            super(itemView);
            imageProduct=itemView.findViewById(R.id.productImage);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            add_cart=itemView.findViewById(R.id.add_to_cart);
        }
    }
}
