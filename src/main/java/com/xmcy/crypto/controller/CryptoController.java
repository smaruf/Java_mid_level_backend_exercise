package com.xmcy.crypto.controller;

import com.xmcy.crypto.model.Crypto;
import com.xmcy.crypto.model.CryptoType;
import com.xmcy.crypto.model.Months;
import com.xmcy.crypto.model.SortDirection;
import com.xmcy.crypto.service.CryptoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.xmcy.crypto.exception.CryptoNotFoundException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/crypto")
@Api(value = "Crypto Controller", description = "Operations pertaining to cryptocurrencies")
@RequiredArgsConstructor
@Validated
public class CryptoController {
    private final CryptoService cryptoService;

    @ApiOperation(value = "View a list of available cryptocurrencies", response = List.class)
    @GetMapping
    public List<Crypto> getAllCryptos() {
        return cryptoService.getAllCryptos();
    }

    @ApiOperation(value = "Get a cryptocurrency by ID", response = Crypto.class)
    @GetMapping("/{id}")
    public Optional<Crypto> getCryptoById(
            @ApiParam(value = "ID of the cryptocurrency to retrieve", required = true) @PathVariable Long id) {
        return cryptoService.getCryptoById(id);
    }

    @ApiOperation(value = "Add a new cryptocurrency", response = Crypto.class)
    @PostMapping
    public Crypto addCrypto(
            @ApiParam(value = "Cryptocurrency object to add", required = true) @Valid @RequestBody Crypto crypto) {
        return cryptoService.addCrypto(crypto);
    }

    @ApiOperation(value = "Update an existing cryptocurrency", response = Crypto.class)
    @PutMapping("/{id}")
    public Crypto updateCrypto(
            @ApiParam(value = "ID of the cryptocurrency to update", required = true) @PathVariable Long id,
            @ApiParam(value = "Updated cryptocurrency object", required = true) @Valid @RequestBody Crypto cryptoDetails) {
        return cryptoService.updateCrypto(id, cryptoDetails);
    }

    @ApiOperation(value = "Delete a cryptocurrency")
    @DeleteMapping("/{id}")
    public void deleteCrypto(
            @ApiParam(value = "ID of the cryptocurrency to delete", required = true) @PathVariable Long id) {
        cryptoService.deleteCrypto(id);
    }

    @ApiOperation(value = "Get cryptocurrency recommendations", response = List.class)
    @GetMapping("/recommendations")
    public List<Crypto> getRecommendations() {
        return cryptoService.getRecommendations();
    }

    @ApiOperation(value = "Get the cryptocurrency with the minimum value by name", response = Crypto.class)
    @GetMapping("/min/{name}")
    public Crypto getCryptoWithMinValueByName(
            @ApiParam(value = "Name of the cryptocurrency to retrieve the minimum value for", required = true) @PathVariable String name) {
        return cryptoService.getCryptoWithMinValueByName(CryptoType.findByName(name).getName())
                .orElseThrow(() -> new RuntimeException("No cryptocurrency found with the name: " + name));
    }

    @ApiOperation(value = "Get the cryptocurrency with the maximum value by name", response = Crypto.class)
    @GetMapping("/max/{name}")
    public Crypto getCryptoWithMaxValueByName(
            @ApiParam(value = "Name of the cryptocurrency to retrieve the maximum value for", required = true) @PathVariable String name) {
        return cryptoService.getCryptoWithMaxValueByName(CryptoType.findByName(name).getName())
                .orElseThrow(() -> new CryptoNotFoundException("No cryptocurrency found with the name: " + name));
    }

    @ApiOperation(value = "Get all cryptocurrencies with the maximum value grouped by name", response = List.class)
    @GetMapping("/max-values")
    public List<Crypto> getAllMaxValuesGroupedByName() {
        return cryptoService.getAllMaxValuesGroupedByName();
    }

    @ApiOperation(value = "Get the oldest cryptocurrency based on timing", response = Crypto.class)
    @GetMapping("/oldest")
    public Crypto getOldestCrypto() {
        return cryptoService.getOldestCrypto()
                .orElseThrow(() -> new CryptoNotFoundException("No cryptocurrencies found"));
    }

    @ApiOperation(value = "Get the newest cryptocurrency based on timing", response = Crypto.class)
    @GetMapping("/newest")
    public Crypto getNewestCrypto() {
        return cryptoService.getNewestCrypto()
                .orElseThrow(() -> new CryptoNotFoundException("No cryptocurrencies found"));
    }

