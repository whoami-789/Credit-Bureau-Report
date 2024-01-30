package org.creditbureaureport.service;

import jakarta.persistence.EntityNotFoundException;
import org.apache.commons.net.ftp.FTPSClient;
import org.creditbureaureport.dto.*;
import org.creditbureaureport.model.AzolikFiz;
import org.creditbureaureport.model.Dokument;
import org.creditbureaureport.model.Kredit;
import org.creditbureaureport.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


@Service
public class ReportService {

    private final AzolikFizRepository azolikFizRepository;
    private final AzolikYurRepository yurReportRepository;

    private final DokRepository dokRepository;
    private final KreditRepository kreditRepository;
    private final SaldoRepository saldoRepository;


    @Autowired
    public ReportService(AzolikFizRepository reportRepository, AzolikYurRepository yurReportRepository, DokRepository dokRepository, KreditRepository kreditRepository, SaldoRepository saldoRepository) {
        this.azolikFizRepository = reportRepository;
        this.yurReportRepository = yurReportRepository;
        this.dokRepository = dokRepository;
        this.kreditRepository = kreditRepository;
        this.saldoRepository = saldoRepository;
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
        FTPSClient ftpsClient = new FTPSClient(); // Создаем FTPS клиент

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
                    telHome = "|";
                }

                writer.write("ID|MKOR0001||" + inputDateFormatter.format(fizProjection.getDatsIzm()) + "|" + (fizProjection.getKodchlen() != null ? fizProjection.getKodchlen() : "|") + "|" + fizProjection.getImya() + "|"
                        + fizProjection.getFam() + "|" + fizProjection.getOtch() + "|||" + genderCode + "|" + ((fizProjection.getDatsRojd() != null) ? inputDateFormatter.format(fizProjection.getDatsRojd()) : "")
                        + "||UZ||MI|" + new_address + "||||" + "|" + "||||||||||||1|" + fizProjection.getKodPension()
                        + ((fizProjection.getInn() != null) ? "|2|" + (fizProjection.getInn().replaceAll("\\s", "")) : "|") + "|1" + "|" +
                        ((fizProjection.getSerNumPasp() != null) ? (fizProjection.getSerNumPasp().replaceAll("\\s", "")) : "")
                        + "|" + ((fizProjection.getVidanPasp() != null) ? (inputDateFormatter.format(fizProjection.getVidanPasp())) : "") +
                        "||" + ((fizProjection.getPaspdo() != null) ? (inputDateFormatter.format(fizProjection.getPaspdo())) : "") + "|||||"
                        + telMobil + telHome + "|||||||||||||||||||||||||||||||||||");
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

            String zipFileName = file.getAbsolutePath() + ".zip";    // Создаем имя ZIP-файла

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

