package com.example.tampcart.tampcart.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.tampcart.R;
import com.example.tampcart.databinding.ActivityCartBinding;
import com.example.tampcart.tampcart.adapters.CartAdapter;
import com.example.tampcart.tampcart.models.Product;
import com.hishd.tinycart.model.Cart;
import com.hishd.tinycart.model.Item;
import com.hishd.tinycart.util.TinyCartHelper;

import java.util.ArrayList;
import java.util.Map;

public class CartActivity extends AppCompatActivity {
ActivityCartBinding binding;
CartAdapter adapter;
ArrayList<Product> products;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        products=new ArrayList<>();

        Cart cart = TinyCartHelper.getCart();

        for (Map.Entry<Item,Integer> item : cart.getAllItemsWithQty().entrySet()) {
            Product product=(Product) item.getKey();
            int quantity=item.getValue();
            product.setQuantity(quantity);

            products.add(product);
        }


        adapter=new CartAdapter(this, products, new CartAdapter.CartListner() {
            @Override
            public void onQuantityChanged() {
                binding.subTotal.setText(String.format(" INR %.2f " ,cart.getTotalPrice()));
            }
        });
        LinearLayoutManager layoutManager= new LinearLayoutManager(this);
        DividerItemDecoration itemDecoration= new DividerItemDecoration(this,layoutManager.getOrientation());
        binding.cartlist.setLayoutManager(layoutManager);
        binding.cartlist.addItemDecoration(itemDecoration);
        binding.cartlist.setAdapter(adapter);

        binding.subTotal.setText(String.format(" INR %.2f " ,cart.getTotalPrice()));

        binding.continuebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
startActivity(new Intent(CartActivity.this,CheckOutActivity.class));
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}