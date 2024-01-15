package org.creditbureaureport.controler;

import org.creditbureaureport.service.ReportService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;


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
}
