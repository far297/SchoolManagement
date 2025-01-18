package com.example.schoolmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Register extends AppCompatActivity {
        private DatabaseHelper databaseHelper;
        private EditText editTextName, editTextEmail, editTextPassword;
        private Button buttonRole, buttonSignUp;
        private String selectedRole = ""; // Store selected role

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.register); // Set the layout to signup.xml

            // Initialize views
            editTextName = findViewById(R.id.editTextName);
            editTextEmail = findViewById(R.id.editTextEmail);
            editTextPassword = findViewById(R.id.editTextPassword);
            buttonRole = findViewById(R.id.buttonRole); // Button for selecting role
            buttonSignUp = findViewById(R.id.buttonSignUp);

            // Initialize DatabaseHelper
            databaseHelper = new DatabaseHelper(this);

            // Set onClickListener for the Role button to show the dropdown menu
            buttonRole.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Create a PopupMenu for selecting the role
                    PopupMenu popupMenu = new PopupMenu(Register.this, buttonRole);
                    popupMenu.getMenuInflater().inflate(R.menu.drawer_role, popupMenu.getMenu()); // Inflate the role menu

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            selectedRole = item.getTitle().toString(); // Get selected role
                            buttonRole.setText(selectedRole); // Update button text with selected role
                            return true;
                        }
                    });

                    popupMenu.show(); // Show the popup menu
                }
            });

            // Set onClickListener for the Sign-Up button
            buttonSignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Retrieve user inputs
                    String name = editTextName.getText().toString().trim();
                    String email = editTextEmail.getText().toString().trim();
                    String password = editTextPassword.getText().toString().trim();

                    // Validate inputs
                    if (name.isEmpty()) {
                        editTextName.setError("Full Name is required");
                        editTextName.requestFocus();
                        return;
                    }

                    if (email.isEmpty()) {
                        editTextEmail.setError("Email is required");
                        editTextEmail.requestFocus();
                        return;
                    }

                    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        editTextEmail.setError("Enter a valid email address");
                        editTextEmail.requestFocus();
                        return;
                    }

                    if (password.isEmpty()) {
                        editTextPassword.setError("Password is required");
                        editTextPassword.requestFocus();
                        return;
                    }

                    if (password.length() < 8) {
                        editTextPassword.setError("Password must be at least 8 characters");
                        editTextPassword.requestFocus();
                        return;
                    }

                    if (selectedRole.isEmpty()) {
                        Toast.makeText(Register.this, "Please select a role", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Check if email is already in use
                    if (databaseHelper.isEmailRegistered(email)) {
                        Toast.makeText(Register.this, "Email is already registered", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Disable button to prevent multiple clicks
                    buttonSignUp.setEnabled(false);
                    buttonSignUp.setText("Signing Up...");

                    // Insert user into the database
                    boolean isInserted = databaseHelper.insertUser(name, email, password, selectedRole);

                    if (isInserted) {
                        // Sign-up successful, show a message and proceed
                        Toast.makeText(Register.this, "Sign-Up Successful!", Toast.LENGTH_LONG).show();

                        // Navigate to LogIn activity
                        Intent intent = new Intent(Register.this, Login.class);
                        startActivity(intent);
                        finish(); // Close the SignUp activity
                    } else {
                        // If insert fails
                        Toast.makeText(Register.this, "Sign-Up Failed! Please try again.", Toast.LENGTH_SHORT).show();
                        buttonSignUp.setEnabled(true);
                        buttonSignUp.setText("Sign Up");
                    }
                }
            });
        }
    }
