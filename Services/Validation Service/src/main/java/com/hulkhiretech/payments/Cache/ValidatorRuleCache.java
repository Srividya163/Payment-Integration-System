package com.hulkhiretech.payments.Cache;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.HashOperations;

import com.hulkhiretech.payments.repository.interfaces.ValidationRuleRepository;
import com.hulkhiretech.payments.repository.interfaces.ValidatorRulesParamsRepository;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Component 
@Slf4j 
public class ValidatorRuleCache {

    
    //private List<String> validatorRules;
    //private Map<String, Map<String,String>> validatorRuleParams; 
    public static final String VALIDATOR_RULES_KEY = "validator-rules";
    public static final String VALIDATOR_RULE_PARAMS_PREFIX ="validator-rule-params";

    private final ValidationRuleRepository validationRuleRepository;
    private final ValidatorRulesParamsRepository validatorRulesParamsRepository;

    private final RedisTemplate<String, String> redisTemplate;

    private ListOperations<String,String> listOperations;
    private HashOperations<String,String,String> hashOperations;

    public ValidatorRuleCache(ValidationRuleRepository validationRuleRepository,
                              ValidatorRulesParamsRepository validatorRulesParamsRepository,
                              RedisTemplate<String, String> redisTemplate) {

        this.validationRuleRepository = validationRuleRepository;
        this.validatorRulesParamsRepository = validatorRulesParamsRepository;
        this.redisTemplate = redisTemplate;
        this.listOperations = redisTemplate.opsForList();
        this.hashOperations = redisTemplate.opsForHash();
    }

    public List<String> getValidatorRules(){
        return listOperations.range(VALIDATOR_RULES_KEY,0,-1);
    }

    public Map<String, String> getValidatorParamsForRule(String ruleName){
        String redisKey = VALIDATOR_RULE_PARAMS_PREFIX + ruleName;
        return hashOperations.entries(redisKey);
    }
    /**
     * write method to set rules in cache take list of strings as input
     * and set in redis list  VALIDATOR_RULES_KEY
     */
    public void setValidatorRules(List<String> rules){
        redisTemplate.delete(VALIDATOR_RULES_KEY);

        if(rules!=null && !rules.isEmpty()){
            listOperations.rightPushAll(VALIDATOR_RULES_KEY,rules);
            log.info("Updated validator rules in redis cache:{}", rules);
            
        }else{
            log.info("No validator rules provided to set in cache; skipping");
        }
    }

    /**
     * write a method which takes Map<String, Map<String,String>> as input
     * and store in redis hash with key validator_rule_params:{validatorName}
     * and field as paramNAme and value as paramValue
     */
    public void setValidatorRuleParams(Map<String,Map<String, String>> validatorRuleParams){
        if(validatorRuleParams == null || validatorRuleParams.isEmpty()){
            log.info("No validator rule params to update in redis cache; cleared all existed params");
            return;
        }

        for(Map.Entry<String, Map<String, String>> entry: validatorRuleParams.entrySet()){
            String validatorName = entry.getKey();
            Map<String,String> params=entry.getValue();
            String redisKey = VALIDATOR_RULE_PARAMS_PREFIX + validatorName;

            redisTemplate.delete(redisKey);

            if(params != null && !params.isEmpty()){
                hashOperations.putAll(redisKey, params);
                log.info("Updated params for validator {} in redis cache: {}", validatorName, params);
            }else{
                log.info("No params to update for validator {} in Redis cache;cleared", validatorName);
            }
        }
    }
    /*public Map<String, String> getValidatorParamsForRule(String ruleName){
        return validatorRuleParams.getOrDefault(ruleName, Map.of());
    }*/


    @PostConstruct
    public void init(){
        try{
            List<String>validatorRules= getValidatorRules();
            if(validatorRules !=null && !validatorRules.isEmpty()){
                log.info("Loaded {} validator rules from redis cache validatorRules:{}",
                validatorRules != null  ? validatorRules.size() : 0, validatorRules);

                return;
            }
            log.info("No validator rules found in redis cache; loading from db");
            List<String>dbRules = validationRuleRepository.loadActiveValidatorNamesOrderByPriority();

            if(dbRules!=null && !dbRules.isEmpty()){
                setValidatorRules(dbRules);
                log.info("loaded {} validator rules from db", dbRules.size());
            }else{
                //this.validatorRules = List.of();
                log.info("No validator rules found in db; using empty rule set");
            }

            Map<String, Map<String, String>> validatorRuleParams = validatorRulesParamsRepository
            .loadValidationRuleParams();
            if(validatorRuleParams==null){
                validatorRuleParams = Map.of();
            }

            setValidatorRuleParams(validatorRuleParams);
            log.info("Loaded validator rule config entries: {}", validatorRuleParams.size());
        }catch(Exception e){
            log.error("Failed to load validator rules or params from db; using empty rule set and config");
            setValidatorRules(List.of());
        }
    }

}
