package org.creditbureaureport.service;

import org.creditbureaureport.dto.ContractDTO;
import org.creditbureaureport.dto.DateRangeDTO;
import org.creditbureaureport.model.AzolikFiz;
import org.creditbureaureport.model.AzolikYur;
import org.creditbureaureport.repository.AzolikFizRepository;
import org.creditbureaureport.repository.AzolikYurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static java.time.LocalDate.of;


@Service
public class ReportService {

    private final AzolikFizRepository azolikFizRepository;
    private final AzolikYurRepository yurReportRepository;


    @Autowired
    public ReportService(AzolikFizRepository reportRepository, AzolikYurRepository yurReportRepository) {
        this.azolikFizRepository = reportRepository;
        this.yurReportRepository = yurReportRepository;
    }

//    @Transactional(readOnly = true)
//    public List<AzolikFiz> getAllAzolikFiz() {
//        return creditBureauRepository.getAzolikFizRepository().findAll();
//    }

    public String getAllFizProjections(LocalDate startDate, LocalDate endDate) {
        List<AzolikFiz> fizProjections = azolikFizRepository.findByMonthAndYear(startDate, endDate);
//        List<AzolikYur> yurProjections = yurReportRepository.findByMonthAndYear(startDate, endDate);
        List<ContractDTO> contractDTOList = new ArrayList<>();
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("ddMMyyyy");
        DateTimeFormatter inputDateFormatter = DateTimeFormatter.ofPattern("ddMMyyyy");

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
            writer.write("HD|MKOR0001|" + "26122023" + "|1.0|1|Initial test");
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

                writer.write("ID|MKOR0001||" + inputDateFormatter.format(fizProjection.getDatsIzm()) + "|" + (fizProjection.getKodchlen() != null ? fizProjection.getKodchlen() : "|") + "|" + fizProjection.getImya() + "|"
                        + fizProjection.getFam() + "|" + fizProjection.getOtch() + "|||" + genderCode + "|" + ((fizProjection.getDatsRojd() != null) ? inputDateFormatter.format(fizProjection.getDatsRojd()) : "")
                        + "||UZ||MI|" + new_address + "||||" + "|" + "||||||||||||1|" + fizProjection.getKodPension()
                        + ((fizProjection.getInn() != null) ? "|2|" + (fizProjection.getInn().replaceAll("\\s", "")) : "|") + "|1" + "|" +
                        ((fizProjection.getSerNumPasp() != null) ? (fizProjection.getSerNumPasp().replaceAll("\\s", "")) : "")
                        + "|" + ((fizProjection.getVidanPasp() != null) ? (inputDateFormatter.format(fizProjection.getVidanPasp())) : "") +
                        "||" + ((fizProjection.getPaspdo() != null) ? (inputDateFormatter.format(fizProjection.getPaspdo())) : "") + "|||||"
                        + ((fizProjection.getTelmobil() != null) ? "|2|" + (fizProjection.getTelmobil().replaceAll("\\s", "")) : "|")
                        + ((fizProjection.getTelhome() != null) ? "|1|" + (fizProjection.getTelhome().replaceAll("\\s", "")) : "|")
                        + "|||||||||||||||||||||||||||||||||||");
                writer.newLine(); // Добавить новую строку
            }
//            for (AzolikYur yurProjection : yurProjections) {
//                writer.write("BD|MKOR0001||" + inputDateFormatter.format(yurProjection.getDatsIzm()) + "|" + yurProjection.getKodchlen() + "|" + yurProjection.getName() + "|" + yurProjection.getShotname() + "|" + "||||||||||" + yurProjection.getAdres() + "||||" + yurProjection.getKodRayon() + "|" + yurProjection.getKodObl() + "||||||||||||3|" + yurProjection.getInn() + "||||2|" + yurProjection.getTelmobil().replaceAll("\\s", "") + "|1|" + yurProjection.getTelhome().replaceAll("\\s", ""));
//                writer.newLine(); // Добавить новую строку
//            }
            List<Object[]> contracts = azolikFizRepository.findAzolikFizKreditSaldoGrafikDokZalogZalogDetalZalogXranenie(startDate, endDate);

