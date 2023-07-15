package com.example.myapplication;

public class State {

    private String name; // название
    private String pcs;  // столица
    private String narkh;  // столица
    private String kod;  // столица
    private String id_mol;  // столица
    private String summa;
    private int flagResource; // ресурс флага

    public State(String name, String pcs, String narkh , String kod, String id_mol, String summa , int flag){

        this.name=name;
        this.pcs=pcs;
        this.narkh=narkh;
        this.kod=kod;
        this.id_mol=id_mol;
        this.summa = summa;
        this.flagResource=flag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKod() {
        return kod;
    }

    public void setKod(String kod) {
        this.kod = kod;
    }

    public String getId_mol() {
        return id_mol;
    }

    public void setId_mol(String id_mol) {
        this.id_mol = id_mol;
    }

    public String getPcs() {
        return pcs;
    }

    public void setPcs(String pcs) {
        this.pcs = pcs;
    }

    public String getNarkh() {
        return narkh;
    }

    public void setNarkh(String narkh) {
        this.narkh = narkh;
    }

    public String getSumma() {
        return summa;
    }

    public void setSumma(String summa) {
        this.summa = summa;
    }

    public int getFlagResource() {
        return flagResource;
    }

    public void setFlagResource(int flagResource) {
        this.flagResource = flagResource;
    }
}