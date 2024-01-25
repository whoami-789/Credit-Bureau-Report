package org.creditbureaureport.controler;

import org.creditbureaureport.dto.CombinedDataDTO;
import org.creditbureaureport.dto.ContractDetailsDTO;
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

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping
    public String getAllFizProjections(
            @RequestParam("startDate") LocalDate startDate,
            @RequestParam("endDate") LocalDate endDate) {
        return kreditService.getAllFizProjections(startDate, endDate);
    }

//    @GetMapping("/report")
//    public ResponseEntity<List<ContractDTO>> getAzolikFizContracts() {
//        List<ContractDTO> contracts = kreditService.findAzolikFizKreditSaldoGrafikDokZalogZalogDetalZalogXranenie();
//        return new ResponseEntity<>(contracts, HttpStatus.OK);
//    }

//    @GetMapping("/contracts")
//    public ResponseEntity<List<AzolikFiz>> getAllAzolikFiz() {
//        List<AzolikFiz> azolikFizList = kreditService.getAllAzolikFiz();
//        return new ResponseEntity<>(azolikFizList, HttpStatus.OK);
//    }

    @GetMapping("/combined")
    public ResponseEntity<List<CombinedDataDTO>> getCombinedData() {
        List<CombinedDataDTO> combinedData = kreditService.fetchCombinedData();
        return ResponseEntity.ok(combinedData);
    }

    @GetMapping("/{contractId}")
    public ResponseEntity<ContractDetailsDTO> getContractDetails(@PathVariable String contractId) {
        ContractDetailsDTO contractDetails = kreditService.getContractDetailsByNumdog(contractId);
        return ResponseEntity.ok(contractDetails);
    }

//    @GetMapping("/kredit/dats-izm")
//    public ResponseEntity<List<KreditDTO>> getKreditsWithDetails(
//            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
//            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
//        List<KreditDTO> kreditDTOs = kreditService.getKreditsWithDetails(startDate, endDate);
//        if (kreditDTOs.isEmpty()) {
//            return ResponseEntity.noContent().build();
//        }
//        return ResponseEntity.ok(kreditDTOs);
//    }

    @GetMapping("/dok/dats")
    public ResponseEntity<String> getDokWithDetails(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
       kreditService.getReport(startDate, endDate);
       return ResponseEntity.ok("Success reporting");
    }
}
