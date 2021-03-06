package com.dlsw.cn.cms.configuration;

import com.dlsw.cn.cms.configuration.*;
import com.dlsw.cn.common.util.encrypt.AESCryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.cors.CorsUtils;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by zhanWang on 2016/03/17.
 */
@Configuration
@EnableWebSecurity()
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Value("${web.maximumSessions}")
    private int maximumSessions;

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private RESTAuthenticationEntryPoint authenticationEntryPoint;
    @Autowired
    private RESTAuthenticationSuccessHandler authenticationSuccessHandler;
    @Autowired
    private RESTAccessDeniedHandler accessDeniedHandler;
    @Autowired
    private RESTLogoutSuccessHandler logoutSuccessHandler;

    @Bean
    public static ServletListenerRegistrationBean httpSessionEventPublisher() {
        return new ServletListenerRegistrationBean(new HttpSessionEventPublisher());
    }

    @Autowired
    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new AESCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setHideUserNotFoundExceptions(false);
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/js/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin().failureHandler(( request,  response,  exception) ->{
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, exception.getMessage());
        });
        http.logout().invalidateHttpSession(true).logoutSuccessHandler(logoutSuccessHandler);
        http.authorizeRequests()
                .antMatchers("/**/**","/**/*.html","/**/*.js","/*.html","/*.js",
                        "/*.txt","/fonts/**", "/l10n/**", "/**/favicon.ico", "/webjars/springfox-swagger-ui/**", "/swagger-resources/**", "/v2/**",
                        "/api/user/login","/druid/**").permitAll()
                .antMatchers("/api/user/**", "/api/rebate/**").hasAnyRole("USER")
                .anyRequest().authenticated()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .and()
                .headers().frameOptions().disable()
                .and()
                .formLogin();
        http.addFilterAfter(new CSRFHeaderFilter(), CsrfFilter.class);
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .sessionFixation().changeSessionId()
                .maximumSessions(maximumSessions)
                .sessionRegistry(sessionRegistry());
        http.csrf().csrfTokenRepository(csrfTokenRepository()).disable();
        http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint).accessDeniedHandler(accessDeniedHandler);
        http.formLogin().successHandler(authenticationSuccessHandler);
    }

    private CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setHeaderName("X-XSRF-TOKEN");
        return repository;
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        SessionRegistry sessionRegistry = new SessionRegistryImpl();
        return sessionRegistry;
    }

}
