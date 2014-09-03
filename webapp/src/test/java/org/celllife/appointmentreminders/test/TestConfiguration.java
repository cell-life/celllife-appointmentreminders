package org.celllife.appointmentreminders.test;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;

@ContextConfiguration({
        "classpath:/META-INF/spring/spring-aop.xml",
        "classpath:/META-INF/spring/spring-application.xml",
        "classpath:/META-INF/spring/spring-cache.xml",
        "classpath:/META-INF/spring/spring-config-test.xml",
        "classpath:/META-INF/spring/spring-domain.xml",
        "classpath:/META-INF/spring/spring-dozer.xml",
        "classpath:/META-INF/spring/spring-smooks.xml",
        "classpath:/META-INF/spring/spring-jdbc-test.xml",
        "classpath:/META-INF/spring/spring-json.xml",
        "classpath:/META-INF/spring/spring-orm.xml",
        "classpath:/META-INF/spring/spring-task.xml",
        "classpath:/META-INF/spring/spring-tx.xml",
        "classpath:/META-INF/quartz/quartz-config.xml",
        "classpath:/META-INF/spring-data/spring-data-jpa.xml",
        "classpath:/META-INF/spring-integration/spring-integration-communicate.xml"
})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class TestConfiguration {
}
