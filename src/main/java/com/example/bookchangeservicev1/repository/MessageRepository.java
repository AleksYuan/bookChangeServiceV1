package com.example.bookchangeservicev1.repository;

import com.example.bookchangeservicev1.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllByChat_Id(Long id);
    void deleteAllByChat_Id(Long id);
}
