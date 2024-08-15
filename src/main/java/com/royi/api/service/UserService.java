package com.royi.api.service;

import com.royi.api.domain.User;
import com.royi.api.dto.RequestUserDto;
import com.royi.api.dto.ResponseUserDto;
import com.royi.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public ResponseUserDto getUsers() {
        ResponseUserDto responseUserDto = new ResponseUserDto();
        responseUserDto.setOk(true);
        responseUserDto.setUsers(userRepository.findAll());
        return responseUserDto;
    }

    // POST 요청으로 전달된 User 리스트에 기반한 검색 로직
    public ResponseUserDto findUsers(RequestUserDto requestUserDto) {
        List<User> requestedUsers = requestUserDto.getUsers(); // 요청된 User 리스트
        List<String> usernames = requestedUsers.stream()
                .map(User::getUsername)
                .collect(Collectors.toList()); // 각 User의 username 추출

        // 데이터베이스에서 요청된 username 리스트와 일치하는 User들을 검색
        List<User> foundUsers = userRepository.findByUsernameIn(usernames);

        // 검색 결과를 ResponseUserDto로 반환
        ResponseUserDto responseUserDto = new ResponseUserDto();
        responseUserDto.setOk(!foundUsers.isEmpty()); // 검색 결과가 비어 있지 않다면 true
        responseUserDto.setUsers(foundUsers); // 검색된 User 리스트 설정

        return responseUserDto; // ResponseUserDto 반환
    }
}