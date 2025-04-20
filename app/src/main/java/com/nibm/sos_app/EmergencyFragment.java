package com.nibm.sos_app;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EmergencyFragment extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private String emergencyContact;
    private static final String FIXED_EMERGENCY_NUMBER = "0763660265"; // Hardcoded emergency call number

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_emergency, container, false);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("register1");

        // Find SOS button
        Button sosButton = view.findViewById(R.id.btn_sos);
        sosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check for SMS and CALL permissions
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.SEND_SMS, Manifest.permission.CALL_PHONE}, 1);
                } else {
                    sendSOSMessage(); // Send message and call if permissions are granted
                }
            }
        });

        // Fetch emergency contact from Firebase
        fetchEmergencyContact();

        return view;
    }

    // Fetch emergency contact from Firebase
    private void fetchEmergencyContact() {
        String userId = mAuth.getCurrentUser().getUid();
        databaseReference.child(userId).child("emergencyMobile").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    emergencyContact = snapshot.getValue(String.class);
                } else {
                    emergencyContact = null; // No emergency contact found
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Error fetching emergency contact!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Send SOS message to emergency contact and call 0773252312
    private void sendSOSMessage() {
        if (emergencyContact != null && !emergencyContact.isEmpty()) {
            try {
                String message = "🚨 SOS! I need help! Please assist me.";
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(emergencyContact, null, message, null, null);
                Toast.makeText(getActivity(), "SOS Message Sent to " + emergencyContact, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(getActivity(), "Error sending SOS message!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), "No emergency contact found in Firebase!", Toast.LENGTH_SHORT).show();
        }

        // Always make a call to 0773252312
        makeEmergencyCall(FIXED_EMERGENCY_NUMBER);
    }

    // Make a call to the fixed emergency number (0773252312)
    private void makeEmergencyCall(String phoneNumber) {
        try {
            Toast.makeText(getActivity(), "Calling: " + phoneNumber, Toast.LENGTH_LONG).show();
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + phoneNumber));
            startActivity(callIntent);
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error making emergency call!", Toast.LENGTH_SHORT).show();
        }
    }

    // Handle permission requests
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) { // SMS & Call Permission
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                sendSOSMessage(); // Retry sending SOS
            } else {
                Toast.makeText(getActivity(), "Permissions denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
