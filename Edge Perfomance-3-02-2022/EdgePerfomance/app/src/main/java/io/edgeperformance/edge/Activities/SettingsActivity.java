package io.edgeperformance.edge.Activities;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.edgeperformance.edge.Authentication.SignInActivity;
import io.edgeperformance.edge.Models.ThemeSettings;
import io.edgeperformance.edge.R;

public class SettingsActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private CardView dark_mode, rate, share, feedback, logout;
    private Switch dark_mode_switch;
    TextView name;
    ImageView editName;

    Dialog popup;

    GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private String userID;
    private FirebaseFirestore db;
    private DocumentReference document_reference;

    ThemeSettings themeSettings;

    public SettingsActivity() {
    }

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
        name = findViewById(R.id.name);
        editName = findViewById(R.id.edit_name);
        dark_mode = findViewById(R.id.dark_mode);
        dark_mode_switch = findViewById(R.id.dark_mode_switch);

        logout = findViewById(R.id.logout);

        popup = new Dialog(this);


        if (themeSettings.loadNightModeState() == false) {
            dark_mode_switch.setChecked(false);
        } else {
            dark_mode_switch.setChecked(true);
        }

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getUid();
        db = FirebaseFirestore.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //Retrieving UserName
        loadData();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SettingsActivity.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        editName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.setContentView(R.layout.popup_add_name);
                popup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                EditText name = popup.findViewById(R.id.name);
                CardView save = popup.findViewById(R.id.save);

                db.collection("UserName").document(userID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if (documentSnapshot.exists()) {

                            String Name = documentSnapshot.getString("name");
                            name.setText(Name);

                        } else {
                            Toast.makeText(getApplicationContext(), "No Username found!", Toast.LENGTH_LONG).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String Name = name.getText().toString();

                        document_reference = db.collection("UserName").document(userID);

                        if (!Name.isEmpty()){

                            Map<String, Object> userMap = new HashMap<>();

                            userMap.put("name", Name);
                            userMap.put("timestamp", FieldValue.serverTimestamp());
                            document_reference.set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(getApplicationContext(), "Adding...", Toast.LENGTH_SHORT).show();
                                    popup.dismiss();
                                    Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                        }else {
                            Toast.makeText(getApplicationContext(), "You must enter your name!", Toast.LENGTH_SHORT).show();
                        }



                    }
                });
                popup.show();

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

    private void loadData() {
        db.collection("UserName").document(userID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.exists()) {

                    String Name = documentSnapshot.getString("name");
                    name.setText(Name);

                } else {
                    Toast.makeText(getApplicationContext(), "No Username found!", Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

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