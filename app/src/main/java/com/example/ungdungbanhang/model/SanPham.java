package com.example.ungdungbanhang.model;

import java.io.Serializable;

public class SanPham implements Serializable {
    private String id;
    private String Tensanpham;
    private String Giasanpham;
    private String Motasanpham;
    private String linkImage;
    public SanPham(){}
    public SanPham(String id, String tensanpham, String giasanpham, String motasanpham,String linkImage) {
        this.id = id;
        this.Tensanpham = tensanpham;
        this.Giasanpham = giasanpham;
        this.Motasanpham = motasanpham;
        this.linkImage = linkImage;
    }

    public String getGiasanpham() {
        return Giasanpham;
    }

    public void setGiasanpham(String giasanpham) {
        Giasanpham = giasanpham;
    }

    public String getLinkImage() {
        return linkImage;
    }

    public void setLinkImage(String linkImage) {
        this.linkImage = linkImage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTensanpham() {
        return Tensanpham;
    }

    public void setTensanpham(String tensanpham) {
        Tensanpham = tensanpham;
    }

    public String getMotasanpham() {
        return Motasanpham;
    }

    public void setMotasanpham(String motasanpham) {
        Motasanpham = motasanpham;
    }

    @Override
    public String toString() {
        return "SanPham{" +
                "id='" + id + '\'' +
                ", Tensanpham='" + Tensanpham + '\'' +
                ", Giasanpham='" + Giasanpham + '\'' +
                ", Motasanpham='" + Motasanpham + '\'' +
                '}';
    }
}
