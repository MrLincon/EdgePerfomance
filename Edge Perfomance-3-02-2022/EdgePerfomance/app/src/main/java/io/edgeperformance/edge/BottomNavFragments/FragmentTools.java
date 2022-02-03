package io.edgeperformance.edge.BottomNavFragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import io.edgeperformance.edge.Activities.AudioListActivity;
import io.edgeperformance.edge.Activities.PlayerActivity;
import io.edgeperformance.edge.R;
import io.edgeperformance.edge.utils.AppConstants;


public class FragmentTools extends Fragment {

    View view;
    CardView simplebreath, simple, breathework, threesec, stress;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tools, container, false);

        clickEvents();
        return view;
    }

    private void clickEvents() {
        view.findViewById(R.id.Simple).setOnClickListener(v -> {
            openAudioList(AppConstants.SESSION_KEY);
        });
        view.findViewById(R.id.breath_work).setOnClickListener(v -> {
            openAudioList(AppConstants.BREATH_WORK_KEY);

        });
        view.findViewById(R.id.stress).setOnClickListener(v -> {
            openAudioList(AppConstants.MINDFULNESS_KEY);
        });
    }

    private void openAudioList(String value) {
        Intent intent = new Intent(getActivity(), AudioListActivity.class);
        intent.putExtra(AppConstants.KEY_TAG, value);
        startActivity(intent);
    }

}
