package uk.co.mulecode.fileservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import uk.co.mulecode.fileservice.component.validators.ValidIngestionFile;
import uk.co.mulecode.fileservice.service.FileIngestionService;

import java.nio.charset.StandardCharsets;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static uk.co.mulecode.fileservice.component.validators.patterns.RegexPatterns.CSV_LINE_PATTERN;

@Slf4j
@Validated
@RestController
@RequestMapping("/process")
@RequiredArgsConstructor
public class FileProcessorController extends ControllerUtils {

    private final static String RESPONSE_FILE_NAME = "OutcomeFile.json";

    private final FileIngestionService fileIngestionService;

    @PostMapping(value = "/upload", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> processFile(@Valid @ValidIngestionFile(pattern = CSV_LINE_PATTERN) MultipartFile file) {
        log.info("Processing file: {}", file.getOriginalFilename());

        String processedFileResponse = fileIngestionService.processFile(file);
        final byte[] responseBytes = processedFileResponse.getBytes(StandardCharsets.UTF_8);

        return ResponseEntity.ok().headers(buildHeadersFileResponse(responseBytes, RESPONSE_FILE_NAME)).body(responseBytes);
    }

}
