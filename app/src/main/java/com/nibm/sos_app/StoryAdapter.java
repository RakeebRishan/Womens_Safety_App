package com.nibm.sos_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class StoryAdapter extends BaseAdapter {
    private Context context;
    private List<Story> storyList;

    public StoryAdapter(Context context, List<Story> storyList) {
        this.context = context;
        this.storyList = storyList;
    }

    @Override
    public int getCount() {
        return storyList.size();
    }

    @Override
    public Object getItem(int position) {
        return storyList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_story, parent, false);
        }

        TextView titleTextView = convertView.findViewById(R.id.story_title);
        TextView descriptionTextView = convertView.findViewById(R.id.story_description);

        Story story = storyList.get(position);
        titleTextView.setText(story.getTitle());
        descriptionTextView.setText(story.getDescription());

        return convertView;
    }
}
