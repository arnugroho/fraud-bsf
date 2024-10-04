package com.arnugroho.fraud_bfs;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@RestController
@RequestMapping("/fraud-detection")
public class FraudDetectionController {

    // Contoh data transaksi (noAkun, idTransaksi, waktuTransaksi, jumlahTransaksi)
//    private final List<Transaksi> transaksiList = Arrays.asList(
//            new Transaksi("A001", "T001", LocalDateTime.of(2024, 10, 3, 8, 0), 50_000_000),
//            new Transaksi("A001", "T002", LocalDateTime.of(2024, 10, 3, 8, 15), 60_000_000),
//            new Transaksi("A001", "T003", LocalDateTime.of(2024, 10, 3, 8, 25), 100_000_000), // Fraud
//            new Transaksi("A002", "T004", LocalDateTime.of(2024, 10, 3, 8, 10), 30_000_000),
//            new Transaksi("A002", "T005", LocalDateTime.of(2024, 10, 3, 8, 45), 200_000_000) // Fraud
//    );

    private String jsonString = "[\n    {\n        \"noAkun\": \"ACC6357\",\n        \"idTransaksi\": \"TX000001\",\n        \"waktuTransaksi\": \"2024-01-15 01:15:36\",\n        \"jumlahTransaksi\": 42512122\n    },\n    {\n        \"noAkun\": \"ACC8066\",\n        \"idTransaksi\": \"TX000002\",\n        \"waktuTransaksi\": \"2024-01-15 00:45:36\",\n        \"jumlahTransaksi\": 33296480\n    },\n    {\n        \"noAkun\": \"ACC3851\",\n        \"idTransaksi\": \"TX000003\",\n        \"waktuTransaksi\": \"2024-01-15 00:06:26\",\n        \"jumlahTransaksi\": 10077415\n    },\n    {\n        \"noAkun\": \"ACC7489\",\n        \"idTransaksi\": \"TX000004\",\n        \"waktuTransaksi\": \"2024-01-15 02:44:31\",\n        \"jumlahTransaksi\": 27626533\n    },\n    {\n        \"noAkun\": \"ACC8254\",\n        \"idTransaksi\": \"TX000005\",\n        \"waktuTransaksi\": \"2024-01-15 02:55:05\",\n        \"jumlahTransaksi\": 38619542\n    },\n    {\n        \"noAkun\": \"ACC6093\",\n        \"idTransaksi\": \"TX000006\",\n        \"waktuTransaksi\": \"2024-01-15 02:46:05\",\n        \"jumlahTransaksi\": 20187581\n    },\n    {\n        \"noAkun\": \"ACC4444\",\n        \"idTransaksi\": \"TX000007\",\n        \"waktuTransaksi\": \"2024-01-15 02:02:04\",\n        \"jumlahTransaksi\": 39334820\n    },\n    {\n        \"noAkun\": \"ACC7489\",\n        \"idTransaksi\": \"TX000008\",\n        \"waktuTransaksi\": \"2024-01-15 00:12:58\",\n        \"jumlahTransaksi\": 22880960\n    },\n    {\n        \"noAkun\": \"ACC6093\",\n        \"idTransaksi\": \"TX000009\",\n        \"waktuTransaksi\": \"2024-01-15 01:53:05\",\n        \"jumlahTransaksi\": 36622844\n    },\n    {\n        \"noAkun\": \"ACC6093\",\n        \"idTransaksi\": \"TX000010\",\n        \"waktuTransaksi\": \"2024-01-15 02:12:13\",\n        \"jumlahTransaksi\": 10919653\n    },\n    {\n        \"noAkun\": \"ACC6357\",\n        \"idTransaksi\": \"TX000011\",\n        \"waktuTransaksi\": \"2024-01-15 00:02:44\",\n        \"jumlahTransaksi\": 34252087\n    },\n    {\n        \"noAkun\": \"ACC8246\",\n        \"idTransaksi\": \"TX000012\",\n        \"waktuTransaksi\": \"2024-01-15 02:40:37\",\n        \"jumlahTransaksi\": 10008056\n    },\n    {\n        \"noAkun\": \"ACC8254\",\n        \"idTransaksi\": \"TX000013\",\n        \"waktuTransaksi\": \"2024-01-15 01:40:03\",\n        \"jumlahTransaksi\": 18966992\n    },\n    {\n        \"noAkun\": \"ACC9490\",\n        \"idTransaksi\": \"TX000014\",\n        \"waktuTransaksi\": \"2024-01-15 02:08:32\",\n        \"jumlahTransaksi\": 34057858\n    },\n    {\n        \"noAkun\": \"ACC9338\",\n        \"idTransaksi\": \"TX000015\",\n        \"waktuTransaksi\": \"2024-01-15 00:31:30\",\n        \"jumlahTransaksi\": 28591187\n    },\n    {\n        \"noAkun\": \"ACC3782\",\n        \"idTransaksi\": \"TX000016\",\n        \"waktuTransaksi\": \"2024-01-15 02:24:28\",\n        \"jumlahTransaksi\": 141171589\n    },\n    {\n        \"noAkun\": \"ACC3782\",\n        \"idTransaksi\": \"TX000017\",\n        \"waktuTransaksi\": \"2024-01-15 00:02:46\",\n        \"jumlahTransaksi\": 9665568\n    },\n    {\n        \"noAkun\": \"ACC4444\",\n        \"idTransaksi\": \"TX000018\",\n        \"waktuTransaksi\": \"2024-01-15 00:57:00\",\n        \"jumlahTransaksi\": 34801195\n    },\n    {\n        \"noAkun\": \"ACC1418\",\n        \"idTransaksi\": \"TX000019\",\n        \"waktuTransaksi\": \"2024-01-15 01:37:48\",\n        \"jumlahTransaksi\": 29305487\n    },\n    {\n        \"noAkun\": \"ACC7489\",\n        \"idTransaksi\": \"TX000020\",\n        \"waktuTransaksi\": \"2024-01-15 02:29:57\",\n        \"jumlahTransaksi\": 7453593\n    },\n    {\n        \"noAkun\": \"ACC4444\",\n        \"idTransaksi\": \"TX000021\",\n        \"waktuTransaksi\": \"2024-01-15 02:34:57\",\n        \"jumlahTransaksi\": 43760090\n    },\n    {\n        \"noAkun\": \"ACC1418\",\n        \"idTransaksi\": \"TX000022\",\n        \"waktuTransaksi\": \"2024-01-15 01:21:52\",\n        \"jumlahTransaksi\": 8855516\n    },\n    {\n        \"noAkun\": \"ACC5761\",\n        \"idTransaksi\": \"TX000023\",\n        \"waktuTransaksi\": \"2024-01-15 01:11:34\",\n        \"jumlahTransaksi\": 34946242\n    },\n    {\n        \"noAkun\": \"ACC1094\",\n        \"idTransaksi\": \"TX000024\",\n        \"waktuTransaksi\": \"2024-01-15 00:04:19\",\n        \"jumlahTransaksi\": 22885161\n    },\n    {\n        \"noAkun\": \"ACC9673\",\n        \"idTransaksi\": \"TX000025\",\n        \"waktuTransaksi\": \"2024-01-15 01:04:03\",\n        \"jumlahTransaksi\": 114607506\n    },\n    {\n        \"noAkun\": \"ACC8066\",\n        \"idTransaksi\": \"TX000026\",\n        \"waktuTransaksi\": \"2024-01-15 01:47:56\",\n        \"jumlahTransaksi\": 29523251\n    },\n    {\n        \"noAkun\": \"ACC5214\",\n        \"idTransaksi\": \"TX000027\",\n        \"waktuTransaksi\": \"2024-01-15 01:53:34\",\n        \"jumlahTransaksi\": 26426632\n    },\n    {\n        \"noAkun\": \"ACC5761\",\n        \"idTransaksi\": \"TX000028\",\n        \"waktuTransaksi\": \"2024-01-15 00:59:43\",\n        \"jumlahTransaksi\": 40401331\n    },\n    {\n        \"noAkun\": \"ACC4296\",\n        \"idTransaksi\": \"TX000029\",\n        \"waktuTransaksi\": \"2024-01-15 01:47:06\",\n        \"jumlahTransaksi\": 38345506\n    },\n    {\n        \"noAkun\": \"ACC4444\",\n        \"idTransaksi\": \"TX000030\",\n        \"waktuTransaksi\": \"2024-01-15 00:29:47\",\n        \"jumlahTransaksi\": 21438678\n    },\n    {\n        \"noAkun\": \"ACC1094\",\n        \"idTransaksi\": \"TX000031\",\n        \"waktuTransaksi\": \"2024-01-15 01:28:48\",\n        \"jumlahTransaksi\": 48123475\n    },\n    {\n        \"noAkun\": \"ACC6093\",\n        \"idTransaksi\": \"TX000032\",\n        \"waktuTransaksi\": \"2024-01-15 01:24:28\",\n        \"jumlahTransaksi\": 11293610\n    },\n    {\n        \"noAkun\": \"ACC8246\",\n        \"idTransaksi\": \"TX000033\",\n        \"waktuTransaksi\": \"2024-01-15 01:25:04\",\n        \"jumlahTransaksi\": 41202066\n    },\n    {\n        \"noAkun\": \"ACC3851\",\n        \"idTransaksi\": \"TX000034\",\n        \"waktuTransaksi\": \"2024-01-15 00:01:57\",\n        \"jumlahTransaksi\": 5859309\n    },\n    {\n        \"noAkun\": \"ACC5214\",\n        \"idTransaksi\": \"TX000035\",\n        \"waktuTransaksi\": \"2024-01-15 00:09:21\",\n        \"jumlahTransaksi\": 21244431\n    },\n    {\n        \"noAkun\": \"ACC8254\",\n        \"idTransaksi\": \"TX000036\",\n        \"waktuTransaksi\": \"2024-01-15 02:19:40\",\n        \"jumlahTransaksi\": 49429603\n    },\n    {\n        \"noAkun\": \"ACC4444\",\n        \"idTransaksi\": \"TX000037\",\n        \"waktuTransaksi\": \"2024-01-15 02:47:09\",\n        \"jumlahTransaksi\": 34837768\n    },\n    {\n        \"noAkun\": \"ACC4444\",\n        \"idTransaksi\": \"TX000038\",\n        \"waktuTransaksi\": \"2024-01-15 00:58:06\",\n        \"jumlahTransaksi\": 17323708\n    },\n    {\n        \"noAkun\": \"ACC9490\",\n        \"idTransaksi\": \"TX000039\",\n        \"waktuTransaksi\": \"2024-01-15 01:56:25\",\n        \"jumlahTransaksi\": 45883574\n    },\n    {\n        \"noAkun\": \"ACC8246\",\n        \"idTransaksi\": \"TX000040\",\n        \"waktuTransaksi\": \"2024-01-15 01:27:36\",\n        \"jumlahTransaksi\": 14196644\n    },\n    {\n        \"noAkun\": \"ACC6327\",\n        \"idTransaksi\": \"TX000041\",\n        \"waktuTransaksi\": \"2024-01-15 00:55:25\",\n        \"jumlahTransaksi\": 34964351\n    },\n    {\n        \"noAkun\": \"ACC6357\",\n        \"idTransaksi\": \"TX000042\",\n        \"waktuTransaksi\": \"2024-01-15 00:20:50\",\n        \"jumlahTransaksi\": 1159805\n    },\n    {\n        \"noAkun\": \"ACC4444\",\n        \"idTransaksi\": \"TX000043\",\n        \"waktuTransaksi\": \"2024-01-15 01:06:20\",\n        \"jumlahTransaksi\": 42142604\n    },\n    {\n        \"noAkun\": \"ACC5214\",\n        \"idTransaksi\": \"TX000044\",\n        \"waktuTransaksi\": \"2024-01-15 02:18:40\",\n        \"jumlahTransaksi\": 28475500\n    },\n    {\n        \"noAkun\": \"ACC8254\",\n        \"idTransaksi\": \"TX000045\",\n        \"waktuTransaksi\": \"2024-01-15 00:52:40\",\n        \"jumlahTransaksi\": 46857539\n    },\n    {\n        \"noAkun\": \"ACC9673\",\n        \"idTransaksi\": \"TX000046\",\n        \"waktuTransaksi\": \"2024-01-15 02:39:33\",\n        \"jumlahTransaksi\": 141439776\n    },\n    {\n        \"noAkun\": \"ACC9490\",\n        \"idTransaksi\": \"TX000047\",\n        \"waktuTransaksi\": \"2024-01-15 01:49:19\",\n        \"jumlahTransaksi\": 31343027\n    },\n    {\n        \"noAkun\": \"ACC5214\",\n        \"idTransaksi\": \"TX000048\",\n        \"waktuTransaksi\": \"2024-01-15 02:10:27\",\n        \"jumlahTransaksi\": 13399953\n    },\n    {\n        \"noAkun\": \"ACC3851\",\n        \"idTransaksi\": \"TX000049\",\n        \"waktuTransaksi\": \"2024-01-15 00:57:28\",\n        \"jumlahTransaksi\": 42954241\n    },\n    {\n        \"noAkun\": \"ACC5537\",\n        \"idTransaksi\": \"TX000050\",\n        \"waktuTransaksi\": \"2024-01-15 02:30:31\",\n        \"jumlahTransaksi\": 21093484\n    }\n]";


