package com.ddoong2.config.auth;


import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
            .csrf().requireCsrfProtectionMatcher(new RequestMatcher() {

                private Pattern allowedMethods = Pattern.compile("^(GET|HEAD|TRACE|OPTIONS)$");
                private RegexRequestMatcher apiMatcher = new RegexRequestMatcher("/api/v[0-9]*/.+", null);

                @Override
                public boolean matches(HttpServletRequest request) {

                    // No CSRF due to allowedMethod
                    if(allowedMethods.matcher(request.getMethod()).matches())
                        return false;

                    // No CSRF due to api call
                    if(apiMatcher.matches(request))
                        return false;

                    // CSRF for everything else that is not an API call or an allowedMethod
                    return true;
                }
            })
                .and()
            .headers().frameOptions().sameOrigin()
                .and()
            .authorizeRequests()
                .antMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**", "/profile").permitAll()
                .anyRequest().anonymous()
                .and()
            .logout()
                .permitAll();
    }
}
