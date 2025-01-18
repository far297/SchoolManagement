package com.example.schoolmanagement;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class DashboardTeacher extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

                setContentView(R.layout.dashboard_teacher); // Set the layout to teacher_dashboard.xml

                // Initialize views
        drawerLayout = findViewById(R.id.teacher_drawer_layout);
        navigationView = findViewById(R.id.nav_view_teacher);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.BLACK);
        // Set up toolbar

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Dashboard");


        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

                // Set up Navigation Drawer item clicks
        navigationView.setNavigationItemSelectedListener(item -> {

                    int itemId = item.getItemId();


                    if (itemId == R.id.menu_result_hub) {
                        // Navigate to ResultHubActivity
                        Intent resultHubIntent = new Intent(DashboardTeacher.this, ResultHub.class);
                        startActivity(resultHubIntent);
                    } else if (itemId == R.id.menu_edit_results) {
                        // Navigate to EditResultsActivity (for editing results)
                        Intent editResultsIntent = new Intent(DashboardTeacher.this, EditResult.class);
                        startActivity(editResultsIntent);
                    } else if (itemId == R.id.menu_logout) {
                        // Handle Logout
                        Toast.makeText(DashboardTeacher.this, "Logging out", Toast.LENGTH_SHORT).show();
                        Intent loginIntent = new Intent(DashboardTeacher.this, Login.class);
                        startActivity(loginIntent);
                        finish(); // Close the current activity after logging out
                    } else {
                        return false;
                    }

                drawerLayout.closeDrawer(GravityCompat.START);
        return true;  // Indicating the event was handled
    });
}

}
