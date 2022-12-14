package hanu.a2_1901040237;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hanu.a2_1901040237.adapter.ProductCartAdapter;
import hanu.a2_1901040237.database.ProductInCartManager;
import hanu.a2_1901040237.model.Product;

public class CartActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        List<Product> products;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_activity);
        RecyclerView rvCart=findViewById(R.id.cart_recycler);
        rvCart.setLayoutManager(new LinearLayoutManager(CartActivity.this));
        products = new ProductInCartManager(CartActivity.this).getAll_products();
        rvCart.setAdapter(new ProductCartAdapter(CartActivity.this,products));
        TextView sum = findViewById(R.id.totalprice);
        double total_price=0;
        for (int i=0;i<products.size();i++) {
            total_price+= products.get(i).getUnitPrice()*products.get(i).getQuantity();
        }
        sum.setText(total_price+" VND");
    }
}
