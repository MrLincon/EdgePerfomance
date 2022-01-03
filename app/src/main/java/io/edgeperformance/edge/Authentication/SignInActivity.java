package io.edgeperformance.edge.Authentication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import io.edgeperformance.edge.Activities.MainActivity;
import io.edgeperformance.edge.Models.ThemeSettings;
import io.edgeperformance.edge.R;

public class SignInActivity extends AppCompatActivity {

    private TextView tv_sign_up;

    //Google Auth
    private CardView signInButton;
    private CardView signInProgress;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 1;
    private FirebaseFirestore db;
    private DocumentReference document_ref;
    private String userID;

    //Email Auth
    TextInputEditText email_field, password_field;
    TextInputLayout email_layout, password_layout;
    CardView login;
    LinearLayout linearLayout;
    ProgressBar progressBar;
    TextView sign_up, forgot_password;

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
        setContentView(R.layout.activity_signin);

        tv_sign_up = findViewById(R.id.textView_signUp);

        //Google
        signInButton = findViewById(R.id.sign_in_button);
        signInProgress = findViewById(R.id.sign_in_progress);

        //Email
        email_layout = findViewById(R.id.email_layout);
        password_layout = findViewById(R.id.password_layout);

        email_field = findViewById(R.id.userEmail);
        password_field = findViewById(R.id.userPassword);
        login = findViewById(R.id.btn_login);
        progressBar = findViewById(R.id.login_progressbar);

        linearLayout = findViewById(R.id.linearLayout);

        sign_up = findViewById(R.id.textView_signUp);
        forgot_password = findViewById(R.id.forgot_password);


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        signInProgress.setVisibility(View.INVISIBLE);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = email_field.getText().toString().trim();
                String password = password_field.getText().toString().trim();

                if (email.isEmpty()) {
                    email_layout.setError("E-mail is required");
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    email_layout.setError("Please enter a valid email");
                    return;
                }

                if (password.isEmpty()) {
                    password_layout.setError("Password is required");
                    return;
                }

                if (password.length() < 6) {
                    password_layout.setError("Minimum length of password should be 6");
                    return;
                } else {
                    login.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                updateUI(mAuth.getCurrentUser());
                            } else {
                                login.setVisibility(View.VISIBLE);
                                linearLayout.setVisibility(View.VISIBLE);
                                Toast.makeText(SignInActivity.this, "Authentication Failed!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });


        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent forgot_password = new Intent(SignInActivity.this, PasswordRecoveryActivity.class);
                startActivity(forgot_password);
            }
        });


        tv_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sign_up = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(sign_up);
                finish();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Log.w("TAG", "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        signInProgress.setVisibility(View.VISIBLE);
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            signInProgress.setVisibility(View.VISIBLE);
                            userID = mAuth.getUid();

                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            String email = currentUser.getEmail();

                            document_ref = db.collection("UserDetails").document(userID);


                            document_ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {

                                    Map<String, Object> userMap = new HashMap<>();

                                    userMap.put("email", email);
                                    userMap.put("usedID", userID);

                                    if (documentSnapshot.exists()) {

                                        document_ref.update(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        });

                                    } else {
                                        document_ref.set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            signInProgress.setVisibility(View.INVISIBLE);
                            Toast.makeText(SignInActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent mainActivity = new Intent(SignInActivity.this, MainActivity.class);
        startActivity(mainActivity);
        finish();
    }

    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
}