    @ApiOperation(value = "Get the oldest cryptocurrency by name", response = Crypto.class)
    @GetMapping("/oldest/{name}")
    public Crypto getOldestCryptoByName(
            @ApiParam(value = "Name of the cryptocurrency to retrieve the oldest value for", required = true) @PathVariable String name) {
        return cryptoService.getOldestCryptoByName(CryptoType.findByName(name).getName())
                .orElseThrow(() -> new CryptoNotFoundException("No cryptocurrency found with the name: " + name));
    }

    @ApiOperation(value = "Get the newest cryptocurrency by name", response = Crypto.class)
    @GetMapping("/newest/{name}")
    public Crypto getNewestCryptoByName(
            @ApiParam(value = "Name of the cryptocurrency to retrieve the newest value for", required = true) @PathVariable String name) {
        return cryptoService.getNewestCryptoByName(CryptoType.findByName(name).getName())
                .orElseThrow(() -> new CryptoNotFoundException("No cryptocurrency found with the name: " + name));
    }

    @ApiOperation(value = "Get all cryptocurrencies with the oldest value grouped by name", response = List.class)
    @GetMapping("/oldest-values")
    public List<Crypto> getAllOldestValuesGroupedByName() {
        return cryptoService.getAllOldestValuesGroupedByName();
    }

    @ApiOperation(value = "Get all cryptocurrencies with the newest value grouped by name", response = List.class)
    @GetMapping("/newest-values")
    public List<Crypto> getAllNewestValuesGroupedByName() {
        return cryptoService.getAllNewestValuesGroupedByName();
    }

    @ApiOperation(value = "Get cryptocurrencies sorted by timing with name and sort direction", response = List.class)
    @GetMapping("/sorted")
    public List<Crypto> getCryptosSortedByTiming(
            @ApiParam(value = "Name of the cryptocurrency to sort", required = true) @RequestParam String name,
            @ApiParam(value = "Sort direction (asc or desc)", required = true) @RequestParam String sortDirection) {
        return cryptoService.getCryptosSortedByTiming(CryptoType.findByName(name).getName(), SortDirection.fromString(sortDirection));
    }

    @ApiOperation(value = "Get normalized values of cryptocurrencies", response = Map.class)
    @GetMapping("/normalized-values")
    public Map<String, Double> getNormalizedValues() {
        return cryptoService.getNormalizedValues();
    }

    @ApiOperation(value = "Get normalized values of cryptocurrencies by name", response = Map.class)
    @GetMapping("/normalized-values/{name}")
    public Map<String, Double> getNormalizedValuesByName(
            @ApiParam(value = "Name of the cryptocurrency to retrieve normalized values for", required = true) @PathVariable String name) {
        return cryptoService.getNormalizedValuesByName(CryptoType.findByName(name).getName());
    }

    @ApiOperation(value = "Get the oldest cryptocurrency by name and month", response = Crypto.class)
    @GetMapping("/oldest/{name}/{month}")
    public Crypto getOldestCryptoByNameAndMonth(
            @ApiParam(value = "Name of the cryptocurrency to retrieve the oldest value for", required = true) @PathVariable String name,
            @ApiParam(value = "Month to compare with timing", required = true) @PathVariable int month) {
        return cryptoService.getOldestCryptoByNameAndMonth(CryptoType.findByName(name).getName(), Months.fromValue(month))
                .orElseThrow(() -> new CryptoNotFoundException("No cryptocurrency found with the name: " + name + " and month: " + month));
    }

    @ApiOperation(value = "Get the newest cryptocurrency by name and month", response = Crypto.class)
    @GetMapping("/newest/{name}/{month}")
    public Crypto getNewestCryptoByNameAndMonth(
            @ApiParam(value = "Name of the cryptocurrency to retrieve the newest value for", required = true) @PathVariable String name,
            @ApiParam(value = "Month to compare with timing", required = true) @PathVariable int month) {
        return cryptoService.getNewestCryptoByNameAndMonth(CryptoType.findByName(name).getName(), Months.fromValue(month))
                .orElseThrow(() -> new CryptoNotFoundException("No cryptocurrency found with the name: " + name + " and month: " + month));
    }

