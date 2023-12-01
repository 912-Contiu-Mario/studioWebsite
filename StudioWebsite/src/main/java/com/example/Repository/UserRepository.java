package com.example.Repository;

import com.example.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User,String> {

    @Query("SELECT u FROM User u WHERE u.username = :username")
    User findUserByUsername(@Param("username") String username);
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO USER VALUES " + "( " + ":#{#user.username} +:#{#user.password}+:#{#user.role}" + ")", nativeQuery = true)
    public abstract void insert(@Param("user") User user);
}