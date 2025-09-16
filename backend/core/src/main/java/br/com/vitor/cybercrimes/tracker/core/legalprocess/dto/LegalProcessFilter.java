package br.com.vitor.cybercrimes.tracker.core.legalprocess.dto;

import br.com.vitor.cybercrimes.tracker.core.commons.BaseRequestFilter;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
public class LegalProcessFilter extends BaseRequestFilter {

    private String processCode;
    private String acordaoCode;
    private String className;
    private String subject;
    private String rapporteur;
    private String district;
    private String judgingBody;
    private LocalDate judgmentDate;
    private LocalDate publishDate;
    private String headnote;
    private Boolean isCybercrime;

    public LegalProcessFilter() {
    }

    public LegalProcessFilter(int pageNumber, int pageSize) {
        super.setPageNumber(pageNumber);
        super.setPageSize(pageSize);
    }
}