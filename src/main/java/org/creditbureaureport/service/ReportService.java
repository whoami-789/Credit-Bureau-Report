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
        List<Object[]> contracts = azolikFizRepository.findAzolikFizKreditSaldoGrafikDokZalogZalogDetalZalogXranenie();
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
                }
                else if (fizProjection.getLsvznos() != null) System.out.println(fizProjection.getLsvznos());

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
                }

                else if (fizProjection.getInn() == null && fizProjection.getPaspdo() == null &&
                        fizProjection.getTelhome() == null && fizProjection.getTelmobil() == null &&
                        fizProjection.getVidanPasp() == null && fizProjection.getDatsRojd() == null) {
                    writer.write("ID|MKOR0001||" + inputDateFormatter.format(fizProjection.getDatsIzm()) + "|" + fizProjection.getKodchlen() + "|" + fizProjection.getImya() + "|"
                            + fizProjection.getFam() + "|" + fizProjection.getOtch() + "|||" + genderCode + "|77777777"
                            + "||UZ||MI|" + fizProjection.getAdres() + "||||" + fizProjection.getKodRayon() + "|" + fizProjection.getKodObl() + "|||||||||||||1|" + fizProjection.getKodPension()
                            + "|2|77777777|" + "|" + fizProjection.getTipDok() + "|" + fizProjection.getSerNumPasp().replaceAll("\\s", "")
                            + "|77777777" + "||77777777|" + "||||||2|998777777777" + "|1|998777777777"
                            + "|||||||||||||||||||||||||||||||||||");
                }


                else if (fizProjection.getInn() == null && fizProjection.getPaspdo() == null && fizProjection.getTelhome() == null && fizProjection.getTelmobil() == null) {
                    writer.write("ID|MKOR0001||" + inputDateFormatter.format(fizProjection.getDatsIzm()) + "|" + fizProjection.getKodchlen() + "|" + fizProjection.getImya() + "|"
                            + fizProjection.getFam() + "|" + fizProjection.getOtch() + "|||" + genderCode + "|" + inputDateFormatter.format(fizProjection.getDatsRojd())
                            + "||UZ||MI|" + fizProjection.getAdres() + "||||" + fizProjection.getKodRayon() + "|" + fizProjection.getKodObl() + "|||||||||||||1|" + fizProjection.getKodPension()
                            + "|2|77777777|" + "|" + fizProjection.getTipDok() + "|" + fizProjection.getSerNumPasp().replaceAll("\\s", "")
                            + "|" + inputDateFormatter.format(fizProjection.getVidanPasp()) + "||77777777|" + "||||||2|998777777777" + "|1|998777777777"
                            + "|||||||||||||||||||||||||||||||||||");
                }

                else if (fizProjection.getInn() == null && fizProjection.getPaspdo() == null &&
                        fizProjection.getDatsRojd() == null && fizProjection.getSerNumPasp() == null &&
                        fizProjection.getVidanPasp() == null && fizProjection.getTelhome() == null) {
                    writer.write("ID|MKOR0001||" + inputDateFormatter.format(fizProjection.getDatsIzm()) + "|" + fizProjection.getKodchlen() + "|" + fizProjection.getImya() + "|"
                            + fizProjection.getFam() + "|" + fizProjection.getOtch() + "|||" + genderCode + "|77777777"
                            + "||UZ||MI|" + fizProjection.getAdres() + "||||" + fizProjection.getKodRayon() + "|" + fizProjection.getKodObl() + "|||||||||||||1|" + fizProjection.getKodPension()
                            + "|2|77777777|" + "|" + fizProjection.getTipDok() + "|77777777"
                            + "|77777777" + "||77777777|" + "||||||2|"
                            + fizProjection.getTelmobil().replaceAll("\\s", "") + "|1|77777777"
                            + "|||||||||||||||||||||||||||||||||||");
                }

                else if (fizProjection.getInn() == null && fizProjection.getPaspdo() == null) {
                    writer.write("ID|MKOR0001||" + inputDateFormatter.format(fizProjection.getDatsIzm()) + "|" + fizProjection.getKodchlen() + "|" + fizProjection.getImya() + "|"
                            + fizProjection.getFam() + "|" + fizProjection.getOtch() + "|||" + genderCode + "|" + inputDateFormatter.format(fizProjection.getDatsRojd())
                            + "||UZ||MI|" + fizProjection.getAdres() + "||||" + fizProjection.getKodRayon() + "|" + fizProjection.getKodObl() + "|||||||||||||1|" + fizProjection.getKodPension()
                            + "|2|77777777|" + "|" + fizProjection.getTipDok() + "|" + fizProjection.getSerNumPasp().replaceAll("\\s", "")
                            + "|" + inputDateFormatter.format(fizProjection.getVidanPasp()) + "||77777777" + "||||||2|"
                            + fizProjection.getTelmobil().replaceAll("\\s", "") + "|1|" + fizProjection.getTelhome().replaceAll("\\s", "")
                            + "|||||||||||||||||||||||||||||||||||");
                }

                else if (fizProjection.getInn() == null) {
                    writer.write("ID|MKOR0001||" + inputDateFormatter.format(fizProjection.getDatsIzm()) + "|" + fizProjection.getKodchlen() + "|" + fizProjection.getImya() + "|"
                            + fizProjection.getFam() + "|" + fizProjection.getOtch() + "|||" + genderCode + "|" + inputDateFormatter.format(fizProjection.getDatsRojd())
                            + "||UZ||MI|" + fizProjection.getAdres() + "||||" + fizProjection.getKodRayon() + "|" + fizProjection.getKodObl() + "|||||||||||||1|" + fizProjection.getKodPension()
                            + "|2|77777777|" + "|" + fizProjection.getTipDok() + "|" + fizProjection.getSerNumPasp().replaceAll("\\s", "")
                            + "|" + inputDateFormatter.format(fizProjection.getVidanPasp()) + "||" + inputDateFormatter.format(fizProjection.getPaspdo()) + "||||||2|"
                            + fizProjection.getTelmobil().replaceAll("\\s", "") + "|1|" + fizProjection.getTelhome().replaceAll("\\s", "")
                            + "|||||||||||||||||||||||||||||||||||");
                }

                else if (fizProjection.getPaspdo() == null) {
                    writer.write("ID|MKOR0001||" + inputDateFormatter.format(fizProjection.getDatsIzm()) + "|" + fizProjection.getKodchlen() + "|" + fizProjection.getImya() + "|"
                            + fizProjection.getFam() + "|" + fizProjection.getOtch() + "|||" + genderCode + "|" + inputDateFormatter.format(fizProjection.getDatsRojd())
                            + "||UZ||MI|" + fizProjection.getAdres() + "||||" + fizProjection.getKodRayon() + "|" + fizProjection.getKodObl() + "|||||||||||||1|" + fizProjection.getKodPension()
                            + "|2|" + fizProjection.getInn().replaceAll("\\s", "") + "|" + fizProjection.getTipDok() + "|" + fizProjection.getSerNumPasp().replaceAll("\\s", "")
                            + "|" + inputDateFormatter.format(fizProjection.getVidanPasp()) + "||77777777|" + "||||||2|"
                            + fizProjection.getTelmobil().replaceAll("\\s", "") + "|1|" + fizProjection.getTelhome().replaceAll("\\s", "")
                            + "|||||||||||||||||||||||||||||||||||");
                }

                else {
                    writer.write("ID|MKOR0001||" + inputDateFormatter.format(fizProjection.getDatsIzm()) + "|" + fizProjection.getKodchlen() + "|" + fizProjection.getImya() + "|"
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
        List<Object[]> results = azolikFizRepository.findAzolikFizKreditSaldoGrafikDokZalogZalogDetalZalogXranenie();
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
        dto.setKod((String) result[5]);
        dto.setSost((Byte) result[6]);
        dto.setStatus((Byte) result[7]);
        dto.setValut((Integer) result[8]);
        dto.setDatadog((Date) result[9]);
        dto.setDats((Date) result[10]);
        dto.setDatsZakr((Date) result[11]);
        dto.setSumma((BigDecimal) result[12]);
        dto.setGraf((Byte) result[13]);
        dto.setNalbeznal((Byte) result[14]);
        dto.setXatar((Byte) result[15]);
        dto.setTipkred((Short) result[16]);
        dto.setProsent((BigDecimal) result[17]);
        dto.setSaldoSums((BigDecimal) result[18]);
        dto.setGrafikDats((Date) result[19]);
        dto.setPogKredAndPogProc((BigDecimal) result[20]);
        dto.setDokDats((Date) result[21]);
        dto.setZalogSums((BigDecimal) result[22]);
        dto.setKodCb((String) result[23]);

        return dto;
    }


}
