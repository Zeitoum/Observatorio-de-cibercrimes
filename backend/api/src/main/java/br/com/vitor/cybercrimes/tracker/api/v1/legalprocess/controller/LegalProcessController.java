package br.com.vitor.cybercrimes.tracker.api.v1.legalprocess.controller;

import br.com.vitor.cybercrimes.tracker.core.commons.PagedResponse;
import br.com.vitor.cybercrimes.tracker.core.legalprocess.dto.LegalProcessDTO;
import br.com.vitor.cybercrimes.tracker.core.legalprocess.dto.LegalProcessFilter;
import br.com.vitor.cybercrimes.tracker.core.legalprocess.service.FileProcessorService;
import br.com.vitor.cybercrimes.tracker.core.legalprocess.service.LegalProcessService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/v1/legal-process", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class LegalProcessController {

    private final FileProcessorService fileProcessorService;
    private final LegalProcessService legalProcessService;

    @GetMapping
    public ResponseEntity<PagedResponse<LegalProcessDTO>> findAll(@Valid LegalProcessFilter legalProcessFilter) {
        PagedResponse<LegalProcessDTO> pagedResponse = legalProcessService.findAll(legalProcessFilter);
        return ResponseEntity.ok(pagedResponse);
    }

    @PostMapping(value = "/process-file", consumes = MediaType.TEXT_PLAIN_VALUE)
    public void processFile(@RequestBody String spreadsheetFilePath) {
        fileProcessorService.processSpreadsheetFile(spreadsheetFilePath);
    }
}