package com.example.davyson.easyntaste;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.davyson.teste.R;

import java.util.ArrayList;
import java.util.List;

import basic.Receita;
import connection.RetrofitBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements RecyclerClick {

    AnimationDrawable addAnimation;
    AnimationDrawable findAnimation;
    List<String> ingredt;
    GridView gridView;
    RecyclerView lista;
    String in;
    ProgressDialog progDailog;
    LinearLayoutManager lm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView add = findViewById(R.id.imageAdd);
        final ImageView search = findViewById(R.id.imageSearch);
        final CardView cardSearch = findViewById(R.id.cardSearch);
        //final CardView cardlist = findViewById(R.id.cardlist);
        final EditText edtIngerdientes = findViewById(R.id.search_ingredient);
        final GridView gridIngre = findViewById(R.id.gridIngredientes);
        search.setBackgroundResource(R.drawable.find_animation);

        final RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        p.alignWithParent = true;
        p.topMargin = 0;
        p.leftMargin = 0;

        add.setBackgroundResource(R.drawable.add_animation);
        lista = (RecyclerView) findViewById(R.id.listReceitas);

        addAnimation = (AnimationDrawable) add.getBackground();

        findAnimation = (AnimationDrawable) search.getBackground();

        gridView = findViewById(R.id.gridIngredientes);

        lm = new LinearLayoutManager(this);

        lm.setOrientation(LinearLayoutManager.VERTICAL);

        ingredt = new ArrayList<>();
        final GridAdapter adapter = new GridAdapter(this, ingredt);
        gridView.setAdapter(adapter);

        edtIngerdientes.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (!edtIngerdientes.getText().toString().equals("")) {

                        adapter.addIngrediente(edtIngerdientes.getText().toString());
                        edtIngerdientes.setText("");
                        cardSearch.setLayoutParams(p);
                        gridIngre.setVisibility(View.VISIBLE);
                        search.setVisibility(View.VISIBLE);

                    } else {
                        edtIngerdientes.setError("Você precisa digitar um ingrediente!");
                    }
                }
                return false;
            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addAnimation.isRunning()) {
                    addAnimation.stop();
                    addAnimation.start();
                } else {
                    addAnimation.start();
                }
                if (!edtIngerdientes.getText().toString().equals("")) {

                    adapter.addIngrediente(edtIngerdientes.getText().toString());
                    edtIngerdientes.setText("");
                    cardSearch.setLayoutParams(p);
                    gridIngre.setVisibility(View.VISIBLE);
                    search.setVisibility(View.VISIBLE);

                } else {
                    edtIngerdientes.setError("Você precisa digitar um ingrediente!");
                }
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (findAnimation.isRunning()) {
                    findAnimation.stop();
                    findAnimation.start();
                } else {
                    findAnimation.start();
                }
                if (!ingredt.isEmpty()) {
                    //cardlist.setVisibility(View.VISIBLE);
                    progDailog = new ProgressDialog(MainActivity.this);
                    progDailog.setMessage("Buscando Receitas...");
                    progDailog.setIndeterminate(false);
                    progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progDailog.show();
                    new BuscarReceitas().execute();
                    //new BuscarReceita().execute();
                    InputMethodManager imm = (InputMethodManager) getSystemService(MainActivity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                } else {
                    Toast.makeText(MainActivity.this, "Necessário adicionar ao menos um ingrediente", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void requestRecipe() {
        if (concatenarLista().equals("")) {
            Toast.makeText(this, "Necessário adicionar ao menos um ingrediente", Toast.LENGTH_LONG).show();
        } else {
            Call<List<Receita>> call = new RetrofitBuilder().recipeService().searchRecipe(concatenarLista(), 0);
            call.enqueue(new Callback<List<Receita>>() {
                @Override
                public void onResponse(Call<List<Receita>> call, Response<List<Receita>> response) {
                    final List<Receita> rec = response.body();
                    RecipeAdapeter adapeter = new RecipeAdapeter(getBaseContext(), rec);
                    lista.setLayoutManager(lm);
                    lista.setAdapter(adapeter);
                    adapeter.setRecyclerClick(MainActivity.this);
                    lista.setItemViewCacheSize(50);
                    progDailog.dismiss();
                }

                @Override
                public void onFailure(Call<List<Receita>> call, Throwable t) {
                    Toast.makeText(getBaseContext(), "Error 404", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public String concatenarLista() {

        in = "";
        for (int i = 0; i < ingredt.size(); i++) {
            in += ingredt.get(i) + ", ";

        }
        String newString = in.substring(0, in.lastIndexOf(','));
        return newString;

    }

    @Override
    public void itemClick(View view, int position, Receita r) {
        Toast.makeText(this, String.valueOf(position), Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, DetalheActivity.class);
        i.putExtra("imgUrl", r.getImg());
        i.putExtra("Title", r.getTitulo());
        i.putExtra("url", r.getUrl());
        startActivity(i);
    }

    public class BuscarReceitas extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            requestRecipe();
            return null;
        }
    }
}
