package com.xmcy.crypto.controller;

import com.xmcy.crypto.model.Crypto;
import com.xmcy.crypto.model.SortDirection;
import com.xmcy.crypto.service.CryptoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.Collections;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CryptoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CryptoService cryptoService;

    @InjectMocks
    private CryptoController cryptoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(cryptoController).build();
    }

    @Test
    void testGetAllCryptos() throws Exception {
        when(cryptoService.getAllCryptos()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/crypto"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testGetCryptoById() throws Exception {
        Crypto crypto = new Crypto(1L, 1L, "Bitcoin", 50000.0, "2023-01-01");
        when(cryptoService.getCryptoById(anyLong())).thenReturn(Optional.of(crypto));

        mockMvc.perform(get("/api/crypto/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Bitcoin"));
    }

    @Test
    void testAddCrypto() throws Exception {
        Crypto crypto = new Crypto(1L, 1L, "Bitcoin", 50000.0, "2023-01-01");
        when(cryptoService.addCrypto(any(Crypto.class))).thenReturn(crypto);

        mockMvc.perform(post("/api/crypto")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Bitcoin\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Bitcoin"));
    }

    @Test
    void testUpdateCrypto() throws Exception {
        Crypto crypto = new Crypto(1L, 1L, "Bitcoin", 50000.0, "2023-01-01");
        when(cryptoService.updateCrypto(anyLong(), any(Crypto.class))).thenReturn(crypto);

        mockMvc.perform(put("/api/crypto/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Bitcoin\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Bitcoin"));
    }

    @Test
    void testDeleteCrypto() throws Exception {
        mockMvc.perform(delete("/api/crypto/{id}", 1L))
                .andExpect(status().isOk());
    }

    @Test
    void testGetRecommendations() throws Exception {
        when(cryptoService.getRecommendations()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/crypto/recommendations"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testGetCryptoWithMinValueByName() throws Exception {
        Crypto crypto = new Crypto(1L, 1L, "Bitcoin", 50000.0, "2023-01-01");
        when(cryptoService.getCryptoWithMinValueByName(anyString())).thenReturn(Optional.of(crypto));

        mockMvc.perform(get("/api/crypto/min/{name}", "Bitcoin"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Bitcoin"));
    }

    @Test
    void testGetCryptoWithMaxValueByName() throws Exception {
        Crypto crypto = new Crypto(1L, 1L, "Bitcoin", 50000.0, "2023-01-01");
        when(cryptoService.getCryptoWithMaxValueByName(anyString())).thenReturn(Optional.of(crypto));

        mockMvc.perform(get("/api/crypto/max/{name}", "Bitcoin"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Bitcoin"));
    }

    @Test
    void testGetAllMaxValuesGroupedByName() throws Exception {
        when(cryptoService.getAllMaxValuesGroupedByName()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/crypto/max-values"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testGetOldestCrypto() throws Exception {
        Crypto crypto = new Crypto(1L, 1L, "Bitcoin", 50000.0, "2023-01-01");
        when(cryptoService.getOldestCrypto()).thenReturn(Optional.of(crypto));

        mockMvc.perform(get("/api/crypto/oldest"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Bitcoin"));
    }

    @Test
    void testGetNewestCrypto() throws Exception {
        Crypto crypto = new Crypto(1L, 1L, "Bitcoin", 50000.0, "2023-01-01");
        when(cryptoService.getNewestCrypto()).thenReturn(Optional.of(crypto));

        mockMvc.perform(get("/api/crypto/newest"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Bitcoin"));
    }

    @Test
    void testGetOldestCryptoByName() throws Exception {
        Crypto crypto = new Crypto(1L, 1L, "Bitcoin", 50000.0, "2023-01-01");
        when(cryptoService.getOldestCryptoByName(anyString())).thenReturn(Optional.of(crypto));

        mockMvc.perform(get("/api/crypto/oldest/{name}", "Bitcoin"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Bitcoin"));
    }

    @Test
    void testGetNewestCryptoByName() throws Exception {
        Crypto crypto = new Crypto(1L, 1L, "Bitcoin", 50000.0, "2023-01-01");
        when(cryptoService.getNewestCryptoByName(anyString())).thenReturn(Optional.of(crypto));

        mockMvc.perform(get("/api/crypto/newest/{name}", "Bitcoin"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Bitcoin"));
    }

    @Test
    void testGetAllOldestValuesGroupedByName() throws Exception {
        when(cryptoService.getAllOldestValuesGroupedByName()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/crypto/oldest-values"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testGetAllNewestValuesGroupedByName() throws Exception {
        when(cryptoService.getAllNewestValuesGroupedByName()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/crypto/newest-values"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testGetCryptosSortedByTiming() throws Exception {
        when(cryptoService.getCryptosSortedByTiming(anyString(), any(SortDirection.class)))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/crypto/sorted")
                .param("name", "Bitcoin")
                .param("sortDirection", "asc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testGetNormalizedValues() throws Exception {
        when(cryptoService.getNormalizedValues()).thenReturn(Collections.emptyMap());

        mockMvc.perform(get("/api/crypto/normalized-values"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isMap());
    }

    @Test
    void testGetNormalizedValuesByName() throws Exception {
        when(cryptoService.getNormalizedValuesByName(anyString())).thenReturn(Collections.emptyMap());

        mockMvc.perform(get("/api/crypto/normalized-values/{name}", "Bitcoin"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isMap());
    }

    @Test
    void testGetOldestCryptoByNameAndMonth() throws Exception {
        Crypto crypto = new Crypto(1L, 1L, "Bitcoin", 50000.0, null);
        when(cryptoService.getOldestCryptoByNameAndMonth(anyString(), any())).thenReturn(Optional.of(crypto));

        mockMvc.perform(get("/api/crypto/oldest/{name}/{month}", "Bitcoin", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Bitcoin"));
    }

    @Test
    void testGetNewestCryptoByNameAndMonth() throws Exception {
        Crypto crypto = new Crypto(1L, 1L, "Bitcoin", 50000.0, null);
        when(cryptoService.getNewestCryptoByNameAndMonth(anyString(), any())).thenReturn(Optional.of(crypto));

        mockMvc.perform(get("/api/crypto/newest/{name}/{month}", "Bitcoin", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Bitcoin"));
    }

    @Test
    void testGetCryptoWithMaxValueByNameAndMonth() throws Exception {
        Crypto crypto = new Crypto(1L, 1L, "Bitcoin", 50000.0, null);
        when(cryptoService.getCryptoWithMaxValueByNameAndMonth(anyString(), any())).thenReturn(Optional.of(crypto));

        mockMvc.perform(get("/api/crypto/max/{name}/{month}", "Bitcoin", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Bitcoin"));
    }

    @Test
    void testGetCryptoWithMinValueByNameAndMonth() throws Exception {
        Crypto crypto = new Crypto(1L, 1L, "Bitcoin", 50000.0, null);
        when(cryptoService.getCryptoWithMinValueByNameAndMonth(anyString(), any())).thenReturn(Optional.of(crypto));

        mockMvc.perform(get("/api/crypto/min/{name}/{month}", "Bitcoin", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Bitcoin"));
    }

    @Test
    void testGetCryptoWithHighestNormalization() throws Exception {
        Crypto crypto = new Crypto(1L, 1L, "Bitcoin", 50000.0, null);
        when(cryptoService.getCryptoWithHighestNormalization(anyString())).thenReturn(Optional.of(crypto));

        mockMvc.perform(get("/api/crypto/highest-normalization")
                .param("date", "01/01/2023"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Bitcoin"))
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    public void testGetCryptoWithHighestNormalizationByNameAndLastNoOfDays() throws Exception {
        Crypto crypto = new Crypto(1L, 1L, "Bitcoin", 50000.0, "Buy");
        when(cryptoService.getCryptoWithHighestNormalizationByNameAndDays(anyString(), anyInt())).thenReturn(Optional.of(crypto));

        mockMvc.perform(get("/api/crypto/highest-normalization/{name}/{lastNoOfDays}", "Bitcoin", 30))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Bitcoin"))
                .andExpect(jsonPath("$.price").value(50000.0));

        verify(cryptoService, times(1)).getCryptoWithHighestNormalizationByNameAndDays(anyString(), anyInt());
    }

    @Test
    public void testGetCryptoWithMinValueByNameAndLastNoOfDays() throws Exception {
        Crypto crypto = new Crypto(1L, 1L, "Bitcoin", 30000.0, "Buy");
        when(cryptoService.getCryptoWithMinValueByNameAndDays(anyString(), anyInt())).thenReturn(Optional.of(crypto));

        mockMvc.perform(get("/api/crypto/min/{name}/lastNoOfDays/{lastNoOfDays}", "Bitcoin", 30))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Bitcoin"))
                .andExpect(jsonPath("$.price").value(30000.0));

        verify(cryptoService, times(1)).getCryptoWithMinValueByNameAndDays(anyString(), anyInt());
    }

    @Test
    public void testGetCryptoWithMaxValueByNameAndLastNoOfDays() throws Exception {
        Crypto crypto = new Crypto(1L, 1L, "Bitcoin", 60000.0, "Buy");
        when(cryptoService.getCryptoWithMaxValueByNameAndDays(anyString(), anyInt())).thenReturn(Optional.of(crypto));

        mockMvc.perform(get("/api/crypto/max/{name}/lastNoOfDays/{lastNoOfDays}", "Bitcoin", 30))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Bitcoin"))
                .andExpect(jsonPath("$.price").value(60000.0));

        verify(cryptoService, times(1)).getCryptoWithMaxValueByNameAndDays(anyString(), anyInt());
    }
}