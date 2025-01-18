package com.example.schoolmanagement;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.LinkedHashMap;
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
import java.util.Map;

public class ResultHub extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;
    private Spinner spinnerRemoveStudent;
    private Spinner spinnerSelectClass;
    private Button btnDelete;
    private TableLayout tableLayoutResults;

    private List<StudentResult> studentResults;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_hub);


        // Initialize views
        drawerLayout = findViewById(R.id.drawer_layout_teacher);
        navigationView = findViewById(R.id.nav_view_teacher);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.BLACK);
        // Set up toolbar

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("RESULT HUB");


        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Set up Navigation Drawer item clicks
        navigationView.setNavigationItemSelectedListener(item -> {

            int itemId = item.getItemId();
            if (itemId == R.id.menu_dashboard_teacher) {
                // Navigate to EditResultsActivity (for editing results)
                Intent editResultsIntent = new Intent(ResultHub.this, DashboardTeacher.class);
                startActivity(editResultsIntent);
            }else if (itemId == R.id.menu_edit_results) {
                // Navigate to EditResultsActivity (for editing results)
                Intent editResultsIntent = new Intent(ResultHub.this, EditResult.class);
                startActivity(editResultsIntent);
            } else if (itemId == R.id.menu_logout) {
                // Handle Logout
                Toast.makeText(ResultHub.this, "Logging out", Toast.LENGTH_SHORT).show();
                Intent loginIntent = new Intent(ResultHub.this, Login.class);
                startActivity(loginIntent);
                finish(); // Close the current activity after logging out
            } else {
                return false;
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        spinnerSelectClass = findViewById(R.id.spinnerClass);
        tableLayoutResults = findViewById(R.id.tableLayoutResults);
        spinnerRemoveStudent = findViewById(R.id.spinnerRemoveStudent);
        btnDelete = findViewById(R.id.btnDelete);

        dbHelper = new DatabaseHelper(this);
        studentResults = dbHelper.getAllResults();

        // Populate spinners and display data
        populateClassSpinner();
        populateRemoveStudentSpinner();
        displayResultsInTable(studentResults);

        setupEventListeners();


        handleIncomingData();
    }

    private void setupEventListeners() {
        // Filter results by selected class
        spinnerSelectClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedClass = parent.getItemAtPosition(position).toString();
                if (!selectedClass.equals("No Classes Available")) {
                    List<StudentResult> filteredResults = dbHelper.getResultsByClass(selectedClass);
                    displayResultsInTable(filteredResults);
                } else {
                    tableLayoutResults.removeAllViews();
                    Toast.makeText(ResultHub.this, "No results to display!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Remove student on button click
        btnDelete.setOnClickListener(v -> {
            String selectedStudent = (String) spinnerRemoveStudent.getSelectedItem();
            if (selectedStudent != null && !selectedStudent.equals("No Students Available")) {
                dbHelper.deleteResult(selectedStudent);
                studentResults = dbHelper.getAllResults();
                displayResultsInTable(studentResults);
                populateRemoveStudentSpinner();
                Toast.makeText(ResultHub.this, "Student Removed Successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ResultHub.this, "No student selected to remove!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleIncomingData() {
        Intent intent = getIntent();
        String student = intent.getStringExtra("student");
        String subject = intent.getStringExtra("subject");
        String marks = intent.getStringExtra("marks");
        String grade = intent.getStringExtra("grade");
        String className = intent.getStringExtra("className");

        if (student != null && subject != null && marks != null && grade != null) {
            addOrUpdateStudentResult(student, subject, Integer.parseInt(marks), grade, className);
        }
    }

    private void populateClassSpinner() {
        List<String> classNames = dbHelper.getAllClasses ();
        if (classNames.isEmpty()) {
            classNames.add("No Classes Available");
        }
        ArrayAdapter<String> adapterClasses = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, classNames);
        spinnerSelectClass.setAdapter(adapterClasses);
    }

    private void populateRemoveStudentSpinner() {
        List<String> studentNames = new ArrayList<>();
        for (StudentResult result : studentResults) {
            studentNames.add(result.getStudentName());
        }
        if (studentNames.isEmpty()) {
            studentNames.add("No Students Available");
        }
        ArrayAdapter<String> adapterStudents = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, studentNames);
        spinnerRemoveStudent.setAdapter(adapterStudents);
    }

    private void displayResultsInTable(List<StudentResult> results) {
        tableLayoutResults.removeAllViews();

        // Create and add table header
        TableRow headerRow = new TableRow(this);
        headerRow.addView(createHeaderCell("Student Name"));
        headerRow.addView(createHeaderCell("Subject"));
        headerRow.addView(createHeaderCell("Mark"));
        headerRow.addView(createHeaderCell("Grade"));
        tableLayoutResults.addView(headerRow);

        // Add rows for each result
        // Group data by student name
        Map<String, List<StudentResult>> groupedResults = new LinkedHashMap<>();
        for (StudentResult result : results) {
            groupedResults.computeIfAbsent(result.getStudentName(), k -> new ArrayList<>()).add(result);
        }

        // Render grouped results
        for (Map.Entry<String, List<StudentResult>> entry : groupedResults.entrySet()) {
            String studentName = entry.getKey();
            List<StudentResult> studentResults = entry.getValue();

            for (int i = 0; i < studentResults.size(); i++) {
                TableRow row = new TableRow(this);

                if (i == 0) {
                    // Show student name only once
                    TextView nameCell = createDataCell(studentName);
                    nameCell.setPadding(8, 8, 8, 8);
                    nameCell.setBackgroundColor(getResources().getColor(android.R.color.white));
                    row.addView(nameCell);
                } else {
                    row.addView(new TextView(this)); // Empty cell for subsequent rows
                }

                // Add subject, marks, and grade
                StudentResult result = studentResults.get(i);
                TextView subjectCell = createDataCell(result.getSubject());
                TextView marksCell = createDataCell(String.valueOf(result.getMarks()));
                TextView gradeCell = createDataCell(result.getGrade());

                row.addView(subjectCell);
                row.addView(marksCell);
                row.addView(gradeCell);

                tableLayoutResults.addView(row);
            }
        }
    }

    private void addOrUpdateStudentResult(String student, String subject, int marks, String grade, String className) {
        StudentResult newResult = new StudentResult(student, className, subject, marks, grade);
        dbHelper.insertOrUpdateResult(newResult); // Insert or update result
        studentResults = dbHelper.getAllResults(); // Refresh data
        displayResultsInTable(studentResults);
        populateRemoveStudentSpinner();
    }

    private TextView createHeaderCell(String text) {
        TextView headerCell = new TextView(this);
        headerCell.setText(text);
        headerCell.setPadding(8, 8, 8, 8);
        headerCell.setTextSize(16);
        headerCell.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        return headerCell;
    }

    private TextView createDataCell(String text) {
        TextView dataCell = new TextView(this);
        dataCell.setText(text);
        dataCell.setPadding(8, 8, 8, 8);
        return dataCell;
    }
}