package com.hulkhiretech.payments.repository.interfaces;

import java.util.Map;

public interface ValidatorRulesParamsRepository {

    Map<String, Map<String, String>> loadValidationRuleParams();
}