package io.edgeperformance.edge.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;

import java.io.IOException;

import io.edgeperformance.edge.Models.AudioModel;
import io.edgeperformance.edge.R;
import io.edgeperformance.edge.utils.AppConstants;
import io.edgeperformance.edge.utils.AppUtils;

public class PlayerActivity extends AppCompatActivity {

    Activity activity = PlayerActivity.this;
    MediaPlayer mediaPlayer;
    boolean isPrepared = false;
    //    String audioURL = "https://www.learningcontainer.com/wp-content/uploads/2020/02/Kalimba.mp3";
    CountDownTimer countDownTimer;
    AppCompatTextView tvTimer, tvTitle, tvAudioDescription;
    Uri uri;
    AudioModel audio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        initUi();
        clickEvent();
    }

    private void initUi() {
        tvTimer = findViewById(R.id.tvTimer);
        tvTitle = findViewById(R.id.tvTitle);
        tvAudioDescription = findViewById(R.id.tvAudioDescription);

        audio = getIntent().getParcelableExtra(AppConstants.AUDIO_TAG);


        tvTitle.setText(audio.getName());

        if (!audio.getDescription().isEmpty()) {
            tvAudioDescription.setVisibility(View.VISIBLE);
            tvAudioDescription.setText(audio.getDescription());
        }

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        uri = Uri.parse(audio.getAudio());
        prepareAudio();
//
    }

    private void clickEvent() {

        findViewById(R.id.btnPlay).setOnClickListener(v -> {
            if (isPrepared) {
                mediaPlayer.start();
                countDownTimer.start();
                findViewById(R.id.btnPlay).setVisibility(View.GONE);
                findViewById(R.id.tvTimer).setVisibility(View.VISIBLE);
            }
        });
    }

    private void prepareAudio() {
        try {
            mediaPlayer.setDataSource(String.valueOf(uri));
            mediaPlayer.setOnPreparedListener(mp -> {
                isPrepared = true;
                Log.e("Prepared", isPrepared + "");
                runOnUiThread(() -> prepareTimer(mediaPlayer.getDuration()));
            });
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnCompletionListener(mp -> {
                findViewById(R.id.btnPlay).setVisibility(View.VISIBLE);
                findViewById(R.id.tvTimer).setVisibility(View.GONE);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void prepareTimer(int duration) {
       /* MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(activity, uri);
        String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        int millSecond = Integer.parseInt(durationStr);*/
        tvTimer.setText(AppUtils.convertMillisToTime(duration));
        countDownTimer = new CountDownTimer(duration, 1000) {
            public void onTick(long millisUntilFinished) {
                tvTimer.setText(AppUtils.convertMillisToTime(millisUntilFinished));
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                tvTimer.setText(AppUtils.convertMillisToTime(duration));
            }

        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            countDownTimer.cancel();
        }
    }
}