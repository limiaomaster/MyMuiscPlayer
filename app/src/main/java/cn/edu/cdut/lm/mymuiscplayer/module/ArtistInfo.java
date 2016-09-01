package cn.edu.cdut.lm.mymuiscplayer.module;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by LimiaoMaster on 2016/8/31 19:55
 */

public class ArtistInfo implements Parcelable {

    private String artistName;
    private int numberOfTracks;
    private long artistId;

    public ArtistInfo(){

    }

    protected ArtistInfo(Parcel in) {
        artistName = in.readString();
        numberOfTracks = in.readInt();
        artistId = in.readLong();
    }

    public static final Creator<ArtistInfo> CREATOR = new Creator<ArtistInfo>() {
        @Override
        public ArtistInfo createFromParcel(Parcel in) {
            return new ArtistInfo(in);
        }

        @Override
        public ArtistInfo[] newArray(int size) {
            return new ArtistInfo[size];
        }
    };

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }



    public long getArtistId() {
        return artistId;
    }

    public void setArtistId(long artistId) {
        this.artistId = artistId;
    }


    public int getNumberOfTracks() {
        return numberOfTracks;
    }

    public void setNumberOfTracks(int numberOfTracks) {
        this.numberOfTracks = numberOfTracks;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(artistName);
        dest.writeInt(numberOfTracks);
        dest.writeLong(artistId);
    }
}
