package com.xmcy.crypto.controller;

import com.xmcy.crypto.model.Crypto;
import com.xmcy.crypto.model.CryptoType;
import com.xmcy.crypto.model.Months;
import com.xmcy.crypto.model.SortDirection;
import com.xmcy.crypto.service.CryptoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
@RequiredArgsConstructor
@Validated
public class CryptoController {
    private final CryptoService cryptoService;

    @Operation(summary = "View a list of available cryptocurrencies")
    @GetMapping
    public List<Crypto> getAllCryptos() {
        return cryptoService.getAllCryptos();
    }

    @Operation(summary = "Get a cryptocurrency by ID")
    @GetMapping("/{id}")
    public Optional<Crypto> getCryptoById(
            @Parameter(description  = "ID of the cryptocurrency to retrieve", required = true) @PathVariable Long id) {
        return cryptoService.getCryptoById(id);
    }

    @Operation(
        summary = "Add a new cryptocurrency",
        description = "Adds a new cryptocurrency to the database",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Cryptocurrency added successfully"
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Invalid cryptocurrency data"
            )
        }
    )
    @PostMapping
    public Crypto addCrypto(
            @Parameter(description  = "Cryptocurrency object to add", required = true) @Valid @RequestBody Crypto crypto) {
        return cryptoService.addCrypto(crypto);
    }

    @Operation(
        summary = "Update an existing cryptocurrency",
        description = "Updates the details of an existing cryptocurrency",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Cryptocurrency updated successfully"
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Cryptocurrency not found"
            )
        }
    )
    @PutMapping("/{id}")
    public Crypto updateCrypto(
            @Parameter(description  = "ID of the cryptocurrency to update", required = true) @PathVariable Long id,
            @Parameter(description  = "Updated cryptocurrency object", required = true) @Valid @RequestBody Crypto cryptoDetails) {
        return cryptoService.updateCrypto(id, cryptoDetails);
    }

    @Operation(
        summary = "Delete a cryptocurrency",
        description = "Deletes a cryptocurrency from the database",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Cryptocurrency deleted successfully"
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Cryptocurrency not found"
            )
        }
    )
    @DeleteMapping("/{id}")
    public void deleteCrypto(
            @Parameter(description  = "ID of the cryptocurrency to delete", required = true) @PathVariable Long id) {
        cryptoService.deleteCrypto(id);
    }

    @Operation(
        summary = "Get cryptocurrency recommendations",
        description = "Retrieves a list of recommended cryptocurrencies",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Recommendations retrieved successfully"
            )
        }
    )
    @GetMapping("/recommendations")
    public List<Crypto> getRecommendations() {
        return cryptoService.getRecommendations();
    }

    @Operation(
        summary = "Get the cryptocurrency with the minimum value by name",
        description = "Retrieves the cryptocurrency with the minimum value for a given name",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Cryptocurrency retrieved successfully"
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Cryptocurrency not found"
            )
        }
    )
    @GetMapping("/min/{name}")
    public Crypto getCryptoWithMinValueByName(
            @Parameter(description  = "Name of the cryptocurrency to retrieve the minimum value for", required = true) @PathVariable String name) {
        return cryptoService.getCryptoWithMinValueByName(CryptoType.findByName(name).getName())
                .orElseThrow(() -> new RuntimeException("No cryptocurrency found with the name: " + name));
    }

    @Operation(
            summary = "Get the cryptocurrency with the maximum value by name",
            description = "Retrieves the cryptocurrency with the maximum value for a given name",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Cryptocurrency retrieved successfully"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Cryptocurrency not found"
                    )
            }
    )
    @GetMapping("/max/{name}")
    public Crypto getCryptoWithMaxValueByName(
            @Parameter(description  = "Name of the cryptocurrency to retrieve the maximum value for", required = true) @PathVariable String name) {
        return cryptoService.getCryptoWithMaxValueByName(CryptoType.findByName(name).getName())
                .orElseThrow(() -> new CryptoNotFoundException("No cryptocurrency found with the name: " + name));
    }

    @Operation(
            summary = "Get all cryptocurrencies with the maximum value grouped by name",
            description = "Retrieves the cryptocurrency with the maximum value for a given name",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Cryptocurrency retrieved successfully"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Cryptocurrency not found"
                    )
            }
    )
    @GetMapping("/max-values")
    public List<Crypto> getAllMaxValuesGroupedByName() {
        return cryptoService.getAllMaxValuesGroupedByName();
    }
    @Operation(
            summary = "Get the oldest cryptocurrency based on timing",
            description = "Get the oldest cryptocurrency based on timing",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Cryptocurrency retrieved successfully"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Cryptocurrency not found"
                    )
            }
    )
    @GetMapping("/oldest")
    public Crypto getOldestCrypto() {
        return cryptoService.getOldestCrypto()
                .orElseThrow(() -> new CryptoNotFoundException("No cryptocurrencies found"));
    }

    @Operation(
            summary = "Get the newest cryptocurrency based on timing",
            description = "Get the newest cryptocurrency based on timing",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Cryptocurrency retrieved successfully"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Cryptocurrency not found"
                    )
            }
    )
    @GetMapping("/newest")
    public Crypto getNewestCrypto() {
        return cryptoService.getNewestCrypto()
                .orElseThrow(() -> new CryptoNotFoundException("No cryptocurrencies found"));
    }

    @Operation(
            summary = "Get the oldest cryptocurrency by name",
            description = "Get the oldest cryptocurrency by name",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Cryptocurrency retrieved successfully"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Cryptocurrency not found"
                    )
            }
    )
    @GetMapping("/oldest/{name}")
    public Crypto getOldestCryptoByName(
            @Parameter(description  = "Name of the cryptocurrency to retrieve the oldest value for", required = true) @PathVariable String name) {
        return cryptoService.getOldestCryptoByName(CryptoType.findByName(name).getName())
                .orElseThrow(() -> new CryptoNotFoundException("No cryptocurrency found with the name: " + name));
    }

    @Operation(
            summary = "Get the newest cryptocurrency by name",
            description = "Get the newest cryptocurrency by name",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Cryptocurrency retrieved successfully"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Cryptocurrency not found"
                    )
            }
    )
    @GetMapping("/newest/{name}")
    public Crypto getNewestCryptoByName(
            @Parameter(description  = "Name of the cryptocurrency to retrieve the newest value for", required = true) @PathVariable String name) {
        return cryptoService.getNewestCryptoByName(CryptoType.findByName(name).getName())
                .orElseThrow(() -> new CryptoNotFoundException("No cryptocurrency found with the name: " + name));
    }

    @Operation(
            summary = "Get all cryptocurrencies with the oldest value grouped by name",
            description = "Get all cryptocurrencies with the oldest value grouped by name",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Cryptocurrency retrieved successfully"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Cryptocurrency not found"
                    )
            }
    )
    @GetMapping("/oldest-values")
    public List<Crypto> getAllOldestValuesGroupedByName() {
        return cryptoService.getAllOldestValuesGroupedByName();
    }

    @Operation(
            summary = "Get all cryptocurrencies with the newest value grouped by name",
            description = "Get all cryptocurrencies with the newest value grouped by name",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Cryptocurrency retrieved successfully"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Cryptocurrency not found"
                    )
            }
    )

    @GetMapping("/newest-values")
    public List<Crypto> getAllNewestValuesGroupedByName() {
        return cryptoService.getAllNewestValuesGroupedByName();
    }

    @Operation(
            summary = "Get cryptocurrencies sorted by timing with name and sort direction",
            description = "Get cryptocurrencies sorted by timing with name and sort direction",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Cryptocurrency retrieved successfully"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Cryptocurrency not found"
                    )
            }
    )
    @GetMapping("/sorted")
    public List<Crypto> getCryptosSortedByTiming(
            @Parameter(description  = "Name of the cryptocurrency to sort", required = true) @RequestParam String name,
            @Parameter(description  = "Sort direction (asc or desc)", required = true) @RequestParam String sortDirection) {
        return cryptoService.getCryptosSortedByTiming(CryptoType.findByName(name).getName(), SortDirection.fromString(sortDirection));
    }

    @Operation(
            summary = "Get normalized values of cryptocurrencies",
            description = "Get normalized values of cryptocurrencies",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Cryptocurrency retrieved successfully"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Cryptocurrency not found"
                    )
            }
    )
    @GetMapping("/normalized-values")
    public Map<String, Double> getNormalizedValues() {
        return cryptoService.getNormalizedValues();
    }

    @Operation(
            summary = "Get normalized values of cryptocurrencies by name",
            description = "Get normalized values of cryptocurrencies by name",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Cryptocurrency retrieved successfully"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Cryptocurrency not found"
                    )
            }
    )
    @GetMapping("/normalized-values/{name}")
    public Map<String, Double> getNormalizedValuesByName(
            @Parameter(description  = "Name of the cryptocurrency to retrieve normalized values for", required = true)
            @PathVariable String name) {
        return cryptoService.getNormalizedValuesByName(CryptoType.findByName(name).getName());
    }

    @Operation(
            summary = "Get the oldest cryptocurrency by name and month",
            description = "Get the oldest cryptocurrency by name and month",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Cryptocurrency retrieved successfully"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Cryptocurrency not found"
                    )
            }
    )
    @GetMapping("/oldest/{name}/{month}")
    public Crypto getOldestCryptoByNameAndMonth(
            @Parameter(description  = "Name of the cryptocurrency to retrieve the oldest value for", required = true) @PathVariable String name,
            @Parameter(description  = "Month to compare with timing", required = true) @PathVariable int month) {
        return cryptoService.getOldestCryptoByNameAndMonth(CryptoType.findByName(name).getName(), Months.fromValue(month))
                .orElseThrow(() -> new CryptoNotFoundException("No cryptocurrency found with the name: " + name + " and month: " + month));
    }

    @Operation(
            summary = "Get the newest cryptocurrency by name and month",
            description = "Get the newest cryptocurrency by name and month",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Cryptocurrency retrieved successfully"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Cryptocurrency not found"
                    )
            }
    )
    @GetMapping("/newest/{name}/{month}")
    public Crypto getNewestCryptoByNameAndMonth(
            @Parameter(description  = "Name of the cryptocurrency to retrieve the newest value for", required = true) @PathVariable String name,
            @Parameter(description  = "Month to compare with timing", required = true) @PathVariable int month) {
        return cryptoService.getNewestCryptoByNameAndMonth(CryptoType.findByName(name).getName(), Months.fromValue(month))
                .orElseThrow(() -> new CryptoNotFoundException("No cryptocurrency found with the name: " + name + " and month: " + month));
    }

    @Operation(
            summary = "Get the cryptocurrency with the maximum value by name and month",
            description = "Get the cryptocurrency with the maximum value by name and month",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Cryptocurrency retrieved successfully"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Cryptocurrency not found"
                    )
            }
    )
    @GetMapping("/max/{name}/{month}")
    public Crypto getCryptoWithMaxValueByNameAndMonth(
            @Parameter(description  = "Name of the cryptocurrency to retrieve the maximum value for", required = true) @PathVariable String name,
            @Parameter(description  = "Month to compare with timing", required = true) @PathVariable int month) {
        return cryptoService.getCryptoWithMaxValueByNameAndMonth(CryptoType.findByName(name).getName(), Months.fromValue(month))
                .orElseThrow(() -> new CryptoNotFoundException("No cryptocurrency found with the name: " + name + " and month: " + month));
    }

    @Operation(
            summary = "Get the cryptocurrency with the minimum value by name and month",
            description = "Get the cryptocurrency with the minimum value by name and month",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Cryptocurrency retrieved successfully"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Cryptocurrency not found"
                    )
            }
    )
    @GetMapping("/min/{name}/{month}")
    public Crypto getCryptoWithMinValueByNameAndMonth(
            @Parameter(description  = "Name of the cryptocurrency to retrieve the minimum value for", required = true) @PathVariable String name,
            @Parameter(description  = "Month to compare with timing", required = true) @PathVariable int month) {
        return cryptoService.getCryptoWithMinValueByNameAndMonth(CryptoType.findByName(name).getName(), Months.fromValue(month))
                .orElseThrow(() -> new CryptoNotFoundException("No cryptocurrency found with the name: " + name + " and month: " + month));
    }

    @Operation(
            summary = "Get the cryptocurrency with the highest normalization value for a given date",
            description = "Get the cryptocurrency with the highest normalization value for a given date",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Cryptocurrency retrieved successfully"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Cryptocurrency not found"
                    )
            }
    )
    @GetMapping("/highest-normalization")
    public Crypto getCryptoWithHighestNormalization(
            @Parameter(description  = "Date to compare with timing(MM/dd/yyyy)", required = true) @RequestParam String date) {
        return cryptoService.getCryptoWithHighestNormalization(date)
                .orElseThrow(() -> new CryptoNotFoundException("No cryptocurrency found with the highest normalization value for the date: " + date));
    }

    @Operation(
            summary = "Get the cryptocurrency with the highest normalization value by name and last number of days",
            description = "Get the cryptocurrency with the highest normalization value by name and last number of days",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Cryptocurrency retrieved successfully"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Cryptocurrency not found"
                    )
            }
    )
    @GetMapping("/highest-normalization/{name}/{lastNoOfDays}")
    public Crypto getCryptoWithHighestNormalizationByNameAndLastNoOfDays(
            @Parameter(description  = "Name of the cryptocurrency to retrieve the highest normalization value for", required = true) @PathVariable String name,
            @Parameter(description  = "Last number of days to compare with timing", required = true) @PathVariable int lastNoOfDays) {
        return cryptoService.getCryptoWithHighestNormalizationByNameAndDays(CryptoType.findByName(name).getName(), lastNoOfDays)
                .orElseThrow(() -> new CryptoNotFoundException("No cryptocurrency found with the name: " + name + " and last number of days: " + lastNoOfDays));
    }

    @Operation(
            summary = "Get the cryptocurrency with the minimum value by name and last number of days",
            description = "Get the cryptocurrency with the minimum value by name and last number of days",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Cryptocurrency retrieved successfully"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Cryptocurrency not found"
                    )
            }
    )
    @GetMapping("/min/{name}/lastNoOfDays/{lastNoOfDays}")
    public Crypto getCryptoWithMinValueByNameAndLastNoOfDays(
            @Parameter(description  = "Name of the cryptocurrency to retrieve the minimum value for", required = true) @PathVariable String name,
            @Parameter(description  = "Last number of days to compare with timing", required = true) @PathVariable int lastNoOfDays) {
        return cryptoService.getCryptoWithMinValueByNameAndDays(CryptoType.findByName(name).getName(), lastNoOfDays)
                .orElseThrow(() -> new CryptoNotFoundException("No cryptocurrency found with the name: " + name + " and last number of days: " + lastNoOfDays));
    }

    @Operation(
            summary = "Get the cryptocurrency with the maximum value by name and last number of days",
            description = "Get the cryptocurrency with the maximum value by name and last number of days",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Cryptocurrency retrieved successfully"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Cryptocurrency not found"
                    )
            }
    )
    @GetMapping("/max/{name}/lastNoOfDays/{lastNoOfDays}")
    public Crypto getCryptoWithMaxValueByNameAndLastNoOfDays(
            @Parameter(description  = "Name of the cryptocurrency to retrieve the maximum value for", required = true) @PathVariable String name,
            @Parameter(description  = "Last number of days to compare with timing", required = true) @PathVariable int lastNoOfDays) {
        return cryptoService.getCryptoWithMaxValueByNameAndDays(CryptoType.findByName(name).getName(), lastNoOfDays)
                .orElseThrow(() -> new CryptoNotFoundException("No cryptocurrency found with the name: " + name + " and last number of days: " + lastNoOfDays));
    }

}