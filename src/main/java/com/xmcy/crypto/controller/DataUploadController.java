package com.xmcy.crypto.controller;

import com.xmcy.crypto.helper.CsvParser;
import com.xmcy.crypto.helper.DataInserter;
import com.xmcy.crypto.model.Crypto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/upload")
@RequiredArgsConstructor
public class DataUploadController {

    private final CsvParser csvParser;
    private final DataInserter dataInserter;

    @Operation(
        summary = "Upload CSV data",
        description = "Uploads CSV data and inserts it into the database",
        responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "CSV data uploaded successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "400",
                description = "Invalid CSV data"
            )
        }
    )
    @PostMapping("/csv")
    public ResponseEntity<String> uploadCsvData(
            @Parameter(description = "CSV content as a string", required = true)
            @RequestBody String csvContent) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(csvContent.getBytes(StandardCharsets.UTF_8));
        Resource resource = new InputStreamResource(inputStream);
        List<Crypto> cryptos = csvParser.parseCsv(resource);
        dataInserter.insertData(cryptos);
        return ResponseEntity.ok("CSV data uploaded successfully");
    }
}
