package com.xmcy.crypto.config;

import com.xmcy.crypto.helper.CsvDataLoader;

import jakarta.annotation.PostConstruct;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;


@Configuration
public class DbConfig {
    private static final Logger logger = LoggerFactory.getLogger(DbConfig.class);

    @Autowired
    private PathMatchingResourcePatternResolver resourceResolver;

    @Autowired
    private CsvDataLoader csvDataLoader;

    @PostConstruct
    public void loadCsvData() {
        try {
            Resource[] resources = resourceResolver.getResources("classpath:price/*.csv");
            csvDataLoader.loadCsvData(resources);
        } catch (Exception e) {
            logger.error("Error loading CSV data", e);
        }
    }
}