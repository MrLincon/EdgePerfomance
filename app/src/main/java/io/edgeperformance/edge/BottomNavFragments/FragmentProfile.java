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

import io.edgeperformance.edge.Activities.GoalsActivity;
import io.edgeperformance.edge.Activities.ValuesActivity;
import io.edgeperformance.edge.R;


public class FragmentProfile extends Fragment {

    CardView values, goals;

    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);


        values = view.findViewById(R.id.values);
        goals = view.findViewById(R.id.goals);

        values.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent values = new Intent(getActivity(), ValuesActivity.class);
                startActivity(values);
            }
        });

        goals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goals = new Intent(getActivity(), GoalsActivity.class);
                startActivity(goals);
            }
        });

        return view;
    }

}
