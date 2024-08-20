package com.royi.api.repository;

import com.royi.api.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface UserRepository extends JpaRepository<User, String> {

    // 주어진 username 리스트에 해당하는 User들을 검색
    List<User> findByUsernameIn(List<String> usernames);

    @Query("SELECT u FROM User u WHERE u.username IN :usernames AND u.updateDate BETWEEN :fromDate AND :toDate")
    List<User> findByUsernameInAndUpdateDateBetween(
            @Param("usernames") List<String> usernames,
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate
    );

    @Query(value = "SELECT " +
            "TO_CHAR(UPDATE_DATE, 'YYYY-MM-DD') AS updateDate, " +
            "COUNT(UPDATE_DATE) AS count " +
            "FROM USERS " +
            "WHERE USERNAME IN (:usernames) " +
            "AND UPDATE_DATE BETWEEN :fromDate AND :toDate " +
            "GROUP BY TO_CHAR(UPDATE_DATE, 'YYYY-MM-DD') " +
            "UNION ALL " +
            "SELECT NULL AS updateDate, COUNT(*) AS count " +
            "FROM USERS " +
            "WHERE USERNAME IN (:usernames) " +
            "AND UPDATE_DATE BETWEEN :fromDate AND :toDate",
            nativeQuery = true)
    List<Object[]> findUserCountsWithTotal(
            @Param("usernames") List<String> usernames,
            @Param("fromDate") String fromDate,
            @Param("toDate") String toDate
    );
}