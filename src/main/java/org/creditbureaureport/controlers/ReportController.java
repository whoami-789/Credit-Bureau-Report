package org.creditbureaureport.controlers;

import org.creditbureaureport.dto.FizProjectionDTO;
import org.creditbureaureport.services.ReportService;
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
    public List<FizProjectionDTO> getAllFizProjections() {
        return kreditService.getAllFizProjections();
    }
}
