package com.example.sharingrecipeapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

import com.example.sharingrecipeapp.Classes.ABrief;
import com.example.sharingrecipeapp.R;

import java.util.List;

public class BriefAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private List<ABrief> BriefList;

    public BriefAdapter(Context context, int layout, List<ABrief> briefList) {
        this.context = context;
        this.layout = layout;
        this.BriefList = briefList;
    }

    @Override
    public int getCount() {
        return BriefList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHoler{
        TextView nameRe, timeRe, starRe, saveRe;
        ImageView imgRe;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHoler holder;
        if (convertView== null){
            holder= new ViewHoler();
            LayoutInflater inflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);
            holder.nameRe= (TextView) convertView.findViewById(R.id.txtNameRe);
            holder.imgRe=(ImageView) convertView.findViewById(R.id.imageView2);
            holder.timeRe= (TextView) convertView.findViewById(R.id.txtTime);
            holder.starRe= (TextView) convertView.findViewById(R.id.txtStar);
            holder.saveRe=(TextView) convertView.findViewById(R.id.txtSave);
            convertView.setTag(holder);

        }else{
            holder= (ViewHoler) convertView.getTag();
        }
        ABrief brief= BriefList.get(position);
        holder.nameRe.setText(brief.getNameRe());
        holder.imgRe.setImageResource(brief.getImgRe());
        holder.timeRe.setText(brief.getTimeRe());
        holder.starRe.setText(brief.getStar());
        holder.saveRe.setText(brief.getSave());
        return convertView;
    }
}
