package com.example.mtgdecks.repository;

import com.example.mtgdecks.model.Decks;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DecksRepository extends MongoRepository<Decks, String>{
    List<Decks> findDecksByCardsContaining(String cardname);
    List<Decks> findDecksByColors(String colors);
    List<Decks> findDecksByName(String name);
    List<Decks> findDecksByAuthor(String author);
    Decks findDecksByAuthorAndAndName(String author, String name);
}
