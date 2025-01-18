package com.example.schoolmanagement;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {
                private DatabaseHelper databaseHelper;
                private EditText emailEditText, passwordEditText;
                private ImageButton showPasswordButton;
                private Button loginButton;
                private RadioGroup roleRadioGroup;
                private TextView signUpTextView;
                private boolean isPasswordVisible = false;

                @Override
                protected void onCreate(Bundle savedInstanceState) {
                    super.onCreate(savedInstanceState);
                    setContentView(R.layout.login);

                    // Initialize DatabaseHelper
                    databaseHelper = new DatabaseHelper(this);

                    // Initialize UI components
                    emailEditText = findViewById(R.id.emailEditText);
                    passwordEditText = findViewById(R.id.passwordEditText);
                    showPasswordButton = findViewById(R.id.showPasswordButton);
                    loginButton = findViewById(R.id.loginButton);
                    roleRadioGroup = findViewById(R.id.roleRadioGroup);
                    signUpTextView = findViewById(R.id.signUpTextView);

                    // Disable login button initially
                    loginButton.setEnabled(false);

                    // Listen for role selection to enable login button
                    roleRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            // Enable login button when a role is selected
                            loginButton.setEnabled(checkedId != -1);
                        }
                    });

                    // Toggle password visibility
                    showPasswordButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (isPasswordVisible) {
                                passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                                showPasswordButton.setImageResource(android.R.drawable.ic_menu_view);
                                isPasswordVisible = false;
                            } else {
                                passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                                showPasswordButton.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
                                isPasswordVisible = true;
                            }
                        }
                    });

                    // Handle login button click
                    loginButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String email = emailEditText.getText().toString().trim();
                            String password = passwordEditText.getText().toString().trim();
                            int selectedRoleId = roleRadioGroup.getCheckedRadioButtonId();

                            // Validate inputs
                            if (email.isEmpty()) {
                                Toast.makeText(Login.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            if (password.isEmpty()) {
                                Toast.makeText(Login.this, "Please enter your password", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            if (selectedRoleId == -1) {
                                Toast.makeText(Login.this, "Please select whether you're a Teacher or Student", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            RadioButton selectedRole = findViewById(selectedRoleId);
                            String role = selectedRole.getText().toString();

                            // Check if the user exists and the credentials match using the DatabaseHelper
                            if (databaseHelper.checkLogin(email, password)) {
                                // Login successful, save login data in SharedPreferences
                                SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("email", email);
                                editor.putString("role", role);
                                editor.apply();

                                // Navigate to the corresponding dashboard based on role
                                Intent intent;
                                if (role.equals("Student")) {
                                    // Navigate to the Student Dashboard
                                    intent = new Intent(Login.this, DashboardStudent.class);
                                } else if (role.equals("Teacher")) {
                                    // Navigate to the Teacher Dashboard
                                    intent = new Intent(Login.this, DashboardTeacher.class);
                                } else {
                                    return; // If no role is selected
                                }
                                startActivity(intent);
                                finish();  // Close the LogIn activity so the user can't go back
                            } else {
                                // Invalid credentials, show error message
                                Toast.makeText(Login.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    // Navigate to SignUp activity
                    signUpTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Login.this, Register.class);
                            startActivity(intent);
                        }
                    });
                }


            }
