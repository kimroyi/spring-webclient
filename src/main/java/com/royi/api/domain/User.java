package com.royi.api.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@Table(name = "users")
public class User {

    @Id
    private String userId;

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;

    @NotEmpty
    private LocalDateTime updateDate;
}
