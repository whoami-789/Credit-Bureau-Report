package org.creditbureaureport.service;

import org.creditbureaureport.model.AzolikFiz;
import org.creditbureaureport.repository.AzolikFizRepository;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class ClientCorrectService {

    private final AzolikFizRepository azolikFizRepository;

    public ClientCorrectService(AzolikFizRepository azolikFizRepository) {
        this.azolikFizRepository = azolikFizRepository;
    }

    public List<AzolikFiz> getClientsWithDetails(String client) {

        List<AzolikFiz> fizProjections = azolikFizRepository.findByKodchlen(client);
        DateTimeFormatter inputDateFormatter = DateTimeFormatter.ofPattern("ddMMyyyy");
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("ddMMyyyy");
        Set<String> innAndPinfl = new HashSet<>();


        try {
            Date currentDate = new Date();

            // Задаем маску формата даты и времени
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            File file = new File("MKOR0001_CSDF_" + dateFormat.format(currentDate) + ".txt");
            FileOutputStream fos = new FileOutputStream(file);
            // Запись BOM для UTF-8 в начало файла
            fos.write(0xEF);
            fos.write(0xBB);
            fos.write(0xBF);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos, StandardCharsets.UTF_8));
            writer.write("HD|MKOR0001|" + outputDateFormat.format(currentDate) + "|1.0|1|Initial test");
            writer.newLine();


            for (AzolikFiz fizProjection : fizProjections) {
                String genderCode = fizProjection.getFsobst() == 1 ? "M" : "F";
                String new_address = "";
                if (fizProjection.getLsvznos() != null) System.out.println(fizProjection.getLsvznos());
                String address = fizProjection.getAdres();
                if (!address.contains("шахар") && !address.contains("туман") &&
                        !address.contains("tuman") && !address.contains("shahar")) {
                    Optional<String> nameuOptional = azolikFizRepository.findNameuByKod(fizProjection.getKodRayon());
                    if (nameuOptional.isPresent()) {
                        new_address = nameuOptional.get() + " " + fizProjection.getAdres();
                    }
                } else {
                    new_address = fizProjection.getAdres();
                }

                String telMobil = "";
                String telHome = "";

                if (!fizProjection.getTelmobil().replaceAll("\\s", "").isEmpty()) {
                    telMobil = "2|" + fizProjection.getTelmobil().replaceAll("\\s", "");
                } else {
                    telMobil = "|";
                }
                if (!fizProjection.getTelhome().replaceAll("\\s", "").isEmpty()) {
                    telHome = "|1|" + fizProjection.getTelmobil().replaceAll("\\s", "");
                } else {
                    telHome = "||";
                }


                String inn = "";
                String pinfl = "";

                if (fizProjection.getInn() != null && inn.matches("^(.)\\1+$")) {
                    fizProjection.setInn(null);
                }


                if (fizProjection.getInn() == null || fizProjection.getInn().trim().isEmpty()) {
                    inn = "|";
                } else {
                    inn = "2|" + fizProjection.getInn().replaceAll("\\s", "");
                }

                if (fizProjection.getKodPension() == null || fizProjection.getKodPension().trim().isEmpty()) {
                    pinfl = "||";
                } else {
                    pinfl = "1|" + fizProjection.getKodPension().replaceAll("\\s", "") + "|";
                }

                int innAndPinflCount = 0;

                if ((fizProjection.getInn() == null || fizProjection.getInn().trim().isEmpty()) ||
                        (fizProjection.getKodPension() == null || fizProjection.getKodPension().trim().isEmpty())) {
                    innAndPinfl.add(String.valueOf(innAndPinflCount + 1));
                }

                if (!(fizProjection.getInn() == null || fizProjection.getInn().trim().isEmpty()) ||
                        !(fizProjection.getKodPension() == null || fizProjection.getKodPension().trim().isEmpty())) {
                    writer.write("ID|MKOR0001||" + inputDateFormatter.format(fizProjection.getDatsIzm()) + "|" + (fizProjection.getKodchlen() != null ? fizProjection.getKodchlen() : "|") + "|" + fizProjection.getImya() + "|"
                            + fizProjection.getFam() + "|" + fizProjection.getOtch() + "|||" + genderCode + "|" + ((fizProjection.getDatsRojd() != null) ? inputDateFormatter.format(fizProjection.getDatsRojd()) : "")
                            + "||UZ||MI|" + new_address + "||||" + "|" + "||||||||||||" + pinfl + inn + "|1" + "|" +
                            ((fizProjection.getSerNumPasp() != null) ? (fizProjection.getSerNumPasp().replaceAll("\\s", "")) : "")
                            + "|" + ((fizProjection.getVidanPasp() != null) ? (inputDateFormatter.format(fizProjection.getVidanPasp())) : "") +
                            "||" + ((fizProjection.getPaspdo() != null) ? (inputDateFormatter.format(fizProjection.getPaspdo())) : "") + "||||||"
                            + telMobil + telHome + "|||||||||||||||||||||||||||||||||||");
                    writer.newLine(); // Добавить новую строку
                }
            }
            writer.write("FT|MKOR0001|" + outputDateFormat.format(currentDate) + "|" + (fizProjections.size() - innAndPinfl.size()));

            writer.close();

            String newFolder = "C:/Users/TPS-admin/Desktop/CRIF-reports/";
//            String newFolder = "/Users/rustamrahmov/Desktop/reports/";
            File directory = new File(newFolder);
            if (!directory.exists()) {
                directory.mkdirs(); // Создает папку и все родительские папки, если они не существуют
            }
            String zipFileName = newFolder + file.getName().replace(".txt", ".zip");

            try (
                    FileOutputStream fileos = new FileOutputStream(zipFileName);
                    ZipOutputStream zos = new ZipOutputStream(fileos);
                    FileInputStream fis2 = new FileInputStream(file)
            ) {
                ZipEntry zipEntry = new ZipEntry(file.getName());
                zos.putNextEntry(zipEntry);

                byte[] bytes = new byte[1024];
                int length;
                while ((length = fis2.read(bytes)) >= 0) {
                    zos.write(bytes, 0, length);
                }

                zos.closeEntry();
                System.out.println("Файл успешно упакован в архив: " + zipFileName);
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return fizProjections;
    }
}
