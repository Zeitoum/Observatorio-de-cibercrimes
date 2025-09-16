package br.com.vitor.cybercrimes.tracker.core.legalprocess.service;

import br.com.vitor.cybercrimes.tracker.core.commons.PagedResponse;
import br.com.vitor.cybercrimes.tracker.core.legalprocess.dto.LegalProcessDTO;
import br.com.vitor.cybercrimes.tracker.core.legalprocess.dto.LegalProcessFileLine;
import br.com.vitor.cybercrimes.tracker.core.legalprocess.dto.LegalProcessFilter;
import br.com.vitor.cybercrimes.tracker.core.legalprocess.dto.LegalProcessPdfInfo;
import br.com.vitor.cybercrimes.tracker.core.legalprocess.mapper.LegalProcessMapper;
import br.com.vitor.cybercrimes.tracker.core.legalprocess.repository.LegalProcessRepository;
import br.com.vitor.cybercrimes.tracker.core.utils.SqlUtils;
import br.com.vitor.cybercrimes.tracker.model.legalprocess.ClassificationLevel1;
import br.com.vitor.cybercrimes.tracker.model.legalprocess.ClassificationLevel2;
import br.com.vitor.cybercrimes.tracker.model.legalprocess.LegalProcess;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LegalProcessService {

    private final LegalProcessRepository repository;

    public void create(LegalProcessFileLine fileLine, Long legalProcessFileId) {
        LegalProcess legalProcess = LegalProcessMapper.fromFileLine(fileLine, legalProcessFileId);

        repository.save(legalProcess);
    }

    public boolean exists(LegalProcessFileLine fileLine) {
        return repository.exists(fileLine.getProcessCode(), fileLine.getParsedAcordaoCode());
    }

    public PagedResponse<LegalProcessDTO> findAll(LegalProcessFilter legalProcessFilter) {
        List<LegalProcessDTO> legalProcesses = repository.findAllPaged(legalProcessFilter);
        long totalItems = repository.countAll(legalProcessFilter);

        return new PagedResponse<>(totalItems, legalProcesses);
    }

    public List<LegalProcessPdfInfo> findAllNotClassified(int pageNumber, int pageSize) {
        int offset = SqlUtils.calculateOffset(pageSize, pageNumber);

        return repository.findAllNotClassified(offset, pageSize);
    }

    public void updateFileId(Long legalProcessId, Long legalProcessFileId) {
        repository.updateFileId(legalProcessId, legalProcessFileId);
    }

    public void updateClassification(Long legalProcessId, boolean isCybercrime, ClassificationLevel1 classificationLevel1,
                                     ClassificationLevel2 classificationLevel2, String justification) {
        repository.updateClassification(legalProcessId, isCybercrime, classificationLevel1, classificationLevel2, justification);
    }
}
