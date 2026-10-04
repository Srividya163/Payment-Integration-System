package com.hulkhiretech.payments.repository.interfaces;

import java.util.List;

public interface ValidationRuleRepository {

    List<String> loadActiveValidatorNamesOrderByPriority();
}