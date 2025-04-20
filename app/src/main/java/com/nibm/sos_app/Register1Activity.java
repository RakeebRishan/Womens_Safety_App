package com.nibm.sos_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register1Activity extends AppCompatActivity {

    private EditText fullNameEditText, emailEditText, passwordEditText, mobileEditText, emergencyNameEditText, emergencyMobileEditText;
    private Button nextButton;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register1);

        // Initialize views
        fullNameEditText = findViewById(R.id.editText_full_name);
        emailEditText = findViewById(R.id.editText_email);
        passwordEditText = findViewById(R.id.editText_password);
        mobileEditText = findViewById(R.id.editText_mobile);
        emergencyNameEditText = findViewById(R.id.editText_emergency_name);
        emergencyMobileEditText = findViewById(R.id.editText_emergency_mobile);
        nextButton = findViewById(R.id.button_login);
        progressBar = findViewById(R.id.progressBar);

        // Firebase initialization
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        nextButton.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String fullName = fullNameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String mobile = mobileEditText.getText().toString().trim();
        String emergencyName = emergencyNameEditText.getText().toString().trim();
        String emergencyMobile = emergencyMobileEditText.getText().toString().trim();

        // Basic validations
        if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || mobile.isEmpty()
                || emergencyName.isEmpty() || emergencyMobile.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        // Firebase registration
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);

                    if (task.isSuccessful()) {
                        String userId = mAuth.getCurrentUser().getUid();
                        User user = new User(fullName, email, mobile, emergencyName, emergencyMobile);

                        mDatabase.child("register1").child(userId).setValue(user)
                                .addOnCompleteListener(dbTask -> {
                                    if (dbTask.isSuccessful()) {
                                        Toast.makeText(Register1Activity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(Register1Activity.this, LoginActivity.class));
                                        finish();
                                    } else {
                                        Toast.makeText(Register1Activity.this, "Failed to save data: " + dbTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthUserCollisionException e) {
                            Toast.makeText(this, "Email already in use", Toast.LENGTH_SHORT).show();
                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(this, "Registration failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // Firebase user data model
    public static class User {
        public String fullName, email, mobile, emergencyName, emergencyMobile;

        public User() {
            // Required empty constructor for Firebase
        }

        public User(String fullName, String email, String mobile, String emergencyName, String emergencyMobile) {
            this.fullName = fullName;
            this.email = email;
            this.mobile = mobile;
            this.emergencyName = emergencyName;
            this.emergencyMobile = emergencyMobile;
        }
    }
}
