package org.creditbureaureport.controler;

import org.creditbureaureport.dto.ReportDTO;
import org.creditbureaureport.model.AzolikFiz;
import org.creditbureaureport.service.ClientCorrectService;
import org.creditbureaureport.service.NumdogCorrectService;
import org.creditbureaureport.service.ReportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/api/report")
public class ReportController {

    private final ReportService kreditService;
    private final ClientCorrectService clientCorrectService;
    private final NumdogCorrectService numdogCorrectService;

    public ReportController(ReportService kreditService, ClientCorrectService clientCorrectService, NumdogCorrectService numdogCorrectService) {
        this.kreditService = kreditService;
        this.clientCorrectService = clientCorrectService;
        this.numdogCorrectService = numdogCorrectService;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping
    public ResponseEntity<ReportDTO> generateReport(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        ReportDTO report = kreditService.getReport(startDate, endDate);
        return ResponseEntity.ok(report);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/numdog")
    public ResponseEntity<ReportDTO> generateReportByNumdog(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam("numdog") String numdog) {
        ReportDTO report = numdogCorrectService.getReportByNumdog(startDate, endDate, numdog);
        return ResponseEntity.ok(report);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/client")
    public ResponseEntity<List<AzolikFiz>> generateReportByKodchlen(
            @RequestParam("kodchlen") String kodchlen) {
        List<AzolikFiz> report = clientCorrectService.getClientsWithDetails(kodchlen);
        return ResponseEntity.ok(report);
    }
}
