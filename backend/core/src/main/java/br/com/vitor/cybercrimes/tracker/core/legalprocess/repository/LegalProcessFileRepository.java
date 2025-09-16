package br.com.vitor.cybercrimes.tracker.core.legalprocess.repository;

import br.com.vitor.cybercrimes.tracker.model.legalprocess.LegalProcessFile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LegalProcessFileRepository extends CrudRepository<LegalProcessFile, Long> {

    Optional<LegalProcessFile> findByFilePath(String filePath);
}
