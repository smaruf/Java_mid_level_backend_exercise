package com.xmcy.crypto.helper;

import com.xmcy.crypto.model.Crypto;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInserter {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public DataInserter(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public void insertData(List<Crypto> cryptos) {
        String sql = "INSERT INTO cryptos (id, timing, name, price) VALUES (:id, :timing, :name, :price)";
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(cryptos.toArray());
        namedParameterJdbcTemplate.batchUpdate(sql, batch);
    }
}
