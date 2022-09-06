package com.caubogeo.bogeo;

import com.caubogeo.bogeo.domain.auth.MemberAuth;
import com.caubogeo.bogeo.domain.member.Gender;
import com.caubogeo.bogeo.dto.member.MemberRequestDto;
import com.caubogeo.bogeo.dto.member.MemberResponseDto;
import com.caubogeo.bogeo.repository.AuthorityRepository;
import com.caubogeo.bogeo.repository.MemberRepository;
import com.caubogeo.bogeo.repository.RefreshTokenRepository;
import com.caubogeo.bogeo.service.auth.AuthService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthTest {
    @LocalServerPort
    private int port;

    @Autowired
    AuthService authService;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @BeforeEach
    public void beforeEach() {
        MemberRequestDto dto = new MemberRequestDto();
        dto.setId("asdf1234");
        dto.setPassword("asdf1234");
        dto.setAge(12);
        dto.setGender(Gender.WOMAN);

    }

    @After
    public void tearDown() throws Exception {
        authorityRepository.deleteAll();
        memberRepository.deleteAll();
        refreshTokenRepository.deleteAll();
    }

    @Test
    public void 회원가입() throws Exception {
        String id = "ssol0319";
        String password = "asdf1234";
        MemberRequestDto requestDto = MemberRequestDto.builder()
                .id(id)
                .password(password)
                .build();

        String url = "http://localhost:" + port + "/auth/signup";

        //when
        ResponseEntity<MemberResponseDto> responseEntity = testRestTemplate.postForEntity(url, requestDto, MemberResponseDto.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().getId()).isEqualTo("ssol0319");
    }

}
