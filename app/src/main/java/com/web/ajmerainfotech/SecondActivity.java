package com.web.ajmerainfotech;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.web.ajmerainfotech.databinding.ActivityMainBinding;
import com.web.ajmerainfotech.databinding.ActivitySecondBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class SecondActivity extends AppCompatActivity {

    ActivitySecondBinding secondBinding;

    private ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        secondBinding = ActivitySecondBinding.inflate(getLayoutInflater());
        setContentView(secondBinding.getRoot());



        loadingBar = new ProgressDialog(this);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String name = intent.getStringExtra("name");
        String book = intent.getStringExtra("book");
        String price = intent.getStringExtra("price");

        secondBinding.name.setText(""+name);
        secondBinding.bookname.setText(""+book);
        secondBinding.pricebook.setText(""+price);

        if (book.equalsIgnoreCase("na")){
            secondBinding.bookname.setVisibility(View.GONE);
            secondBinding.l1.setVisibility(View.VISIBLE);
            secondBinding.add.setVisibility(View.VISIBLE);
        }

        if(price.equalsIgnoreCase("na")){
            secondBinding.pricebook.setVisibility(View.GONE);
            secondBinding.pricebookenter.setVisibility(View.VISIBLE);
            secondBinding.add.setVisibility(View.VISIBLE);
        }


        secondBinding.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validation(id);
            }

        });


    }


    private void validation(String id) {
        String  book1 = secondBinding.book.getText().toString().trim();
        String  price = secondBinding.price.getText().toString().trim();


        if (secondBinding.book.getText().toString().trim().isEmpty()) {
            secondBinding.book.setError("Book Name can't empty.");
            secondBinding.book.requestFocus();
            return;
        }else if (secondBinding.price.getText().toString().trim().isEmpty()) {
            secondBinding.price.setError("Book Price can't empty.");
            secondBinding.price.requestFocus();
            return;
        }else{
            loadingBar.setTitle(getString(R.string.app_name));
            loadingBar.setMessage("Please Wait...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            //method to get text
            updateData(id,book1,price);
        }
    }

    private void updateData(String id , String book1, String price) {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.Base_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        Api api = retrofit.create(Api.class);
        Call<String> call = api.updateauto(id, book1,price);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                loadingBar.dismiss();
                if (response.isSuccessful()){

                    getClear();

                    Intent intent = new Intent(SecondActivity.this , MainActivity.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(SecondActivity.this, "ADDED", Toast.LENGTH_LONG).show();




                }else{

                    Toast.makeText(SecondActivity.this, "Server Error", Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                loadingBar.dismiss();
                Toast.makeText(SecondActivity.this, "Error Response : " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }


    private void getClear() {
        secondBinding.book.setText("");
        secondBinding.price.setText("");
    }


}