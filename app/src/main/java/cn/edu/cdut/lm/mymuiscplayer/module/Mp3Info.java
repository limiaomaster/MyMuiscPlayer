package cn.edu.cdut.lm.mymuiscplayer.module;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by LimiaoMaster on 2016/8/15 0015 上午 11:15
 */

public class Mp3Info implements Parcelable {
    private int positionInList;
    private long id;
    private String title;
    private String displayName;
    private String artist;
    private long duration;
    private long size;
    private String url;
    private String album;
    private long albumId;
    private boolean selected;

    private String title_pinyin;
    private String artist_pinyin;
    private String album_pinyin;

    private int dateModified;

    protected Mp3Info(Parcel in) {
        positionInList = in.readInt();
        id = in.readLong();
        title = in.readString();
        displayName = in.readString();
        artist = in.readString();
        duration = in.readLong();
        size = in.readLong();
        url = in.readString();
        album = in.readString();
        albumId = in.readLong();
        selected = in.readByte() != 0;
        title_pinyin = in.readString();
        artist_pinyin = in.readString();
        album_pinyin = in.readString();
        dateModified = in.readInt();
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

    public int getDateModified() {
        return dateModified;
    }

    public void setDateModified(int dateModified) {
        this.dateModified = dateModified;
    }

    public Mp3Info(){
    }



    @Override
    public String toString() {
        return "Mp3Info{" +
                "position="+positionInList+
                ", id=" + id +
                ", title='" + title + '\'' +
                ", displayName='" + displayName + '\'' +
                ", artist='" + artist + '\'' +
                ", duration=" + duration +
                ", size=" + size +
                ", url='" + url + '\'' +
                ", album='" + album + '\'' +
                ", albumId=" + albumId +
                '}';
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

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getPositionInList() {
        return positionInList;
    }

    public void setPositionInList(int positionInList) {
        this.positionInList = positionInList;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(positionInList);
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(displayName);
        dest.writeString(artist);
        dest.writeLong(duration);
        dest.writeLong(size);
        dest.writeString(url);
        dest.writeString(album);
        dest.writeLong(albumId);
        dest.writeByte((byte) (selected ? 1 : 0));
        dest.writeString(title_pinyin);
        dest.writeString(artist_pinyin);
        dest.writeString(album_pinyin);
        dest.writeInt(dateModified);
    }
}
