package com.nibm.sos_app;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.fragment.app.DialogFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddStoryDialogFragment extends DialogFragment {
    private EditText titleEditText;
    private EditText descriptionEditText;
    private Button submitButton;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize Firebase database
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("stories"); // "stories" node
    }

    @Override
    public AlertDialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_story, null);

        titleEditText = view.findViewById(R.id.title_edit_text);
        descriptionEditText = view.findViewById(R.id.description_edit_text);
        submitButton = view.findViewById(R.id.submit_button);

        submitButton.setOnClickListener(v -> {
            String title = titleEditText.getText().toString();
            String description = descriptionEditText.getText().toString();

            if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(description)) {
                // Create new story object
                Story newStory = new Story(title, description);

                // Save story to Firebase under "stories" node
                String storyId = databaseReference.push().getKey();  // Create a unique key
                databaseReference.child(storyId).setValue(newStory); // Save it

                // Pass the new story back to HomeFragment
                ((HomeFragment) getTargetFragment()).addStory(newStory);

                dismiss(); // Close the dialog
            }
        });

        // Create the dialog using AlertDialog.Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view)
                .setCancelable(true); // Optional: Make dialog cancellable by touch outside

        return builder.create(); // Return the dialog
    }
}
