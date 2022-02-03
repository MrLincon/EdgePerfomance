package io.edgeperformance.edge.BottomNavFragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import io.edgeperformance.edge.Activities.ToolsDetailsActivity;
import io.edgeperformance.edge.R;


public class FragmentTools extends Fragment {

    View view;

    CardView Session, Mindfulness, BreathWork;

    public static final String EXTRA_NAME = "io.edgeperformance.edge.EXTRA_NAME";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tools, container, false);

        Session = view.findViewById(R.id.session);
        Mindfulness = view.findViewById(R.id.mindfulness);
        BreathWork = view.findViewById(R.id.breath_work);


        Session.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent details = new Intent(getActivity(), ToolsDetailsActivity.class);
                details.putExtra(EXTRA_NAME, "Session");

                startActivity(details);
            }
        });

        Mindfulness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent details = new Intent(getActivity(), ToolsDetailsActivity.class);
                details.putExtra(EXTRA_NAME, "Mindfulness");

                startActivity(details);
            }
        });

        BreathWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent details = new Intent(getActivity(), ToolsDetailsActivity.class);
                details.putExtra(EXTRA_NAME, "Breath Work");

                startActivity(details);
            }
        });



        return view;
    }

}
