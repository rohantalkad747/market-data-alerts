package com.h2o_execution.static_data;

import com.h2o_execution.domain.Security;
import com.h2o_execution.domain.EnhancedSecurity;

public interface IEnhancedSecurityService
{
    EnhancedSecurity getEnhancedSnapshot(Security security);
}
