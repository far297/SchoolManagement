package com.example.schoolmanagement;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
    public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ResultViewHolder> {

        private List<StudentResult> studentResults;

        // Constructor
        public ResultAdapter(List<StudentResult> studentResults) {
            this.studentResults = studentResults;
        }

        @Override
        public ResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.student_result, parent, false);
            return new ResultViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ResultViewHolder holder, int position) {
            StudentResult result = studentResults.get(position);
            holder.studentName.setText(result.getStudentName());
            holder.subject.setText(result.getSubject());
            holder.mark.setText(result.getMarks());
            holder.grade.setText(result.getGrade());
        }

        @Override
        public int getItemCount() {
            return studentResults.size();
        }

        public static class ResultViewHolder extends RecyclerView.ViewHolder {
            public TextView studentName, subject, mark, grade;

            public ResultViewHolder(View itemView) {
                super(itemView);
                studentName = itemView.findViewById(R.id.textViewStudentName);
                subject = itemView.findViewById(R.id.textViewSubject);
                mark = itemView.findViewById(R.id.textViewMark);
                grade = itemView.findViewById(R.id.textViewGrade);
            }
        }
    }


