package br.com.vitor.cybercrimes.tracker.core.legalprocess.mapper;

import br.com.vitor.cybercrimes.tracker.core.legalprocess.dto.LegalProcessDTO;
import br.com.vitor.cybercrimes.tracker.model.legalprocess.ClassificationLevel1;
import br.com.vitor.cybercrimes.tracker.model.legalprocess.ClassificationLevel2;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class LegalProcessRowMapper implements RowMapper<LegalProcessDTO> {

    @Override
    public LegalProcessDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        LegalProcessDTO legalProcess = new LegalProcessDTO();
        legalProcess.setId(rs.getLong("id"));
        legalProcess.setProcessCode(rs.getString("process_code"));
        legalProcess.setAcordaoCode(rs.getLong("acordao_code"));
        legalProcess.setClassName(rs.getString("class"));
        legalProcess.setSubject(rs.getString("subject"));
        legalProcess.setRapporteur(rs.getString("rapporteur"));
        legalProcess.setDistrict(rs.getString("district"));
        legalProcess.setJudgingBody(rs.getString("judging_body"));
        legalProcess.setJudgmentDate(rs.getObject("judgment_date", LocalDate.class));
        legalProcess.setPublishDate(rs.getObject("publish_date", LocalDate.class));
        legalProcess.setHeadnote(rs.getString("headnote"));
        legalProcess.setLegalProcessFileId(rs.getLong("legal_process_file_id"));
        legalProcess.setPdfUrl(rs.getString("pdf_url"));
        legalProcess.setIsCybercrime(rs.getBoolean("is_cybercrime"));
        legalProcess.setJustification(rs.getString("justification"));

        var classificationLevel1 = rs.getString("classification_level_1");
        var classificationLevel2 = rs.getString("classification_level_2");

        if (classificationLevel1 != null) {
            legalProcess.setClassificationLevel1(ClassificationLevel1.valueOf(classificationLevel1));
        }

        if (classificationLevel2 != null) {
            legalProcess.setClassificationLevel2(ClassificationLevel2.valueOf(classificationLevel2));
        }

        return legalProcess;
    }
}
