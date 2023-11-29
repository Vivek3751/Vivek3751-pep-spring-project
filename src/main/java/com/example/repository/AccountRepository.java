package com.example.repository;
import com.example.entity.Account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface AccountRepository extends JpaRepository<Account, Integer>{

    //@Modifying
    //@Transactional
    /*@Query("INSERT INTO Account(username,password) vaules (?,?)")
    void newUserAccountRegistration(@Param("username") String username, @Param("password") String password);
    @Query("SELECT username FROM Account WHERE username=?")
    public String getUserNameString(Account account); */
    @Query("FROM Account WHERE username=:username")
    public Account getAccountByUsername(@Param("username") String username);
    
}

