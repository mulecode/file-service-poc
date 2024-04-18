package uk.co.mulecode.fileservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping("/process")
@RequiredArgsConstructor
public class FileProcessorController extends ControllerUtils {

    private final static String RESPONSE_FILE_NAME = "OutcomeFile.json";

    @PostMapping(value = "/upload", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> processFile(MultipartFile file) throws Exception {
        log.info("Processing file: {}", file.getOriginalFilename());

        final byte[] responseBytes = file.getBytes();

        return ResponseEntity.ok().headers(buildHeadersFileResponse(responseBytes, RESPONSE_FILE_NAME)).body(responseBytes);
    }

}
