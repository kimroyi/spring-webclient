package com.royi.api.service;

import com.royi.api.domain.User;
import com.royi.api.dto.RequestCriteriaDto;
import com.royi.api.dto.RequestUserDto;
import com.royi.api.dto.ResponsePaginationDto;
import com.royi.api.dto.ResponseUserDto;
import com.royi.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public ResponseUserDto getUsers() {
        List<User> foundUsers = userRepository.findAll();
        ResponseUserDto responseUserDto = new ResponseUserDto();
        responseUserDto.setOk(true);
        responseUserDto.setUsers(foundUsers);

        ResponsePaginationDto paginationDto = ResponsePaginationDto.builder()
                .resultCount(foundUsers.size())
                .build();
        responseUserDto.setResponsePagination(paginationDto);

        return responseUserDto;
    }

    // POST 요청으로 전달된 User 리스트에 기반한 검색 로직
    public ResponseUserDto findUsersByUsername(RequestUserDto requestUserDto) {
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

        ResponsePaginationDto paginationDto = ResponsePaginationDto.builder()
                .resultCount(foundUsers.size())
                .build();
        responseUserDto.setResponsePagination(paginationDto);

        return responseUserDto; // ResponseUserDto 반환
    }

    public ResponseUserDto findUsersByUserNameAndCriteria(RequestUserDto requestUserDto) {
        List<User> requestedUsers = requestUserDto.getUsers(); // 요청된 User 리스트
        List<String> usernames = requestedUsers.stream()
                .map(User::getUsername)
                .collect(Collectors.toList()); // 각 User의 username 추출

        // Parse date range from RequestCriteria
        RequestCriteriaDto criteria = requestUserDto.getCriteria();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        LocalDateTime fromDate = LocalDateTime.parse(criteria.getFromDate(), formatter);
        LocalDateTime toDate = LocalDateTime.parse(criteria.getToDate(), formatter);

        // 데이터베이스에서 요청된 username 리스트와 일치하고 날짜 범위에 포함되는 User들을 검색
        List<User> foundUsers = userRepository.findByUsernameInAndUpdateDateBetween(usernames, fromDate, toDate);

        // 검색 결과를 ResponseUserDto로 반환
        ResponseUserDto responseUserDto = new ResponseUserDto();
        responseUserDto.setOk(!foundUsers.isEmpty()); // 검색 결과가 비어 있지 않다면 true
        responseUserDto.setUsers(foundUsers); // 검색된 User 리스트 설정

        ResponsePaginationDto paginationDto = ResponsePaginationDto.builder()
                .startIndex(requestUserDto.getPagination().getStartIndex())
                .startIndex(requestUserDto.getPagination().getStartIndex())
                .order(requestUserDto.getPagination().getOrder())
                .resultCount(foundUsers.size())
                .build();
        responseUserDto.setResponsePagination(paginationDto);

        return responseUserDto; // ResponseUserDto 반환
    }
}