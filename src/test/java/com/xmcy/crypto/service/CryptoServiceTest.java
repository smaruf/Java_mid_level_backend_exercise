package com.xmcy.crypto.service;

import com.xmcy.crypto.exception.CryptoNotFoundException;
import com.xmcy.crypto.exception.InvalidCryptoDataException;
import com.xmcy.crypto.model.Crypto;
import com.xmcy.crypto.model.SortDirection;
import com.xmcy.crypto.repository.CryptoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CryptoServiceTest {

    @Mock
    private CryptoRepository cryptoRepository;

    @InjectMocks
    private CryptoService cryptoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCryptos() {
        List<Crypto> cryptos = List.of(new Crypto(1L, 123456789L, "Bitcoin", 50000.0, "Buy"));
        when(cryptoRepository.findAll()).thenReturn(cryptos);

        List<Crypto> result = cryptoService.getAllCryptos();

        assertEquals(1, result.size());
        assertEquals("Bitcoin", result.get(0).name());
    }

    @Test
    void testGetCryptoById() {
        Crypto crypto = new Crypto(1L, 123456789L, "Bitcoin", 50000.0, "Buy");
        when(cryptoRepository.findById(1L)).thenReturn(Optional.of(crypto));

        Optional<Crypto> result = cryptoService.getCryptoById(1L);

        assertTrue(result.isPresent());
        assertEquals("Bitcoin", result.get().name());
    }

    @Test
    void testGetCryptoByIdNotFound() {
        when(cryptoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CryptoNotFoundException.class, () -> cryptoService.getCryptoById(1L));
    }

    @Test
    void testAddCrypto() {
        Crypto crypto = new Crypto(1L, 123456789L, "Bitcoin", 50000.0, "Buy");
        when(cryptoRepository.save(any(Crypto.class))).thenReturn(crypto);

        Crypto result = cryptoService.addCrypto(crypto);

        assertEquals("Bitcoin", result.name());
    }

    @Test
    void testAddCryptoInvalidData() {
        Crypto crypto = new Crypto(1L, 123456789L, null, 50000.0, "Buy");

        assertThrows(InvalidCryptoDataException.class, () -> cryptoService.addCrypto(crypto));
    }

    @Test
    void testUpdateCrypto() {
        Crypto existingCrypto = new Crypto(1L, 123456789L, "Bitcoin", 50000.0, "Buy");
        Crypto updatedCrypto = new Crypto(1L, 123456789L, "Ethereum", 3000.0, "Sell");
        when(cryptoRepository.findById(1L)).thenReturn(Optional.of(existingCrypto));
        when(cryptoRepository.save(any(Crypto.class))).thenReturn(updatedCrypto);

        Crypto result = cryptoService.updateCrypto(1L, updatedCrypto);

        assertEquals("Ethereum", result.name());
    }

    @Test
    void testUpdateCryptoNotFound() {
        Crypto updatedCrypto = new Crypto(1L, 123456789L, "Ethereum", 3000.0, "Sell");
        when(cryptoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CryptoNotFoundException.class, () -> cryptoService.updateCrypto(1L, updatedCrypto));
    }

    @Test
    void testDeleteCrypto() {
        when(cryptoRepository.existsById(1L)).thenReturn(true);
        doNothing().when(cryptoRepository).deleteById(1L);

        assertDoesNotThrow(() -> cryptoService.deleteCrypto(1L));
    }

    @Test
    void testDeleteCryptoNotFound() {
        when(cryptoRepository.existsById(1L)).thenReturn(false);

        assertThrows(CryptoNotFoundException.class, () -> cryptoService.deleteCrypto(1L));
    }

    @Test
    void testGetRecommendations() {
        List<Crypto> cryptos = List.of(new Crypto(1L, 123456789L, "Bitcoin", 50000.0, "Buy"));
        when(cryptoRepository.findAll()).thenReturn(cryptos);

        List<Crypto> result = cryptoService.getRecommendations();

        assertEquals(1, result.size());
        assertEquals("Bitcoin", result.get(0).name());
    }

    @Test
    void testGetCryptoWithMinValueByName() {
        Crypto crypto = new Crypto(1L, 123456789L, "Bitcoin", 50000.0, "Buy");
        when(cryptoRepository.findTopByNameOrderByPriceAsc("Bitcoin")).thenReturn(Optional.of(crypto));

        Optional<Crypto> result = cryptoService.getCryptoWithMinValueByName("Bitcoin");

        assertTrue(result.isPresent());
        assertEquals("Bitcoin", result.get().name());
    }

    @Test
    void testGetCryptoWithMaxValueByName() {
        Crypto crypto = new Crypto(1L, 123456789L, "Bitcoin", 50000.0, "Buy");
        when(cryptoRepository.findTopByNameOrderByPriceDesc("Bitcoin")).thenReturn(Optional.of(crypto));

        Optional<Crypto> result = cryptoService.getCryptoWithMaxValueByName("Bitcoin");

        assertTrue(result.isPresent());
        assertEquals("Bitcoin", result.get().name());
    }

    @Test
    void testGetAllMaxValuesGroupedByName() {
        List<Crypto> cryptos = List.of(new Crypto(1L, 123456789L, "Bitcoin", 50000.0, "Buy"));
        when(cryptoRepository.findAllMaxValuesGroupedByName()).thenReturn(cryptos);

        List<Crypto> result = cryptoService.getAllMaxValuesGroupedByName();

        assertEquals(1, result.size());
        assertEquals("Bitcoin", result.get(0).name());
    }

    @Test
    void testGetAllMinValuesGroupedByName() {
        List<Crypto> cryptos = List.of(new Crypto(1L, 123456789L, "Bitcoin", 50000.0, "Buy"));
        when(cryptoRepository.findAllMinValuesGroupedByName()).thenReturn(cryptos);

        List<Crypto> result = cryptoService.getAllMinValuesGroupedByName();

        assertEquals(1, result.size());
        assertEquals("Bitcoin", result.get(0).name());
    }

    @Test
    void testGetOldestCryptoByName() {
        Crypto crypto = new Crypto(1L, 123456789L, "Bitcoin", 50000.0, "Buy");
        when(cryptoRepository.findTopByNameOrderByTimingAsc("Bitcoin")).thenReturn(Optional.of(crypto));

        Optional<Crypto> result = cryptoService.getOldestCryptoByName("Bitcoin");

        assertTrue(result.isPresent());
        assertEquals("Bitcoin", result.get().name());
    }

    @Test
    void testGetOldestCrypto() {
        Crypto crypto = new Crypto(1L, 123456789L, "Bitcoin", 50000.0, "Buy");
        when(cryptoRepository.findTopByOrderByTimingAsc()).thenReturn(Optional.of(crypto));

        Optional<Crypto> result = cryptoService.getOldestCrypto();

        assertTrue(result.isPresent());
        assertEquals("Bitcoin", result.get().name());
    }

    @Test
    void testGetNewestCrypto() {
        Crypto crypto = new Crypto(1L, 123456789L, "Bitcoin", 50000.0, "Buy");
        when(cryptoRepository.findTopByOrderByTimingDesc()).thenReturn(Optional.of(crypto));

        Optional<Crypto> result = cryptoService.getNewestCrypto();

        assertTrue(result.isPresent());
        assertEquals("Bitcoin", result.get().name());
    }

    @Test
    void testGetNewestCryptoByName() {
        Crypto crypto = new Crypto(1L, 123456789L, "Bitcoin", 50000.0, "Buy");
        when(cryptoRepository.findTopByNameOrderByTimingDesc("Bitcoin")).thenReturn(Optional.of(crypto));

        Optional<Crypto> result = cryptoService.getNewestCryptoByName("Bitcoin");

        assertTrue(result.isPresent());
        assertEquals("Bitcoin", result.get().name());
    }

    @Test
    void testGetCryptosSortedByTimingAsc() {
        Crypto crypto = new Crypto(1L, 123456789L, "Bitcoin", 50000.0, "Buy");
        when(cryptoRepository.findTopByNameOrderByTimingAsc("Bitcoin")).thenReturn(Optional.of(crypto));

        List<Crypto> result = cryptoService.getCryptosSortedByTiming("Bitcoin", SortDirection.ASC);

        assertEquals(1, result.size());
        assertEquals("Bitcoin", result.get(0).name());
    }

    @Test
    void testGetCryptosSortedByTimingDesc() {
        Crypto crypto = new Crypto(1L, 123456789L, "Bitcoin", 50000.0, "Buy");
        when(cryptoRepository.findTopByNameOrderByTimingDesc("Bitcoin")).thenReturn(Optional.of(crypto));

        List<Crypto> result = cryptoService.getCryptosSortedByTiming("Bitcoin", SortDirection.DESC);

        assertEquals(1, result.size());
        assertEquals("Bitcoin", result.get(0).name());
    }

    @Test
    void testGetNormalizedValues() {
        List<Crypto> cryptos = List.of(new Crypto(1L, 123456789L, "Bitcoin", 50000.0, "Buy"),
                new Crypto(2L, 123456789L, "Ethereum", 4000.0, "Buy"),
                new Crypto(4L, 123456789L, "Ethereum", 3000.0, "Buy"),
                new Crypto(3L, 123456789L, "Ripple", 1.0, "Buy"));
        when(cryptoRepository.findAll()).thenReturn(cryptos);

        Map<String, Double> result = cryptoService.getNormalizedValues();

        assertEquals(3, result.size());
        assertTrue(result.containsKey("Bitcoin"));
    }

    @Test
    void testGetNormalizedValuesByName() {
        List<Crypto> cryptos = List.of(new Crypto(1L, 123456789L, "Bitcoin", 50000.0, "Buy"),
                new Crypto(2L, 123457789L, "Ethereum", 4000.0, "Buy"),
                new Crypto(3L, 123458789L, "Ethereum", 2000.0, "Buy"),
                new Crypto(4L, 123459789L, "Ethereum", 3000.0, "Buy"),
                new Crypto(5L, 123460789L, "Ripple", 1.0, "Buy"));
        when(cryptoRepository.findByName("Bitcoin")).thenReturn(cryptos);

        Map<String, Double> result = cryptoService.getNormalizedValuesByName("Bitcoin");

        assertEquals(3, result.size());
        assertTrue(result.containsKey("Bitcoin"));
    }
}