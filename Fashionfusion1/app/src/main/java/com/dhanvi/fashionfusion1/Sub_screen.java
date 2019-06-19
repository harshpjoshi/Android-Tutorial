package com.dhanvi.fashionfusion1;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.dhanvi.fashionfusion1.databinding.ActivitySubScreenBinding;
import com.dhanvi.fashionfusion1.model.MyData;
import com.dhanvi.fashionfusion1.model.Product;

public class Sub_screen extends AppCompatActivity {


    ActivitySubScreenBinding binding;
    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_screen);

        binding = DataBindingUtil.setContentView(this,R.layout.activity_sub_screen);

        product = MyData.getSelectedProduct();

        setData(product);

        setOnClickListener();
        

    }

    private void setOnClickListener() {
        binding.buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buyNow();
            }
        });

        binding.addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart();
            }
        });

        binding.similar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSimilarProduct();
            }
        });

        binding.wishList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToWishList();
            }
        });
    }

    private void addToWishList() {
        Toast.makeText(this, "wish", Toast.LENGTH_SHORT).show();
        MyData.getCartList().add(product);

    }

    private void showSimilarProduct() {
        Intent intent = new Intent(this,grid_view.class);
        intent.putExtra(Cat.category,product.getCategory_id());
        startActivity(intent);
    }

    private void addToCart() {
        Toast.makeText(this, "Cart", Toast.LENGTH_SHORT).show();
        MyData.getCartList().add(product);
    }

    private void buyNow() {
        Toast.makeText(this, "Buy", Toast.LENGTH_SHORT).show();
    }

    private void setData(Product product) {
        binding.imageView.setImageResource(product.getImage());
        binding.price.setText(MyData.getPrice(product.getPrice()));
        binding.detail.setText(product.getDescription());
        binding.name.setText(product.getName());
    }
}
