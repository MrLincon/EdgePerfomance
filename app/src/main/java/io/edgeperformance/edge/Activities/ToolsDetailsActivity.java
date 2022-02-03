package io.edgeperformance.edge.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.edgeperformance.edge.BottomNavFragments.FragmentTools;
import io.edgeperformance.edge.R;

public class ToolsDetailsActivity extends AppCompatActivity {

    ImageView back;
    TextView name;

    String Name;
    ImageView Play, Stop;

    MediaPlayer mediaPlayer;
    public String audioUrl;
    TextView time;
    ProgressBar progressBar;

    FirebaseDatabase firebaseDatabase;
    DocumentReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools_details);

        name = findViewById(R.id.name);
        back = findViewById(R.id.back);
        Play = findViewById(R.id.play);
        Stop = findViewById(R.id.stop);
        time = findViewById(R.id.time);
        progressBar = findViewById(R.id.progress_bar);

        final Intent intent = getIntent();
        Name = intent.getStringExtra(FragmentTools.EXTRA_NAME);
        name.setText(Name);

        mediaPlayer = new MediaPlayer();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                if(mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                    mediaPlayer.release();
                    progressBar.setProgress(0);
                }
            }
        });


        Play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playAudio(audioUrl);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            progressBar.setProgress(mediaPlayer.getCurrentPosition());
                            handler.postDelayed(this, 1000);
                            Play.setVisibility(View.GONE);
                            Stop.setVisibility(View.VISIBLE);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, 0);

            }
        });

        Stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    Stop.setVisibility(View.GONE);
                    Play.setVisibility(View.VISIBLE);
                    progressBar.setProgress(0);

                    mediaPlayer.stop();
                    mediaPlayer.reset();
                    mediaPlayer.release();

                } else {

                }
            }
        });

        loadData();

    }

    private void loadData() {
        if (Name.equals("Session")) {
            session();
        } else if (Name.equals("Mindfulness")) {
            mindfulness();
        } else if (Name.equals("Breath Work")) {
            breathWork();
        }
    }

    private void session() {
        audioUrl = "https://firebasestorage.googleapis.com/v0/b/maxgreen-ecf42.appspot.com/o/images%2FWaka%20waka.mp3?alt=media&token=29858f10-a247-4c7c-93d6-8648412a3661";
    }

    private void mindfulness() {

    }

    private void breathWork() {

    }

    private void playAudio(String audioUrl) {

        try {
            mediaPlayer.setDataSource(audioUrl);

            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                    int duration = mediaPlayer.getDuration();
                    String Time = String.format("%02d min %02d sec",
                            TimeUnit.MILLISECONDS.toMinutes(duration),
                            TimeUnit.MILLISECONDS.toSeconds(duration) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
                    );
//                    time.setText("Duration: "+Time);
                    progressBar.setMax(duration);
                    progressBar.setProgress(mediaPlayer.getCurrentPosition());
                }
            });
            mediaPlayer.prepareAsync();

        } catch (IOException e) {
            Toast.makeText(this, "Error found is " + e, Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        if(mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            progressBar.setProgress(0);
        }

    }
}