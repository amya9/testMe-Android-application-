package com.example.testme.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.testme.QuestionsActivity;
import com.example.testme.R;

import java.util.List;

public class setsGridAdapter extends BaseAdapter {
    public List<String> sets;
    private String category;

    public setsGridAdapter(List<String> sets , String category) {
        this.sets = sets;
        this.category = category;
    }

    @Override
    public int getCount() {
        return sets.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final View view ;
        if(convertView == null){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sets_item , parent ,false);
        }else {
            view = convertView;
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent questionIntent = new Intent(parent.getContext() , QuestionsActivity.class);
                questionIntent.putExtra("category" , category);
                questionIntent.putExtra("setId" , sets.get(position));
                parent.getContext().startActivity(questionIntent);
            }
        });
        ((TextView)view.findViewById(R.id.sets_number)).setText(String.valueOf(position+1));

        return view;
    }
}
