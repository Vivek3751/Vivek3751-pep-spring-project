package com.example.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

import com.example.entity.Account;
import com.example.entity.Message;

public interface MessageRepository extends JpaRepository<Message, Integer>{

        //@Query("DELETE FROM Message m WHERE m.id = ?1")
        //int deleteMsgById(Integer id);

        @Query("SELECT m FROM Message m WHERE m.posted_by = ?1")
        Optional<List<Message>> getPostedBy(Integer posted_by);

        //boolean existsById(Integer posted_by);


}
