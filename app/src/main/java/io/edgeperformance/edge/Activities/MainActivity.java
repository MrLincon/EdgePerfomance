package io.edgeperformance.edge.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import io.edgeperformance.edge.BottomNavFragments.FragmentHome;
import io.edgeperformance.edge.BottomNavFragments.FragmentProfile;
import io.edgeperformance.edge.BottomNavFragments.FragmentTools;
import io.edgeperformance.edge.Models.ThemeSettings;
import io.edgeperformance.edge.R;

public class MainActivity extends AppCompatActivity {

    private ChipNavigationBar bottomNav;
    private ImageView settings, notification;

    ThemeSettings themeSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Theme Settings
        themeSettings = new ThemeSettings(this);
        if (themeSettings.loadNightModeState() == false) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {

            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        //...............
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settings = findViewById(R.id.settings);
        notification = findViewById(R.id.notification);
        bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setItemSelected(R.id.nav_home,true);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new FragmentHome()).commit();
        }

        bottomNav.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment selectedFragment = null;
                switch (i){
                    case R.id.nav_home:
                        selectedFragment = new FragmentHome();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                selectedFragment).commit();
                        break;
                    case R.id.nav_tools:
                        selectedFragment = new FragmentTools();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                selectedFragment).commit();
                        break;
                    case R.id.nav_profile:
                        selectedFragment = new FragmentProfile();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                selectedFragment).commit();
                        break;
                }
            }
        });


        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Notification clicked!", Toast.LENGTH_SHORT).show();
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settings = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(settings);
                finish();
            }
        });
    }
}