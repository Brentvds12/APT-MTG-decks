package com.example.mtgdecks.controller;

import com.example.mtgdecks.model.Decks;
import com.example.mtgdecks.repository.DecksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.websocket.server.PathParam;
import java.util.List;

public class DecksController {

    @Autowired
    private DecksRepository decksRepository;

    @GetMapping("/decks/cards/{cardName}")
    public List<Decks> getDecksByCardName(@PathVariable String cardName){
        return decksRepository.findDecksByCardsContaining(cardName);
    }

    @GetMapping("/decks/colors/{colors}")
    public List<Decks> getDecksByColor(@PathVariable String colors){
        return decksRepository.findDecksByColors(colors);
    }

    @GetMapping("/decks/authors/{author}")
    public List<Decks> getDecksByAuthor(@PathVariable String author){
        return decksRepository.findDecksByAuthor(author);
    }

    @GetMapping("/decks/{name}")
    public List<Decks> getDecksByName(@PathVariable String name){
        return decksRepository.findDecksByName(name);
    }

    @GetMapping("/decks/authors/{author}/{name}")
    public Decks getDecksByAuthorAndName(@PathVariable String author, @PathVariable String name){
        return decksRepository.findDecksByAuthorAndAndName(author, name);
    }

    @PostMapping("/decks")
    public Decks addDeck(@RequestBody Decks decks){
        decksRepository.save(decks);
        return decks;
    }

    @PutMapping("/decks")
    public Decks updateDeck(@RequestBody Decks updatedDeck){
        Decks retrievedDeck = decksRepository.findDecksByAuthorAndAndName(
                updatedDeck.getAuthor(), updatedDeck.getName());

        retrievedDeck.setName(updatedDeck.getName());
        retrievedDeck.setCards(updatedDeck.getCards());

        decksRepository.save(retrievedDeck);

        return retrievedDeck;
    }

    @DeleteMapping("/decks/author/{author}/{name}")
    public ResponseEntity deleteDeck(@PathVariable String author, @PathVariable String name){
        Decks decks = decksRepository.findDecksByAuthorAndAndName(author, name);
        if(decks!=null){
            decksRepository.delete(decks);
            return ResponseEntity.ok().build();
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }

    @PostConstruct
    public void fillDB(){
        if(decksRepository.count()==0){
            decksRepository.save(new Decks("Brent", "Goblins"));
            decksRepository.save(new Decks("Raphael", "Goblins"));
            decksRepository.save(new Decks("Raphael", "Control"));
        }

        System.out.println("Decks test: " + decksRepository.findDecksByName("Goblins").size());
    }
}
