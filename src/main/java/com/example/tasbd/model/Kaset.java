package com.example.tasbd.model;
public class Kaset {
    private Integer id_kaset;
    private String judul;
    private String genre;
    private Integer tahun_tayang;

    public Integer getid_kaset() {
        return id_kaset;
    }
    public void setid_kaset(Integer id_kaset) {
        this.id_kaset = id_kaset;
    }

    public String getjudul() {
        return judul;
    }
    public void setjudul(String judul) {
        this.judul = judul;
    }
    public String getgenre() {
        return genre;
    }
    public void setgenre(String genre) {
        this.genre = genre;
    }
    public Integer gettahun_tayang() {
        return tahun_tayang;
    }
    public void settahun_tayang(Integer tahun_tayang) {
        this.tahun_tayang = tahun_tayang;
    }

}
