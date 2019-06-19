package com.dhanvi.fashionfusion1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

import com.dhanvi.fashionfusion1.database_call.NetworkCall;
import com.dhanvi.fashionfusion1.database_call.jsn;
import com.dhanvi.fashionfusion1.model.MyData;
import com.dhanvi.fashionfusion1.model.Product;

import java.util.HashMap;
import java.util.List;

public class grid_view extends AppCompatActivity {


    String categoryId = "0";
    private List<Product> list;
    private GridView gridview;
    private String categoryNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_view);

        categoryId = getIntent().getStringExtra("categoryId");
        categoryNo = getIntent().getStringExtra(Cat.category);
//        getProductList(categoryId);

        gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));

        getProductListAndSetAdapter(categoryNo);

    }

    private void getProductListAndSetAdapter(String categoryNo) {
        list = MyData.getListOfCategory(categoryNo);
        ListAdapter adapter = new ListAdapter(this,list);
        gridview.setAdapter(adapter);
    }

    private void getProductList(String categoryId) {
        HashMap<String, String> param=new HashMap<>();
        param.put("type","getProductList");
        param.put("categoryId",categoryId);
        NetworkCall.call(param).setDataResponseListener(new NetworkCall.SetDataResponse() {
            @Override
            public void setResponse(String responseStr) {
                if (jsn.checkResponseStr(responseStr)){
                    List<Product> list = jsn.getAllClassFromString(responseStr, Product.class);

                }
            }
        });
    }
}
