package com.example.mtgdecks.controller;

import com.example.mtgdecks.model.Decks;
import com.example.mtgdecks.repository.DecksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.websocket.server.PathParam;
import java.util.List;

@RestController
public class DecksController {

    @Autowired
    private DecksRepository decksRepository;

    private String[] Decklist =
            {"Ornithopter","Ornithopter","Ornithopter","Ornithopter",
                    "Burrenton Forge-Tender","Burrenton Forge-Tender","Burrenton Forge-Tender",
                    "Esper Sentinel","Esper Sentinel","Esper Sentinel",
                    "Puresteel Paladin","Puresteel Paladin","Puresteel Paladin","Puresteel Paladin",
                    "Sanctifier en-Vec","Sanctifier en-Vec",
                    "Stoneforge Mystic","Stoneforge Mystic","Stoneforge Mystic","Stoneforge Mystic",
                    "Steelshaper's Gift",
                    "Colossus Hammer","Colossus Hammer","Colossus Hammer","Colossus Hammer",
                    "Shadowspear",
                    "Springleaf Drum","Springleaf Drum",
                    "Cranial Plating",
                    "Nettlecyst",
                    "Sword of Fire and Ice",
                    "Kaldra Compleat",
                    "Sigarda's Aid","Sigarda's Aid","Sigarda's Aid","Sigarda's Aid",
                    "Flooded Strand","Flooded Strand",
                    "Hallowed Fountain","Hallowed Fountain",
                    "Inkmoth Nexus","Inkmoth Nexus","Inkmoth Nexus",
                    "Seachrome Coast","Seachrome Coast","Seachrome Coast","Seachrome Coast",
                    "Snow-Covered Plains","Snow-Covered Plains","Snow-Covered Plains","Snow-Covered Plains",
                    "Urza's Saga","Urza's Saga","Urza's Saga","Urza's Saga",
                    "Windswept Heath","Windswept Heath","Windswept Heath","Windswept Heath",
                    "Blacksmith's Skill","Blacksmith's Skill",
                    "Burrenton Forge-Tender",
                    "Pithing Needle",
                    "Cathar Commando","Cathar Commando",
                    "Mana Leak","Mana Leak","Mana Leak","Mana Leak",
                    "Manriki-Gusari",
                    "Sanctifier en-Vec","Sanctifier en-Vec",
                    "Thieving Skydiver","Thieving Skydiver"};

    @PostConstruct
    public void fillDB(){
        System.out.println("werkt dit?");
        if(decksRepository.count()==0){
            decksRepository.save(new Decks("Goblins", "Brent", Decklist, "UW"));
            decksRepository.save(new Decks("Goblins", "Raphael", Decklist, "R"));
            decksRepository.save(new Decks("Hammertime", "Raphael", Decklist, "BW"));
        }

        System.out.println("Decks test: " + decksRepository.findAll().size());
    }

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

    @GetMapping("/decks")
    public List<Decks> getDecks(){ return decksRepository.findAll(); }

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
}
