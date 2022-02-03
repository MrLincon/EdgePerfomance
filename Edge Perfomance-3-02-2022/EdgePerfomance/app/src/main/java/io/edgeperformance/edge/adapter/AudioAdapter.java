package io.edgeperformance.edge.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.edgeperformance.edge.Models.AudioModel;
import io.edgeperformance.edge.R;
import io.edgeperformance.edge.interfaces.CallbacksT;

public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.ViewHolder> {

    Activity activity;
    ArrayList<AudioModel> audioList;
    CallbacksT<AudioModel> mListener;
    String name = "Gujarat";

    public AudioAdapter(Activity activity, ArrayList<AudioModel> audioList, CallbacksT<AudioModel> mListener) {
        this.activity = activity;
        this.audioList = audioList;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.row_audio, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AudioModel audio = audioList.get(position);
        if (audio != null) {
            holder.bind(audio);
        }
    }

    @Override
    public int getItemCount() {
        return audioList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        AppCompatTextView tvAudioName, tvAudioDescription;
        CardView cvAudio;
        AppCompatImageView ivAudio;

        public ViewHolder(View view) {
            super(view);
            this.view = view;

            ivAudio = view.findViewById(R.id.ivAudio);
            tvAudioName = view.findViewById(R.id.tvAudioName);
            tvAudioDescription = view.findViewById(R.id.tvAudioDescription);
            cvAudio = view.findViewById(R.id.cvAudio);
        }

        public void bind(AudioModel audio) {
            tvAudioName.setText(audio.getName());
            tvAudioDescription.setVisibility(View.GONE);
            if (!audio.getDescription().isEmpty()) {
                tvAudioDescription.setVisibility(View.VISIBLE);
                tvAudioDescription.setText(audio.getDescription());
            }

            if (audio.isPremium()) {
                ivAudio.setBackgroundResource(R.drawable.ic_lock);
            } else {
                ivAudio.setBackgroundResource(R.drawable.girl);
            }

            cvAudio.setOnClickListener(v -> {
                if (audio.isPremium()) {
                    Toast.makeText(activity, "You have not purchased this audio", Toast.LENGTH_SHORT).show();
                } else {
                    mListener.onSuccess(audio);
                }
            });
        }
    }

}
