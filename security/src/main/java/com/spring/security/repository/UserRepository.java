package com.spring.security.repository;

import com.spring.security.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * JPA Repository는 두 개의 인자를 받는다.
 * 첫번째는 Repository가 해당하는 Entity를 넣어준다.
 * 두번째는 Entity의 id값의 타입을 레퍼런스 타입으로 넣어준다.
 */
public interface UserRepository extends JpaRepository<User, Integer> {

    Boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
}
