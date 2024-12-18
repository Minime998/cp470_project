package com.example.camlingo;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import model.GlobalUserCache;

import model.LessonModel;

public class LessonsRecyclerViewAdapter extends RecyclerView.Adapter<LessonsRecyclerViewAdapter.MyViewHolder> {
    Context context;
    ArrayList<LessonModel> lessonModels;

    public LessonsRecyclerViewAdapter(Context context, ArrayList<LessonModel> lessonModels) {
        this.context = context;
        this.lessonModels = lessonModels;
    }

    @NonNull
    @Override
    public LessonsRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.lessons_row_layout, parent, false); // Fix this to reference a layout, not an ID
        return new LessonsRecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LessonsRecyclerViewAdapter.MyViewHolder holder, int position) {
        LessonModel lesson = lessonModels.get(position);
        holder.lessonName.setText(lessonModels.get(position).getLessonName());
        String lessonNumText = "Lesson " + lessonModels.get(position).getLessonNumber();
        holder.lessonNum.setText(lessonNumText);
        Glide.with(context).load(lessonModels.get(position).getLessonMediaUrl())
                .into(holder.lessonImage);
        holder.continueLearningBtn.setOnClickListener(view -> {
            Intent intent = new Intent(context, LessonItemsRecyclerViewActivity.class);
            intent.putExtra("lessonCollection",lesson.getLessonCollectionName());
            intent.putExtra("documentName", lesson.getLessonDocumentName());
            intent.putExtra("lessonName", lesson.getLessonName());
            intent.putExtra("lessCollection",lesson.getLessonCollectionName());
            context.startActivity(intent);
        });

        Integer currentProgress = GlobalUserCache.getCurrentUser().getProgress().getOrDefault(lesson.getLessonName(),0);
        String progressPercent = currentProgress + " %";
        holder.lessonProgressText.setText(progressPercent);
        holder.lessonProgressBar.setProgress(currentProgress.intValue());

    }

    @Override
    public int getItemCount() {
        return lessonModels.size(); // Return the size of the lessonModels list
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView lessonNum;
        TextView lessonName;
        TextView lessonProgressText;
        ImageView lessonImage;
        Button continueLearningBtn;
        ProgressBar lessonProgressBar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            lessonNum = itemView.findViewById(R.id.lesson_num);
            lessonName = itemView.findViewById(R.id.lesson_type);
            lessonProgressText = itemView.findViewById(R.id.lesson_progress);
            lessonImage = itemView.findViewById(R.id.lesson_icon);
            continueLearningBtn = itemView.findViewById(R.id.continue_learning_btn);
            lessonProgressBar = itemView.findViewById(R.id.lesson_progress_bar);
        }
    }
}
