package com.example.ungdungbanhang.model;


public class LoaiSP {
    public int id;
    public String tenloaisanpham, hinhanhsanpham;

    public  LoaiSP(int id, String tenloaisanpham, String hinhanhsanpham){
        this.id = id;
        this.tenloaisanpham = tenloaisanpham;
        this.hinhanhsanpham = hinhanhsanpham;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTenLoaiSP() {
        return tenloaisanpham;
    }

    public void setTenLoaiSP(String tenLoaiSP) {
        this.tenloaisanpham = tenLoaiSP;
    }

    public String getHinhAnhLoaiSP() {
        return hinhanhsanpham;
    }

    public void setHinhAnhLoaiSP(String hinhAnhLoaiSP) {
        this.hinhanhsanpham = hinhAnhLoaiSP;
    }
}
