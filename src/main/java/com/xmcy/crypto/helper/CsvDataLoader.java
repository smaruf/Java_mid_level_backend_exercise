package com.xmcy.crypto.helper;

import com.xmcy.crypto.model.Crypto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CsvDataLoader {
    private static final Logger logger = LoggerFactory.getLogger(CsvDataLoader.class);

    private final CsvParser csvParser;
    private final DataInserter dataInserter;

    public void loadCsvData(Resource[] resources) {
        try {
            for (Resource resource : resources) {
                List<Crypto> cryptos = csvParser.parseCsv(resource);
                dataInserter.insertData(cryptos);
                logger.info("Loaded data from file: {}", resource.getFilename());
            }
        } catch (Exception e) {
            logger.error("Error loading CSV data", e);
        }
    }
}
