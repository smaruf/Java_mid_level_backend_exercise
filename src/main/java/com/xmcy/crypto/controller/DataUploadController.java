package com.xmcy.crypto.controller;

import com.xmcy.crypto.helper.CsvParser;
import com.xmcy.crypto.helper.DataInserter;
import com.xmcy.crypto.model.Crypto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
@Api(value = "Data Upload Controller")
@RequiredArgsConstructor
public class DataUploadController {

    private final CsvParser csvParser;
    private final DataInserter dataInserter;

    @ApiOperation(value = "Upload CSV data", response = String.class)
    @PostMapping("/csv")
    public ResponseEntity<String> uploadCsvData(
            @ApiParam(value = "CSV content as a string", required = true)
            @RequestBody String csvContent) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(csvContent.getBytes(StandardCharsets.UTF_8));
        Resource resource = new InputStreamResource(inputStream);
        List<Crypto> cryptos = csvParser.parseCsv(resource);
        dataInserter.insertData(cryptos);
        return ResponseEntity.ok("CSV data uploaded successfully");
    }
}
