package io.edgeperformance.edge.Models;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class AudioModel implements Parcelable {
    double assetId;
    String name;
    String description;
    String audio;
    boolean isPremium;

    public AudioModel(double assetId, String name, String description, String audio, boolean isPremium) {
        this.assetId = assetId;
        this.name = name == null ? "" : name;
        this.description = description == null ? "" : description;
        this.audio = audio == null ? "" : audio;
        this.isPremium = isPremium;
    }


    protected AudioModel(Parcel in) {
        assetId = in.readDouble();
        name = in.readString();
        description = in.readString();
        audio = in.readString();
        isPremium = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(assetId);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(audio);
        dest.writeByte((byte) (isPremium ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AudioModel> CREATOR = new Creator<AudioModel>() {
        @Override
        public AudioModel createFromParcel(Parcel in) {
            return new AudioModel(in);
        }

        @Override
        public AudioModel[] newArray(int size) {
            return new AudioModel[size];
        }
    };

    public double getAssetId() {
        return assetId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setAudio(String audio) {
        this.audio = "http:" + audio;
    }

    public String getAudio() {
        return audio;
    }

    public boolean isPremium() {
        return isPremium;
    }
}
