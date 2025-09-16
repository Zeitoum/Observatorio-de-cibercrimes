package br.com.vitor.cybercrimes.tracker.core.legalprocess.mapper;

import br.com.vitor.cybercrimes.tracker.core.legalprocess.dto.LegalProcessFileLine;
import br.com.vitor.cybercrimes.tracker.model.legalprocess.LegalProcess;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class LegalProcessMapper {

    public static LegalProcess fromFileLine(LegalProcessFileLine fileLine, Long fileId) {
        LegalProcess legalProcess = new LegalProcess();
        legalProcess.setProcessCode(fileLine.getProcessCode());
        legalProcess.setAcordaoCode(fileLine.getParsedAcordaoCode());
        legalProcess.setClassName(fileLine.getClassName());
        legalProcess.setSubject(fileLine.getSubject());
        legalProcess.setRapporteur(fileLine.getRapporteur());
        legalProcess.setDistrict(fileLine.getDistrict());
        legalProcess.setJudgingBody(fileLine.getJudgingBody());
        legalProcess.setJudgmentDate(fileLine.getJudgmentDate());
        legalProcess.setPublishDate(fileLine.getPublishDate());
        legalProcess.setHeadnote(fileLine.getHeadNote());
        legalProcess.setLegalProcessFileId(fileId);
        legalProcess.setPdfUrl(fileLine.getFileUrl());

        return legalProcess;
    }
}
