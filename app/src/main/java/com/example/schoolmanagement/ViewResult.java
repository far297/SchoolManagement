package com.example.schoolmanagement;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class ViewResult extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private TableLayout resultsTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_result); // Ensure this layout is correct and contains the IDs used below

        // Initialize the Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("RESULT");
            toolbar.setTitleTextColor(Color.BLACK); // Set the title if necessary
        }

        // Initialize the DrawerLayout and NavigationView
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout_student);
        NavigationView navigationView = findViewById(R.id.navigation_view);

        // Set up the DrawerToggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Set NavigationItemSelectedListener
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.menu_dashboard) {
                Intent intent = new Intent(ViewResult.this, DashboardStudent.class);
                startActivity(intent);
            } else if (id == R.id.menu_logout) {
                Intent intent = new Intent(ViewResult.this, Login.class);
                startActivity(intent);
                finish();
            }

            // Close the navigation drawer
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        // Initialize the DatabaseHelper and TableLayout
        dbHelper = new DatabaseHelper(this);
        resultsTable = findViewById(R.id.resultsTable);

        // Fetch data from the database
        List<StudentResult> results = dbHelper.getAllResults();


        // Dynamically add rows to the table
        for (StudentResult result : results) {
            TableRow row = new TableRow(this);

            // Create TextViews for each column
            TextView subjectTextView = new TextView(this);
            subjectTextView.setText(result.getSubject());
            subjectTextView.setPadding(8, 8, 8, 8);
            subjectTextView.setGravity(Gravity.CENTER);

            TextView marksTextView = new TextView(this);
            marksTextView.setText(String.valueOf(result.getMarks()));
            marksTextView.setPadding(8, 8, 8, 8);
            marksTextView.setGravity(Gravity.CENTER);

            TextView gradeTextView = new TextView(this);
            gradeTextView.setText(result.getGrade());
            gradeTextView.setPadding(8, 8, 8, 8);
            gradeTextView.setGravity(Gravity.CENTER);

            // Add TextViews to the TableRow
            row.addView(subjectTextView);
            row.addView(marksTextView);
            row.addView(gradeTextView);

            // Add TableRow to TableLayout
            resultsTable.addView(row);
        }
    }
}
