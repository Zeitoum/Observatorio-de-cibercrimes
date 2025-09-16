package br.com.vitor.cybercrimes.tracker.core.legalprocess.repository;

import br.com.vitor.cybercrimes.tracker.core.legalprocess.dto.LegalProcessPdfInfo;
import br.com.vitor.cybercrimes.tracker.model.legalprocess.ClassificationLevel1;
import br.com.vitor.cybercrimes.tracker.model.legalprocess.ClassificationLevel2;
import br.com.vitor.cybercrimes.tracker.model.legalprocess.LegalProcess;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LegalProcessRepository extends CrudRepository<LegalProcess, Long>, LegalProcessExtensionRepository {

    @Query("SELECT EXISTS(" +
            " SELECT 1" +
            " FROM legal_process" +
            " WHERE process_code = :processCode AND acordao_code = :acordaoCode" +
            ")")
    boolean exists(@Param("processCode") String processCode, @Param("acordaoCode") Long acordaoCode);

    @Query("SELECT lp.id AS legal_process_id, lp.acordao_code, lp.pdf_url, lpf.file_path" +
            " FROM legal_process lp " +
            " LEFT JOIN legal_process_file lpf ON lpf.id = lp.legal_process_file_id" +
            " WHERE lp.is_cybercrime IS NULL" +
            " OFFSET :offSet LIMIT :limit")
    List<LegalProcessPdfInfo> findAllNotClassified(@Param("offSet") int offSet, @Param("limit") int limit);

    @Modifying
    @Query("UPDATE legal_process " +
            " SET legal_process_file_id = :legalProcessFileId " +
            " WHERE id = :legalProcessId")
    void updateFileId(Long legalProcessId, Long legalProcessFileId);

    @Modifying
    @Query("UPDATE legal_process" +
            " SET is_cybercrime= :isCybercrime, classification_level_1 = :classificationLevel1," +
            " classification_level_2 = :classificationLevel2, justification = :justification" +
            " WHERE id = :legalProcessId")
    void updateClassification(@Param("legalProcessId") Long legalProcessId, @Param("isCybercrime") boolean isCybercrime,
                              @Param("classificationLevel1") ClassificationLevel1 classificationLevel1,
                              @Param("classificationLevel2") ClassificationLevel2 classificationLevel2,
                              @Param("justification") String justification);
}
