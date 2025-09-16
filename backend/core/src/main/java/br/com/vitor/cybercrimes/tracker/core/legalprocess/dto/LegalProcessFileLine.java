package br.com.vitor.cybercrimes.tracker.core.legalprocess.dto;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Log4j2
@Getter
public class LegalProcessFileLine {
    private static final String BASE_FILE_URL = "https://esaj.tjsp.jus.br/cjsg/getArquivo.do?cdAcordao=";
    private static final int PROCESS_CODE_IDX = 0;
    private static final int ACORDAO_CODE_IDX = 1;
    private static final int CLASS_IDX = 2;
    private static final int SUBJECT_IDX = 3;
    private static final int RAPPORTEUR_IDX = 4;
    private static final int DISTRICT_IDX = 5;
    private static final int JUDGING_BODY_IDX = 6;
    private static final int JUDGMENT_DATE_IDX = 7;
    private static final int PUBLISH_DATE_IDX = 8;
    private static final int HEAD_NOTE_IDX = 9;

    private final String line;
    private final String[] lineArray;

    private final String processCode;
    private final String acordaoCode;
    private final String className;
    private final String subject;
    private final String rapporteur;
    private final String district;
    private final String judgingBody;
    private final String judgmentDate;
    private final String publishDate;
    private final String headNote;


    public LegalProcessFileLine(String line, String separator) {
        this.line = line;
        this.lineArray = line.split(separator);
        this.processCode = lineArray[PROCESS_CODE_IDX].trim();
        this.acordaoCode = lineArray[ACORDAO_CODE_IDX].trim();
        this.className = lineArray[CLASS_IDX].trim();
        this.subject = lineArray[SUBJECT_IDX].trim();
        this.rapporteur = lineArray[RAPPORTEUR_IDX].trim();
        this.district = lineArray[DISTRICT_IDX].trim();
        this.judgingBody = lineArray[JUDGING_BODY_IDX].trim();
        this.judgmentDate = lineArray[JUDGMENT_DATE_IDX].trim();
        this.publishDate = lineArray[PUBLISH_DATE_IDX].trim();
        this.headNote = extractHeadnote(lineArray);
    }

    private static String extractHeadnote(String[] lineArray) {
        if (lineArray.length > 10) {
            StringBuilder headNote = new StringBuilder();

            for (int i = HEAD_NOTE_IDX; i < lineArray.length; i++) {
                headNote.append(lineArray[i]);
            }

            return headNote.toString().trim();
        }
        return lineArray[HEAD_NOTE_IDX].trim();
    }

    public boolean isHeader() {
        return !Character.isDigit(acordaoCode.charAt(0));
    }

    public Long getParsedAcordaoCode() {
        try {
            return Long.parseLong(acordaoCode);
        } catch (NumberFormatException e) {
            log.error("Invalid acordao code: {}", acordaoCode, e);
            throw new RuntimeException("Invalid acordao code: " + acordaoCode, e);
        }
    }

    public LocalDate getJudgmentDate() {
        if (judgmentDate.equals("NA")) {
            return null;
        }

        try {
            return LocalDate.parse(judgmentDate);
        } catch (DateTimeParseException e) {
            log.error("Invalid judgment date: {}", judgmentDate, e);
            throw new RuntimeException("Invalid judgment date: " + judgmentDate, e);
        }
    }

    public LocalDate getPublishDate() {
        if (publishDate.equals("NA")) {
            return null;
        }

        try {
            return LocalDate.parse(publishDate);
        } catch (DateTimeParseException e) {
            log.error("Invalid publish date: {}", publishDate, e);
            throw new RuntimeException("Invalid publish date: " + publishDate, e);
        }
    }

    public String getFileUrl() {
        return BASE_FILE_URL + acordaoCode;
    }
}
