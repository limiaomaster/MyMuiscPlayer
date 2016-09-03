package cn.edu.cdut.lm.mymuiscplayer.module;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by LimiaoMaster on 2016/9/1 16:22
 */

public class AlbumInfo implements Parcelable {
    //专辑名称
    private String albumName;
    //专辑在数据库中的id
    private int albumID = -1;
    //专辑的歌曲数目
    private int numberOfTracks = 0;

    private String artist;

    //专辑封面图片路径
    private String artPath;

    public AlbumInfo(){

    }

    protected AlbumInfo(Parcel in) {
        albumName = in.readString();
        albumID = in.readInt();
        numberOfTracks = in.readInt();
        artist = in.readString();
        artPath = in.readString();
    }

    public static final Creator<AlbumInfo> CREATOR = new Creator<AlbumInfo>() {
        @Override
        public AlbumInfo createFromParcel(Parcel in) {
            return new AlbumInfo(in);
        }

        @Override
        public AlbumInfo[] newArray(int size) {
            return new AlbumInfo[size];
        }
    };

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public int getAlbumID() {
        return albumID;
    }

    public void setAlbumID(int albumID) {
        this.albumID = albumID;
    }

    public int getNumberOfTracks() {
        return numberOfTracks;
    }

    public void setNumberOfTracks(int numberOfTracks) {
        this.numberOfTracks = numberOfTracks;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getArtPath() {
        return artPath;
    }

    public void setArtPath(String artPath) {
        this.artPath = artPath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(albumName);
        dest.writeInt(albumID);
        dest.writeInt(numberOfTracks);
        dest.writeString(artist);
        dest.writeString(artPath);
    }
}
