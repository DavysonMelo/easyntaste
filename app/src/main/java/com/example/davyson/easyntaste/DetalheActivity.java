package com.example.davyson.easyntaste;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.davyson.teste.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import basic.Receita;
import connection.RetrofitBuilder;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalheActivity extends AppCompatActivity {

    private TextView ingredientes;
    String ing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe);

        TextView titulo = findViewById(R.id.detalheTitulo);
        CircleImageView imgDetalhe = findViewById(R.id.imgReceita);
        ingredientes = findViewById(R.id.ingredientes);

        titulo.setText(getIntent().getStringExtra("Title"));
        new ImgLoad(getIntent().getStringExtra("imgUrl"), imgDetalhe).execute();

        requestBody();

    }

    class ImgLoad extends AsyncTask<Void, Void, Void> {
        private String imgUrl;
        private CircleImageView recipeImg;
        private Bitmap image;

        public ImgLoad(String url, CircleImageView img) {
            this.imgUrl = url;
            this.recipeImg = img;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL url = new URL(imgUrl);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                InputStream is = con.getInputStream();
                image = BitmapFactory.decodeStream(is);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            recipeImg.setImageBitmap(image);
        }
    }

    public void requestBody() {
        Call<List<Receita>> call = new RetrofitBuilder().recipeService().searchRecipe(getIntent().getStringExtra("url"), 1);
        call.enqueue(new Callback<List<Receita>>() {

            @Override
            public void onResponse(Call<List<Receita>> call, Response<List<Receita>> response) {
                List<Receita> rec = response.body();

                for (int i = 0; i < rec.size(); i++){
                    ing += rec.get(i).getIngredients();
                    ingredientes.setText(ing + "\n");
                }
            }

            @Override
            public void onFailure(Call<List<Receita>> call, Throwable t) {
                Toast.makeText(getBaseContext(), "Error 404", Toast.LENGTH_LONG).show();
                t.getMessage();
            }
        });
    }

}
