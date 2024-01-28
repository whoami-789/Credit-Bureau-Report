package org.creditbureaureport.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ReportDTO {
//    private List<KreditDTO> kreditsByModificationDate;
    private List<KreditDTO> kreditsByDocumentDate;
}