    @GetMapping("/check-fraud")
    public List<String> checkFraud() throws IOException {
        Map<String, List<Transaksi>> transaksiByAkun = new HashMap<>();
        Map<String, Double> totalTransaksiPerAkun = new HashMap<>();

        // Kelompokkan transaksi berdasarkan noAkun
        for (Transaksi t : getJson()) {
            transaksiByAkun.computeIfAbsent(t.getNoAkun(), k -> new ArrayList<>()).add(t);
            totalTransaksiPerAkun.put(t.getNoAkun(),
                    totalTransaksiPerAkun.getOrDefault(t.getNoAkun(), 0.0) + t.getJumlahTransaksi());
        }

        // Sort totalTransaksiPerAkun berdasarkan nilai (total transaksi) dari yang terbesar ke terkecil
        List<Map.Entry<String, Double>> sortedTransaksiList = totalTransaksiPerAkun.entrySet()
                .stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue())) // Sorting descending
                .toList();

        List<String> frauds = new ArrayList<>();

        // Periksa transaksi untuk setiap akun
        for (Map.Entry<String, Double> entry : sortedTransaksiList) {
            String noAkun = entry.getKey();

            if (detectFraud(transaksiByAkun.get(noAkun))) {
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


    public List<Transaksi> getJson() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("dummy_fraud_50.json").getFile());

        ObjectMapper objectMapper = new ObjectMapper();
        JavaTimeModule module = new JavaTimeModule();

        // Register custom serializers/deserializers for LocalDateTime
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(formatter));
        module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(formatter));

        objectMapper.registerModule(module);

//        List<Transaksi> transaksiList = objectMapper.readValue(file, new TypeReference<List<Transaksi>>() {
//        });
        List<Transaksi> transaksiList = objectMapper.readValue(jsonString, new TypeReference<List<Transaksi>>() {});



        for (Transaksi transaksi : transaksiList) {
            System.out.println(transaksi.getIdTransaksi());
        }

        return transaksiList;
    }
}
