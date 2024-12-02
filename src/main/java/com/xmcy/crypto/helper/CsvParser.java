package com.xmcy.crypto.helper;

import com.xmcy.crypto.model.Crypto;
import com.xmcy.crypto.model.CryptoType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class CsvParser {

    private static final Logger logger = LoggerFactory.getLogger(CsvParser.class);

    public List<Crypto> parseCsv(Resource resource) {
        List<Crypto> cryptos = new LinkedList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            String content = reader.lines().collect(Collectors.joining("\n"));
            extractCryptosFromContent(content, cryptos);
        } catch (Exception e) {
            logger.error("Error parsing CSV data", e);
        }
        return cryptos;
    }

    private void extractCryptosFromContent(String content, List<Crypto> cryptos) {
        String[] lines = content.split("\n");
        for (int i = 1; i < lines.length; i++) { // Skip header line
            Crypto crypto = extractCryptoFromString(lines[i]);
            cryptos.add(crypto);
        }
    }

    private Crypto extractCryptoFromString(String line) {
        String[] values = line.split(",");
        return new Crypto(
                generateRandomId(),
                Long.valueOf(values[0]),
                CryptoType.findByName(values[1]).getName(),
                Double.valueOf(values[2]),
                null // Assuming recommendation is not in the CSV
        );
    }

    private long generateRandomId() {
        return UUID.randomUUID().getMostSignificantBits() & 0x7FFFFFFFFFFFFFFFL;
    }
}
