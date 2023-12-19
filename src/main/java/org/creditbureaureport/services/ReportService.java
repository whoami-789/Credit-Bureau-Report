package org.creditbureaureport.services;

import org.creditbureaureport.dto.FizProjectionDTO;
import org.creditbureaureport.models.AzolikFiz;
import org.creditbureaureport.repositories.AzolikFizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.text.SimpleDateFormat;


@Service
public class ReportService {

    private final AzolikFizRepository reportRepository;

    @Autowired
    public ReportService(AzolikFizRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    public List<FizProjectionDTO> getAllFizProjections() {
        List<AzolikFiz> fizProjections = reportRepository.findAll();
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("ddMMyyyy");
        DateTimeFormatter inputDateFormatter = DateTimeFormatter.ofPattern("ddMMyyyy");

        try {
            File file = new File("example.txt");
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            for (AzolikFiz fizProjection : fizProjections) {
                writer.write("ID|CRIFTEST||" + inputDateFormatter.format(fizProjection.getDatsIzm()) + "|" + fizProjection.getKodchlen() + "|" + fizProjection.getImya() + "|"
                        + fizProjection.getFam() + "|" + fizProjection.getOtch() + "|||" + fizProjection.getFsobst() + "|" + outputDateFormat.format(fizProjection.getDatsRojd())
                        + "||UZ||MI|" + fizProjection.getAdres() + "||||" + fizProjection.getKodRayon() + "|" + fizProjection.getKodObl() + "|||||||||||||1|" + fizProjection.getKodPension()
                        + "|2|" + fizProjection.getInn().replaceAll("\\s", "") + "|" + fizProjection.getTipDok() + "|" + fizProjection.getSerNumPasp().replaceAll("\\s", "")
                        + "|" + outputDateFormat.format(fizProjection.getVidanPasp()) + "||" + outputDateFormat.format(fizProjection.getPaspdo()) + "||||||2|"
                        + fizProjection.getTelmobil().replaceAll("\\s", "") + "|1|" + fizProjection.getTelhome().replaceAll("\\s", "")
                        + "|||||||||||||||||||||||||||||||||||");
                if (fizProjection.getIndpred() == 1) {
                    writer.write("ID|CRIFTEST||" + outputDateFormat.format(fizProjection.getDatsIzm()) + "|" + fizProjection.getKodchlen() + "|" + fizProjection.getImya() + "|" + fizProjection.getFam() + "|" + fizProjection.getOtch() + "|||" + fizProjection.getFsobst() + "|" + outputDateFormat.format(fizProjection.getDatsRojd()) + "||UZ||MI|" + fizProjection.getAdres() + "||||" + fizProjection.getKodRayon() + "|" + fizProjection.getKodObl() + "|||||||||||||1|" + fizProjection.getKodPension() + "|2|" + fizProjection.getInn().replaceAll("\\s", "") + "|" + fizProjection.getTipDok() + "|" + fizProjection.getSerNumPasp().replaceAll("\\s", "") + "|" + outputDateFormat.format(fizProjection.getVidanPasp()) + "||" + outputDateFormat.format(fizProjection.getPaspdo()) + "||||||2|" + fizProjection.getTelmobil().replaceAll("\\s", "") + "|1|" + fizProjection.getTelhome().replaceAll("\\s", "")+ "||||||||" + fizProjection.getName() + "||" + fizProjection.getAdres() + "||||" + fizProjection.getKodRayon() + "|" + fizProjection.getKodObl() + "||||||||||||3|" + fizProjection.getInn() + "|||2|" + fizProjection.getTelmobil() + "|1|" + fizProjection.getTelhome());
                }
                writer.newLine(); // Добавить новую строку
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fizProjections.stream()
                .map(this::mapToFizProjectionDTO)
                .collect(Collectors.toList());
    }

    private FizProjectionDTO mapToFizProjectionDTO(AzolikFiz fizProjection) {
        FizProjectionDTO dto = new FizProjectionDTO();
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

}
