package com.nibm.sos_app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileFragment extends Fragment {
    private ImageView profilePicture;
    private TextView tvName, tvEmail, tvPhone;
    private ProgressBar progressBar;
    private Button deleteButton;
    private FirebaseUser currentUser;
    private DatabaseReference databaseReference;
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize views
        profilePicture = view.findViewById(R.id.profile_picture);
        ImageView editIcon = view.findViewById(R.id.edit_icon);
        tvName = view.findViewById(R.id.tv_name);
        tvEmail = view.findViewById(R.id.tv_email);
        tvPhone = view.findViewById(R.id.tv_phone);
        progressBar = view.findViewById(R.id.progressBar);
        deleteButton = view.findViewById(R.id.deleteButton);

        // Get current user
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference("register1").child(userId);
            loadProfileData();
        }

        // Image picker setup
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                        Uri selectedImageUri = result.getData().getData();
                        profilePicture.setImageURI(selectedImageUri);
                    }
                }
        );

        editIcon.setOnClickListener(v -> openImagePicker());

        // Set delete button click listener
        deleteButton.setOnClickListener(v -> deleteUserProfile());

        return view;
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
    }

    private void loadProfileData() {
        progressBar.setVisibility(View.VISIBLE);
        databaseReference.get().addOnSuccessListener(snapshot -> {
            if (snapshot.exists()) {
                String fullName = snapshot.child("fullName").getValue(String.class);
                String email = snapshot.child("email").getValue(String.class);
                String phone = snapshot.child("phone").getValue(String.class);

                tvName.setText(fullName);
                tvEmail.setText(email);
                tvPhone.setText(phone);
            }
            progressBar.setVisibility(View.GONE);
        }).addOnFailureListener(e -> progressBar.setVisibility(View.GONE));
    }

    private void deleteUserProfile() {
        if (currentUser != null) {
            progressBar.setVisibility(View.VISIBLE);

            // Delete user data from Firebase Database
            databaseReference.removeValue().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Delete user from Firebase Authentication
                    currentUser.delete().addOnCompleteListener(authTask -> {
                        progressBar.setVisibility(View.GONE);
                        if (authTask.isSuccessful()) {
                            Toast.makeText(getActivity(), "Profile Deleted", Toast.LENGTH_SHORT).show();
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(getActivity(), LoginActivity.class));
                            getActivity().finish();
                        } else {
                            Toast.makeText(getActivity(), "Error: " + authTask.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Failed to delete profile data", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
