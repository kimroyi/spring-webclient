package com.royi.api.repository;

import com.royi.api.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, String> {

    // 주어진 username 리스트에 해당하는 User들을 검색
    List<User> findByUsernameIn(List<String> usernames);
}