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
    private String numdog;
    private String vidkred;
    private Byte sost;
    private Byte status;
    private Date datadog;
    private Date datsIzm;
    private Date datsZakr;
    private BigDecimal summa;
    private Byte graf;
    private Byte nalbeznal;
    private Byte xatar;
    private Short tipkred;
    private BigDecimal prosent;

    // поля из [grafik]
    private Date grafikDats;

    // поля из [dok]
    private Date dokDats;

    private String kodCb;

    // поля из [azolik_fiz]
    private String kodchlen;
    private String name;

    private int pod;
    private Date beforeReport;
    private int sum_prosr;
    private Date afterReport;
    private BigDecimal sums_z;
    private BigDecimal total_sums;
    private BigDecimal prosr_proc;
    private BigDecimal prosr_kred;
    private int sumVznos;
    private int sumVznosAll;
    private BigDecimal next_summ;
    private int counted_payments;
    private int count_sums_prosr_proc;
    private int count_sums_prosr_kred;
    private String z_ls;
    private Date date_priem;
    private Date date_vozvrat;
    private String overdue;
    private Byte klass;



    public ContractDTO(ContractDTO contractDTO) {

    }
}
