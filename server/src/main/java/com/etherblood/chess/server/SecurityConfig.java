package com.etherblood.chess.server;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

/**
 *
 * @author Philipp
 */
@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/resources/**").permitAll() 
//                .anyRequest().authenticated()
                .and()
            .formLogin()
                .loginPage("/index.html")
//                .loginProcessingUrl("/auth/login/guest")
                .permitAll()
                .and()
            .logout()                                    
                .permitAll()
                .and()
            .csrf()
                .disable();
        
//        http.formLogin().loginProcessingUrl("/auth/login/guest").loginPage("/index.html").defaultSuccessUrl("/sandbox.html")
//                .and().csrf().disable();
        
//        https://bitbucket.org/poear/custom-login-page/src/96a7259a10c84d75abd42b0636543f8528150452/src/main/java/com/example/CustomLoginViewApplication.java?at=master&fileviewer=file-view-default
        
//        https://docs.spring.io/spring-security/site/docs/4.2.2.RELEASE/guides/html5/form-javaconfig.html
//        http
//        .authorizeRequests()
//            .anyRequest().authenticated() 
//            .and()
//        .formLogin()                      
//            .and()
//        .httpBasic(); 
    }

}
