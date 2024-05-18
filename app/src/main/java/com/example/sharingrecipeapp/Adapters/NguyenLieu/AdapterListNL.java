package com.example.sharingrecipeapp.Adapters.NguyenLieu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sharingrecipeapp.Classes.NguyenLieu;
import com.example.sharingrecipeapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterListNL extends BaseAdapter {

    List<NguyenLieu> list;

    public AdapterListNL(List<NguyenLieu> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_ingredients,parent,false);
        NguyenLieu nguyenLieu = list.get(position);

        ImageView imageView = view.findViewById(R.id.imgin);
        TextView name = view.findViewById(R.id.namein);
        EditText editText = view.findViewById(R.id.quantityin);

        Picasso.get().load(nguyenLieu.getImg()).into(imageView);
        name.setText(nguyenLieu.getName());
        String sl = String.valueOf(nguyenLieu.getSL());
        editText.setText(sl);
        return view;
    }
}
