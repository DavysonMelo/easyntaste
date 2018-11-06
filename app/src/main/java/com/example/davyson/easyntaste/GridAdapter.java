package com.example.davyson.easyntaste;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.davyson.teste.R;

import java.util.List;

public class GridAdapter extends BaseAdapter {

    private Context context;
    private List<String> ingredientes;

    public GridAdapter(Context context, List<String> ingredientes){
        this.context = context;
        this.ingredientes = ingredientes;
    }

    @Override
    public int getCount() {
        return ingredientes.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.grid_item, viewGroup, false);
        TextView txv = view.findViewById(R.id.txvIngredientes);
        if(ingredientes.get(i)!=null) {
            txv.setText(ingredientes.get(i));
        }
        final int position = i;
        ImageView img = view.findViewById(R.id.deleteItem);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ingredientes.remove(position);
                notifyDataSetChanged();
            }
        });
        return view;
    }
    public void addIngrediente(String x){
        ingredientes.add(x);
        notifyDataSetChanged();
    }

}
