package com.example.schoolmanagement;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;

public class DashboardStudent extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_student); // Set the layout to student_dashboard.xml

        // Initialize views
        drawerLayout = findViewById(R.id.student_drawer_layout);
        navigationView = findViewById(R.id.nav_view_student);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.BLACK);
        // Set up toolbar

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Dashboard");


        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        // Handle Navigation Item Clicks
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.menu_view_result) {
                Intent intent = new Intent(DashboardStudent.this, ViewResult.class);
                startActivity(intent);
            } else if (id == R.id.menu_logout) {
                Intent intent = new Intent(DashboardStudent.this, Login.class);
                startActivity(intent);
                finish();
            } else {
                return false;  // If no matching case
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;  // Indicating the event was handled
        });
    }

}