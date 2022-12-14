package hanu.a2_1901040237.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
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

import hanu.a2_1901040237.CartActivity;
import hanu.a2_1901040237.R;
import hanu.a2_1901040237.database.DbHelper;
import hanu.a2_1901040237.model.Constants;
import hanu.a2_1901040237.model.Product;

public class ProductCartAdapter extends RecyclerView.Adapter<ProductCartAdapter.CartHolder> {
    private Context context;
    private List<Product> products;

    public ProductCartAdapter(Context context, List<Product> products) {
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public ProductCartAdapter.CartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View productView =inflater.inflate(R.layout.item_cart, parent,false);
        return new CartHolder(productView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductCartAdapter.CartHolder holder, @SuppressLint("RecyclerView") int position) {
        bind(holder, position);
        holder.increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                increaseQuantity(holder,position);
            }
        });
        holder.decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decreaseQuantity(holder, position);
            }
        });
    }
public void increaseQuantity(ProductCartAdapter.CartHolder holder, int position){
    DbHelper dbHelper = new DbHelper(this.context);
    SQLiteDatabase db= dbHelper.getWritableDatabase();
    String increase_sql = "UPDATE cart SET quantity = quantity + 1 WHERE id = "+ products.get(position).getId();
            products.get(position).setQuantity(products.get(position).getQuantity()+1);
            db.execSQL(increase_sql);
            db.close();
            calcSum();
            notifyItemChanged(position);
}
    public void decreaseQuantity(ProductCartAdapter.CartHolder holder, int position){
        DbHelper dbHelper = new DbHelper(this.context);
        SQLiteDatabase db= dbHelper.getWritableDatabase();
        switch (products.get(position).getQuantity()){
            case 1: String delete_sql = "DELETE from cart WHERE id="+products.get(position).getId();
            db.execSQL(delete_sql);
                products.remove(products.get(position));
                notifyItemRemoved(position);
                break;
            default: String decrease_sql = "UPDATE cart SET quantity = quantity -1 WHERE id = "+ products.get(position).getId();

            products.get(position).setQuantity(products.get(position).getQuantity()-1);
                db.execSQL(decrease_sql);
        }
        calcSum();
        notifyItemChanged(position);
    }
    public void bind(@NonNull ProductCartAdapter.CartHolder holder, @SuppressLint("RecyclerView") int position){
        Product product = products.get(position);
        holder.cartPrice.setText(Double.toString(product.getUnitPrice())+"VND");
        holder.cart_name.setText(product.getName());
        holder.quantity.setText(Integer.toString(product.getQuantity()));
        holder.sum_product.setText(Double.toString(products.get(position).getUnitPrice() *products.get(position).getQuantity()));
        Handler handler = HandlerCompat.createAsync(Looper.getMainLooper());
        Constants.executor.execute(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = Constants.downloadImage(products.get(position).getThumbnail());

                if (bitmap != null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            holder.imageProductCart.setImageBitmap(bitmap);
                        }
                    });
                }
            }
        });
    }
    private void calcSum(){
        TextView sum=((CartActivity)context).findViewById(R.id.totalprice);
        double total =0;
        for (int i=0;i<products.size();i++) {
            total=total-products.get(i).getUnitPrice()*products.get(i).getQuantity();
        }
        sum.setText(total*-1+"VND");
    }
    @Override
    public int getItemCount() {
        return products.size();
    }
    public class CartHolder extends RecyclerView.ViewHolder {
        ImageView imageProductCart;
        TextView cart_name;
        TextView cartPrice;
        ImageButton increase;
        ImageButton decrease;
        TextView sum_product;
        TextView quantity;
        public CartHolder(@NonNull View itemView) {
            super(itemView);
            imageProductCart=itemView.findViewById(R.id.productCartImage);
            quantity=itemView.findViewById(R.id.quantity);
            increase=itemView.findViewById(R.id.increase);
            decrease=itemView.findViewById(R.id.decrease);
            cart_name = itemView.findViewById(R.id.cart_name);
            cartPrice = itemView.findViewById(R.id.priceCart);
            sum_product=itemView.findViewById(R.id.sum_product);
        }
    }
}
