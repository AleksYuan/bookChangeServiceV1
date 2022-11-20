package com.example.bookchangeservicev1.repository;

import com.example.bookchangeservicev1.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {
    Optional<Person> findByLogin(String login);
    Optional<Person> findByPost(String post);

    Optional<Person> getPersonByLogin(String login);


}
