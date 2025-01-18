package com.example.schoolmanagement;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
    public class StudentResult {

            private String studentName;
            private String className;
            private String subject;
            private int marks;
            private String grade;

            public StudentResult(String studentName, String className, String subject, int marks, String grade) {
                this.studentName = studentName;
                this.className = className;
                this.subject = subject;
                this.marks = marks;
                this.grade = grade;
            }

        public String getStudentName() {
                return studentName;
            }

            public void setStudentName(String studentName) {
                this.studentName = studentName;
            }

            public String getClassName() {
                return className;
            }

            public void setClassName(String className) {
                this.className = className;
            }

            public String getSubject() {
                return subject;
            }

            public void setSubject(String subject) {
                this.subject = subject;
            }

            public int getMarks() {
                return marks;
            }

            public void setMarks(int marks) {
                this.marks = marks;
            }

            public String getGrade() {
                return grade;
            }

            public void setGrade(String grade) {
                this.grade = grade;
            }
        }
