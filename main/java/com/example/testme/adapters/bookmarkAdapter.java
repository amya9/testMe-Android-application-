package com.example.testme.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testme.R;
import com.example.testme.models.questionsModel;

import java.util.List;

public class bookmarkAdapter extends RecyclerView.Adapter<bookmarkAdapter.viewholder> {
    private List<questionsModel> list;


    public bookmarkAdapter(List<questionsModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.boobkmark_item , parent , false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        holder.setData(list.get(position).getQuestion() , list.get(position).getCorrectAnswer() , position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class viewholder extends RecyclerView.ViewHolder{
        private TextView questionView,answerView;
        private ImageButton deleteBtn;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            questionView = itemView.findViewById(R.id.question_tv);
            answerView = itemView.findViewById(R.id.answer_tv);
            deleteBtn = itemView.findViewById(R.id.bookmark_delete);
        }
        private void setData(String question , String answer , final int position){
            this.questionView.setText(question);
            this.answerView.setText(answer);

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    list.remove(position);
                    notifyItemRemoved(position);
                }
            });
        }
    }

}
