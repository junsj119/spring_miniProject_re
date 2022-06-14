package com.hanghae.spring_miniProject.service;

import com.hanghae.spring_miniProject.dto.SignupRequestDto;
import com.hanghae.spring_miniProject.model.User;
import com.hanghae.spring_miniProject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    // 복/암호화
    private final BCryptPasswordEncoder encoder;
    private final PasswordEncoder passwordEncoder;
    //회원가입

    @Transactional
    public void registerUser(SignupRequestDto requestDto){
        // 회원 ID 중복 확인
        String username = requestDto.getUsername();
        Optional<User> found = userRepository.findByUsername(username);
        //- 데이터베이스에 존재하는 닉네임을 입력한 채 회원가입 버튼을 누른 경우 "중복된 닉네임입니다." 라는 에러메세지
        if (found.isPresent())
            throw new IllegalArgumentException("중복된 닉네임입니다.");

        String nickname = requestDto.getNickname();


        //- 닉네임은 `최소 3자 이상, 알파벳 대소문자(a~z, A~Z), 숫자(0~9)`로 구성하기
        String pattern = "^[a-zA-Z0-9]*$";
        if(requestDto.getUsername().length() < 3 && Pattern.matches(pattern, requestDto.getUsername()))
            throw new IllegalArgumentException("닉네임은 3자리 이상 입력해주세요.");

        //패스워드
        String password = passwordEncoder.encode(requestDto.getPassword());

        //- 비밀번호는 `최소 4자 이상이며, 닉네임과 같은 값이 포함된 경우 회원가입에 실패`로 만들기
        if(requestDto.getPassword().length() < 4 || requestDto.getPassword().contains(username))
            throw new IllegalArgumentException("비밀번호 4자리 이상, 혹은 닉네임과 같은 값을 사용할 수 없습니다.");

        //데이터 저장
        User user = new User(username, nickname, password);
        userRepository.save(user);
    }
}
