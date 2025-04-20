package com.nibm.sos_app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.AppCompatImageButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private List<Story> storyList;
    private StoryAdapter adapter;
    private ListView listView;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("stories");

        // Initialize the ListView and Adapter
        listView = view.findViewById(R.id.list_community_stories);
        storyList = new ArrayList<>();
        adapter = new StoryAdapter(getActivity(), storyList);
        listView.setAdapter(adapter);

        // Set up Add Story button
        AppCompatImageButton addStoryButton = view.findViewById(R.id.add_story_button);
        addStoryButton.setOnClickListener(v -> {
            AddStoryDialogFragment dialogFragment = new AddStoryDialogFragment();
            dialogFragment.setTargetFragment(HomeFragment.this, 0);
            dialogFragment.show(getFragmentManager(), "add_story");
        });

        // Load stories from Firebase
        loadStoriesFromFirebase();

        return view;
    }

    // Method to add a new story
    public void addStory(Story newStory) {
        storyList.add(newStory);
        adapter.notifyDataSetChanged();
    }

    // Load all stories from Firebase and display them in the ListView
    private void loadStoriesFromFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                storyList.clear(); // Clear current list
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Story story = snapshot.getValue(Story.class);
                    storyList.add(story);
                }
                adapter.notifyDataSetChanged(); // Update the ListView
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error if needed
            }
        });
    }
}
