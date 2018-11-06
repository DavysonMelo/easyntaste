package com.example.davyson.easyntaste;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.davyson.teste.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import basic.Receita;

public class RecipeAdapeter extends RecyclerView.Adapter<RecipeAdapeter.MyViewHolder> {
    private Context context;
    private List<Receita> lista;
    private LayoutInflater layoutInflater;
    private RecyclerClick mRecyclerClick;
    public RecipeAdapeter(Context context, List<Receita> lista) {
        layoutInflater  = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.lista = lista;
    }

    @Override
    public  MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.list_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public  void onBindViewHolder(MyViewHolder holder, int position) {
        holder.titulo.setText(lista.get(position).getTitulo());
        holder.ingredientes.setText(lista.get(position).getIngredients());
        new LoadImage(lista.get(position).getImg(), holder.img).execute();
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }
    public void setRecyclerClick(RecyclerClick r){
        mRecyclerClick =r;
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView titulo;
        public TextView ingredientes;
        public ImageView img;

        public MyViewHolder(View itemView) {

            super(itemView);
            titulo = itemView.findViewById(R.id.txvTitulo);
            ingredientes = itemView.findViewById(R.id.txvContent);
            img = itemView.findViewById(R.id.recipeImg);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(mRecyclerClick != null){
                mRecyclerClick.itemClick(view, getPosition(), lista.get(getAdapterPosition()));
            }
        }
    }

    class LoadImage extends AsyncTask<Void, Void, Void>{
        private String imgUrl;
        private Bitmap imgReceita;
        private ImageView imagem;

        public LoadImage(String url, ImageView img){
            this.imgUrl = url;
            this.imagem = img;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL url = new URL(imgUrl);
                HttpURLConnection con =(HttpURLConnection) url.openConnection();
                InputStream is = con.getInputStream();
                imgReceita = BitmapFactory.decodeStream(is);
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
            imagem.setImageBitmap(imgReceita);
        }
    }

}
