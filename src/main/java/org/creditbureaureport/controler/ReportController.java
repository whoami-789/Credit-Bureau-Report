package org.creditbureaureport.controler;

import org.creditbureaureport.dto.ContractDTO;
import org.creditbureaureport.dto.FizDTO;
import org.creditbureaureport.service.ReportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/report")
public class ReportController {

    private final ReportService kreditService;

    public ReportController(ReportService kreditService) {
        this.kreditService = kreditService;
    }

    @GetMapping
    public String getAllFizProjections() {
        return kreditService.getAllFizProjections();
    }

    @GetMapping("/contracts")
    public ResponseEntity<List<ContractDTO>> getAzolikFizContracts() {
        List<ContractDTO> contracts = kreditService.findAzolikFizKreditSaldoGrafikDokZalogZalogDetalZalogXranenie();
        return new ResponseEntity<>(contracts, HttpStatus.OK);
    }
}
