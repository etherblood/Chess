package com.etherblood.chess.server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *
 * @author Philipp
 */
@EnableScheduling
public class Application {
    
private static final String SPRING_CONTEXT_FILEPATH = "main_spring_context.xml";

    public static void main(String[] args) {
        try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(SPRING_CONTEXT_FILEPATH)) {
            context.registerShutdownHook();
            DefaultAuthentication adminAuthentication = new DefaultAuthentication(DefaultAuthentication.LOCAL_ADMIN_PRINCIPAL, null, UserRoles.ADMIN);
            SecurityContextHolder.getContext().setAuthentication(adminAuthentication);
            runAdminThread(context);
            SecurityContextHolder.clearContext();
}
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
//    @Bean
//    public static CsrfTokenRepository csrfTokenRepository() {
//        return new CookieCsrfTokenRepository();
//    }
}
