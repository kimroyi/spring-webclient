package com.royi.api.service;

import com.royi.api.domain.User;
import com.royi.api.dto.RequestCriteria;
import com.royi.api.dto.RequestUser;
import com.royi.api.dto.ResponsePagination;
import com.royi.api.dto.ResponseUser;
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

    public ResponseUser getUsers() {
        List<User> foundUsers = userRepository.findAll();
        ResponseUser responseUser = new ResponseUser();
        responseUser.setOk(true);
        responseUser.setUsers(foundUsers);

        ResponsePagination paginationDto = ResponsePagination.builder()
                .resultCount(foundUsers.size())
                .build();
        responseUser.setResponsePagination(paginationDto);

        return responseUser;
    }

    // POST 요청으로 전달된 User 리스트에 기반한 검색 로직
    public ResponseUser findUsersByUsername(RequestUser requestUser) {
        List<User> requestedUsers = requestUser.getUsers(); // 요청된 User 리스트
        List<String> usernames = requestedUsers.stream()
                .map(User::getUsername)
                .collect(Collectors.toList()); // 각 User의 username 추출

        // 데이터베이스에서 요청된 username 리스트와 일치하는 User들을 검색
        List<User> foundUsers = userRepository.findByUsernameIn(usernames);

        // 검색 결과를 ResponseUserDto로 반환
        ResponseUser responseUser = new ResponseUser();
        responseUser.setOk(!foundUsers.isEmpty()); // 검색 결과가 비어 있지 않다면 true
        responseUser.setUsers(foundUsers); // 검색된 User 리스트 설정

        ResponsePagination paginationDto = ResponsePagination.builder()
                .resultCount(foundUsers.size())
                .build();
        responseUser.setResponsePagination(paginationDto);

        return responseUser; // ResponseUserDto 반환
    }

    public ResponseUser findUsersByUserNameAndCriteria(RequestUser requestUser) {
        List<User> requestedUsers = requestUser.getUsers(); // 요청된 User 리스트
        List<String> usernames = requestedUsers.stream()
                .map(User::getUsername)
                .collect(Collectors.toList()); // 각 User의 username 추출

        // Parse date range from RequestCriteria
        RequestCriteria criteria = requestUser.getCriteria();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        LocalDateTime fromDate = LocalDateTime.parse(criteria.getFromDate(), formatter);
        LocalDateTime toDate = LocalDateTime.parse(criteria.getToDate(), formatter);

        // 데이터베이스에서 요청된 username 리스트와 일치하고 날짜 범위에 포함되는 User들을 검색
        List<User> foundUsers = userRepository.findByUsernameInAndUpdateDateBetween(usernames, fromDate, toDate);

        // 검색 결과를 ResponseUserDto로 반환
        ResponseUser responseUser = new ResponseUser();
        responseUser.setOk(!foundUsers.isEmpty()); // 검색 결과가 비어 있지 않다면 true
        responseUser.setUsers(foundUsers); // 검색된 User 리스트 설정

        ResponsePagination paginationDto = ResponsePagination.builder()
                .startIndex(requestUser.getPagination().getStartIndex())
                .startIndex(requestUser.getPagination().getStartIndex())
                .order(requestUser.getPagination().getOrder())
                .resultCount(foundUsers.size())
                .build();
        responseUser.setResponsePagination(paginationDto);

        return responseUser; // ResponseUserDto 반환
    }
}