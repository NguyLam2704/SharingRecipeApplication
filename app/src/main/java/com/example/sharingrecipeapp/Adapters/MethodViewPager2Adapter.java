package com.example.sharingrecipeapp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sharingrecipeapp.Classes.Method;
import com.example.sharingrecipeapp.R;

import java.util.List;

public class MethodViewPager2Adapter extends RecyclerView.Adapter<MethodViewPager2Adapter.MethodViewHolder>{
    private List<Method> mListMethod;

    public MethodViewPager2Adapter(List<Method> mListMethod) {
        this.mListMethod = mListMethod;
    }

    @NonNull
    @Override
    public MethodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_method, parent, false);
        return new MethodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MethodViewHolder holder, int position) {
        Method method = mListMethod.get(position);
        if (method == null){
            return;
        }

        holder.text.setText(method.getStep());
        holder.buoc.setText("Bước "+(position+1)+"/"+(mListMethod.size())+": ");
    }

    @Override
    public int getItemCount() {
        if(mListMethod != null){
            return mListMethod.size();
        }
        return 0;
    }

    public class MethodViewHolder extends RecyclerView.ViewHolder {

        TextView text, buoc;

        public MethodViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text_method);
            buoc = itemView.findViewById(R.id.buoc);
        }
    }
}
