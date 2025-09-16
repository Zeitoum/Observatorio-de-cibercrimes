package br.com.vitor.cybercrimes.tracker.core.legalprocess.repository;

import br.com.vitor.cybercrimes.tracker.core.legalprocess.dto.LegalProcessDTO;
import br.com.vitor.cybercrimes.tracker.core.legalprocess.dto.LegalProcessFilter;

import java.util.List;

public interface LegalProcessExtensionRepository {

    List<LegalProcessDTO> findAllPaged(LegalProcessFilter legalProcessFilter);

    long countAll(LegalProcessFilter legalProcessFilter);
}
