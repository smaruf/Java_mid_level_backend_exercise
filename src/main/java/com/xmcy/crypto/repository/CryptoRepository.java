package com.xmcy.crypto.repository;

import com.xmcy.crypto.model.Crypto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CryptoRepository extends JpaRepository<Crypto, Long> {
    Optional<Crypto> findTopByNameOrderByPriceAsc(String name);
    Optional<Crypto> findTopByNameOrderByPriceDesc(String name);

    @Query("SELECT c FROM Crypto c WHERE c.price = (SELECT MAX(c2.price) FROM Crypto c2 WHERE c2.name = c.name)")
    List<Crypto> findAllMaxValuesGroupedByName();
    
    @Query("SELECT c FROM Crypto c WHERE c.price = (SELECT MIN(c2.price) FROM Crypto c2 WHERE c2.name = c.name)")
    List<Crypto> findAllMinValuesGroupedByName();

    Optional<Crypto> findByTimingAndName(Long timestamp, String name);
    Optional<Crypto> findTopByNameOrderByTimingAsc(String name);
    Optional<Crypto> findTopByOrderByTimingAsc();
    Optional<Crypto> findTopByOrderByTimingDesc();
    Optional<Crypto> findTopByNameOrderByTimingDesc(String name);
    List<Crypto> findByName(String name);
    List<Crypto> findAllByTimingBetween(long startingOfDateMillis, long endOfDateMillis);
}