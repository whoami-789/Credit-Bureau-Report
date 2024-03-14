package org.creditbureaureport.service;

import org.creditbureaureport.dto.*;
import org.creditbureaureport.model.AzolikFiz;
import org.creditbureaureport.model.Kredit;
import org.creditbureaureport.model.Zalog;
import org.creditbureaureport.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
    private final ZalogRepository zalogRepository;


    @Autowired
    public ReportService(AzolikFizRepository reportRepository, AzolikYurRepository yurReportRepository, DokRepository dokRepository, KreditRepository kreditRepository, SaldoRepository saldoRepository, ZalogRepository zalogRepository) {
        this.azolikFizRepository = reportRepository;
        this.yurReportRepository = yurReportRepository;
        this.dokRepository = dokRepository;
        this.kreditRepository = kreditRepository;
        this.saldoRepository = saldoRepository;
        this.zalogRepository = zalogRepository;
    }

    public ReportDTO getReport(LocalDate startDate, LocalDate endDate) {
        List<KreditDTO> kreditsByModificationDate = getKreditsWithDetails(startDate, endDate);
        return new ReportDTO(kreditsByModificationDate);
    }

    public List<KreditDTO> getKreditsWithDetails(LocalDate startDate, LocalDate endDate) {
        List<Kredit> kreditList = kreditRepository.findByDatadogBetween(startDate, endDate);
        Set<String> uniqueKods = new HashSet<>();
        Set<String> innAndPinfl = new HashSet<>();
        DateTimeFormatter inputDateFormatter = DateTimeFormatter.ofPattern("ddMMyyyy");
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("ddMMyyyy");


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


            for (Kredit kredit : kreditList) {

                if (!uniqueKods.contains(kredit.getKod())) {
                    uniqueKods.add(kredit.getKod());
                    List<AzolikFiz> azolikFizList = azolikFizRepository.findByKodchlen(kredit.getKod());
                    for (AzolikFiz fizProjection : azolikFizList) {
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
                }
            }


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
                kreditDTO.setLs22812(kredit.getLs22812());
                kreditDTO.setLsSsudKred(kredit.getLssudKred());
                System.out.println("ЛсСсудКред " + kredit.getLssudKred());


                // Получение и добавление документов
                List<DokumentDTO> dokumentDTOs = dokRepository.findByKreditComplexConditions(
                        kredit.getLskred(), kredit.getLsproc(), kredit.getLsprosrKred(), kredit.getLsprosrProc(), kredit.getLspeni(), kredit.getLs22812(), kredit.getLssudKred()
                ).stream().map(dokument -> {
                    if (dokument == null) {
                        return null;
                    }
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

                List<SaldoDTO> saldoDTOS = saldoRepository.findByDokumentLscor(kredit.getLskred(), kredit.getLsproc(), kredit.getLsprosrKred(), kredit.getLsprosrProc(), kredit.getLspeni(), kredit.getLssudKred()
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
                    zalogDTO.setLs(zalog.getLs());
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

            StringBuilder dataBuilder = new StringBuilder();

            String vidKred = "";
            String status = "";
            String vznos = "";
            String overdue = "";
            int tiplred = 0;
            LocalDate actualContractEndDate;
            int pod = 0;
            double principalOverduePaymentAmount = 0;
            double interestOverduePaymentsNumber = 0;
            double principalOverduePaymentNumber = 0;
            double overduePaymentsNumber = 0;
            int n = 0;
            String contractStatusDomain = "";


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
                                            d.getLs().equals(kreditDTO.getLspeni()) &&
                                                    d.getLs().equals(kreditDTO.getLsSsudKred())
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

                    if (countedGrafik == 0) {
                        countedGrafik = 1;
                    }

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
                            .filter(d -> !d.getDats().isAfter(refDate))
                            .filter(d -> d.getLs().equals(kreditDTO.getLsKred()) || d.getLs().equals(kreditDTO.getLsprosrKred())
                                    || d.getLs().equals(kreditDTO.getLsSsudKred()))
                            .filter(d -> d.getLscor().startsWith("10101")
                                    || d.getLscor().startsWith("10503")
                                    || d.getLscor().startsWith("10509")
                                    || d.getLscor().equals(kreditDTO.getLs22812()))
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

                    // Находим запись с максимальной датой, которая не позже refDate
                    Optional<SaldoDTO> maxDateSaldo = kreditDTO.getSaldo().stream()
                            .filter(s -> s.getLs().equals(kreditDTO.getLsSsudKred()) && (s.getDats().isBefore(refDate) || s.getDats().isEqual(refDate)))
                            .max(Comparator.comparing(SaldoDTO::getDats));

                    // Извлекаем сумму из найденной записи, если она существует, иначе возвращаем BigDecimal.ZERO
                    BigDecimal saldoSumsLsSudKred = maxDateSaldo.map(SaldoDTO::getSums).orElse(BigDecimal.ZERO);

                    // Для вывода также извлекаем дату из найденной записи, если она существует
                    String maxDateStr = maxDateSaldo.map(saldo -> saldo.getDats().toString()).orElse("нет данных");

                    // Вывод информации, включая номер договора, lsSsudKred, сумму и дату максимального значения
                    System.out.println(kreditDTO.getNumdog() + " " + kreditDTO.getLsSsudKred() + " " + saldoSumsLsSudKred + " " + maxDateStr);


                    double difference = grafikPogKred.doubleValue() - dokSums.doubleValue();
                    if (difference > 0) {
                        principalOverduePaymentAmount = difference;
                    } else {
                        principalOverduePaymentAmount = 0; // Это условие также покрывает случай, когда difference < 0
                    }

                    if (principalOverduePaymentAmount < 0) principalOverduePaymentAmount = 0;

                    int outstandingBalance = (saldoSumsLsKred.intValue() + saldoSumsLsProc.intValue()
                            + (saldoSumsLsprosrKred != null ? saldoSumsLsprosrKred.intValue() : 0)
                            + (saldoSumsLsPeni != null ? saldoSumsLsPeni.intValue() : 0)
                            + (saldoSumsLsSudKred != null ? saldoSumsLsSudKred.intValue() : 0)) - (int) principalOverduePaymentAmount;

                    if (outstandingBalance < 0) outstandingBalance = 0;


                    BigDecimal pogKredForMaxDate = kreditDTO.getGrafiks().stream()
                            .filter(g -> !g.getDats().isAfter(refDate)) // фильтрация записей до и включая reference_date
                            .max(Comparator.comparing(GrafikDTO::getDats)) // поиск максимальной даты
                            .map(GrafikDTO::getPogKred) // извлечение значения pog_kred
                            .orElse(BigDecimal.ZERO); // если нет подходящих записей, возвращаем 0


                    Integer dokDatsLsLscor = Math.toIntExact(kreditDTO.getDokuments().stream()
                            .filter(d -> !d.getDats().isAfter(refDate))
                            .filter(d -> d.getLs().equals(kreditDTO.getLsProc()) && d.getLscor().equals(kreditDTO.getLsprosrProc()))
                            .map(DokumentDTO::getDats)
                            .count());

                    Integer dokDatsLsLscorStartsWith = Math.toIntExact(kreditDTO.getDokuments().stream()
                            .filter(d -> !d.getDats().isAfter(refDate))
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

                    BigDecimal pogProcForMaxDate = kreditDTO.getGrafiks().stream()
                            .filter(g -> !g.getDats().isAfter(refDate)) // фильтрация записей до и включая reference_date
                            .max(Comparator.comparing(GrafikDTO::getDats)) // поиск максимальной даты
                            .map(GrafikDTO::getPogProc) // извлечение значения pog_kred
                            .orElse(BigDecimal.ZERO); // если нет подходящих записей, возвращаем 0


                    if (status.equals("CA") || status.equals("CL")) {
                        interestOverduePaymentsNumber = 0;

                    } else if (sumsForMaxDate.intValue() == 0) {
                        interestOverduePaymentsNumber = 0;
                    } else {
                        interestOverduePaymentsNumber = Math.ceil(sumsForMaxDate.doubleValue() / pogProcForMaxDate.doubleValue());
                    }


                    if (pogKredForMaxDate.intValue() == 0) {
                        principalOverduePaymentNumber = 0;
                    } else {
                        principalOverduePaymentNumber = principalOverduePaymentAmount / pogKredForMaxDate.doubleValue();
                    }


                    if (principalOverduePaymentNumber > interestOverduePaymentsNumber) {
                        overduePaymentsNumber = principalOverduePaymentNumber;
                    } else if (principalOverduePaymentNumber < interestOverduePaymentsNumber) {
                        overduePaymentsNumber = interestOverduePaymentsNumber;
                    } else if (principalOverduePaymentNumber == interestOverduePaymentsNumber) {
                        overduePaymentsNumber = principalOverduePaymentNumber;
                    } else if (principalOverduePaymentNumber == 0 && interestOverduePaymentsNumber == 0) {
                        overduePaymentsNumber = 0;
                    }

                    if (status.equals("CA") || status.equals("CL")) {
                        principalOverduePaymentNumber = 0;
                        principalOverduePaymentAmount = 0;
                        interestOverduePaymentsNumber = 0;
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

                    SaldoDTO latestSaldoLsSsud = kreditDTO.getSaldo().stream()
                            .filter(s -> s.getLs().equals(kreditDTO.getLsSsudKred()))
                            .filter(s -> s.getDats().isBefore(refDate) || s.getDats().isEqual(refDate))
                            .max(Comparator.comparing(SaldoDTO::getDats))
                            .orElse(null);
                    BigDecimal sumlssud = latestSaldoLsSsud != null ? latestSaldoLsSsud.getSums() : null;


                    int outstandingPaymentNumber = Math.toIntExact(kreditDTO.getGrafiks().stream()
                            .map(GrafikDTO::getDats)
                            .filter(dats -> dats.isAfter(refDate) || dats.isEqual(refDate))
                            .count());

                    if (outstandingPaymentNumber == 0 && status.equals("AC")) {
                        outstandingPaymentNumber = 1;
                    }


                    if (status.equals("CA") || status.equals("CL")) outstandingPaymentNumber = 0;
                    int totalSum = (sumlspeni != null ? sumlspeni.intValue() : 0)
                            + (sumlsProc != null ? sumlsProc.intValue() : 0)
                            + (sumlsKred != null ? sumlsKred.intValue() : 0)
                            + (sumlsprosrProc != null ? sumlsprosrProc.intValue() : 0)
                            + (sumlsprosrKred != null ? sumlsprosrKred.intValue() : 0)
                            + (sumlssud != null ? sumlssud.intValue() : 0);

                    if (outstandingPaymentNumber > 0) {
                        pod = totalSum / outstandingPaymentNumber;
                    } else if ((status.equals("CA") || status.equals("CL")) && countedGrafik >= 1) {
                        pod = kreditDTO.getSumma().intValue() / countedGrafik;
                    } else {
                        pod = totalSum;
                    }

                    if (pod == 0 && countedGrafik >= 1) {
                        pod = kreditDTO.getSumma().intValue() / countedGrafik;
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

                    String zalogLs = "";
                    String zalogKodCb = "";
                    double zalogSums = 0;
                    List<String> zalogsLs = zalogRepository.findLs(kreditDTO.getNumdog());
                    List<String> zalogsSums = zalogRepository.findSums(kreditDTO.getNumdog());
                    List<String> zalogsKodCb = zalogRepository.findKodCb(kreditDTO.getNumdog());

                    zalogLs = zalogsLs.get(0);
                    zalogSums = Double.parseDouble(zalogsSums.get(0));
                    zalogKodCb = zalogsKodCb.get(0);

                    int overduePeriod = 0;

                    if (status.equals("CA") || status.equals("CL")) {
                        overduePeriod = 1;
                    } else if (kreditDTO.getDatadog().getYear() == refDate.getYear() &&
                            kreditDTO.getDatadog().getMonthValue() == refDate.getMonthValue()) {
                        overduePeriod = 0;
                    } else {
                        overduePeriod = (int) Math.ceil(overduePaymentsNumber) + 1;
                    }

                    if (overduePeriod > 8) {
                        overduePeriod = 8;
                    }


                    String guarantor = "";
                    if (zalogLs == null || zalogLs.isEmpty()) {
                        guarantor = "";
                    } else {
                        guarantor = kreditDTO.getKod();
                    }

                    String uniqueKey;
                    if ("AC".equals(status)) {
                        // Для статуса AC ключ будет включать статус, numdog и refDate
                        uniqueKey = "AC_" + kreditDTO.getNumdog() + "_" + inputDateFormatter.format(refDate);
                    } else {
                        // Для других статусов ключ будет включать только numdog
                        uniqueKey = kreditDTO.getNumdog() + status;
                    }
                    // Изменяем логику определения уникальности для status "AC"
                    boolean isUnique = !processedEntries.contains(uniqueKey);

                    if (overduePeriod > 1 && status.equals("AC")) {
                        contractStatusDomain = "E";
                    } else {
                        contractStatusDomain = "";
                    }


                    List<AzolikFiz> fizProjections = azolikFizRepository.findByKodchlen(kreditDTO.getKod());

                    for (AzolikFiz fizProjection : fizProjections) {

                        String inn = fizProjection.getInn();
                        String pinfl = fizProjection.getKodPension();

                        if (!(inn == null || inn.trim().isEmpty()) || !(pinfl == null || pinfl.trim().isEmpty())) {

                            if (!(status.equals("AC") && outstandingBalance == 0 && principalOverduePaymentAmount == 0)) {
                                if (!kreditDTO.getLsKred().isEmpty() || !kreditDTO.getLsProc().isEmpty()) {
                                    if (!kreditDTO.getDatadog().isAfter(refDate) && !(firstPaymentDate == null) && !(latestDate == null) && (countedGrafik >= 1)) {
                                        if (isUnique) {
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
                                                    .append("|")
                                                    .append(contractStatusDomain)
                                                    .append("|UZS|UZS|")
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
                                                    .append(pod)
                                                    .append("|")
                                                    .append(inputDateFormatter.format(firstPaymentDate))
                                                    .append("|")
                                                    .append((nextPaymentDate != null) ? inputDateFormatter.format(nextPaymentDate) : "")
                                                    .append("||")
                                                    .append(outstandingPaymentNumber)
                                                    .append("|")
                                                    .append(outstandingBalance)
                                                    .append("|")
                                                    .append((int) Math.ceil(overduePaymentsNumber))
                                                    .append("|")
                                                    .append((int) Math.ceil(principalOverduePaymentNumber))
                                                    .append("|")
                                                    .append((int) interestOverduePaymentsNumber)
                                                    .append("|")
                                                    .append((int) principalOverduePaymentAmount + sumsForMaxDate.intValue())
                                                    .append("|")
                                                    .append((int) principalOverduePaymentAmount)
                                                    .append("|")
                                                    .append(sumsForMaxDate.intValue())
                                                    .append("|")
                                                    .append(overduePeriod)
                                                    .append("||||||||||")
                                                    .append(kreditDTO.getProsent().intValue())
                                                    .append("||||")
                                                    .append(zalogLs != null ? zalogLs : "")
                                                    .append("|")
                                                    .append(guarantor)
                                                    .append("||")
                                                    .append(zalogSums != 0 ? (int) zalogSums : "")
                                                    .append("|UZS|||")
                                                    .append(zalogKodCb != null ? zalogKodCb : "")
                                                    .append("||||||||||||||||||||||||||||||||||||||||||||||||||||||||||")
                                                    .append("\n");

                                            processedEntries.add(uniqueKey);
                                            n++; // Предполагается, что n - счетчик успешно обработанных записей
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            String finalData = dataBuilder.toString();
            writer.write(finalData);
            System.out.println(finalData);
            writer.write("FT|MKOR0001|" + outputDateFormat.format(currentDate) + "|" + ((uniqueKods.size() - innAndPinfl.size()) + n));

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

            return kreditDTOList;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
