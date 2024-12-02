package com.xmcy.crypto.helper;

import com.xmcy.crypto.model.Crypto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.io.Resource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CsvParserTest {

    private CsvParser csvParser;
    private Resource resource;

    @BeforeEach
    public void setUp() {
        csvParser = new CsvParser();
        resource = Mockito.mock(Resource.class);
    }

    @Test
    public void testParseCsv_withValidCsv() throws Exception {
        String csvContent = "timing,name,price\n1,Bitcoin,45000.0\n2,Ethereum,3000.0";
        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes(StandardCharsets.UTF_8));
        Mockito.when(resource.getInputStream()).thenReturn(inputStream);

        List<Crypto> cryptos = csvParser.parseCsv(resource);

        assertEquals(2, cryptos.size());
        assertEquals(1L, cryptos.get(0).timing());
        assertEquals("Bitcoin", cryptos.get(0).name());
        assertEquals(45000.0, cryptos.get(0).price());
        assertEquals(2L, cryptos.get(1).timing());
        assertEquals("Ethereum", cryptos.get(1).name());
        assertEquals(3000.0, cryptos.get(1).price());
    }

    @Test
    public void testParseCsv_withEmptyCsv() throws Exception {
        String csvContent = "timing,name,price\n";
        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes(StandardCharsets.UTF_8));
        Mockito.when(resource.getInputStream()).thenReturn(inputStream);

        List<Crypto> cryptos = csvParser.parseCsv(resource);

        assertEquals(0, cryptos.size());
    }
}