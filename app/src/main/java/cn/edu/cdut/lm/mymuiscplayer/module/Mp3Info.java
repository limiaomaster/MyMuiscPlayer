package cn.edu.cdut.lm.mymuiscplayer.module;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by LimiaoMaster on 2016/8/15 0015 上午 11:15
 */

public class Mp3Info implements Parcelable {
    private int positionInThisList;

    private int id;
    private int musicId;
    private String title;
    private String displayName;
    private String artist;
    private int duration;
    private int size;
    private String url;
    private String album;
    private int albumId;
    private boolean selected;

    private String title_pinyin;
    private String artist_pinyin;
    private String album_pinyin;

    private int dateModified;

    private int sampling_rate;
    private int bit_rate;
    private String quality;

    public Mp3Info(){
    }


    protected Mp3Info(Parcel in) {
        positionInThisList = in.readInt();
        id = in.readInt();
        musicId = in.readInt();
        title = in.readString();
        displayName = in.readString();
        artist = in.readString();
        duration = in.readInt();
        size = in.readInt();
        url = in.readString();
        album = in.readString();
        albumId = in.readInt();
        selected = in.readByte() != 0;
        title_pinyin = in.readString();
        artist_pinyin = in.readString();
        album_pinyin = in.readString();
        dateModified = in.readInt();
        sampling_rate = in.readInt();
        bit_rate = in.readInt();
        quality = in.readString();
    }

    public static final Creator<Mp3Info> CREATOR = new Creator<Mp3Info>() {
        @Override
        public Mp3Info createFromParcel(Parcel in) {
            return new Mp3Info(in);
        }

        @Override
        public Mp3Info[] newArray(int size) {
            return new Mp3Info[size];
        }
    };

    public int getPositionInThisList() {
        return positionInThisList;
    }

    public void setPositionInThisList(int positionInThisList) {
        this.positionInThisList = positionInThisList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMusicId() {
        return musicId;
    }

    public void setMusicId(int musicId) {
        this.musicId = musicId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getTitle_pinyin() {
        return title_pinyin;
    }

    public void setTitle_pinyin(String title_pinyin) {
        this.title_pinyin = title_pinyin;
    }

    public String getArtist_pinyin() {
        return artist_pinyin;
    }

    public void setArtist_pinyin(String artist_pinyin) {
        this.artist_pinyin = artist_pinyin;
    }

    public String getAlbum_pinyin() {
        return album_pinyin;
    }

    public void setAlbum_pinyin(String album_pinyin) {
        this.album_pinyin = album_pinyin;
    }

    public int getDateModified() {
        return dateModified;
    }

    public void setDateModified(int dateModified) {
        this.dateModified = dateModified;
    }

    public int getSampling_rate() {
        return sampling_rate;
    }

    public void setSampling_rate(int sampling_rate) {
        this.sampling_rate = sampling_rate;
    }

    public int getBit_rate() {
        return bit_rate;
    }

    public void setBit_rate(int bit_rate) {
        this.bit_rate = bit_rate;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(positionInThisList);
        dest.writeInt(id);
        dest.writeInt(musicId);
        dest.writeString(title);
        dest.writeString(displayName);
        dest.writeString(artist);
        dest.writeInt(duration);
        dest.writeInt(size);
        dest.writeString(url);
        dest.writeString(album);
        dest.writeInt(albumId);
        dest.writeByte((byte) (selected ? 1 : 0));
        dest.writeString(title_pinyin);
        dest.writeString(artist_pinyin);
        dest.writeString(album_pinyin);
        dest.writeInt(dateModified);
        dest.writeInt(sampling_rate);
        dest.writeInt(bit_rate);
        dest.writeString(quality);
    }
}
