package io.edgeperformance.edge.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.edgeperformance.edge.R;

public class GoalsActivity extends AppCompatActivity {

    ImageView back;
    TextView defQ, defDes, defET, prgQ, prgDes, prgET, resQ,
            resDes, resET, barQ, barDes, actQ, actDes, actEt;
    EditText barET1, barET2;

    TextView defAns, prgAns, resAns, barAns, actAns;

    CardView defSave, prgSave, resSave, barSave, actSave;


    private String userID;
    FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private DocumentReference document_reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);

        back = findViewById(R.id.back);

        defQ = findViewById(R.id.def_que);
        defDes = findViewById(R.id.def_des);
        defET = findViewById(R.id.definition_et);
        defSave = findViewById(R.id.def_save);
        prgQ = findViewById(R.id.prog_que);
        prgDes = findViewById(R.id.prog_des);
        prgET = findViewById(R.id.progress_et);
        prgSave = findViewById(R.id.prog_save);
        resQ = findViewById(R.id.reasons_que);
        resDes = findViewById(R.id.reasons_des);
        resET = findViewById(R.id.reasons_et);
        resSave = findViewById(R.id.res_save);
        barQ = findViewById(R.id.barrier_que);
        barDes = findViewById(R.id.barrier_des);
        barET1 = findViewById(R.id.barrier_et1);
        barET2 = findViewById(R.id.barrier_et2);
        barSave = findViewById(R.id.bar_save);
        actQ = findViewById(R.id.actions_que);
        actDes = findViewById(R.id.actions_des);
        actEt = findViewById(R.id.actions_et);
        actSave = findViewById(R.id.act_save);

        defAns = findViewById(R.id.def_ans);
        prgAns = findViewById(R.id.prog_ans);
        resAns = findViewById(R.id.res_ans);
        barAns = findViewById(R.id.bar_ans);
        actAns = findViewById(R.id.act_ans);

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getUid();
        db = FirebaseFirestore.getInstance();

        document_reference = db.collection("Goals").document(userID);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        defSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDefinition();
            }
        });

        prgSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getProgress();
            }
        });

        resSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getReasons();
            }
        });

        barSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBarriers();
            }
        });

        actSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActions();
            }
        });

        getData();

    }


    private void getDefinition() {
        String Ans = defET.getText().toString();

        if (!Ans.isEmpty()) {
            document_reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    if (documentSnapshot.exists()) {
                        Map<String, Object> userMap = new HashMap<>();

                        userMap.put("what", Ans);
                        userMap.put("user_id", userID);
                        userMap.put("timestamp", FieldValue.serverTimestamp());
                        document_reference.update(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getApplicationContext(), "Adding..", Toast.LENGTH_SHORT).show();
                                Intent saved = new Intent(getApplicationContext(), GoalsActivity.class);
                                startActivity(saved);
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        Map<String, Object> userMap = new HashMap<>();

                        userMap.put("what", Ans);
                        userMap.put("user_id", userID);
                        userMap.put("timestamp", FieldValue.serverTimestamp());
                        document_reference.set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getApplicationContext(), "Adding..", Toast.LENGTH_SHORT).show();
                                Intent saved = new Intent(getApplicationContext(), GoalsActivity.class);
                                startActivity(saved);
                                finish();
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
        } else {
            Toast.makeText(getApplicationContext(), "You have to enter your answer!", Toast.LENGTH_SHORT).show();
        }
    }

    private void getProgress() {
        String Ans = prgET.getText().toString();

        if (!Ans.isEmpty()) {
            document_reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    if (documentSnapshot.exists()) {
                        Map<String, Object> userMap = new HashMap<>();

                        userMap.put("metric", Ans);
                        userMap.put("user_id", userID);
                        userMap.put("timestamp", FieldValue.serverTimestamp());
                        document_reference.update(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getApplicationContext(), "Adding..", Toast.LENGTH_SHORT).show();
                                Intent saved = new Intent(getApplicationContext(), GoalsActivity.class);
                                startActivity(saved);
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        Map<String, Object> userMap = new HashMap<>();

                        userMap.put("metric", Ans);
                        userMap.put("user_id", userID);
                        userMap.put("timestamp", FieldValue.serverTimestamp());
                        document_reference.set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getApplicationContext(), "Adding..", Toast.LENGTH_SHORT).show();
                                Intent saved = new Intent(getApplicationContext(), GoalsActivity.class);
                                startActivity(saved);
                                finish();
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
        } else {
            Toast.makeText(getApplicationContext(), "You have to enter your answer!", Toast.LENGTH_SHORT).show();
        }
    }

    private void getReasons() {

        String Ans = resET.getText().toString();

        if (!Ans.isEmpty()) {


            db.collection("Goals").document(userID)
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    DocumentSnapshot document = task.getResult();

                    List<String> answer = (List<String>) document.get("why");
                    if (answer != null) {
                        int size = answer.size();
                        if (size > 2) {
                            Toast.makeText(getApplicationContext(), "You have reached the maximum number of answers!", Toast.LENGTH_SHORT).show();
                            resET.setText("");
                        } else {

                            document_reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {

                                    if (documentSnapshot.exists()) {
                                        Map<String, Object> userMap = new HashMap<>();

                                        userMap.put("why", FieldValue.arrayUnion(Ans));
                                        userMap.put("user_id", userID);
                                        userMap.put("timestamp", FieldValue.serverTimestamp());
                                        document_reference.update(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(getApplicationContext(), "Adding..", Toast.LENGTH_SHORT).show();
                                                Intent saved = new Intent(getApplicationContext(), GoalsActivity.class);
                                                startActivity(saved);
                                                finish();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    } else {
                                        Map<String, Object> userMap = new HashMap<>();

                                        userMap.put("why", Arrays.asList(Ans));
                                        userMap.put("user_id", userID);
                                        userMap.put("timestamp", FieldValue.serverTimestamp());
                                        document_reference.set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(getApplicationContext(), "Adding..", Toast.LENGTH_SHORT).show();
                                                Intent saved = new Intent(getApplicationContext(), GoalsActivity.class);
                                                startActivity(saved);
                                                finish();
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


                        }
                    } else {


                        document_reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                if (documentSnapshot.exists()) {
                                    Map<String, Object> userMap = new HashMap<>();

                                    userMap.put("why", FieldValue.arrayUnion(Ans));
                                    userMap.put("user_id", userID);
                                    userMap.put("timestamp", FieldValue.serverTimestamp());
                                    document_reference.update(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(getApplicationContext(), "Adding..", Toast.LENGTH_SHORT).show();
                                            Intent saved = new Intent(getApplicationContext(), GoalsActivity.class);
                                            startActivity(saved);
                                            finish();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    });
                                } else {
                                    Map<String, Object> userMap = new HashMap<>();

                                    userMap.put("why", Arrays.asList(Ans));
                                    userMap.put("user_id", userID);
                                    userMap.put("timestamp", FieldValue.serverTimestamp());
                                    document_reference.set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(getApplicationContext(), "Adding..", Toast.LENGTH_SHORT).show();
                                            Intent saved = new Intent(getApplicationContext(), GoalsActivity.class);
                                            startActivity(saved);
                                            finish();
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


                    }
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "You have to enter your answer!", Toast.LENGTH_SHORT).show();
        }


    }

    private void getBarriers() {
        String answer1 = barET1.getText().toString();
        String answer2 = barET2.getText().toString();
        String Ans = "If " + answer1 + ", I will " + answer2;

        if (!answer1.isEmpty() && !answer2.isEmpty()) {

            db.collection("Goals").document(userID)
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    DocumentSnapshot document = task.getResult();

                    List<String> answer = (List<String>) document.get("barriers");
                    if (answer != null) {
                        int size = answer.size();
                        if (size > 2) {
                            Toast.makeText(getApplicationContext(), "You have reached the maximum number of answers!", Toast.LENGTH_SHORT).show();
                            barET1.setText("");
                            barET2.setText("");
                        } else {
                            document_reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {

                                    if (documentSnapshot.exists()) {
                                        Map<String, Object> userMap = new HashMap<>();

                                        userMap.put("barriers", FieldValue.arrayUnion(Ans));
                                        userMap.put("user_id", userID);
                                        userMap.put("timestamp", FieldValue.serverTimestamp());
                                        document_reference.update(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(getApplicationContext(), "Adding..", Toast.LENGTH_SHORT).show();
                                                Intent saved = new Intent(getApplicationContext(), GoalsActivity.class);
                                                startActivity(saved);
                                                finish();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    } else {
                                        Map<String, Object> userMap = new HashMap<>();

                                        userMap.put("barriers", Arrays.asList(Ans));
                                        userMap.put("user_id", userID);
                                        userMap.put("timestamp", FieldValue.serverTimestamp());
                                        document_reference.set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(getApplicationContext(), "Adding..", Toast.LENGTH_SHORT).show();
                                                Intent saved = new Intent(getApplicationContext(), GoalsActivity.class);
                                                startActivity(saved);
                                                finish();
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

                        }
                    } else {
                        document_reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                if (documentSnapshot.exists()) {
                                    Map<String, Object> userMap = new HashMap<>();

                                    userMap.put("barriers", FieldValue.arrayUnion(Ans));
                                    userMap.put("user_id", userID);
                                    userMap.put("timestamp", FieldValue.serverTimestamp());
                                    document_reference.update(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(getApplicationContext(), "Adding..", Toast.LENGTH_SHORT).show();
                                            Intent saved = new Intent(getApplicationContext(), GoalsActivity.class);
                                            startActivity(saved);
                                            finish();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    });
                                } else {
                                    Map<String, Object> userMap = new HashMap<>();

                                    userMap.put("barriers", Arrays.asList(Ans));
                                    userMap.put("user_id", userID);
                                    userMap.put("timestamp", FieldValue.serverTimestamp());
                                    document_reference.set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(getApplicationContext(), "Adding..", Toast.LENGTH_SHORT).show();
                                            Intent saved = new Intent(getApplicationContext(), GoalsActivity.class);
                                            startActivity(saved);
                                            finish();
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

                    }
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "You have to enter your answer!", Toast.LENGTH_SHORT).show();
        }


    }

    private void getActions() {
        String Ans = actEt.getText().toString();

        if (!Ans.isEmpty()) {

            db.collection("Goals").document(userID)
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    DocumentSnapshot document = task.getResult();

                    List<String> answer = (List<String>) document.get("how");
                    if (answer != null) {
                        int size = answer.size();
                        if (size > 4) {
                            Toast.makeText(getApplicationContext(), "You have reached the maximum number of answers!", Toast.LENGTH_SHORT).show();
                            actEt.setText("");
                        } else {

                            document_reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {

                                    if (documentSnapshot.exists()) {
                                        Map<String, Object> userMap = new HashMap<>();

                                        userMap.put("how", FieldValue.arrayUnion(Ans));
                                        userMap.put("user_id", userID);
                                        userMap.put("timestamp", FieldValue.serverTimestamp());
                                        document_reference.update(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(getApplicationContext(), "Adding..", Toast.LENGTH_SHORT).show();
                                                Intent saved = new Intent(getApplicationContext(), GoalsActivity.class);
                                                startActivity(saved);
                                                finish();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    } else {
                                        Map<String, Object> userMap = new HashMap<>();

                                        userMap.put("how", Arrays.asList(Ans));
                                        userMap.put("user_id", userID);
                                        userMap.put("timestamp", FieldValue.serverTimestamp());
                                        document_reference.set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(getApplicationContext(), "Adding..", Toast.LENGTH_SHORT).show();
                                                Intent saved = new Intent(getApplicationContext(), GoalsActivity.class);
                                                startActivity(saved);
                                                finish();
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
                        }
                    } else {
                        document_reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                if (documentSnapshot.exists()) {
                                    Map<String, Object> userMap = new HashMap<>();

                                    userMap.put("how", FieldValue.arrayUnion(Ans));
                                    userMap.put("user_id", userID);
                                    userMap.put("timestamp", FieldValue.serverTimestamp());
                                    document_reference.update(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(getApplicationContext(), "Adding..", Toast.LENGTH_SHORT).show();
                                            Intent saved = new Intent(getApplicationContext(), GoalsActivity.class);
                                            startActivity(saved);
                                            finish();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    });
                                } else {
                                    Map<String, Object> userMap = new HashMap<>();

                                    userMap.put("how", Arrays.asList(Ans));
                                    userMap.put("user_id", userID);
                                    userMap.put("timestamp", FieldValue.serverTimestamp());
                                    document_reference.set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(getApplicationContext(), "Adding..", Toast.LENGTH_SHORT).show();
                                            Intent saved = new Intent(getApplicationContext(), GoalsActivity.class);
                                            startActivity(saved);
                                            finish();
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


                    }
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "You have to enter your answer!", Toast.LENGTH_SHORT).show();
        }
    }


    private void getData() {

        //Definition
        db.collection("Questions").document("Definition")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();

                String question = String.valueOf(document.get("Question"));
                String description = String.valueOf(document.get("Description"));

                defQ.setText(question);
                defDes.setText(description);

            }
        });

        db.collection("Goals").document(userID)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();

                String answer = String.valueOf(document.get("what"));

                if (answer.equals("null")) {
                    defAns.setText("There are no answers...");
                } else {
                    defAns.setText(answer);
                }

            }
        });
//..........................


        //Progress
        db.collection("Questions").document("Progress")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();

                String question = String.valueOf(document.get("Question"));
                String description = String.valueOf(document.get("Description"));

                prgQ.setText(question);
                prgDes.setText(description);
            }
        });

        db.collection("Goals").document(userID)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();

                String answer = String.valueOf(document.get("metric"));

                if (answer.equals("null")) {
                    prgAns.setText("There are no answers...");
                } else {
                    prgAns.setText(answer);
                }

            }
        });