    @ApiOperation(value = "Get the cryptocurrency with the maximum value by name and month", response = Crypto.class)
    @GetMapping("/max/{name}/{month}")
    public Crypto getCryptoWithMaxValueByNameAndMonth(
            @ApiParam(value = "Name of the cryptocurrency to retrieve the maximum value for", required = true) @PathVariable String name,
            @ApiParam(value = "Month to compare with timing", required = true) @PathVariable int month) {
        return cryptoService.getCryptoWithMaxValueByNameAndMonth(CryptoType.findByName(name).getName(), Months.fromValue(month))
                .orElseThrow(() -> new CryptoNotFoundException("No cryptocurrency found with the name: " + name + " and month: " + month));
    }

    @ApiOperation(value = "Get the cryptocurrency with the minimum value by name and month", response = Crypto.class)
    @GetMapping("/min/{name}/{month}")
    public Crypto getCryptoWithMinValueByNameAndMonth(
            @ApiParam(value = "Name of the cryptocurrency to retrieve the minimum value for", required = true) @PathVariable String name,
            @ApiParam(value = "Month to compare with timing", required = true) @PathVariable int month) {
        return cryptoService.getCryptoWithMinValueByNameAndMonth(CryptoType.findByName(name).getName(), Months.fromValue(month))
                .orElseThrow(() -> new CryptoNotFoundException("No cryptocurrency found with the name: " + name + " and month: " + month));
    }

    @ApiOperation(value = "Get the cryptocurrency with the highest normalization value for a given date", response = Crypto.class)
    @GetMapping("/highest-normalization")
    public Crypto getCryptoWithHighestNormalization(
            @ApiParam(value = "Date to compare with timing(MM/dd/yyyy)", required = true) @RequestParam String date) {
        return cryptoService.getCryptoWithHighestNormalization(date)
                .orElseThrow(() -> new CryptoNotFoundException("No cryptocurrency found with the highest normalization value for the date: " + date));
    }

    @ApiOperation(value = "Get the cryptocurrency with the highest normalization value by name and last number of days", response = Crypto.class)
    @GetMapping("/highest-normalization/{name}/{lastNoOfDays}")
    public Crypto getCryptoWithHighestNormalizationByNameAndLastNoOfDays(
            @ApiParam(value = "Name of the cryptocurrency to retrieve the highest normalization value for", required = true) @PathVariable String name,
            @ApiParam(value = "Last number of days to compare with timing", required = true) @PathVariable int lastNoOfDays) {
        return cryptoService.getCryptoWithHighestNormalizationByNameAndDays(CryptoType.findByName(name).getName(), lastNoOfDays)
                .orElseThrow(() -> new CryptoNotFoundException("No cryptocurrency found with the name: " + name + " and last number of days: " + lastNoOfDays));
    }

    @ApiOperation(value = "Get the cryptocurrency with the minimum value by name and last number of days", response = Crypto.class)
    @GetMapping("/min/{name}/lastNoOfDays/{lastNoOfDays}")
    public Crypto getCryptoWithMinValueByNameAndLastNoOfDays(
            @ApiParam(value = "Name of the cryptocurrency to retrieve the minimum value for", required = true) @PathVariable String name,
            @ApiParam(value = "Last number of days to compare with timing", required = true) @PathVariable int lastNoOfDays) {
        return cryptoService.getCryptoWithMinValueByNameAndDays(CryptoType.findByName(name).getName(), lastNoOfDays)
                .orElseThrow(() -> new CryptoNotFoundException("No cryptocurrency found with the name: " + name + " and last number of days: " + lastNoOfDays));
    }

    @ApiOperation(value = "Get the cryptocurrency with the maximum value by name and last number of days", response = Crypto.class)
    @GetMapping("/max/{name}/lastNoOfDays/{lastNoOfDays}")
    public Crypto getCryptoWithMaxValueByNameAndLastNoOfDays(
            @ApiParam(value = "Name of the cryptocurrency to retrieve the maximum value for", required = true) @PathVariable String name,
            @ApiParam(value = "Last number of days to compare with timing", required = true) @PathVariable int lastNoOfDays) {
        return cryptoService.getCryptoWithMaxValueByNameAndDays(CryptoType.findByName(name).getName(), lastNoOfDays)
                .orElseThrow(() -> new CryptoNotFoundException("No cryptocurrency found with the name: " + name + " and last number of days: " + lastNoOfDays));
    }

}