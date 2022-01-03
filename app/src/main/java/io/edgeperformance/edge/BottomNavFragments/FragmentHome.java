package io.edgeperformance.edge.BottomNavFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import io.edgeperformance.edge.Models.ViewPagerAdapter;
import io.edgeperformance.edge.R;


public class FragmentHome extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        tabLayout = view.findViewById(R.id.tabLayoutID);
        viewPager = view.findViewById(R.id.viewPager);

        tabLayout.setupWithViewPager(viewPager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(0, true);

        return view;
    }

}
