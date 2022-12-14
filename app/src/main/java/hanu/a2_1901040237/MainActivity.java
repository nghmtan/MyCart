package hanu.a2_1901040237;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.os.HandlerCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import hanu.a2_1901040237.adapter.ProductAdapter;
import hanu.a2_1901040237.model.Constants;
import hanu.a2_1901040237.model.Product;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private static List<Product> products = new ArrayList<>();
    private ProductAdapter productAdapter = new ProductAdapter(products,MainActivity.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView=findViewById(R.id.recycler_product);
        SearchView filter = findViewById(R.id.filter);
        Handler handler = HandlerCompat.createAsync(Looper.getMainLooper());
        Constants.executor.execute(new Runnable() {
            @Override
            public void run() {
                String api = "https://mpr-cart-api.herokuapp.com/products";
                String json = Constants.loadJSON(api);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(json==null){
                            Toast.makeText(MainActivity.this,"Load failed",Toast.LENGTH_LONG).show();
                        }
                        else{
                            try {
                                JSONArray jsonArray = new JSONArray(json);
                                for (int i=0; i<jsonArray.length();i++){
                                    JSONObject root = jsonArray.getJSONObject(i);
                                    int id = root.getInt("id");
                                    String name = root.getString("name");
                                    String thumbnail = root.getString("thumbnail");
                                    double unitPrice= root.getDouble("unitPrice");
                                    products.add(new Product(id,thumbnail,name,unitPrice));
                                }
                                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(MainActivity.this, 2);
                                recyclerView.setLayoutManager(layoutManager);
                                recyclerView.setAdapter(productAdapter);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                });
            }
        });
        filter.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                List<Product> searched = new ArrayList<>();
                for (int i= 0; i<products.size();i++){
                    Product product = products.get(i);
                    if(product.getName().contains(query)){
                        searched.add(product);
                    }
                }
                productAdapter.setProducts(searched);
                productAdapter.notifyDataSetChanged();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String search_text) {
                List<Product> searched = new ArrayList<>();
                for (int i= 0; i<products.size();i++){
                    Product product = products.get(i);
                    if(product.getName().contains(search_text)){
                        searched.add(product);
                    }
                }
                productAdapter.setProducts(searched);
                productAdapter.notifyDataSetChanged();
                return false;
            }
        });
        ImageButton cart_activity_view=findViewById(R.id.cart);
        cart_activity_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),CartActivity.class);
                startActivity(intent);
            }
        });
    }

}