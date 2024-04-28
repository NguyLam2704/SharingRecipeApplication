package com.example.sharingrecipeapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sharingrecipeapp.Classes.ListIngredient;
import com.example.sharingrecipeapp.R;

import java.util.List;

public class ListInAdapter extends BaseAdapter {

    private final Context context;
    private final int layout;
    private final List<ListIngredient> ingList;

    public ListInAdapter(Context context, int layout, List<ListIngredient> ingList) {
        this.context = context;
        this.layout = layout;
        this.ingList = ingList;
    }

    @Override
    public int getCount() {
        return ingList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    static class ViewHolder{
        ImageView img;
        TextView txtName, txtQuan;
        CheckBox cbIn;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);
            holder = new ViewHolder();
            convertView.setTag(holder);
        } else {
            holder= (ViewHolder) convertView.getTag();
        }
        // anh xa view
        holder.txtName = (TextView) convertView.findViewById(R.id.namein);
        holder.txtQuan = (TextView) convertView.findViewById(R.id.quantityin);
        holder.img= (ImageView) convertView.findViewById(R.id.imgin);
        holder.cbIn   = (CheckBox) convertView.findViewById(R.id.checkIn);
        //Gan gia tri
        ListIngredient ingredient = ingList.get(position);

        if (ingredient !=null) {
            holder.txtName.setText(ingredient.getNameIn());
            holder.txtQuan.setText(ingredient.getQuantityIn());
            holder.img.setImageResource(ingredient.getImageIn());
            holder.cbIn.setChecked(ingredient.isCheck());
        }
        return convertView;
    }
}
