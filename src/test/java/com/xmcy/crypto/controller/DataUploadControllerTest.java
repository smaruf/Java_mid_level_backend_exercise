package com.xmcy.crypto.controller;

import com.xmcy.crypto.helper.CsvParser;
import com.xmcy.crypto.helper.DataInserter;
import com.xmcy.crypto.model.Crypto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DataUploadControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CsvParser csvParser;

    @Mock
    private DataInserter dataInserter;

    @InjectMocks
    private DataUploadController dataUploadController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(dataUploadController).build();
    }

    @Test
    public void testUploadCsvData() throws Exception {
        String csvContent = "name,price\nBitcoin,50000\nEthereum,4000";
        List<Crypto> cryptos = List.of(new Crypto(1L, 1L, "Bitcoin", 50000D, "hold"),
                new Crypto(2L, 1L, "Bitcoin", 4000D, "hold"));

        when(csvParser.parseCsv(any())).thenReturn(cryptos);
        doNothing().when(dataInserter).insertData(cryptos);

        mockMvc.perform(post("/api/upload/csv")
                .contentType(MediaType.APPLICATION_JSON)
                .content(csvContent))
                .andExpect(status().isOk())
                .andExpect(content().string("CSV data uploaded successfully"));
    }
}