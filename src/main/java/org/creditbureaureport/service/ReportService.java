package org.creditbureaureport.service;

import org.creditbureaureport.dto.ContractDTO;
import org.creditbureaureport.dto.FizDTO;
import org.creditbureaureport.dto.YurDTO;
import org.creditbureaureport.model.AzolikFiz;
import org.creditbureaureport.model.AzolikYur;
import org.creditbureaureport.repository.AzolikFizRepository;
import org.creditbureaureport.repository.AzolikYurRepository;
import org.creditbureaureport.repository.CreditBureauRepository;
import org.creditbureaureport.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ReportService {

    private final AzolikFizRepository azolikFizRepository;
    private final AzolikYurRepository yurReportRepository;
    private final DocumentRepository documentRepository;
    private final CreditBureauRepository creditBureauRepository;


    @Autowired
    public ReportService(AzolikFizRepository reportRepository, AzolikYurRepository yurReportRepository, DocumentRepository documentRepository, CreditBureauRepository creditBureauRepository) {
        this.azolikFizRepository = reportRepository;
        this.yurReportRepository = yurReportRepository;
        this.documentRepository = documentRepository;
        this.creditBureauRepository = creditBureauRepository;
    }

    @Transactional(readOnly = true)
    public List<AzolikFiz> getAllAzolikFiz() {
        return creditBureauRepository.getAzolikFizRepository().findAll();
    }

    public String getAllFizProjections() {
        List<AzolikFiz> fizProjections = azolikFizRepository.findByMonthAndYear(11, 2023);
        List<AzolikYur> yurProjections = yurReportRepository.findByMonthAndYear(12, 2023);
        List<Object[]> contracts = azolikFizRepository.findAzolikFizKreditSaldoGrafikDokZalogZalogDetalZalogXranenie(11, 2023);
        List<ContractDTO> contractDTOList = new ArrayList<>();
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("ddMMyyyy");
        DateTimeFormatter inputDateFormatter = DateTimeFormatter.ofPattern("ddMMyyyy");

        try {
            Date currentDate = new Date();

            // Задаем маску формата даты и времени
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            File file = new File("MKOR0001_CSDF_" + dateFormat.format(currentDate) + ".txt");
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write("HD|MKOR0001|" + "26122023" + "|1.0|1|Initial test");
            writer.newLine();
            for (AzolikFiz fizProjection : fizProjections) {
                String genderCode = fizProjection.getFsobst() == 1 ? "M" : "F";


                if (fizProjection.getIndpred() == 1) {
                    writer.write("ID|MKOR0001||" + outputDateFormat.format(fizProjection.getDatsIzm()) + "|" + fizProjection.getKodchlen() + "|" + fizProjection.getImya() + "|" + fizProjection.getFam() + "|" + fizProjection.getOtch() + "|||" + genderCode + "|" + outputDateFormat.format(fizProjection.getDatsRojd()) + "||UZ||MI|" + fizProjection.getAdres() + "||||" + fizProjection.getKodRayon() + "|" + fizProjection.getKodObl() + "|||||||||||||1|" + fizProjection.getKodPension() + "|2|" + fizProjection.getInn().replaceAll("\\s", "") + "|" + fizProjection.getTipDok() + "|" + fizProjection.getSerNumPasp().replaceAll("\\s", "") + "|" + outputDateFormat.format(fizProjection.getVidanPasp()) + "||" + outputDateFormat.format(fizProjection.getPaspdo()) + "||||||2|" + fizProjection.getTelmobil().replaceAll("\\s", "") + "|1|" + fizProjection.getTelhome().replaceAll("\\s", "") + "||||||||" + fizProjection.getName() + "||" + fizProjection.getAdres() + "||||" + fizProjection.getKodRayon() + "|" + fizProjection.getKodObl() + "||||||||||||3|" + fizProjection.getInn() + "|||2|" + fizProjection.getTelmobil() + "|1|" + fizProjection.getTelhome());
                } else if (fizProjection.getLsvznos() != null) System.out.println(fizProjection.getLsvznos());

                else if (fizProjection.getInn() == null && fizProjection.getPaspdo() == null &&
                        fizProjection.getVidanPasp() == null && fizProjection.getDatsRojd() == null &&
                        fizProjection.getSerNumPasp() == null && fizProjection.getTelmobil() == null &&
                        fizProjection.getTelhome() == null) {
                    writer.write("ID|MKOR0001||" + inputDateFormatter.format(fizProjection.getDatsIzm()) + "|" + fizProjection.getKodchlen() + "|" + fizProjection.getImya() + "|"
                            + fizProjection.getFam() + "|" + fizProjection.getOtch() + "|||" + genderCode + "|77777777"
                            + "||UZ||MI|" + fizProjection.getAdres() + "||||" + fizProjection.getKodRayon() + "|" + fizProjection.getKodObl() + "|||||||||||||1|" + fizProjection.getKodPension()
                            + "|2|77777777|" + "|" + fizProjection.getTipDok() + "|77777777"
                            + "|77777777" + "||77777777|" + "||||||2|77777777"
                            + "|1|77777777"
                            + "|||||||||||||||||||||||||||||||||||");
                } else if (fizProjection.getInn() == null && fizProjection.getPaspdo() == null &&
                        fizProjection.getTelhome() == null && fizProjection.getTelmobil() == null &&
                        fizProjection.getVidanPasp() == null && fizProjection.getDatsRojd() == null) {
                    writer.write("ID|MKOR0001||" + inputDateFormatter.format(fizProjection.getDatsIzm()) + "|" + fizProjection.getKodchlen() + "|" + fizProjection.getImya() + "|"
                            + fizProjection.getFam() + "|" + fizProjection.getOtch() + "|||" + genderCode + "|77777777"
                            + "||UZ||MI|" + fizProjection.getAdres() + "||||" + fizProjection.getKodRayon() + "|" + fizProjection.getKodObl() + "|||||||||||||1|" + fizProjection.getKodPension()
                            + "|2|77777777|" + "|" + fizProjection.getTipDok() + "|" + fizProjection.getSerNumPasp().replaceAll("\\s", "")
                            + "|77777777" + "||77777777|" + "||||||2|998777777777" + "|1|998777777777"
                            + "|||||||||||||||||||||||||||||||||||");
                } else if (fizProjection.getInn() == null && fizProjection.getPaspdo() == null && fizProjection.getTelhome() == null && fizProjection.getTelmobil() == null) {
                    writer.write("ID|MKOR0001||" + inputDateFormatter.format(fizProjection.getDatsIzm()) + "|" + fizProjection.getKodchlen() + "|" + fizProjection.getImya() + "|"
                            + fizProjection.getFam() + "|" + fizProjection.getOtch() + "|||" + genderCode + "|" + inputDateFormatter.format(fizProjection.getDatsRojd())
                            + "||UZ||MI|" + fizProjection.getAdres() + "||||" + fizProjection.getKodRayon() + "|" + fizProjection.getKodObl() + "|||||||||||||1|" + fizProjection.getKodPension()
                            + "|2|77777777|" + "|" + fizProjection.getTipDok() + "|" + fizProjection.getSerNumPasp().replaceAll("\\s", "")
                            + "|" + inputDateFormatter.format(fizProjection.getVidanPasp()) + "||77777777|" + "||||||2|998777777777" + "|1|998777777777"
                            + "|||||||||||||||||||||||||||||||||||");
                } else if (fizProjection.getInn() == null && fizProjection.getPaspdo() == null &&
                        fizProjection.getDatsRojd() == null && fizProjection.getSerNumPasp() == null &&
                        fizProjection.getVidanPasp() == null && fizProjection.getTelhome() == null) {
                    writer.write("ID|MKOR0001||" + inputDateFormatter.format(fizProjection.getDatsIzm()) + "|" + fizProjection.getKodchlen() + "|" + fizProjection.getImya() + "|"
                            + fizProjection.getFam() + "|" + fizProjection.getOtch() + "|||" + genderCode + "|77777777"
                            + "||UZ||MI|" + fizProjection.getAdres() + "||||" + fizProjection.getKodRayon() + "|" + fizProjection.getKodObl() + "|||||||||||||1|" + fizProjection.getKodPension()
                            + "|2|77777777|" + "|" + fizProjection.getTipDok() + "|77777777"
                            + "|77777777" + "||77777777|" + "||||||2|"
                            + fizProjection.getTelmobil().replaceAll("\\s", "") + "|1|77777777"
                            + "|||||||||||||||||||||||||||||||||||");
                } else if (fizProjection.getInn() == null && fizProjection.getPaspdo() == null) {
                    writer.write("ID|MKOR0001||" + inputDateFormatter.format(fizProjection.getDatsIzm()) + "|" + fizProjection.getKodchlen() + "|" + fizProjection.getImya() + "|"
                            + fizProjection.getFam() + "|" + fizProjection.getOtch() + "|||" + genderCode + "|" + inputDateFormatter.format(fizProjection.getDatsRojd())
                            + "||UZ||MI|" + fizProjection.getAdres() + "||||" + fizProjection.getKodRayon() + "|" + fizProjection.getKodObl() + "|||||||||||||1|" + fizProjection.getKodPension()
                            + "|2|77777777|" + "|" + fizProjection.getTipDok() + "|" + fizProjection.getSerNumPasp().replaceAll("\\s", "")
                            + "|" + inputDateFormatter.format(fizProjection.getVidanPasp()) + "||77777777" + "||||||2|"
                            + fizProjection.getTelmobil().replaceAll("\\s", "") + "|1|" + fizProjection.getTelhome().replaceAll("\\s", "")
                            + "|||||||||||||||||||||||||||||||||||");
                } else if (fizProjection.getInn() == null) {
                    writer.write("ID|MKOR0001||" + inputDateFormatter.format(fizProjection.getDatsIzm()) + "|" + fizProjection.getKodchlen() + "|" + fizProjection.getImya() + "|"
                            + fizProjection.getFam() + "|" + fizProjection.getOtch() + "|||" + genderCode + "|" + inputDateFormatter.format(fizProjection.getDatsRojd())
                            + "||UZ||MI|" + fizProjection.getAdres() + "||||" + fizProjection.getKodRayon() + "|" + fizProjection.getKodObl() + "|||||||||||||1|" + fizProjection.getKodPension()
                            + "|2|77777777|" + "|" + fizProjection.getTipDok() + "|" + fizProjection.getSerNumPasp().replaceAll("\\s", "")
                            + "|" + inputDateFormatter.format(fizProjection.getVidanPasp()) + "||" + inputDateFormatter.format(fizProjection.getPaspdo()) + "||||||2|"
                            + fizProjection.getTelmobil().replaceAll("\\s", "") + "|1|" + fizProjection.getTelhome().replaceAll("\\s", "")
                            + "|||||||||||||||||||||||||||||||||||");
                } else if (fizProjection.getPaspdo() == null) {
                    writer.write("ID|MKOR0001||" + inputDateFormatter.format(fizProjection.getDatsIzm()) + "|" + fizProjection.getKodchlen() + "|" + fizProjection.getImya() + "|"
                            + fizProjection.getFam() + "|" + fizProjection.getOtch() + "|||" + genderCode + "|" + inputDateFormatter.format(fizProjection.getDatsRojd())
                            + "||UZ||MI|" + fizProjection.getAdres() + "||||" + fizProjection.getKodRayon() + "|" + fizProjection.getKodObl() + "|||||||||||||1|" + fizProjection.getKodPension()
                            + "|2|" + fizProjection.getInn().replaceAll("\\s", "") + "|" + fizProjection.getTipDok() + "|" + fizProjection.getSerNumPasp().replaceAll("\\s", "")
                            + "|" + inputDateFormatter.format(fizProjection.getVidanPasp()) + "||77777777|" + "||||||2|"
                            + fizProjection.getTelmobil().replaceAll("\\s", "") + "|1|" + fizProjection.getTelhome().replaceAll("\\s", "")
                            + "|||||||||||||||||||||||||||||||||||");
                } else {
                    writer.write("ID|MKOR0001||" + inputDateFormatter.format(fizProjection.getDatsIzm()) + "|" + (fizProjection.getKodchlen() != null ? fizProjection.getKodchlen() : "||") + "|" + fizProjection.getImya() + "|"
                            + fizProjection.getFam() + "|" + fizProjection.getOtch() + "|||" + genderCode + "|" + inputDateFormatter.format(fizProjection.getDatsRojd())
                            + "||UZ||MI|" + fizProjection.getAdres() + "||||" + fizProjection.getKodRayon() + "|" + fizProjection.getKodObl() + "|||||||||||||1|" + fizProjection.getKodPension()
                            + "|2|" + fizProjection.getInn().replaceAll("\\s", "") + "|" + fizProjection.getTipDok() + "|" + fizProjection.getSerNumPasp().replaceAll("\\s", "")
                            + "|" + inputDateFormatter.format(fizProjection.getVidanPasp()) + "||" + inputDateFormatter.format(fizProjection.getPaspdo()) + "||||||2|"
                            + fizProjection.getTelmobil().replaceAll("\\s", "") + "|1|" + fizProjection.getTelhome().replaceAll("\\s", "")
                            + "|||||||||||||||||||||||||||||||||||");
                }
                writer.newLine(); // Добавить новую строку
            }
//            for (AzolikYur yurProjection : yurProjections) {
//                writer.write("BD|MKOR0001||" + inputDateFormatter.format(yurProjection.getDatsIzm()) + "|" + yurProjection.getKodchlen() + "|" + yurProjection.getName() + "|" + yurProjection.getShotname() + "|" + "||||||||||" + yurProjection.getAdres() + "||||" + yurProjection.getKodRayon() + "|" + yurProjection.getKodObl() + "||||||||||||3|" + yurProjection.getInn() + "||||2|" + yurProjection.getTelmobil().replaceAll("\\s", "") + "|1|" + yurProjection.getTelhome().replaceAll("\\s", ""));
//                writer.newLine(); // Добавить новую строку
//            }
//            for (Object[] contract : contracts) {
//                System.out.println(Arrays.toString(contract));
//                writer.write("CI|MKOR0001||" + contract[8] + "|" + contract[0] + "|B|" + contract[1] + "|" + contract[2] + "|" + contract[3] + "|" + contract[4] + "|UZS|UZS|" + contract[7] + "|" + contract[16] + "|" + contract[18] + "||" + contract[10] + "|" + contract[11] + "||" + contract[12] + "|" + contract[17] + "|" + contract[16] + "|" + contract[16] + "|" + contract[17] + "||" + contract[19] + "||||" + contract[19] + "|" + contract[20] + "|" + contract[21] + "|" + contract[22] + "|" + contract[23]);
//                writer.newLine(); // Добавить новую строку
//            }
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
                dto.setGrafikDats((Date) contract[17]);
                dto.setDokDats((Date) contract[16]);
                dto.setPod((int) contract[18]);
                dto.setBeforeReport((Date) contract[19]);
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

                String vidKred = "";
                String status = "";
                String vznos = "";
                if (dto.getVidkred().equals("2")) {
                    vidKred = "25";
                } else if (dto.getVidkred().equals("1")) {
                    vidKred = "30";
                } else if (dto.getVidkred().equals("3")) {
                    vidKred = "32";
                }

                if (dto.getStatus().equals("2")) {
                    status = "AC";
                } else if (dto.getStatus().equals("5")) {
                    status = "CL";
                }

                if (dto.getSumVznosAll() == dto.getSumVznos()) {
                    vznos = "M";
                } else if ((dto.getSumVznosAll() == 12 && dto.getSumVznos() == 1) || (dto.getSumVznosAll() == 24 && dto.getSumVznos() == 2)) {
                    vznos = "Y";
                } else if ((dto.getSumVznosAll() != 12 && dto.getSumVznos() == 1) || (dto.getSumVznosAll() > dto.getSumVznos())) {
                    vznos = "I";
                }

                writer.write("CI|MKOR0001||" + outputDateFormat.format(dto.getDatsIzm()) + "|" + dto.getKodchlen() + "|B|" +
                        dto.getNumdog().replaceAll("\\s", "") + "|" + vidKred + "|" + status + "|UZS|UZS|" +
                        outputDateFormat.format(dto.getDatadog()) + "||" + outputDateFormat.format(dto.getGrafikDats()) + "|" +
                        ((dto.getDatsZakr() != null) ? outputDateFormat.format(dto.getDatsZakr()) : "") + "|" +
                        outputDateFormat.format(dto.getDokDats()) + "||" + dto.getSumma() + "|" + dto.getSumVznos() + "|" +
                        vznos + "|MXD|" + dto.getPod() + "|" + ((dto.getBeforeReport() != null) ? outputDateFormat.format(dto.getBeforeReport()) : "") +
                        "|" + ((dto.getAfterReport() != null) ? outputDateFormat.format(dto.getAfterReport()) : "") + "|" +
                        ((dto.getNext_summ() != null) ? dto.getNext_summ() : "") + "|" + (dto.getCounted_payments() != 0 ? dto.getCounted_payments() : "") +
                        "|" + (dto.getTotal_sums().doubleValue() != 0.00 ? dto.getTotal_sums() : "") + "|" +
                        (dto.getCount_sums_prosr_proc() + dto.getCount_sums_prosr_kred() != 0 ? dto.getCount_sums_prosr_proc() + dto.getCount_sums_prosr_kred() : "") +
                        "|" + (dto.getCount_sums_prosr_kred() != 0 ? dto.getCount_sums_prosr_kred() : "") + "|" +
                        (dto.getCount_sums_prosr_proc() != 0 ? dto.getCount_sums_prosr_proc() : "") + "|");
                writer.newLine();
            }
            writer.write("FT|MKOR0001|26122023|" + fizProjections.size());

            writer.close();

//            createZipArchiveWithUTF8BOM(file, new File(filePath));

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error" + e.getMessage());
        }

        return "Report compiled";
    }

