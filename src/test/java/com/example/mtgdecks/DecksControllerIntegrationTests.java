package com.example.mtgdecks;

import com.example.mtgdecks.model.Decks;
import com.example.mtgdecks.repository.DecksRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class DecksControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DecksRepository decksRepository;

    private String[] Decklist = {
            "Ornithopter","Ornithopter","Ornithopter","Ornithopter",
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
            "Thieving Skydiver","Thieving Skydiver"
    };

    private Decks deck1 = new Decks("Goblins", "Brent", Decklist, "R");
    private Decks deck2 = new Decks("Goblins", "Raphael", Decklist, "UW");
    private Decks deck3 = new Decks("Hammertime", "Raphael", Decklist, "UW");
    private Decks decktobedeleted = new Decks("Hammertime", "Hans", Decklist, "UW");

    @BeforeEach
    public void beforeAllTests() {
        decksRepository.deleteAll();
        decksRepository.save(deck1);
        decksRepository.save(deck2);
        decksRepository.save(deck3);
        decksRepository.save(decktobedeleted);
    }

    @AfterEach
    public void afterAllTests(){
        decksRepository.deleteAll();
    }

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void givenDecks_whenGetDecksByNameAndAuthor_thenReturnJsonDecks() throws Exception {

        mockMvc.perform(get("/decks/authors/{author}/{name}", "Brent", "Goblins"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Goblins")))
                .andExpect(jsonPath("$.author", is("Brent")))
                .andExpect(jsonPath("$.colors", is("R")))
                .andExpect(jsonPath("$.cards", is(Decklist)));
    }
}
