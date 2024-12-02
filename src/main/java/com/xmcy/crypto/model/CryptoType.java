package com.xmcy.crypto.model;

import java.util.Arrays;

import com.xmcy.crypto.exception.CryptoNotFoundException;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CryptoType {
    BITCOIN("Bitcoin", "BTC"),
    ETHEREUM("Ethereum", "ETH"),
    RIPPLE("Ripple", "XRP"),
    DOGECOIN("Dogecoin", "DOGE"),
    LITECOIN("Litecoin", "LTC");

    String name;
    String symbol;

    public static CryptoType findByName(String name) {
        return Arrays.stream(CryptoType.values())
                .filter(cryptoType -> cryptoType.name.equalsIgnoreCase(name))
                .findFirst()
                .orElseGet(() -> findBySymbol(name));
    }

    private static CryptoType findBySymbol(String symbol) {
        return Arrays.stream(CryptoType.values())
                .filter(cryptoType -> cryptoType.symbol.equalsIgnoreCase(symbol))
                .findFirst()
                .orElseThrow(() -> new CryptoNotFoundException("Invalid crypto symbol: " + symbol));
    }
}
