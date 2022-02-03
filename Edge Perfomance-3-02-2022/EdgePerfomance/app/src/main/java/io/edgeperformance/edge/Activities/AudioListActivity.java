package io.edgeperformance.edge.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;

import com.contentful.java.cda.CDAArray;
import com.contentful.java.cda.CDAAsset;
import com.contentful.java.cda.CDAClient;
import com.contentful.java.cda.CDAEntry;
import com.contentful.java.cda.CDAResource;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;

import org.json.JSONObject;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;

import io.edgeperformance.edge.Models.AudioModel;
import io.edgeperformance.edge.R;
import io.edgeperformance.edge.adapter.AudioAdapter;
import io.edgeperformance.edge.interfaces.CallbacksT;
import io.edgeperformance.edge.utils.AppConstants;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AudioListActivity extends AppCompatActivity {

    Activity activity = AudioListActivity.this;
    String key = "";
    Gson gson;
    CDAClient client;
    AppCompatTextView tvTitle;
    ArrayList<AudioModel> audioList = new ArrayList<>();
    RecyclerView rvAudio;
    AudioAdapter audioAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_list);

        initUI();
    }

    private void initUI() {
        tvTitle = findViewById(R.id.tvTitle);
        rvAudio = findViewById(R.id.rvAudio);
        key = getIntent().getStringExtra(AppConstants.KEY_TAG);
        gson = new GsonBuilder().setPrettyPrinting().create();

        if (key.equalsIgnoreCase(AppConstants.SESSION_KEY)) {
            tvTitle.setText("Sessions");
        } else if (key.equalsIgnoreCase(AppConstants.BREATH_WORK_KEY)) {
            tvTitle.setText("Breath Work");
        } else {
            tvTitle.setText("Mindfulness");
        }


        audioAdapter = new AudioAdapter(activity, audioList, new CallbacksT<AudioModel>() {
            @Override
            public void onSuccess(AudioModel data) {
                Intent intent = new Intent(activity, PlayerActivity.class);
                intent.putExtra(AppConstants.AUDIO_TAG, data);
                startActivity(intent);
            }
        });
        rvAudio.setAdapter(audioAdapter);

        client = CDAClient.builder()
                .setSpace(AppConstants.SPACE_ID)
                .setToken(AppConstants.API_TOKEN)
                .build();

//        fetchingRecords();
//        fetchingRecordsNew();

        new RetrieveFeedTask().execute();
    }

    class RetrieveFeedTask extends AsyncTask<String, Void, Boolean> {

        private Exception exception;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            findViewById(R.id.pbLoader).setVisibility(View.VISIBLE);
            rvAudio.setVisibility(View.GONE);
        }

        protected Boolean doInBackground(String... urls) {
            try {
                CDAArray dataArray = client.fetch(CDAEntry.class).withContentType(key).all();
                ArrayList<AudioModel> tempList = new ArrayList<>();
                if (dataArray != null) {
                    for (CDAResource resource : dataArray.items()) {
                        CDAEntry entry = (CDAEntry) resource;
                        boolean isPremium = false;
                        try {
                            isPremium = entry.getField("premium");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        tempList.add(new AudioModel(entry.getField("assetId")
                                , entry.getField("name")
                                , entry.getField("description")
                                , ((CDAAsset) entry.getField("audio")).id()
                                , isPremium));
                    }
                }
                CDAArray assetArray = client.fetch(CDAAsset.class).all();
                if (assetArray != null) {
                    String json = gson.toJson(assetArray);
                    JSONObject obj = new JSONObject(json);
                    for (AudioModel audioModel : tempList) {
                        for (CDAResource resource : assetArray.items()) {
                            CDAAsset asset = (CDAAsset) resource;
                            if (audioModel.getAudio().equalsIgnoreCase(asset.id())) {
                                audioModel.setAudio(((LinkedTreeMap<String, String>) asset.getField("file")).get("url"));
                                audioList.add(audioModel);
                                break;
                            }
                        }
                    }
                }
                return true;
            } catch (Exception e) {
                this.exception = e;
                return false;
            }
        }

        protected void onPostExecute(Boolean success) {
            if (success) {
                findViewById(R.id.pbLoader).setVisibility(View.GONE);
                rvAudio.setVisibility(View.VISIBLE);
                runOnUiThread(() -> audioAdapter.notifyDataSetChanged());
            }
        }
    }

}