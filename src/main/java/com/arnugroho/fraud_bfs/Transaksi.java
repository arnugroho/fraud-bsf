package com.arnugroho.fraud_bfs;

import java.time.LocalDateTime;

public class Transaksi {
    private String noAkun;
    private String idTransaksi;
    private LocalDateTime waktuTransaksi;
    private double jumlahTransaksi;

    public Transaksi(String noAkun, String idTransaksi, LocalDateTime waktuTransaksi, double jumlahTransaksi) {
        this.noAkun = noAkun;
        this.idTransaksi = idTransaksi;
        this.waktuTransaksi = waktuTransaksi;
        this.jumlahTransaksi = jumlahTransaksi;
    }

    public String getNoAkun() {
        return noAkun;
    }

    public void setNoAkun(String noAkun) {
        this.noAkun = noAkun;
    }

    public String getIdTransaksi() {
        return idTransaksi;
    }

    public void setIdTransaksi(String idTransaksi) {
        this.idTransaksi = idTransaksi;
    }

    public LocalDateTime getWaktuTransaksi() {
        return waktuTransaksi;
    }

    public void setWaktuTransaksi(LocalDateTime waktuTransaksi) {
        this.waktuTransaksi = waktuTransaksi;
    }

    public double getJumlahTransaksi() {
        return jumlahTransaksi;
    }

    public void setJumlahTransaksi(double jumlahTransaksi) {
        this.jumlahTransaksi = jumlahTransaksi;
    }
}