//..........................

        //Reasons
        db.collection("Questions").document("Reasons")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();

                String question = String.valueOf(document.get("Question"));
                String description = String.valueOf(document.get("Description"));

                resQ.setText(question);
                resDes.setText(description);
            }
        });

        db.collection("Goals").document(userID)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();

                List<String> answer = (List<String>) document.get("why");

                if (answer != null) {
                    List<String> answers = (List<String>) document.get("why");
                    String Answers = "";
                    for (int i = 0; i < answers.size(); i++) {
                        Answers = answers.get(i);
                        resAns.append((i + 1) + ". " + Answers + "\n");
                    }
                } else {
                    resAns.setText("There are no answers...");
                }

            }
        });
//..........................

        //Barriers
        db.collection("Questions").document("Barrier")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();

                String question = String.valueOf(document.get("Question"));
                String description = String.valueOf(document.get("Description"));

                barQ.setText(question);
                barDes.setText(description);
            }
        });

        db.collection("Goals").document(userID)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();

                List<String> answer = (List<String>) document.get("barriers");


                if (answer == null) {
                    barAns.setText("There are no answers...");
                } else {
                    List<String> answers = (List<String>) document.get("barriers");
                    String Answers = "";
                    for (int i = 0; i < answers.size(); i++) {
                        Answers = answers.get(i);
                        barAns.append((i + 1) + ". " + Answers + "\n");
                    }
                }

            }
        });
//..........................


        //Actions
        db.collection("Questions").document("Actions")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();

                String question = String.valueOf(document.get("Question"));
                String description = String.valueOf(document.get("Description"));

                actQ.setText(question);
                actDes.setText(description);
            }
        });

        db.collection("Goals").document(userID)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();

                List<String> answer = (List<String>) document.get("how");


                if (answer == null) {
                    actAns.setText("There are no answers...");
                } else {
                    List<String> answers = (List<String>) document.get("how");
                    String Answers = "";
                    for (int i = 0; i < answers.size(); i++) {
                        Answers = answers.get(i);
                        actAns.append((i + 1) + ". " + Answers + "\n");
                    }
                }

            }
        });
//..........................
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}