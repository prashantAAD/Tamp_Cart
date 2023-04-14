package com.example.tampcart.tampcart.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tampcart.databinding.ActivityMainBinding;
import com.example.tampcart.tampcart.adapters.CategoryAdapter;
import com.example.tampcart.tampcart.adapters.ProductAdapter;
import com.example.tampcart.tampcart.models.Category;
import com.example.tampcart.tampcart.models.Product;
import com.example.tampcart.tampcart.utils.Constants;
import com.mancj.materialsearchbar.SimpleOnSearchActionListener;

import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    CategoryAdapter categoryAdapter;
    ArrayList<Category> categories;
    ProductAdapter productAdapter;
    ArrayList<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.searchBar.setOnSearchActionListener(new SimpleOnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {


            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                Intent intent=new Intent(MainActivity.this, SearchActivity.class);
                intent.putExtra("query",text.toString());
                startActivity(intent);

            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });

        initCategories();
        initProducts();
        initSlider();
    }

    private void initSlider() {
        getRecentOffers();
        }


    void initCategories() {
        categories = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(this, categories);

        getCategories();

        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        binding.categoriesList.setLayoutManager(layoutManager);
        binding.categoriesList.setAdapter(categoryAdapter);
    }

    void getCategories() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.GET_CATEGORIES_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject mainobj = new JSONObject(response);
                    if (mainobj.getString("status").equals("success")) {
                        JSONArray categoryArray = mainobj.getJSONArray("categories");
                        for (int i = 0; i < categoryArray.length(); i++) {
                            JSONObject object = categoryArray.getJSONObject(i);
                            Category category = new Category(
                                    object.getString("name"),
                                    Constants.CATEGORIES_IMAGE_URL + object.getString("icon"),
                                    object.getString("color"),
                                    object.getString("brief"),
                                    object.getInt("id")
                            );
                            categories.add(category);

                        }
                        categoryAdapter.notifyDataSetChanged();
                    } else {

                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

        });
        queue.add(stringRequest);
    }

    void getRecentProducts() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constants.GET_PRODUCTS_URL + "?count=8";
        // Request a string response from the provided URL.
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                try {
                    JSONObject mainobj = new JSONObject(response);
                    if (mainobj.getString("status").equals("success")) {
                        JSONArray productArray = mainobj.getJSONArray("products");
                        for (int i = 0; i < productArray.length(); i++) {
                            JSONObject object = productArray.getJSONObject(i);
                            Product product = new Product(
                                    object.getString("name"),
                                    Constants.PRODUCTS_IMAGE_URL + object.getString("image"),
                                    object.getString("status"),
                                    object.getDouble("price"),
                                    object.getDouble("price_discount"),
                                    object.getInt("stock"),
                                    object.getInt("id")


                            );
                            products.add(product);

                        }
                        productAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(request);
    }

    void getRecentOffers() {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.GET_OFFERS_URL, response -> {
            try {
                JSONObject mainobj = new JSONObject(response);
                if (mainobj.getString("status").equals("success")) {
                    JSONArray offerArray = mainobj.getJSONArray("news_infos");
                    for (int i = 0; i < offerArray.length(); i++) {
                        JSONObject object = offerArray.getJSONObject(i);
                        binding.carousel.addData(new CarouselItem(
                                Constants.NEWS_IMAGE_URL + object.getString("image"),
                                object.getString("title")
                        ));
                    }
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }, error -> {
        });
        queue.add(stringRequest);
    }

    void initProducts() {
        products = new ArrayList<>();
        productAdapter = new ProductAdapter(this, products);
        getRecentProducts();

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        binding.productList.setLayoutManager(layoutManager);
        binding.productList.setAdapter(productAdapter);
    }
}