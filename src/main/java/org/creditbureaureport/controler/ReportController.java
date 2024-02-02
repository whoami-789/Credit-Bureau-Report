package org.creditbureaureport.controler;

import org.creditbureaureport.dto.CombinedDataDTO;
import org.creditbureaureport.dto.ContractDetailsDTO;
import org.creditbureaureport.dto.ReportDTO;
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

    public ReportController(ReportService kreditService) {
        this.kreditService = kreditService;
    }

    //    @CrossOrigin(origins = "http://localhost:3000")
//    @GetMapping
//    public String getAllFizProjections(
//            @RequestParam("startDate") LocalDate startDate,
//            @RequestParam("endDate") LocalDate endDate) {
//        return kreditService.getAllFizProjections(startDate, endDate);
//    }
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
        ReportDTO report = kreditService.getReportByNumdog(startDate, endDate, numdog);
        return ResponseEntity.ok(report);
    }
}
