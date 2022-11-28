package com.web.ajmerainfotech;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.web.ajmerainfotech.databinding.ActivityMainBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding mainBinding;

    private ProgressDialog loadingBar;

    List<AuthorResponse> authorResponses = new ArrayList<>();
    private AuthorAdapter authorAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());


        loadingBar = new ProgressDialog(this);


        getShow();

        mainBinding.recyclerview.setHasFixedSize(true);
        mainBinding.recyclerview.setLayoutManager(new LinearLayoutManager(MainActivity.this,LinearLayoutManager.VERTICAL,false));



        mainBinding.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validation();
            }

        });



    }

    private void validation() {
      String  authorname = mainBinding.name.getText().toString().trim();


        if (mainBinding.name.getText().toString().trim().isEmpty()) {
            mainBinding.name.setError("Name can't empty.");
            mainBinding.name.requestFocus();
            return;
        }else{
            loadingBar.setTitle(getString(R.string.app_name));
            loadingBar.setMessage("Please Wait...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            //method to get text
            sendData(authorname);
        }
    }

    private void sendData(String authorname) {

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date date = new Date();
        String  currentdate = formatter.format(date);


        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.Base_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

            Api api = retrofit.create(Api.class);
            Call<String> call = api.addauto(authorname,currentdate);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    loadingBar.dismiss();
                    if (response.isSuccessful()){

                            getClear();
                            getShow();
                            Toast.makeText(MainActivity.this, "ADDED", Toast.LENGTH_LONG).show();




                    }else{

                        Toast.makeText(MainActivity.this, "Server Error", Toast.LENGTH_LONG).show();

                    }

                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    loadingBar.dismiss();
                    Toast.makeText(MainActivity.this, "Error Response : " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

        }

    private void getClear() {
        mainBinding.name.setText("");
    }

    private void getShow() {


        Retrofit retrofit1 = new Retrofit.Builder().baseUrl(Api.Base_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        Api api1 = retrofit1.create(Api.class);


        Call<List<AuthorResponse>> callproduct = api1.getauto();
        callproduct.enqueue(new Callback<List<AuthorResponse>>() {
            @Override
            public void onResponse(Call<List<AuthorResponse>> call, Response<List<AuthorResponse>> response) {
                Log.e(TAG, "onResponse: code:" + response.code());
                List<AuthorResponse> productDetail = response.body();
                if (response.isSuccessful()){
                    authorResponses = response.body();
                    authorAdapter = new AuthorAdapter(authorResponses, MainActivity.this);
                    mainBinding.recyclerview.setAdapter(authorAdapter);

                }else{
                }
            }

            @Override
            public void onFailure(Call<List<AuthorResponse>> call, Throwable t) {

            }
        });



    }



    public class AuthorAdapter extends RecyclerView.Adapter<AuthorAdapter.ViewHolder> {

        Context context;
        List<AuthorResponse> authorResponseList;
        Dialog dialog;

        public AuthorAdapter(List<AuthorResponse> authorResponseList,Context context){
            this.context = context;
            this.authorResponseList = authorResponseList;
        }


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.layout,parent,false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            String id = authorResponseList.get(position).getId();
            String name = authorResponseList.get(position).getAuthorname();
            String book = authorResponseList.get(position).getBookname();
            String price = authorResponseList.get(position).getBookprice();


            holder.authorid.setText(""+id);
            holder.authorname.setText(""+name);
            holder.book.setText(""+book);
            holder.price.setText(""+price);

            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.check.setVisibility(View.VISIBLE);
                }
            });


            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    Intent intent = new Intent(MainActivity.this , SecondActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("name", name);
                    intent.putExtra("book", book);
                    intent.putExtra("price", price);
                    startActivity(intent);
                }
            });


            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    SendDelete(authorResponseList.get(position).getId());

                }
            });

        }

        private void SendDelete(String id) {

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Api.Base_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            Api api = retrofit.create(Api.class);
            Call<String> call = api.deleteauto(id);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    loadingBar.dismiss();
                    if (response.isSuccessful()){

                        getShow();
                        Toast.makeText(MainActivity.this, "Delete", Toast.LENGTH_LONG).show();




                    }else{

                        Toast.makeText(MainActivity.this, "Server Error", Toast.LENGTH_LONG).show();

                    }

                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    loadingBar.dismiss();
                    Toast.makeText(MainActivity.this, "Error Response : " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });


        }


        @Override
        public int getItemCount() {

            return authorResponseList.size();

        }


        public class ViewHolder extends RecyclerView.ViewHolder{

            TextView authorid,authorname,book,price,delete,view,edit;
            LinearLayout check;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                authorid = itemView.findViewById(R.id.authorid);
                check = itemView.findViewById(R.id.check);
                authorname = itemView.findViewById(R.id.authorname);
                book = itemView.findViewById(R.id.book);
                price = itemView.findViewById(R.id.price);
                delete = itemView.findViewById(R.id.delete);
                view = itemView.findViewById(R.id.view);
                edit = itemView.findViewById(R.id.edit);
            }
        }
    }




}