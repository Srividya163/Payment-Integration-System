package com.hulkhiretech.payments.constant;

import java.util.Optional;

import com.hulkhiretech.payments.service.impl.businessValidator.ValidatorRule1;
import com.hulkhiretech.payments.service.impl.businessValidator.ValidatorRule3;
import com.hulkhiretech.payments.service.impl.businessValidator.validatorRule2;
import com.hulkhiretech.payments.service.interfaces.BusinessValidator;

public enum ValidatorRuleNameEnum {

    VALIDATOR_RULE1("VALIDATOR_RULE1", ValidatorRule1.class),
    VALIDATOR_RULE2("VALIDATOR_RULE2", validatorRule2.class),
    VALIDATOR_RULE3("VALIDATOR_RULE3", ValidatorRule3.class);

    private final String ruleName;
    private final Class<? extends BusinessValidator> validatorClass;

    ValidatorRuleNameEnum(String ruleName, Class<? extends BusinessValidator> validatorClass) {
        this.ruleName = ruleName;
        this.validatorClass = validatorClass;
    }

    public String getRuleName() {
        return ruleName;
    }

    public Class<? extends BusinessValidator> getValidatorClass() {
        return validatorClass;
    }

    /**
     * Get the validator class by rule name
     * @param ruleName the rule name to search for
     * @return the corresponding validator class
     * @throws IllegalArgumentException if the rule name is not found
     */
    public static Optional<Class<? extends BusinessValidator>> getValidatorClassByRule(String ruleName) {
        if (ruleName == null || ruleName.isEmpty()) {
            throw new IllegalArgumentException("Rule name must not be null or empty");
        }
        
        for (ValidatorRuleNameEnum rule : ValidatorRuleNameEnum.values()) {
            if (rule.getRuleName().equalsIgnoreCase(ruleName)) {
                return Optional.of(rule.getValidatorClass());
            }
        }
        
        return Optional.empty();
    }
}