            try (FileInputStream zipFis = new FileInputStream(zipFileName)) {
                ftpsClient.connect("213.230.64.118", 990); // Адрес и порт сервера
                ftpsClient.login("MKOR0001", "Ph4NyTRD"); // Логин и пароль

                ftpsClient.execPBSZ(0);  // Защитный буфер
                ftpsClient.execPROT("P"); // Защита данных
                ftpsClient.enterLocalPassiveMode(); // Пассивный режим

                String remoteFilePath = "/Submission/Input/" + zipFileName; // Путь на сервере

                boolean result = ftpsClient.storeFile(remoteFilePath, zipFis); // Загрузка файла
                System.out.println("Upload status: " + result);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error" + e.getMessage());
        } finally {
            try {
                ftpsClient.disconnect();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
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

    public ReportDTO getReport(LocalDate startDate, LocalDate endDate) {
//        List<KreditDTO> kreditsByDocumentDate = getKreditsWithDetailsByDocumentDate(startDate, endDate);
        List<KreditDTO> kreditsByModificationDate = getKreditsWithDetails(startDate, endDate);

        return new ReportDTO(kreditsByModificationDate);
    }

    public List<KreditDTO> getKreditsWithDetails(LocalDate startDate, LocalDate endDate) {


        // Находим кредиты за заданный период
        List<Kredit> kredits = kreditRepository.findByDatsIzmBetween(startDate, endDate);

        System.out.println(kredits.size());
        Map<LocalDate, String> refDates = new LinkedHashMap<>();
        Set<String> processedEntries = new HashSet<>();


        // Трансформируем каждый Kredit в KreditDTO
        List<KreditDTO> kreditDTOList = kredits.stream().map(kredit -> {


            LocalDate current = startDate;
            while (current.isBefore(endDate) || current.equals(endDate)) {
                LocalDate endOfMonth = current.with(TemporalAdjusters.lastDayOfMonth());
                if (endOfMonth.isBefore(endDate) || endOfMonth.equals(endDate)) {
                    refDates.put(endOfMonth, "END_OF_MONTH");
                    current = endOfMonth.plusDays(1);
                } else {
                    // Если endDate не совпадает с концом месяца
                    if (!endDate.equals(endOfMonth)) {
                        refDates.put(endDate, "END_DATE");
                    }
                    break;
                }
            }

            System.out.println("Обработка кредита с ID: " + kredit.getNumdog() + "\n" + kredit.getLskred() + "\n" + kredit.getLsproc() + "\n" + kredit.getLsprosrKred() + "\n" + kredit.getLsprosrProc() + "\n" + kredit.getLspeni());
            KreditDTO kreditDTO = new KreditDTO();
            // Заполнение данных кредита
            kreditDTO.setNumdog(kredit.getNumdog());
            kreditDTO.setDatadog(kredit.getDatadog());
            kreditDTO.setProsent(kredit.getProsent());
            kreditDTO.setSumma(kredit.getSumma());
            kreditDTO.setDatsIzm(kredit.getDatsIzm());
            kreditDTO.setLspeni(kredit.getLspeni());
            kreditDTO.setLsprosrProc(kredit.getLsprosrProc());
            kreditDTO.setLsprosrKred(kredit.getLsprosrKred());
            kreditDTO.setStatus(kredit.getStatus());
            kreditDTO.setDatsZakr(kredit.getDatsZakr());
            kreditDTO.setVidKred(kredit.getVidkred());
            kreditDTO.setKod(kredit.getKod());
            kreditDTO.setLsKred(kredit.getLskred());
            kreditDTO.setLsProc(kredit.getLsproc());
            kreditDTO.setName(kredit.getAzolikFiz().getName());


            // Получение и добавление документов
            List<DokumentDTO> dokumentDTOs = dokRepository.findByKreditComplexConditions(
                    kredit.getLskred(), kredit.getLsproc(), kredit.getLsprosrKred(), kredit.getLsprosrProc(), kredit.getLspeni()
            ).stream().map(dokument -> {
                if (dokument == null) {
                    return null;
                }
                System.out.println(dokument.getLs() + "\n" + dokument.getLscor());
                DokumentDTO dokumentDTO = new DokumentDTO();
                // Заполнение данных документа
                dokumentDTO.setNumdok(dokument.getNumdok());
                dokumentDTO.setDats(dokument.getDats());
                dokumentDTO.setSums(dokument.getSums());
                dokumentDTO.setLs(dokument.getLs());
                dokumentDTO.setLscor(dokument.getLscor());

                return dokumentDTO;
            }).collect(Collectors.toList());
            kreditDTO.setDokuments(dokumentDTOs);

            List<SaldoDTO> saldoDTOS = saldoRepository.findByDokumentLscor(kredit.getLskred(), kredit.getLsproc(), kredit.getLsprosrKred(), kredit.getLsprosrProc(), kredit.getLspeni()
            ).stream().map(saldo -> {
                if (saldo == null) {
                    return null;
                }
                System.out.println(saldo.getLs());
                SaldoDTO saldoDTO = new SaldoDTO();
                // Заполнение данных сальдо
                saldoDTO.setSums(saldo.getSums());
                saldoDTO.setDats(saldo.getDats());
                saldoDTO.setLs(saldo.getLs());
                saldoDTO.setActivate(saldo.getActivate());
                //...
                return saldoDTO;
            }).filter(Objects::nonNull).collect(Collectors.toList());
            kreditDTO.setSaldo(saldoDTOS);

            // Получение и добавление графиков погашения
            List<GrafikDTO> grafikDTOs = kredit.getGrafiks().stream().map(grafik -> {
                GrafikDTO grafikDTO = new GrafikDTO();
                // Заполнение данных графика
                grafikDTO.setDats(grafik.getDats());
                grafikDTO.setPogKred(grafik.getPogKred());
                grafikDTO.setMes(grafik.getMes());
                grafikDTO.setPogKred(grafik.getPogKred());
                grafikDTO.setPogProc(grafik.getPogProc());
                // ...
                return grafikDTO;
            }).collect(Collectors.toList());
            kreditDTO.setGrafiks(grafikDTOs);

            List<ZalogDTO> zalogDTOs = kredit.getZalogs().stream().map(zalog -> {
                ZalogDTO zalogDTO = new ZalogDTO();
                zalogDTO.setSums(zalog.getSums());
                zalogDTO.setKodCb(zalog.getKodCb());
                // Дополнительное заполнение других полей ZalogDTO
                return zalogDTO;
            }).collect(Collectors.toList());
            kreditDTO.setZalogs(zalogDTOs);

            // Добавление данных из ZalogXranenie
            List<ZalogXranenieDTO> zalogXranenieDTOs = kredit.getZalogXranenieList().stream().map(zalogXranenie -> {
                ZalogXranenieDTO zalogXranenieDTO = new ZalogXranenieDTO();
                zalogXranenieDTO.setData_priem(zalogXranenie.getData_priem());
                zalogXranenieDTO.setData_vozvrat(zalogXranenie.getData_vozvrat());
                // Дополнительное заполнение других полей ZalogXranenieDTO
                return zalogXranenieDTO;
            }).collect(Collectors.toList());
            kreditDTO.setZalogXranenieList(zalogXranenieDTOs);
            return kreditDTO;

        }).filter(Objects::nonNull).collect(Collectors.toList());

        SimpleDateFormat outputDateFormat = new SimpleDateFormat("ddMMyyyy");
        DateTimeFormatter inputDateFormatter = DateTimeFormatter.ofPattern("ddMMyyyy");
        FTPSClient ftpsClient = new FTPSClient(); // Создаем FTPS клиент
        StringBuilder dataBuilder = new StringBuilder();

        String vidKred = "";
        String status = "";
        String vznos = "";
        String overdue = "";
        int tiplred = 0;
        LocalDate actualContractEndDate;
        int pod = 0;
        int principalOverduePaymentAmount = 0;
        int interestOverduePaymentsNumber;
        int principalOverduePaymentNumber;
        int overduePaymentsNumber = 0;

        for (KreditDTO kreditDTO : kreditDTOList) {

            System.out.println("Кредит: " + kreditDTO.getNumdog());

            if (kreditDTO.getVidKred() == 2) {
                vidKred = "25";
            } else if (kreditDTO.getVidKred() == 1) {
                vidKred = "30";
            } else if (kreditDTO.getVidKred() == 3) {
                vidKred = "32";
            }

            LocalDate latestDate = kreditDTO.getGrafiks().stream()
                    .max(Comparator.comparing(GrafikDTO::getDats))
                    .map(GrafikDTO::getDats)
                    .orElse(null);


            for (Map.Entry<LocalDate, String> entry : refDates.entrySet()) {
                LocalDate refDate = entry.getKey();

                LocalDate latestDocumentDateBeforeRef = kreditDTO.getDokuments().stream()
                        .filter(d ->
                                d.getLs().equals(kreditDTO.getLsKred()) ||
                                        d.getLs().equals(kreditDTO.getLsProc()) ||
                                        d.getLs().equals(kreditDTO.getLsprosrKred()) ||
                                        d.getLs().equals(kreditDTO.getLsprosrProc()) ||
                                        d.getLs().equals(kreditDTO.getLspeni())
                        )
                        .filter(d ->
                                d.getLscor().startsWith("10101") ||
                                        d.getLscor().startsWith("10503") ||
                                        d.getLscor().startsWith("10509")
                        )
                        .map(DokumentDTO::getDats)
                        .filter(d -> d != null && (d.isEqual(refDate) || d.isBefore(refDate)))
                        .max(Comparator.naturalOrder())
                        .orElse(null);

                Integer countedGrafik = Math.toIntExact(kreditDTO.getGrafiks().stream()
                        .map(GrafikDTO::getDats)
                        .count());

                if (kreditDTO.getDatsZakr() == null || kreditDTO.getDatsZakr().isAfter(refDate)) {
                    actualContractEndDate = null;
                } else {
                    actualContractEndDate = kreditDTO.getDatsZakr();
                }

                if (kreditDTO.getDatsZakr() == null || kreditDTO.getDatsZakr().isAfter(refDate)) {
                    status = "AC";
                } else if (latestDate != null && latestDate.isAfter(kreditDTO.getDatsZakr())) {
                    status = "CA";
                } else {
                    status = "CL";
                }

                BigDecimal grafikPogKred = kreditDTO.getGrafiks().stream()
                        .filter(g -> !g.getDats().isAfter(refDate))
                        .map(GrafikDTO::getPogKred)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                BigDecimal dokSums = kreditDTO.getDokuments().stream()
                        .filter(d -> d.getLs().equals(kreditDTO.getLsKred()) || d.getLs().equals(kreditDTO.getLsprosrKred()))
                        .filter(d -> d.getLscor().startsWith("10101")
                                || d.getLscor().startsWith("10503")
                                || d.getLscor().startsWith("10509"))
                        .map(DokumentDTO::getSums)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);


                BigDecimal saldoSumsLsKred = kreditDTO.getSaldo().stream()
                        .filter(s -> s.getLs().equals(kreditDTO.getLsKred()) && !s.getDats().isAfter(refDate))
                        .max(Comparator.comparing(SaldoDTO::getDats))
                        .map(SaldoDTO::getSums)
                        .orElse(BigDecimal.ZERO);

                BigDecimal saldoSumsLsProc = kreditDTO.getSaldo().stream()
                        .filter(s -> s.getLs().equals(kreditDTO.getLsProc()) && !s.getDats().isAfter(refDate))
                        .max(Comparator.comparing(SaldoDTO::getDats))
                        .map(SaldoDTO::getSums)
                        .orElse(BigDecimal.ZERO);

                BigDecimal saldoSumsLsprosrKred = kreditDTO.getSaldo().stream()
                        .filter(s -> s.getLs().equals(kreditDTO.getLsprosrKred()) && !s.getDats().isAfter(refDate))
                        .max(Comparator.comparing(SaldoDTO::getDats))
                        .map(SaldoDTO::getSums)
                        .orElse(BigDecimal.ZERO);

                BigDecimal saldoSumsLsPeni = kreditDTO.getSaldo().stream()
                        .filter(s -> s.getLs().equals(kreditDTO.getLspeni()) && !s.getDats().isAfter(refDate))
                        .max(Comparator.comparing(SaldoDTO::getDats))
                        .map(SaldoDTO::getSums)
                        .orElse(BigDecimal.ZERO);

                if (grafikPogKred.intValue() - dokSums.intValue() > 0) {
                    principalOverduePaymentAmount = grafikPogKred.intValue() - dokSums.intValue();
                } else if (grafikPogKred.intValue() - dokSums.intValue() < 0) {
                    principalOverduePaymentAmount = 0;
                }


                int outstandingBalance = (saldoSumsLsKred.intValue() + saldoSumsLsProc.intValue()
                        + (saldoSumsLsprosrKred != null ? saldoSumsLsprosrKred.intValue() : 0)
                        + (saldoSumsLsPeni != null ? saldoSumsLsPeni.intValue() : 0)) - principalOverduePaymentAmount;

                BigDecimal pogKredForMaxDate = kreditDTO.getGrafiks().stream()
                        .filter(g -> !g.getDats().isAfter(refDate)) // фильтрация записей до и включая reference_date
                        .max(Comparator.comparing(GrafikDTO::getDats)) // поиск максимальной даты
                        .map(GrafikDTO::getPogKred) // извлечение значения pog_kred
                        .orElse(BigDecimal.ZERO); // если нет подходящих записей, возвращаем 0


                Integer dokDatsLsLscor = Math.toIntExact(kreditDTO.getDokuments().stream()
                        .filter(d -> d.getDats().isEqual(refDate))
                        .filter(d -> d.getLs().equals(kreditDTO.getLsProc()) && d.getLscor().equals(kreditDTO.getLsprosrProc()))
                        .map(DokumentDTO::getDats)
                        .count());

                Integer dokDatsLsLscorStartsWith = Math.toIntExact(kreditDTO.getDokuments().stream()
                        .filter(d -> d.getDats().isEqual(refDate))
                        .filter(d -> d.getLs().equals(kreditDTO.getLsprosrProc())
                                && (d.getLscor().startsWith("10101")
                                || d.getLscor().startsWith("10503")
                                || d.getLscor().startsWith("10509")))
                        .map(DokumentDTO::getDats)
                        .count());

                BigDecimal sumsForMaxDate = kreditDTO.getSaldo().stream()
                        .filter(s -> s.getLs().equals(kreditDTO.getLsprosrProc()) && !s.getDats().isAfter(refDate)) // фильтрация записей
                        .max(Comparator.comparing(SaldoDTO::getDats)) // поиск максимальной даты
                        .map(SaldoDTO::getSums) // извлечение значения sums
                        .orElse(BigDecimal.ZERO); // если нет подходящих записей, возвращаем 0

                if (dokDatsLsLscor - dokDatsLsLscorStartsWith <= 0 && sumsForMaxDate.intValue() > 0) {
                    interestOverduePaymentsNumber = 1;
                } else if (dokDatsLsLscor - dokDatsLsLscorStartsWith > 0) {
                    interestOverduePaymentsNumber = dokDatsLsLscor - dokDatsLsLscorStartsWith;
                } else {
                    interestOverduePaymentsNumber = 0;
                }


                if (pogKredForMaxDate.intValue() == 0) {
                    principalOverduePaymentNumber = 0;
                } else principalOverduePaymentNumber = principalOverduePaymentAmount / pogKredForMaxDate.intValue();


                if (principalOverduePaymentNumber > interestOverduePaymentsNumber) {
                    overduePaymentsNumber = principalOverduePaymentNumber;
                } else if (principalOverduePaymentNumber < interestOverduePaymentsNumber) {
                    overduePaymentsNumber = interestOverduePaymentsNumber;
                } else if (principalOverduePaymentNumber == interestOverduePaymentsNumber) {
                    overduePaymentsNumber = principalOverduePaymentNumber;
                } else if (principalOverduePaymentNumber == 0 && interestOverduePaymentsNumber == 0) {
                    overduePaymentsNumber = 0;
                }

                SaldoDTO latestSaldoLsKred = kreditDTO.getSaldo().stream()
                        .filter(s -> s.getLs().equals(kreditDTO.getLsKred()))
                        .filter(s -> s.getDats().isBefore(refDate) || s.getDats().isEqual(refDate))
                        .max(Comparator.comparing(SaldoDTO::getDats))
                        .orElse(null);
                BigDecimal sumlsKred = latestSaldoLsKred != null ? latestSaldoLsKred.getSums() : null;


                SaldoDTO latestSaldoLsProc = kreditDTO.getSaldo().stream()
                        .filter(s -> s.getLs().equals(kreditDTO.getLsProc()))
                        .filter(s -> s.getDats().isBefore(refDate) || s.getDats().isEqual(refDate))
                        .max(Comparator.comparing(SaldoDTO::getDats))
                        .orElse(null);
                BigDecimal sumlsProc = latestSaldoLsProc != null ? latestSaldoLsProc.getSums() : null;


                SaldoDTO latestSaldoLsProsrKred = kreditDTO.getSaldo().stream()
                        .filter(s -> s.getLs().equals(kreditDTO.getLsprosrKred()))
                        .filter(s -> s.getDats().isBefore(refDate) || s.getDats().isEqual(refDate))
                        .max(Comparator.comparing(SaldoDTO::getDats))
                        .orElse(null);
                BigDecimal sumlsprosrKred = latestSaldoLsProsrKred != null ? latestSaldoLsProsrKred.getSums() : null;


                SaldoDTO latestSaldoLsProsrProc = kreditDTO.getSaldo().stream()
                        .filter(s -> s.getLs().equals(kreditDTO.getLsprosrProc()))
                        .filter(s -> s.getDats().isBefore(refDate) || s.getDats().isEqual(refDate))
                        .max(Comparator.comparing(SaldoDTO::getDats))
                        .orElse(null);
                BigDecimal sumlsprosrProc = latestSaldoLsProsrProc != null ? latestSaldoLsProsrProc.getSums() : null;


                SaldoDTO latestSaldoLsPeni = kreditDTO.getSaldo().stream()
                        .filter(s -> s.getLs().equals(kreditDTO.getLspeni()))
                        .filter(s -> s.getDats().isBefore(refDate) || s.getDats().isEqual(refDate))
                        .max(Comparator.comparing(SaldoDTO::getDats))
                        .orElse(null);
                BigDecimal sumlspeni = latestSaldoLsPeni != null ? latestSaldoLsPeni.getSums() : null;


                int outstandingPaymentNumber = Math.toIntExact(kreditDTO.getGrafiks().stream()
                        .map(GrafikDTO::getDats)
                        .filter(dats -> dats.isAfter(refDate) || dats.isEqual(refDate))
                        .count());


                if (status.equals("CA") || status.equals("CL")) outstandingPaymentNumber = 0;
                int totalSum = (sumlspeni != null ? sumlspeni.intValue() : 0)
                        + (sumlsProc != null ? sumlsProc.intValue() : 0)
                        + (sumlsKred != null ? sumlsKred.intValue() : 0)
                        + (sumlsprosrProc != null ? sumlsprosrProc.intValue() : 0)
                        + (sumlsprosrKred != null ? sumlsprosrKred.intValue() : 0);

                if (outstandingPaymentNumber > 0) {
                    pod = totalSum / outstandingPaymentNumber;
                } else {
                    pod = totalSum;
                }

                LocalDate firstPaymentDate = kreditDTO.getGrafiks().stream()
                        .min(Comparator.comparing(GrafikDTO::getDats))
                        .map(GrafikDTO::getDats)
                        .orElse(null);

                LocalDate nextPaymentDate = kreditDTO.getGrafiks().stream()
                        .filter(g -> g.getDats().isEqual(refDate) || g.getDats().isAfter(refDate))
                        .min(Comparator.comparing(GrafikDTO::getDats))
                        .map(GrafikDTO::getDats)
                        .orElse(null);

                if (nextPaymentDate == null) {
                    nextPaymentDate = null;
                } else if (kreditDTO.getDatsZakr() == null) {
                    nextPaymentDate = kreditDTO.getGrafiks().stream()
                            .filter(g -> g.getDats().isEqual(refDate) || g.getDats().isAfter(refDate))
                            .min(Comparator.comparing(GrafikDTO::getDats))
                            .map(GrafikDTO::getDats)
                            .orElse(null);
                } else if (status.equals("CA") || status.equals("CL")) {
                    nextPaymentDate = null;
                }

                String zalogLs = kreditDTO.getZalogs().stream()
                        .map(ZalogDTO::getLs)
                        .collect(Collectors.joining());

                String zalogKodCb = kreditDTO.getZalogs().stream()
                        .map(ZalogDTO::getKodCb)
                        .collect(Collectors.joining());

                BigDecimal zalogSums = kreditDTO.getZalogs().stream()
                        .map(ZalogDTO::getSums)
                        .findAny().orElse(null);


                List<OverdueDTO> overdueDTOs = kreditRepository.SpisProsrKred(kreditDTO.getNumdog(), refDate)
                        .stream()
                        .map(resultObject -> {
                            Object[] resultArray = (Object[]) resultObject; // Приведение к Object[]
                            System.out.println(Arrays.toString(resultArray));
                            OverdueDTO overdueDTO = new OverdueDTO();
                            overdueDTO.setPlannedPaymentDate((java.sql.Date) resultArray[1]); // Первый параметр
                            overdueDTO.setOverdueSumm((BigDecimal) resultArray[2]); // Второй параметр
                            overdueDTO.setOverduePeriod((String) resultArray[3]); // Третий параметр
                            return overdueDTO;
                        })
                        .collect(Collectors.toList());


                kreditDTO.setOverdue(overdueDTOs);

                String overduePeriod = kreditDTO.getOverdue().stream()
                        .map(OverdueDTO::getOverduePeriod)
                        .collect(Collectors.joining(", "));

                String uniqueKey = inputDateFormatter.format(refDate) + "_" + kreditDTO.getNumdog() + "_" + status;

                boolean isUniqueOrActive = !processedEntries.contains(uniqueKey) || "AC".equals(status);

                if (!kreditDTO.getDatadog().isAfter(refDate) && !(firstPaymentDate == null) && !(latestDate == null)) {
                    if (isUniqueOrActive) {
                        dataBuilder.append("CI|MKOR0001||")
                                .append(inputDateFormatter.format(refDate))
                                .append("|")
                                .append(kreditDTO.getKod())
                                .append("|B|")
                                .append(kreditDTO.getNumdog().replaceAll("\\s", ""))
                                .append("|")
                                .append(vidKred)
                                .append("|")
                                .append(status)
                                .append("||UZS|UZS|")
                                .append(inputDateFormatter.format(kreditDTO.getDatadog()))
                                .append("|")
                                .append(inputDateFormatter.format(kreditDTO.getDatadog()))
                                .append("|")
                                .append(inputDateFormatter.format(latestDate))
                                .append("|")
                                .append(actualContractEndDate != null ? inputDateFormatter.format(actualContractEndDate) : "")
                                .append("|")
                                .append(latestDocumentDateBeforeRef != null ? inputDateFormatter.format(latestDocumentDateBeforeRef) : "")
                                .append("||")
                                .append(kreditDTO.getSumma().intValue())
                                .append("|")
                                .append(countedGrafik)
                                .append("|M|MXD|")
                                .append(pod) // +
                                .append("|")
                                .append(inputDateFormatter.format(firstPaymentDate)) // +
                                .append("|")
                                .append((nextPaymentDate != null) ? inputDateFormatter.format(nextPaymentDate) : "") // +
                                .append("||")
                                .append(outstandingPaymentNumber) // +
                                .append("|")
                                ////////////////////////////////
                                .append(outstandingBalance) // +
                                .append("|")
                                .append(overduePaymentsNumber) // +
                                .append("|")
                                .append(principalOverduePaymentNumber) // +
                                .append("|")
                                .append(interestOverduePaymentsNumber) // +
                                .append("|")
                                .append(principalOverduePaymentAmount + sumsForMaxDate.intValue()) // +
                                .append("|")
                                .append(principalOverduePaymentAmount)// +
                                .append("|")
                                .append(sumsForMaxDate.intValue()) // +
                                .append("|")
                                .append(overduePeriod) // -
                                .append("||||||||||")
                                .append(kreditDTO.getProsent().intValue())
                                .append("||||")
                                .append(zalogLs)
                                .append("|")
                                .append(kreditDTO.getKod())
                                .append("|")
                                .append(kreditDTO.getName())
                                .append("|")
                                .append(zalogSums.intValue())
                                .append("|UZS|||")
                                .append(zalogKodCb)
                                .append("|||||||||||||||||||||||||||||||||||||")
                                .append("\n");

                        if (!"AC".equals(status)) processedEntries.add(uniqueKey);
                    }
                }
            }
        }

        String finalData = dataBuilder.toString();
        System.out.println(finalData);

        return kreditDTOList;
    }

}
