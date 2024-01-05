package org.creditbureaureport.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.creditbureaureport.model.AzolikFiz;
import org.springframework.cglib.core.Local;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContractDTO {

    // поля из [kredit]
    private String kod;
    private String numdog;
    private String vidkred;
    private Byte sost;
    private Byte status;
    private Integer valut;
    private Date datadog;
    private Date dats;
    private Date datsIzm;
    private Date datsZakr;
    private BigDecimal summa;
    private Byte graf;
    private Byte nalbeznal;
    private Byte xatar;
    private Short  tipkred;
    private BigDecimal prosent;

    // поля из [grafik]
    private Date grafikDats;
    private BigDecimal pogKredAndPogProc;

    // поля из [dok]
    private Date dokDats;

    // поля из [saldo]
    private BigDecimal saldoSums;

    // поля из [zalog]
    private BigDecimal zalogSums;
    private String kodCb;

//    // поля из [zalog_xranenie]
//    private Date dataPriem;
//    private Date dataVozvrat;
//
//    // поля из [zalog_detal]
//    private Integer kodZalog;
//    private String stroka;
//    private String detal;

    // поля из [azolik_fiz]
    private String kodchlen;
    private String name;

    private BigDecimal pod;
    private Date beforeReport;
    private Date afterReport;
    private BigDecimal sums_z;
    private BigDecimal total_sums;
    private BigDecimal prosr_proc;
    private BigDecimal prosr_kred;

    public ContractDTO(ContractDTO contractDTO) {

    }
}
