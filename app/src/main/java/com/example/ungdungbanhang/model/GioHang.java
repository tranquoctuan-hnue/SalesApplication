package com.example.ungdungbanhang.model;

import java.io.Serializable;

public class GioHang implements Serializable {
    String id;
    String tenSP;
    String giaSP;
    String hinhSP;
    int soluongSP;

    public GioHang(String id, String tenSP, String giaSP, String hinhSP, int soluongSP) {
        this.id = id;
        this.tenSP = tenSP;
        this.giaSP = giaSP;
        this.hinhSP = hinhSP;
        this.soluongSP = soluongSP;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTenSP() {
        return tenSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    public String getGiaSP() {
        return giaSP;
    }

    public void setGiaSP(String giaSP) {
        this.giaSP = giaSP;
    }

    @Override
    public String toString() {
        return "GioHang{" +
                "id='" + id + '\'' +
                ", tenSP='" + tenSP + '\'' +
                ", giaSP='" + giaSP + '\'' +
                ", hinhSP='" + hinhSP + '\'' +
                ", soluongSP=" + soluongSP +
                '}';
    }

    public String getHinhSP() {
        return hinhSP;
    }

    public void setHinhSP(String hinhSP) {
        this.hinhSP = hinhSP;
    }

    public int getSoluongSP() {
        return soluongSP;
    }

    public void setSoluongSP(int soluongSP) {
        this.soluongSP = soluongSP;
    }
}
