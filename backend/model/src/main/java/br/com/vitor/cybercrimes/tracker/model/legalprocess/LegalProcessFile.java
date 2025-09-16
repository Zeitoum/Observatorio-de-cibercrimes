package br.com.vitor.cybercrimes.tracker.model.legalprocess;

import br.com.vitor.cybercrimes.tracker.model.BaseEntity;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table(name = "legal_process_file")
public class LegalProcessFile extends BaseEntity {

    @Id
    private Long id;
    private String fileName;
    private String filePath;
    private Long fileSize;
}
