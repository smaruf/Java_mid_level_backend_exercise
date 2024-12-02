package com.xmcy.crypto.config;

import com.xmcy.crypto.helper.CsvDataLoader;
import com.xmcy.crypto.helper.CsvParser;
import com.xmcy.crypto.helper.DataInserter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import static org.mockito.Mockito.*;

public class DbConfigTests {

    @Mock
    private PathMatchingResourcePatternResolver resourceResolver;

    @Mock
    private CsvParser csvParser;

    @Mock
    private DataInserter dataInserter;

    @InjectMocks
    private CsvDataLoader csvDataLoader;

    @InjectMocks
    private DbConfig dbConfig;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void tearDown() {
        reset(resourceResolver, csvParser, dataInserter, csvDataLoader);
    }

    @Test
    public void testLoadCsvData() throws Exception {
        // Mock the resources
        Resource mockResource = new ClassPathResource("price/BTC_values.csv");
        Resource[] resources = new Resource[]{mockResource};
        when(resourceResolver.getResources("classpath:price/*.csv")).thenReturn(resources);

        // Mock the CsvParser
        when(csvParser.parseCsv(mockResource)).thenReturn(anyList());

        // Call the method to test
        dbConfig.loadCsvData();

        // Verify the interactions
        verify(resourceResolver, times(1)).getResources("classpath:price/*.csv");
        verify(csvParser, times(1)).parseCsv(mockResource);
        verify(dataInserter, times(1)).insertData(anyList());
    }
}
