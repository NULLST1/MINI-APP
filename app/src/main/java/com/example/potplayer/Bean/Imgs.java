package com.example.potplayer.Bean;

public class Imgs {
    private String imgname;
    private int imgid;

    public Imgs(String imgname, int imgid)
    {
        this.imgid =imgid;
        this.imgname = imgname;

    }
    public String getImgname() {
        return imgname;
    }

    public void setImgname(String imgname) {
        this.imgname = imgname;
    }

    public int getImgid() {
        return imgid;
    }

    public void setImgid(int imgid) {
        this.imgid = imgid;
    }
}
