package com.hanghae.spring_miniProject.security;


import com.hanghae.spring_miniProject.jwt.JwtAuthenticationFilter;
import com.hanghae.spring_miniProject.jwt.JwtAuthorizationFilter;
import com.hanghae.spring_miniProject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity // 스프링 Security 지원을 가능하게 함
@EnableGlobalMethodSecurity(securedEnabled = true)  //@secured 어노테이션 활성화
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserRepository userRepository;
    @Override
    public void configure (WebSecurity web){
        // h2-console 사용에 대한 허용 (CSRF, FrameOptions 무시)
        web.ignoring().antMatchers("/h2-console/**");
    }

//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//
//        configuration.setAllowedOrigins(Arrays.asList("*"));
//        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT"));
//        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
//        configuration.setAllowCredentials(true);
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }



    @Bean
    public BCryptPasswordEncoder encodePassword() {
        return new BCryptPasswordEncoder();
    }

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//
//        // 회원 관리 처리 API (POST /user/**) 에 대해 CSRF 무시
//        //http.csrf().ignoringAntMatchers("/user/**");
//
//        //POST 요청들이 문제없이 처리된다. csef 무시
//        http.csrf().disable();
//
//        http.authorizeRequests()
//                //회원 관리 처리 API 전부를 login 없이 허용
//                .antMatchers("/user/**").permitAll()
//                // image 폴더를 login 없이 허용
//                .antMatchers("/images/**").permitAll()
//                // css 폴더를 login 없이 허용
//                .antMatchers("/css/**").permitAll()
//                //.antMatchers("/api/**").permitAll()
//                .antMatchers("/api/**").permitAll()
//
//
//                // 어떤 요청이든 '인증'
//                .anyRequest().authenticated()
//                .and()
//                .cors()
//                .and()
//                // 로그인 기능 허용
//                .formLogin()
//
//                //.loginPage("/user/login")   //로그인 할 때 longin.html 페이지로
//
//                .loginProcessingUrl("/user/login")  //로그인 처리(보안검색대 가고 그런 과정들)
//                .defaultSuccessUrl("http://localhost:3000")     //로그인이 성공할 시 해당 url로 이동
//                .failureUrl("http://localhost:3000/login")    //실패했을 때 url
//                .permitAll()
//                .and()
//                .logout()
//                .logoutUrl("/user/logout")
//                .permitAll()
//                .and()
//                .exceptionHandling();


        /*
         *         loginPage ()  – 사용자 정의 로그인 페이지
         *         loginProcessingUrl () – 사용자 이름과 암호를 제출할 URL     //원래는 /login인데 재정의 할 수 있다.
         *         defaultSuccessUrl () – 성공적인 로그인 후 랜딩 페이지
         *         failureUrl () – 로그인 실패 후 방문 페이지
         *         logoutUrl () – 사용자 정의 로그 아웃
         *         logoutSuccessUrl () – 로그아웃 성공 시 페이지 이동
         **/

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager());
        jwtAuthenticationFilter.setFilterProcessesUrl("/user/login");

        http.csrf().disable();
        http.headers().frameOptions().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // 시큐리티 폼로그인기능 비활성화
                .formLogin().disable()
                // 로그인폼 화면으로 리다이렉트 비활성화
                .httpBasic().disable()
                // UsernamePasswordAuthenticationFilter 단계에서 json로그인과 jwt토큰을 만들어 response 반환
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) // AuthenticationManager

                // BasicAuthenticationFilter 단계에서 jwt토큰 검증
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), userRepository))

                .authorizeRequests()

                // PreFlight 요청 모두 허가
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()

                // 게시글 인증
                //.antMatchers(HttpMethod.DELETE, "/api/memos/**").authenticated()
                //.antMatchers(HttpMethod.POST, "/api/memos/**").authenticated()
                //.antMatchers(HttpMethod.PUT, "/api/memos/**").authenticated()

                // 댓글 인증    얘네를 하면 로그인을 하지 않고 했을 때 예외처리가 아닌 403 error가 뜬다.
                //.antMatchers(HttpMethod.DELETE, "/api/comment/**").authenticated()
                //.antMatchers(HttpMethod.POST, "/api/comment/**").authenticated()
                //.antMatchers(HttpMethod.PUT, "/api/comment/**").authenticated()

                // 그 외 요청 모두 허가
                .anyRequest().permitAll()
                .and().cors();
    }
}
