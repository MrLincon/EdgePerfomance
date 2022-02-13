package io.edgeperformance.edge.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.edgeperformance.edge.R;

public class ValuesActivity extends AppCompatActivity {

    FloatingActionButton fab;
    CardView save;
    ChipGroup categories;
    String Category;

    Chip abundance, achievement, adventure, ambition, assertiveness, autonomy, authenticity,
            awe, adjustment, balance, challenge, citizenship, compassion, competency, community,
            collaboration, commitment, connection, consistency, choice, competition, composure, contribution, courage, creativity ;

    private String userID;
    FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private DocumentReference document_reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_values);

        fab = findViewById(R.id.floating_action_button);
        save = findViewById(R.id.save);
        categories = findViewById(R.id.categories);

        abundance = findViewById(R.id.abundance);
        achievement = findViewById(R.id.achievement);
        adventure = findViewById(R.id.adventure);
        ambition = findViewById(R.id.ambition);
        assertiveness = findViewById(R.id.assertiveness);
        autonomy = findViewById(R.id.autonomy);
        authenticity = findViewById(R.id.authenticity);
        awe = findViewById(R.id.awe);
        adjustment = findViewById(R.id.adjustment);
        balance = findViewById(R.id.balance);
        challenge = findViewById(R.id.challenge);
        citizenship = findViewById(R.id.citizenship);
        compassion = findViewById(R.id.compassion);
        competency = findViewById(R.id.competency);
        community = findViewById(R.id.community);
        collaboration = findViewById(R.id.collaboration);
        commitment = findViewById(R.id.commitment);
        connection = findViewById(R.id.connection);
        consistency = findViewById(R.id.consistency);
        choice = findViewById(R.id.choice);
        competition = findViewById(R.id.competition);
        composure = findViewById(R.id.composure);
        contribution = findViewById(R.id.contribution);
        courage = findViewById(R.id.courage);
        creativity = findViewById(R.id.creativity);


        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getUid();
        db = FirebaseFirestore.getInstance();

        document_reference = db.collection("Values").document(userID);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getData();


        CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Chip chip = findViewById(buttonView.getId());
                if (isChecked) {
                    //Get all checked chips in the group
                    List<Integer> ids = categories.getCheckedChipIds();
                    if (ids.size() > 5) {
                        chip.setChecked(false);  //force to unchecked the chip
                        Toast.makeText(getApplicationContext(), "Maximum number of values have been selected!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };

        abundance.setOnCheckedChangeListener(checkedChangeListener);
        achievement.setOnCheckedChangeListener(checkedChangeListener);
        adventure.setOnCheckedChangeListener(checkedChangeListener);
        ambition.setOnCheckedChangeListener(checkedChangeListener);
        assertiveness.setOnCheckedChangeListener(checkedChangeListener);
        autonomy.setOnCheckedChangeListener(checkedChangeListener);
        authenticity.setOnCheckedChangeListener(checkedChangeListener);
        awe.setOnCheckedChangeListener(checkedChangeListener);
        adjustment.setOnCheckedChangeListener(checkedChangeListener);
        balance.setOnCheckedChangeListener(checkedChangeListener);
        challenge.setOnCheckedChangeListener(checkedChangeListener);
        citizenship.setOnCheckedChangeListener(checkedChangeListener);
        compassion.setOnCheckedChangeListener(checkedChangeListener);
        competency.setOnCheckedChangeListener(checkedChangeListener);
        community.setOnCheckedChangeListener(checkedChangeListener);
        collaboration.setOnCheckedChangeListener(checkedChangeListener);
        commitment.setOnCheckedChangeListener(checkedChangeListener);
        connection.setOnCheckedChangeListener(checkedChangeListener);
        consistency.setOnCheckedChangeListener(checkedChangeListener);
        choice.setOnCheckedChangeListener(checkedChangeListener);
        competition.setOnCheckedChangeListener(checkedChangeListener);
        composure.setOnCheckedChangeListener(checkedChangeListener);
        contribution.setOnCheckedChangeListener(checkedChangeListener);
        courage.setOnCheckedChangeListener(checkedChangeListener);
        creativity.setOnCheckedChangeListener(checkedChangeListener);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> list = new ArrayList<String>();
                for (int i=0; i<categories.getChildCount();i++){
                    Chip chip = (Chip)categories.getChildAt(i);
                    if (chip.isChecked()){
                        String chipText = String.valueOf(chip.getText());
                        list.add(chipText);
                    }
                }
                if (!list.isEmpty()) {
                    Map<String, Object> userMap = new HashMap<>();

                    userMap.put("values", list);
                    userMap.put("user_id", userID);
                    userMap.put("timestamp", FieldValue.serverTimestamp());
                    document_reference.set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getApplicationContext(), "Adding..", Toast.LENGTH_SHORT).show();
                            Intent saved = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(saved);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "You must select a value!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void getData() {
        db.collection("Values").document(userID)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();

                List<String> categories = (List<String>) document.get("values");
                if (categories != null) {
                    String Values = "";
                    for (int i = 0; i < categories.size(); i++) {
                        Values = categories.get(i);

                        switch (Values){
                            case "Abundance":
                                abundance.setChecked(true);
                                break;
                            case "Achievement":
                                achievement.setChecked(true);
                                break;
                            case "Adventure":
                                adventure.setChecked(true);
                                break;
                            case "Ambition":
                                ambition.setChecked(true);
                                break;
                            case "Assertiveness":
                                assertiveness.setChecked(true);
                                break;
                            case "Autonomy":
                                autonomy.setChecked(true);
                                break;
                            case "Authenticity":
                                authenticity.setChecked(true);
                                break;
                            case "Awe":
                                awe.setChecked(true);
                                break;
                            case "Adjustment":
                                adjustment.setChecked(true);
                                break;
                            case "Balance":
                                balance.setChecked(true);
                                break;
                            case "Challenge":
                                challenge.setChecked(true);
                                break;
                            case "Citizenship":
                                citizenship.setChecked(true);
                                break;
                            case "Compassion":
                                compassion.setChecked(true);
                                break;
                            case "Competency":
                                competency.setChecked(true);
                                break;
                            case "Community":
                                community.setChecked(true);
                                break;
                            case "Collaboration":
                                collaboration.setChecked(true);
                                break;
                            case "Commitment":
                                commitment.setChecked(true);
                                break;
                            case "Connection":
                                connection.setChecked(true);
                                break;
                            case "Consistency":
                                consistency.setChecked(true);
                                break;
                            case "Choice":
                                choice.setChecked(true);
                                break;
                            case "Competition":
                                competition.setChecked(true);
                                break;
                            case "Composure":
                                composure.setChecked(true);
                                break;
                            case "Contribution":
                                contribution.setChecked(true);
                                break;
                            case "Courage":
                                courage.setChecked(true);
                                break;
                            case "Creativity":
                                creativity.setChecked(true);
                                break;
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}