            for (Object[] contract : contracts) {
                ContractDTO dto = new ContractDTO();
                dto.setKodchlen((String) contract[0]);
                dto.setName((String) contract[1]);
                dto.setNumdog((String) contract[2]);
                dto.setVidkred((contract[3].toString()));
                dto.setDatsIzm((Date) contract[4]);
                dto.setSost((Byte) contract[5]);
                dto.setStatus((Byte) contract[6]);
                dto.setDatadog((Date) contract[7]);
                dto.setDatsZakr((Date) contract[8]);
                dto.setSumma((BigDecimal) contract[9]);
                dto.setGraf((Byte) contract[10]);
                dto.setNalbeznal((Byte) contract[11]);
                dto.setXatar((Byte) contract[12]);
                dto.setTipkred((Short) contract[13]);
                dto.setProsent((BigDecimal) contract[14]);
                dto.setGrafikDats((Date) contract[16]);
                dto.setDokDats((Date) contract[15]);
                dto.setPod((int) contract[17]);
                dto.setBeforeReport((Date) contract[18]);
                dto.setSum_prosr((int) contract[19]);
                dto.setAfterReport((Date) contract[20]);
                dto.setSums_z((BigDecimal) contract[21]);
                dto.setKodCb((String) contract[22]);
                dto.setTotal_sums((BigDecimal) contract[23]);
                dto.setProsr_proc((BigDecimal) contract[24]);
                dto.setProsr_kred((BigDecimal) contract[25]);
                dto.setSumVznos((int) contract[26]);
                dto.setSumVznosAll((int) contract[27]);
                dto.setNext_summ((BigDecimal) contract[28]);
                dto.setCounted_payments((int) contract[29]);
                dto.setCount_sums_prosr_proc((int) contract[30]);
                dto.setCount_sums_prosr_kred((int) contract[31]);
                dto.setZ_ls((String) contract[32]);
                dto.setDate_priem((Date) contract[33]);
                dto.setDate_vozvrat((Date) contract[34]);
                dto.setOverdue((String) contract[35]);
                dto.setKlass((Byte) contract[36]);

                String vidKred = "";
                String status = "";
                String vznos = "";
                String overdue = "";
                int tiplred = 0;

                if (dto.getVidkred().equals("2")) {
                    vidKred = "25";
                } else if (dto.getVidkred().equals("1")) {
                    vidKred = "30";
                } else if (dto.getVidkred().equals("3")) {
                    vidKred = "32";
                }

                if (dto.getStatus() == 2) {
                    status = "AC";
                } else if (dto.getStatus() == 5) {
                    status = "CL";
                }

                if (dto.getSumVznosAll() == dto.getSumVznos()) {
                    vznos = "M";
                } else if ((dto.getSumVznosAll() == 12 && dto.getSumVznos() == 1) || (dto.getSumVznosAll() == 24 && dto.getSumVznos() == 2)) {
                    vznos = "Y";
                } else if ((dto.getSumVznosAll() != 12 && dto.getSumVznos() == 1) || (dto.getSumVznosAll() > dto.getSumVznos())) {
                    vznos = "I";
                }

                if (dto.getTipkred() == 2) {
                    tiplred = 3;
                } else tiplred = 1;

                if (dto.getOverdue().equals("0")) {
                    overdue = "";
                } else if (dto.getOverdue().equals("1-29")) {
                    overdue = "2";
                } else if (dto.getOverdue().equals("30-59")) {
                    overdue = "3";
                } else if (dto.getOverdue().equals("60-89")) {
                    overdue = "4";
                } else if (dto.getOverdue().equals("90-119")) {
                    overdue = "5";
                } else if (dto.getOverdue().equals("120-149")) {
                    overdue = "6";
                } else if (dto.getOverdue().equals("150-179")) {
                    overdue = "7";
                } else if (dto.getOverdue().equals(">=180")) {
                    overdue = "8";
                }

                int total_sum_prosr = dto.getCount_sums_prosr_proc() + dto.getCount_sums_prosr_kred();

                writer.write("CI|MKOR0001||" + outputDateFormat.format(dto.getDatsIzm()) + "|" + dto.getKodchlen() + "|B|" +
                        dto.getNumdog().replaceAll("\\s", "") + "|" + vidKred + "|" + status + "||UZS|UZS|" +
                        outputDateFormat.format(dto.getDatadog()) + "||" + outputDateFormat.format(dto.getGrafikDats()) + "|" +
                        ((dto.getDatsZakr() != null) ? outputDateFormat.format(dto.getDatsZakr()) : "") + "|" +
                        outputDateFormat.format(dto.getDokDats()) + "||" + dto.getSumma().intValue() + "|" + dto.getSumVznos() + "|" +
                        "M" + "|MXD|" + dto.getPod() + "|" + ((dto.getBeforeReport() != null) ? outputDateFormat.format(dto.getBeforeReport()) : "") +
                        "|" + dto.getSum_prosr() + "|" + ((dto.getAfterReport() != null) ? outputDateFormat.format(dto.getAfterReport()) : "") + "|" +
                        ((dto.getNext_summ() != null) ? dto.getNext_summ().intValue() : "") + "|" + (dto.getCounted_payments() != 0 ? dto.getCounted_payments() : "") +
                        "|" + (dto.getTotal_sums().doubleValue() != 0.00 ? dto.getTotal_sums().intValue() : "") + "|" +
                        (total_sum_prosr != 0 ? total_sum_prosr : "") +
                        "|" + (dto.getCount_sums_prosr_kred() != 0 ? dto.getCount_sums_prosr_kred() : "") + "|" +
                        (dto.getCount_sums_prosr_proc() != 0 ? dto.getCount_sums_prosr_proc() : "") + "|" + overdue + "|" +
                        "1|" + dto.getKlass() + "|" + tiplred + "|||||||" + dto.getProsent().intValue() + "||||" + dto.getZ_ls() + "|" +
                        dto.getKodchlen() + "|" + dto.getName() + "|" + dto.getSums_z().intValue() + "|UZS|" +
                        ((dto.getDate_priem() != null) ? outputDateFormat.format(dto.getDate_priem()) : "") + "|" +
                        ((dto.getDate_vozvrat() != null) ? outputDateFormat.format(dto.getDate_vozvrat()) : "") + "|" +
                        dto.getKodCb() + "|||D||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
                writer.newLine();
            }
            writer.write("FT|MKOR0001|26122023|" + (fizProjections.size() + contracts.size()));

            writer.close();

//            createZipArchiveWithUTF8BOM(file, new File(filePath));

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error" + e.getMessage());
        }

