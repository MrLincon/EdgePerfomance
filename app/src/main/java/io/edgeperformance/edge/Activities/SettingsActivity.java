package io.edgeperformance.edge.Activities;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import io.edgeperformance.edge.Authentication.SignInActivity;
import io.edgeperformance.edge.Models.ThemeSettings;
import io.edgeperformance.edge.R;

public class SettingsActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private CardView dark_mode, rate, share, feedback, logout;
    private Switch dark_mode_switch;

    Dialog popup;

    GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;

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
        setContentView(R.layout.activity_settings);

        fab = findViewById(R.id.floating_action_button);
        dark_mode = findViewById(R.id.dark_mode);
        dark_mode_switch = findViewById(R.id.dark_mode_switch);
        rate = findViewById(R.id.rate);
        share = findViewById(R.id.share);
        feedback = findViewById(R.id.feedback);
        logout = findViewById(R.id.logout);

        popup = new Dialog(this);


        if (themeSettings.loadNightModeState() == false) {
            dark_mode_switch.setChecked(false);
        } else {
            dark_mode_switch.setChecked(true);
        }

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SettingsActivity.this,MainActivity.class);
                startActivity(i);
//                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                finish();
            }
        });

        dark_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (themeSettings.loadNightModeState() == false) {
                    themeSettings.setNightModeState(true);
                    restartApp();   //Recreate activity
                } else {
                    themeSettings.setNightModeState(false);
                    restartApp();   //Recreate activity
                }
            }
        });

        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=" + getPackageName())));
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("Comming soon..." + getPackageName())));
                }
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appPackageName = getPackageName();

                String link = "https://play.google.com/store/apps/details?id=" + appPackageName;
                ClipboardManager clipboard = (ClipboardManager) getSystemService(getApplicationContext().CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("", link);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(SettingsActivity.this, "Link copied to clipboard", Toast.LENGTH_SHORT).show();

            }
        });

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent feedback = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + "ahamedlincon.work@gmail.com"));

                try {
                    startActivity(Intent.createChooser(feedback, "Choose an e-mail client"));
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(SettingsActivity.this, "There is no e-mail clint installed!", Toast.LENGTH_SHORT).show();

                }
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser!=null){
                    revokeAccess();
                }else{
                    Intent signIn = new Intent(SettingsActivity.this, SignInActivity.class);
                    startActivity(signIn);
                    finish();
                }
            }
        });
    }

    private void revokeAccess() {
        FirebaseAuth.getInstance().signOut();

        mGoogleSignInClient.revokeAccess().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent intent = new Intent(SettingsActivity.this, SignInActivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "Logging out...", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(SettingsActivity.this,MainActivity.class);
        startActivity(i);
//        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        finish();
    }

    //Recreate activity

    private void restartApp() {
        Intent i = new Intent(SettingsActivity.this,SettingsActivity.class);
        startActivity(i);
        finish();
    }
}