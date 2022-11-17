package com.example.bookchangeservicev1.repository;

import com.example.bookchangeservicev1.dto.Library;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LibraryRepository extends JpaRepository<Library, Integer> {

}
