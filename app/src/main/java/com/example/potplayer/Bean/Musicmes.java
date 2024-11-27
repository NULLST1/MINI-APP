package com.example.potplayer.Bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Musicmes  implements Parcelable {
    private String id;//歌曲id
    private String songname;//歌曲名
    private String singer;//歌手
    private String musictime;//歌曲时间
    private String MmusicAlbum;//歌曲专辑
    private String path;//存放路径
    public Musicmes() {
    }


    public Musicmes(String id, String songname, String singer, String musictime, String mmusicAlbum, String path) {
        this.id = id;
        this.songname = songname;
        this.singer = singer;
        this.musictime = musictime;
        MmusicAlbum = mmusicAlbum;
        this.path = path;
    }

    protected Musicmes(Parcel in) {
        id = in.readString();
        songname = in.readString();
        singer = in.readString();
        musictime = in.readString();
        MmusicAlbum = in.readString();
        path = in.readString();
    }

    public static final Creator<Musicmes> CREATOR = new Creator<Musicmes>() {
        @Override
        public Musicmes createFromParcel(Parcel in) {
            return new Musicmes(in);
        }

        @Override
        public Musicmes[] newArray(int size) {
            return new Musicmes[size];
        }
    };

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMmusicAlbum() {
        return MmusicAlbum;
    }

    public void setMmusicAlbum(String mmusicAlbum) {
        MmusicAlbum = mmusicAlbum;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSongname() {
        return songname;
    }

    public void setSongname(String songname) {
        this.songname = songname;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getMusictime() {
        return musictime;
    }

    public void setMusictime(String musictime) {
        this.musictime = musictime;
    }

    @Override
    public String toString() {
        return "Musicmes{" +
                "id='" + id + '\'' +
                ", songname='" + songname + '\'' +
                ", singer='" + singer + '\'' +
                ", musictime='" + musictime + '\'' +
                ", MmusicAlbum='" + MmusicAlbum + '\'' +
                ", path='" + path + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(songname);
        parcel.writeString(singer);
        parcel.writeString(musictime);
        parcel.writeString(MmusicAlbum);
        parcel.writeString(path);
    }
}
