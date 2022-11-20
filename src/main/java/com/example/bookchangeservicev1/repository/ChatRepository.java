package com.example.bookchangeservicev1.repository;

import com.example.bookchangeservicev1.models.Chat;
import com.example.bookchangeservicev1.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findAllByPeople(Person person);
    Optional<Chat> findByChatCode(String chatCode);
}
