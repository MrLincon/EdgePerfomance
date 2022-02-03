package io.edgeperformance.edge.Authentication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import io.edgeperformance.edge.Activities.MainActivity;
import io.edgeperformance.edge.R;

public class SignUpActivity extends AppCompatActivity {

    TextView tv_sign_in;
    CardView sign_up;
    LinearLayout linearLayout;
    TextInputLayout layout_email, layout_password1, layout_password2;
    TextInputEditText et_email, et_password1, et_password2;
    ProgressBar progressBar;

    //Firebase
    private FirebaseAuth mAuth;
    private String userID;

    private FirebaseFirestore db;
    private DocumentReference document_ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        layout_email = findViewById(R.id.email_layout);
        layout_password1 = findViewById(R.id.password_layout1);
        layout_password2 = findViewById(R.id.password_layout2);

        tv_sign_in = findViewById(R.id.textView_signIn);
        sign_up = findViewById(R.id.btn_login);


        et_email = findViewById(R.id.userEmail);
        et_password1 = findViewById(R.id.userPassword1);
        et_password2 = findViewById(R.id.userPassword2);

        linearLayout = findViewById(R.id.linearLayout);
        progressBar = findViewById(R.id.progressBar);

        sign_up.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        tv_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sign_in = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(sign_in);
                finish();
            }
        });

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });
    }

    private void getData() {
        //For closing keyboard if open

        ConstraintLayout mainLayout;
        mainLayout = findViewById(R.id.main_layout);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);
//..........................


        final String email = et_email.getText().toString().trim();
        final String password1 = et_password1.getText().toString().trim();
        final String password2 = et_password2.getText().toString().trim();

        if (email.isEmpty()) {
            layout_email.setError("E-mail is required");
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            layout_email.setError("Please enter a valid email");
            return;
        }

        if (password1.isEmpty()) {
            layout_password1.setError("Password is required");
            return;
        }

        if (password1.length() < 6) {
            layout_password1.setError("Minimum length of password should be 6");
            return;
        }

        if (password2.isEmpty()) {
            layout_password2.setError("Password is required");
            return;
        }

        if (password2.length() < 6) {
            layout_password2.setError("Minimum length of password should be 6");
            return;
        }
        else {
            if (password1.equals(password2)){
                sign_up.setVisibility(View.GONE);
                linearLayout.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);

                mAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        boolean check = !task.getResult().getSignInMethods().isEmpty();

                        if (!check) {

                            mAuth.createUserWithEmailAndPassword(email, password1)
                                    .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                sign_up.setVisibility(View.VISIBLE);
                                                linearLayout.setVisibility(View.VISIBLE);
                                                progressBar.setVisibility(View.GONE);

                                                userID = mAuth.getUid();

                                                document_ref = db.collection("UserDetails").document(userID);

                                                Map<String, String> userMap = new HashMap<>();

                                                userMap.put("email", email);
                                                userMap.put("usedID", userID);

                                                document_ref.set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                                    }
                                                });

                                                Intent sign_in = new Intent(SignUpActivity.this, MainActivity.class);
                                                startActivity(sign_in);
                                                finish();
                                            }
                                        }
                                    });

                        } else {
                            Toast.makeText(SignUpActivity.this, "E-mail already exists!", Toast.LENGTH_SHORT).show();
                            sign_up.setVisibility(View.VISIBLE);
                            linearLayout.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }else{
                et_password2.setText("");
                Toast.makeText(getApplicationContext(), "Password does not match!", Toast.LENGTH_SHORT).show();

            }


        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent sign_in = new Intent(SignUpActivity.this, SignInActivity.class);
        startActivity(sign_in);
    }
}