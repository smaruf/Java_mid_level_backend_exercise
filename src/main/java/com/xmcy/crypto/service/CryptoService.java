package com.xmcy.crypto.service;

import com.xmcy.crypto.exception.CryptoNotFoundException;
import com.xmcy.crypto.exception.InvalidCryptoDataException;
import com.xmcy.crypto.model.Crypto;
import com.xmcy.crypto.model.Months;
import com.xmcy.crypto.model.SortDirection;
import com.xmcy.crypto.repository.CryptoRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CryptoService {

    private final CryptoRepository cryptoRepository;

    public List<Crypto> getAllCryptos() {
        return cryptoRepository.findAll();
    }

    public Optional<Crypto> getCryptoById(Long id) {
        return Optional.of(cryptoRepository.findById(id)
                .orElseThrow(() -> new CryptoNotFoundException("No data found for Id:" + id)));
    }

    public Crypto addCrypto(Crypto crypto) {
        if (crypto.name() == null || crypto.price() == null) {
            throw new InvalidCryptoDataException("Invalid crypto data");
        }
        return cryptoRepository.save(crypto);
    }

    public Crypto updateCrypto(Long id, Crypto cryptoDetails) {
        Crypto existingCrypto = cryptoRepository.findById(id)
                .orElseThrow(() -> new CryptoNotFoundException("Crypto not found with id: " + id));
        Crypto updatedCrypto = new Crypto(
                existingCrypto.id(),
                cryptoDetails.timing(),
                cryptoDetails.name(),
                cryptoDetails.price(),
                cryptoDetails.recommendation());
        return cryptoRepository.save(updatedCrypto);
    }

    public void deleteCrypto(Long id) {
        if (!cryptoRepository.existsById(id)) {
            throw new CryptoNotFoundException("Crypto not found with id: " + id);
        }
        cryptoRepository.deleteById(id);
    }

    public List<Crypto> getRecommendations() {
        List<Crypto> allCryptos = cryptoRepository.findAll();
        return allCryptos.stream()
                .filter(crypto -> "Buy".equalsIgnoreCase(crypto.recommendation()))
                .collect(Collectors.toList());
    }

    public Optional<Crypto> getCryptoWithMinValueByName(String name) {
        return cryptoRepository.findTopByNameOrderByPriceAsc(name);
    }

    public Optional<Crypto> getCryptoWithMaxValueByName(String name) {
        return cryptoRepository.findTopByNameOrderByPriceDesc(name);
    }

    public List<Crypto> getAllMaxValuesGroupedByName() {
        return cryptoRepository.findAllMaxValuesGroupedByName();
    }

    public List<Crypto> getAllMinValuesGroupedByName() {
        return cryptoRepository.findAllMinValuesGroupedByName();
    }

    public Optional<Crypto> getOldestCryptoByName(String name) {
        return cryptoRepository.findTopByNameOrderByTimingAsc(name);
    }

    public Optional<Crypto> getOldestCrypto() {
        return cryptoRepository.findTopByOrderByTimingAsc();
    }

    public Optional<Crypto> getNewestCrypto() {
        return cryptoRepository.findTopByOrderByTimingDesc();
    }

    public Optional<Crypto> getNewestCryptoByName(String name) {
        return cryptoRepository.findTopByNameOrderByTimingDesc(name);
    }

    public List<Crypto> getAllOldestValuesGroupedByName() {
        return cryptoRepository.findAllMinValuesGroupedByName();
    }

    public List<Crypto> getAllNewestValuesGroupedByName() {
        return cryptoRepository.findAllMaxValuesGroupedByName();
    }

    public List<Crypto> getCryptosSortedByTiming(String name, SortDirection sortDirection) {
        if (SortDirection.ASC.equals(sortDirection)) {
            return cryptoRepository.findTopByNameOrderByTimingAsc(name)
                    .map(List::of)
                    .orElseThrow(() -> new CryptoNotFoundException("No data found for name: " + name));
        } else {
            return cryptoRepository.findTopByNameOrderByTimingDesc(name)
                    .map(List::of)
                    .orElseThrow(() -> new CryptoNotFoundException("No data found for name: " + name));
        }
    }

    public Map<String, Double> getNormalizedValues() {
        var cryptos = cryptoRepository.findAll();
        return calculateNormalizedValues(cryptos);
    }

    public Map<String, Double> getNormalizedValuesByName(String name) {
        var cryptos = cryptoRepository.findByName(name);
        return calculateNormalizedValues(cryptos);
    }

    private Map<String, Double> calculateNormalizedValues(List<Crypto> cryptos) {
        Map<String, Double> normalizedValues = new HashMap<>();
        var groupedByName = cryptos.stream()
                .collect(Collectors.groupingBy(Crypto::name));

        for (Map.Entry<String, List<Crypto>> entry : groupedByName.entrySet()) {
            String name = entry.getKey();
            List<Crypto> cryptoList = entry.getValue();
            var min = cryptoList.stream().mapToDouble(Crypto::price).min();
            var max = cryptoList.stream().mapToDouble(Crypto::price).max();

            if (min.isPresent() && max.isPresent() && min.getAsDouble() != 0) {
                var normalizedValue = (max.getAsDouble() - min.getAsDouble()) / min.getAsDouble();
                normalizedValues.put(name, normalizedValue);
            }
        }

        return normalizedValues;
    }

    public Optional<Crypto> getOldestCryptoByNameAndMonth(String name, Months month) {
        return cryptoRepository.findByName(name)
                .stream()
                .filter(isInMonthRange(month))
                .sorted((a, b) -> a.timing().compareTo(b.timing()))
                .findFirst();
    }

    private Predicate<? super Crypto> isInMonthRange(Months month) {
        return crypto -> crypto.timing() >= month.getStartingOfMonthInMillis()
                && crypto.timing() <= month.getEndOfMonthInMillis();
    }

    public Optional<Crypto> getNewestCryptoByNameAndMonth(String name, Months month) {
        return cryptoRepository.findByName(name)
                .stream()
                .filter(isInMonthRange(month))
                .sorted((a, b) -> b.timing().compareTo(a.timing()))
                .findFirst();
    }

    public Optional<Crypto> getCryptoWithMaxValueByNameAndMonth(String name, Months month) {
        return cryptoRepository.findByName(name)
                .stream()
                .filter(isInMonthRange(month))
                .sorted((a, b) -> a.price().compareTo(b.price()))
                .findFirst();
    }

    public Optional<Crypto> getCryptoWithMinValueByNameAndMonth(String name, Months month) {
        return cryptoRepository.findByName(name)
                .stream()
                .filter(isInMonthRange(month))
                .sorted((a, b) -> b.price().compareTo(a.price()))
                .findFirst();
    }

    public Optional<Crypto> getCryptoWithHighestNormalization(String date) {
        var localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        var startingOfDateMillis = localDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
        var endOfDateMillis = localDate.atTime(23, 59, 59, 999999999)
                .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        var cryptos = cryptoRepository.findAllByTimingBetween(startingOfDateMillis, endOfDateMillis);
        var normalizedValues = calculateNormalizedValues(cryptos);
        var max = normalizedValues.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElseThrow(() -> new CryptoNotFoundException("No data found for date: " + date));
        return cryptos.stream()
                .filter(crypto -> crypto.name().equals(max.getKey())
                        && crypto.price().equals(max.getValue()))
                .findFirst();
    }

    public Optional<Crypto> getCryptoWithHighestNormalizationByNameAndDays(String name, int lastNoOfDays) {
        var localDate = LocalDate.now().minusDays(lastNoOfDays);
        var startingOfDateMillis = localDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
        var endOfDateMillis = localDate.atStartOfDay(null).toInstant().toEpochMilli();
        var cryptos = cryptoRepository.findAllByTimingBetween(startingOfDateMillis, endOfDateMillis);
        var max = calculateNormalizedValues(cryptos)
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElseThrow(() -> new CryptoNotFoundException("No data found for last days: " + lastNoOfDays));
        return cryptos.stream()
                .filter(crypto -> crypto.name().equals(max.getKey())
                        && crypto.price().equals(max.getValue()))
                .findFirst();
    }

    public Optional<Crypto> getCryptoWithMinValueByNameAndDays(String name, int lastNoOfDays) {
        var localDate = LocalDate.now().minusDays(lastNoOfDays);
        var startingOfDateMillis = localDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
        var endOfDateMillis = localDate.atStartOfDay(null).toInstant().toEpochMilli();
        var cryptos = cryptoRepository.findAllByTimingBetween(startingOfDateMillis, endOfDateMillis);
        return cryptos.stream()
                .filter(crypto -> crypto.name().equals(name))
                .min((a, b) -> a.price().compareTo(b.price()));
    }

    public Optional<Crypto> getCryptoWithMaxValueByNameAndDays(String name, int lastNoOfDays) {
        var localDate = LocalDate.now().minusDays(lastNoOfDays);
        var startingOfDateMillis = localDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
        var endOfDateMillis = localDate.atStartOfDay(null).toInstant().toEpochMilli();
        var cryptos = cryptoRepository.findAllByTimingBetween(startingOfDateMillis, endOfDateMillis);
        return cryptos.stream()
                .filter(crypto -> crypto.name().equals(name))
                .min((a, b) -> b.price().compareTo(a.price()));
    }
}