package br.com.vitor.cybercrimes.tracker.model.legalprocess;

import br.com.vitor.cybercrimes.tracker.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "legal_process")
public class LegalProcess extends BaseEntity {

    @Id
    private Long id;
    private String processCode;
    private Long acordaoCode;
    @Column("class")
    private String className;
    private String subject;
    private String rapporteur;
    private String district;
    private String judgingBody;
    private LocalDate judgmentDate;
    private LocalDate publishDate;
    private String headnote;
    private Long legalProcessFileId;
    private String pdfUrl;
    private Boolean isCybercrime;
    private ClassificationLevel1 classificationLevel1;
    private ClassificationLevel2 classificationLevel2;
    private String justification;
}
