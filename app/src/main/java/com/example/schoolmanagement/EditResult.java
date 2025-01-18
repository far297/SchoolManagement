package com.example.schoolmanagement;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Arrays;
import java.util.List;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.List;
public class EditResult extends AppCompatActivity {
        Spinner  spinnerClass, spinnerSubjects, spinnerGrade;
        Button btnReset, btnUpdate;
        DatabaseHelper dbHelper;
        EditText editTextStudentName, editTextClass, editTextSubject, editTextMarks, editTextGrade;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.edit_result);


            // Initialize views
            drawerLayout = findViewById(R.id.drawer_layout_teacher);
            navigationView = findViewById(R.id.nav_view_teacher);
            toolbar = findViewById(R.id.toolbar);
            toolbar.setTitleTextColor(Color.BLACK);
            // Set up toolbar

            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("UPDATE RESULT");


            toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();

            // Set up Navigation Drawer item clicks
            navigationView.setNavigationItemSelectedListener(item -> {

                int itemId = item.getItemId();

                if (itemId == R.id.menu_edit_results) {
                    // Navigate to EditResultsActivity (for editing results)
                    Intent editResultsIntent = new Intent(EditResult.this, DashboardStudent.class);
                    startActivity(editResultsIntent);
                } else
                if (itemId == R.id.menu_result_hub) {
                    // Navigate to ResultHubActivity
                    Intent resultHubIntent = new Intent(EditResult.this, ResultHub.class);
                    startActivity(resultHubIntent);
                } else if (itemId == R.id.menu_logout) {
                    // Handle Logout
                    Toast.makeText(EditResult.this, "Logging out", Toast.LENGTH_SHORT).show();
                    Intent loginIntent = new Intent(EditResult.this, Login.class);
                    startActivity(loginIntent);
                    finish(); // Close the current activity after logging out
                } else {
                    return false;
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;  // Indicating the event was handled
            });
            // Initialize Views
            editTextStudentName = findViewById(R.id.editTextStudentName);
            spinnerClass = findViewById(R.id.spinnerClass);
            spinnerSubjects = findViewById(R.id.spinnerSubjects);
            editTextMarks = findViewById(R.id.editTextMarks);
            spinnerGrade = findViewById(R.id.spinnerGrade);
            btnReset = findViewById(R.id.btnReset);
            btnUpdate = findViewById(R.id.btnUpdate);

            dbHelper = new DatabaseHelper(this);

            // Sample data for spinners
            List<String> classList = Arrays.asList("DELIMA", "INOVATIF", "BIJAK", "JUJUR", "CEKAL");
            List<String> subjectList = Arrays.asList("MATHEMATICS", "SCIENCE", "ENGLISH", "BAHASA MELAYU");
            List<String> gradeList = Arrays.asList("A", "B", "C", "D", "E", "F");

// Set up adapters for the spinners
            ArrayAdapter<String> classAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, classList);
            classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerClass.setAdapter(classAdapter);

            ArrayAdapter<String> subjectAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, subjectList);
            subjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerSubjects.setAdapter(subjectAdapter);

            ArrayAdapter<String> gradeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, gradeList);
            gradeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerGrade.setAdapter(gradeAdapter);

            // Reset button functionality
            btnReset.setOnClickListener(v -> {
                editTextStudentName.setText("");
                editTextClass.setText("");
                editTextSubject.setText("");
                editTextMarks.setText("");
                editTextGrade.setText("");
                Toast.makeText(this, "Fields Reset", Toast.LENGTH_SHORT).show();
            });

            // Update button functionality
            btnUpdate.setOnClickListener(v -> {
                String studentName = editTextStudentName.getText().toString().trim();
                String className = spinnerClass.getSelectedItem().toString();
                String subject = spinnerSubjects.getSelectedItem().toString();
                String marksText = editTextMarks.getText().toString().trim();
                String grade = spinnerGrade.getSelectedItem().toString();

                // Validate inputs
                if (studentName.isEmpty() || marksText.isEmpty() || grade.isEmpty()) {
                    Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        int marks = Integer.parseInt(marksText);
                        saveResultToDatabase(studentName, subject, marks, grade, className);
                        navigateToResultHub(studentName, subject, marksText, grade, className);
                    } catch (NumberFormatException e) {
                        Toast.makeText(this, "Marks must be a valid number", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        // Save data to the database
        private void saveResultToDatabase(String studentName, String subject, int marks, String grade, String className) {
            StudentResult result = new StudentResult(studentName, className, subject, marks, grade); // Include class name
            dbHelper.insertOrUpdateResult(result);

            Toast.makeText(this, "Result Saved Successfully", Toast.LENGTH_SHORT).show();
        }

        // Navigate to ResultHub activity and pass data
        private void navigateToResultHub(String student, String subject, String marks, String grade, String className) {
            Intent intent = new Intent(EditResult.this, ResultHub.class);
            intent.putExtra("student", student);
            intent.putExtra("subject", subject);
            intent.putExtra("marks", marks);
            intent.putExtra("grade", grade);
            intent.putExtra("className", className);  // Pass class name
            startActivity(intent);
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.drawer_menu_teacher, menu);
            return true;
        }

    }