        return "Report compiled";
    }


//    public List<ContractDTO> findAzolikFizKreditSaldoGrafikDokZalogZalogDetalZalogXranenie() {
//        List<Object[]> results = azolikFizRepository.findAzolikFizKreditSaldoGrafikDokZalogZalogDetalZalogXranenie(11, 2023);
//        return mapToContractDTO(results);
//    }

    private List<ContractDTO> mapToContractDTO(List<Object[]> results) {
        return results.stream()
                .map(this::mapToObject)
                .collect(Collectors.toList());
    }

    private ContractDTO mapToObject(Object[] result) {
        ContractDTO dto = new ContractDTO();
        dto.setKodchlen((String) result[0]);
        dto.setName((String) result[1]);
        dto.setNumdog((String) result[2]);
        dto.setVidkred((result[3].toString()));
        dto.setDatsIzm((Date) result[4]);
        dto.setSost((Byte) result[5]);
        dto.setStatus((Byte) result[6]);
        dto.setDatadog((Date) result[7]);
        dto.setDatsZakr((Date) result[8]);
        dto.setSumma((BigDecimal) result[9]);
        dto.setGraf((Byte) result[10]);
        dto.setNalbeznal((Byte) result[11]);
        dto.setXatar((Byte) result[12]);
        dto.setTipkred((Short) result[13]);
        dto.setProsent((BigDecimal) result[14]);
        dto.setGrafikDats((Date) result[17]);
        dto.setDokDats((Date) result[16]);
        dto.setPod((int) result[18]);
        dto.setBeforeReport((Date) result[19]);
        dto.setAfterReport((Date) result[20]);
        dto.setSums_z((BigDecimal) result[21]);
        dto.setKodCb((String) result[22]);
        dto.setTotal_sums((BigDecimal) result[23]);
        dto.setProsr_proc((BigDecimal) result[24]);
        dto.setProsr_kred((BigDecimal) result[25]);


        return dto;
    }


}
