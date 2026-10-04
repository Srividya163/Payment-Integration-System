package com.hulkhiretech.payments.repository.impl;

import java.util.List;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.hulkhiretech.payments.repository.interfaces.ValidationRuleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import java.util.ArrayList;

@Repository
@RequiredArgsConstructor
@Slf4j 
public class ValidationRuleRepositoryImpl
        implements ValidationRuleRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final String SELECT_ACTIVE_VALIDATION_RULES_SQL ="""
            select validatorName 
            from validations.validation_rules
            where isActive =true
            order by priority asc
            """;

    @Override
    public List<String> loadActiveValidatorNamesOrderByPriority() {
        log.info("loading active validator names order by priority");

        List<String> names = namedParameterJdbcTemplate.query(
                SELECT_ACTIVE_VALIDATION_RULES_SQL,
                new MapSqlParameterSource(),
                (rs, rowNum) -> rs.getString("validatorName")
        );
        return names==null ? new ArrayList<>() : names;
    }
}
                    