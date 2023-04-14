package com.example.tampcart.tampcart.adapters;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.tampcart.R;
import com.example.tampcart.databinding.ItemProductBinding;
import com.example.tampcart.tampcart.activity.ProductDetailedActivity;
import com.example.tampcart.tampcart.models.Category;
import com.example.tampcart.tampcart.models.Product;

import java.util.ArrayList;


public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    Context context;
    ArrayList<Product> products;

    public ProductAdapter(Context context, ArrayList<Product> products) {
        this.context = context;
        this.products = products;
    }


    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductAdapter.ProductViewHolder(LayoutInflater.from(context).inflate(R.layout.item_product, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
Product product=products.get(position);
        Glide.with(context).load(product.getImage()).into(holder.binding.image);
        holder.binding.label.setText(product.getName());
        holder.binding.price.setText("INR " + product.getPrice());
        holder.itemView.setOnClickListener(view -> {
            Intent intent=new Intent(context, ProductDetailedActivity.class);
            intent.putExtra("name",product.getName());
            intent.putExtra("image",product.getImage());
            intent.putExtra("id",product.getId());
            intent.putExtra("price",product.getPrice());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        ItemProductBinding binding;


        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemProductBinding.bind(itemView);
        }
    }


}
