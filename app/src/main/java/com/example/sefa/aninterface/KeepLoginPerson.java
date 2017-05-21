package com.example.sefa.aninterface;


import java.io.Serializable;

public class KeepLoginPerson implements Serializable {
    private int ApploginId;
    private String Yetki;
    private String Tcno;
    private String Ad;
    private String Soyad;
    private String DogumTarihi;
    private String Sehir;
    private String Kangrubu;

    public KeepLoginPerson() {
        this.ApploginId = 0;
        this.Yetki = null;
        this.Tcno = null;
        this.Ad = null;
        this.Soyad = null;
        this.DogumTarihi = null;
        this.Sehir = null;
        this.Kangrubu = null;
    }

    public int getApploginId() {
        return ApploginId;
    }

    public void setApploginId(int apploginId) {
        ApploginId = apploginId;
    }

    public String getYetki() {
        return Yetki;
    }

    public void setYetki(String yetki) {
        Yetki = yetki;
    }

    public String getTcno() {
        return Tcno;
    }

    public void setTcno(String tcno) {
        Tcno = tcno;
    }

    public String getAd() {
        return Ad;
    }

    public void setAd(String ad) {
        Ad = ad;
    }

    public String getSoyad() {
        return Soyad;
    }

    public void setSoyad(String soyad) {
        Soyad = soyad;
    }

    public String getDogumTarihi() {
        return DogumTarihi;
    }

    public void setDogumTarihi(String dogumTarihi) {
        DogumTarihi = dogumTarihi;
    }

    public String getSehir() {
        return Sehir;
    }

    public void setSehir(String sehir) {
        Sehir = sehir;
    }

    public String getKangrubu() {
        return Kangrubu;
    }

    public void setKangrubu(String kangrubu) {
        Kangrubu = kangrubu;
    }
}
