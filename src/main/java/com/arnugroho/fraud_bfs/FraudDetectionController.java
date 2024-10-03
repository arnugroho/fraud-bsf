package com.arnugroho.fraud_bfs;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@RestController
@RequestMapping("/fraud-detection")
public class FraudDetectionController {

    // Contoh data transaksi (noAkun, idTransaksi, waktuTransaksi, jumlahTransaksi)
    private final List<Transaksi> transaksiList = Arrays.asList(
            new Transaksi("A001", "T001", LocalDateTime.of(2024, 10, 3, 8, 0), 50_000_000),
            new Transaksi("A001", "T002", LocalDateTime.of(2024, 10, 3, 8, 15), 60_000_000),
            new Transaksi("A001", "T003", LocalDateTime.of(2024, 10, 3, 8, 25), 150_000_000), // Fraud
            new Transaksi("A002", "T004", LocalDateTime.of(2024, 10, 3, 8, 10), 30_000_000),
            new Transaksi("A002", "T005", LocalDateTime.of(2024, 10, 3, 8, 45), 200_000_000) // Fraud
    );

    @GetMapping("/check-fraud")
    public List<String> checkFraud() {
        Map<String, List<Transaksi>> transaksiByAkun = new HashMap<>();

        // Kelompokkan transaksi berdasarkan noAkun
        for (Transaksi t : transaksiList) {
            transaksiByAkun.computeIfAbsent(t.getNoAkun(), k -> new ArrayList<>()).add(t);
        }

        List<String> frauds = new ArrayList<>();

        // Periksa transaksi untuk setiap akun
        for (Map.Entry<String, List<Transaksi>> entry : transaksiByAkun.entrySet()) {
            if (detectFraud(entry.getValue())) {
                frauds.add("Akun: " + entry.getKey() + " terdeteksi melakukan fraud.");
            }
        }

        return frauds.isEmpty() ? List.of("Tidak ada fraud terdeteksi.") : frauds;
    }

    private boolean detectFraud(List<Transaksi> transaksiAkun) {
        // Urutkan berdasarkan waktu transaksi
        transaksiAkun.sort(Comparator.comparing(Transaksi::getWaktuTransaksi));

        Queue<Transaksi> queue = new LinkedList<>();
        double totalJumlah = 0;

        for (Transaksi transaksi : transaksiAkun) {
            // Tambahkan transaksi ke dalam antrian
            queue.add(transaksi);
            totalJumlah += transaksi.getJumlahTransaksi();

            // Hapus transaksi yang lebih dari 30 menit
            while (!queue.isEmpty() && ChronoUnit.MINUTES.between(queue.peek().getWaktuTransaksi(), transaksi.getWaktuTransaksi()) > 30) {
                Transaksi old = queue.poll();
                totalJumlah -= old.getJumlahTransaksi();
            }

            // Cek aturan fraud
            if (totalJumlah > 100_000_000) {
                return true; // Fraud terdeteksi karena total transaksi melebihi 100 juta dalam 30 menit
            }

            if (transaksi.getJumlahTransaksi() > 100_000_000) {
                return true; // Fraud terdeteksi karena satu transaksi lebih dari 100 juta
            }
        }

        return false;
    }
}
