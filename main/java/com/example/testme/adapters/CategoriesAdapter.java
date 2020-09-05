package com.example.testme.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.testme.R;
import com.example.testme.SetsActivity;
import com.example.testme.models.CategoriesModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {
     private List<CategoriesModel> categoriesModelList;

    public CategoriesAdapter(List<CategoriesModel> categoriesModelList) {
        this.categoriesModelList = categoriesModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.categories_item , parent ,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(categoriesModelList.get(position).getUrl() , categoriesModelList.get(position).getName() ,position);

    }

    @Override
    public int getItemCount() {
        return categoriesModelList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private CircleImageView circleIV;
        private TextView titles;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            circleIV = itemView.findViewById(R.id.categories_iv);
            titles = itemView.findViewById(R.id.categories_title);
        }
        private void setData(String url , final String title , final int position){
            Glide.with(itemView.getContext()).load(url).into(circleIV);
            titles.setText(title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent setsIntent = new Intent(itemView.getContext() , SetsActivity.class);
                    setsIntent.putExtra("title" , title);
                    setsIntent.putExtra("position" , position);
                    itemView.getContext().startActivity(setsIntent);
                }
            });
        }
    }
}