//    private static void createZipArchiveWithUTF8BOM(String zipFileName, File fileToZip) throws IOException {
//        try (FileOutputStream fos = new FileOutputStream(zipFileName);
//             ZipArchiveOutputStream zos = new ZipArchiveOutputStream(fos)) {
//
//            // Создаем новую запись в архиве
//            ZipArchiveEntry entry = new ZipArchiveEntry(fileToZip.getName());
//            zos.putArchiveEntry(entry);
//
//            // Записываем данные файла в архив с использованием UTF-8-BOM
//            try (OutputStreamWriter writer = new OutputStreamWriter(zos, StandardCharsets.UTF_8)) {
//                writer.write('\ufeff'); // Записываем BOM
//                IOUtils.copyLarge(fileToZip, writer);
//            }
//
//            // Закрываем текущую запись в архиве
//            zos.closeArchiveEntry();
//        }
//    }

    private FizDTO mapToFizDTO(AzolikFiz fizProjection) {
        FizDTO dto = new FizDTO();
        dto.setKodchlen(fizProjection.getKodchlen());
        dto.setDatsIzm(fizProjection.getDatsIzm());
        dto.setName(fizProjection.getName());
        dto.setFam(fizProjection.getFam());
        dto.setImya(fizProjection.getImya());
        dto.setOtch(fizProjection.getOtch());
        dto.setFsobst(fizProjection.getFsobst());
        dto.setDatsRojd(fizProjection.getDatsRojd());
        dto.setAdres(fizProjection.getAdres());
        dto.setKodRayon(fizProjection.getKodRayon());
        dto.setKodObl(fizProjection.getKodObl());
        dto.setKodPension(fizProjection.getKodPension());
        dto.setInn(fizProjection.getInn());
        dto.setTipDok(fizProjection.getTipDok());
        dto.setSerNumPasp(fizProjection.getSerNumPasp());
        dto.setVidanPasp(fizProjection.getVidanPasp());
        dto.setPaspdo(fizProjection.getPaspdo());
        dto.setTelmobil(fizProjection.getTelmobil());
        dto.setTelhome(fizProjection.getTelhome());
        dto.setIndpred(fizProjection.getIndpred());
        return dto;
    }

    private YurDTO mapToYurDTO(AzolikYur yurProjection) {
        YurDTO dto = new YurDTO();
        dto.setKodchlen(yurProjection.getKodchlen());
        dto.setDatsIzm(yurProjection.getDatsIzm());
        dto.setName(yurProjection.getName());
        dto.setAdres(yurProjection.getAdres());
        dto.setKodRayon(yurProjection.getKodRayon());
        dto.setKodObl(yurProjection.getKodObl());
        dto.setTelmobil(yurProjection.getTelmobil());
        dto.setTelhome(yurProjection.getTelhome());
        return dto;
    }


    public List<ContractDTO> findAzolikFizKreditSaldoGrafikDokZalogZalogDetalZalogXranenie() {
        List<Object[]> results = azolikFizRepository.findAzolikFizKreditSaldoGrafikDokZalogZalogDetalZalogXranenie(11, 2023);
        return mapToContractDTO(results);
    }

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
