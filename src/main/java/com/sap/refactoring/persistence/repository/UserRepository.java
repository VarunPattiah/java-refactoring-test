package com.sap.refactoring.persistence.repository;

import com.sap.refactoring.persistence.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserInfo, Integer> {
    @Query("SELECT u FROM UserInfo u WHERE u.email =:email")
    List<UserInfo> findByEmail(@Param("email") String email);
}
