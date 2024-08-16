package com.commit.campus.service.impl;

import com.commit.campus.dto.SignUpUserRequest;
import com.commit.campus.entity.User;
import com.commit.campus.service.OauthService;
import com.commit.campus.service.UserService;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.UUID;

@Service
@Slf4j
public class OauthServiceImpl implements OauthService {

    private final UserService userService;
    private AuthenticationManager authenticationManager;
    private Environment environment;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String CLIENT_SECRET;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String REDIRECT_URI;

    @Autowired
    public OauthServiceImpl(UserService userService, Environment environment) {
        this.userService = userService;
        this.environment = environment;
    }

    // 외부에서 인증매니저를 주입하여 사용 가능
    @Override
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public String getKakaoAccessToken(String code) {
        String access_Token = "";
        String refresh_Token = "";
        String reqURL = "https://kauth.kakao.com/oauth/token";

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // POST 요청을 위해 기본값이 false인 setDoOutput을 true로 설정
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            // 요청에 필요한 값들을 따로 추가하여 같이 보내기
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=").append(CLIENT_ID);
            sb.append("&client_secret=").append(CLIENT_SECRET); // 필요 시 추가
            sb.append("&redirect_uri=").append(REDIRECT_URI);
            sb.append("&code=").append(code);
            String params = sb.toString();
            bw.write(params);
            bw.flush();

            // 결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            log.info("Response Code : " + responseCode);

            if (responseCode == 200) {
                // 요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                String result = "";

                while ((line = br.readLine()) != null) {
                    result += line;
                }
                log.info("Response Body : " + result);

                // JSON 응답 확인
                if (!result.startsWith("{")) {
                    throw new IOException("Invalid JSON response");
                }

                // Gson 라이브러리에 포함된 클래스로 JSON 파싱 객체 생성
                JsonReader jsonReader = new JsonReader(new StringReader(result));
                jsonReader.setLenient(true); // lenient 설정 추가

                JsonParser parser = new JsonParser();
                JsonElement element = parser.parse(jsonReader);

                access_Token = element.getAsJsonObject().get("access_token").getAsString();
                refresh_Token = element.getAsJsonObject().get("refresh_token").getAsString();

                log.info("access_token : " + access_Token);
                log.info("refresh_token : " + refresh_Token);

                br.close();
            } else {
                // 에러 응답 처리
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                String line;
                String errorResult = "";

                while ((line = br.readLine()) != null) {
                    errorResult += line;
                }
                log.info("Error Response Body : " + errorResult);
                br.close();
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return access_Token;
    }

    @Override
    public void createKakaoUser(String token) {

        String reqURL = "https://kapi.kakao.com/v2/user/me";

        //access_token을 이용하여 사용자 정보 조회
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Authorization", "Bearer " + token); //전송할 header 작성, access_token전송

            //결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            log.info("responseCode : " + responseCode);

            //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            log.info("response body : " + result);

            //Gson 라이브러리로 JSON파싱
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            String id = element.getAsJsonObject().get("id").getAsString();
            String nickname = element.getAsJsonObject().get("properties").getAsJsonObject().get("nickname").getAsString();
            boolean hasEmail = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("has_email").getAsBoolean();
            String email = "";
            if (hasEmail) {
                email = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("email").getAsString();
            }

            // 닉네임과 이메일 출력
            log.info("Id : " + id);
            log.info("Nickname : " + nickname);
            log.info("Email : " + email);

            try {
                User user = userService.findUserByEmail(email);
                // 사용자 존재할 경우 로그인 처리
                log.info("이미 존재하는 사용자: " + user.getEmail());
                authenticateUserWithoutPassword(user);
            } catch (UsernameNotFoundException e) {
                // 사용자 존재하지 않으면 회원가입 처리
                log.info("신규회원으로 가입하기: " + email);
                userService.signUpUser(kakaoSignUpRequest(id, email, nickname));
            }

            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private SignUpUserRequest kakaoSignUpRequest(String id, String email, String nickname) {

        String uuid = UUID.randomUUID().toString().substring(0, 6);
        String password = "패스워드"+uuid;

        String name = "kakao" + id;

        SignUpUserRequest kakaoSignUpUserRequest = SignUpUserRequest.builder()
                .email(email)
                .name(name)
                .nickname(nickname)
                .password(password)
                .userAddr(null)
                .birthDay(null)
                .phoneNumber(null)
                .build();

        return kakaoSignUpUserRequest;
    }

    private void authenticateUserWithoutPassword(User user) {
        UserDetails userDetails = userService.loadUserByUsername(user.getEmail());
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        log.info(String.valueOf(authenticationToken));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);


        if (SecurityContextHolder.getContext().getAuthentication() != null &&
                SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            // JWT 토큰 생성
            Claims claims = Jwts.claims().setSubject(userDetails.getUsername());

            String role = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .findFirst()
                    .get();

            String token = Jwts.builder()
                    .setClaims(claims)
                    .claim("role", role)
                    .setExpiration(new Date(System.currentTimeMillis() +
                            Long.parseLong(environment.getProperty("token.expiration_time"))))
                    .signWith(SignatureAlgorithm.HS512, environment.getProperty("token.secret"))
                    .compact();
            log.info("token:" + token);
            HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
            response.addHeader("Authorization", "Bearer " + token);
            response.addHeader("userId", userDetails.getUsername());
        }
    }



}
