
package com.hdjunction.homework.api.config;

import com.hdjunction.homework.core.db.config.CoreDbConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({CoreDbConfig.class})
public class ApiDbConfig {
}
