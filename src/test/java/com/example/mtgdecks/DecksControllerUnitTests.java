package com.example.mtgdecks;

import com.example.mtgdecks.model.Decks;
import com.example.mtgdecks.repository.DecksRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class DecksControllerUnitTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DecksRepository decksRepository;

    private ObjectMapper mapper = new ObjectMapper();

    private String[] cardlist = {
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

    @Test
    public void givenDecks_whenGetDecksByName_thenReturnJsonDecks() throws Exception{
        Decks deck1 = new Decks("Goblins", "Brent", cardlist, "R");
        Decks deck2 = new Decks("Goblins", "Raphael", cardlist, "UW");

        List<Decks> decksList = new ArrayList<>();
        decksList.add(deck1);
        decksList.add(deck2);

        given(decksRepository.findDecksByName("Goblins")).willReturn(decksList);

        mockMvc.perform(get("/decks/{name}", "Goblins"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].author", is("Brent")))
                .andExpect(jsonPath("$[0].name", is("Goblins")))
                .andExpect(jsonPath("$[0].colors", is("R")))
                .andExpect(jsonPath("$[1].author", is("Raphael")))
                .andExpect(jsonPath("$[1].name", is("Goblins")))
                .andExpect(jsonPath("$[1].colors", is("UW")));
    }

    @Test
    public void whenPostDecks_thenReturnJsonDecks() throws Exception {
        Decks deck4 = new Decks("Control", "Victor", cardlist, "UB");

        mockMvc.perform(post("/decks")
                .content(mapper.writeValueAsString(deck4))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Control")))
                .andExpect(jsonPath("$.author", is("Victor")))
                .andExpect(jsonPath("$.colors", is("UB")));
    }

    @Test
    public void givenDecks_whenPutDecks_thenReturnJsonDecks() throws Exception{
        Decks deck1 = new Decks("Goblins", "Brent", cardlist, "R");

        given(decksRepository.findDecksByAuthorAndAndName("Brent", "Goblins")).willReturn(deck1);

        Decks updatedDeck = new Decks("Goblins", "Brent", cardlist, "UB");

        mockMvc.perform(put("/decks")
                .content(mapper.writeValueAsString(updatedDeck))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Goblins")))
                .andExpect(jsonPath("$.author", is("Brent")))
                .andExpect(jsonPath("$.colors", is("UB")));
    }

    @Test
    public void givenDecks_whenDeleteDecks_thenStatusOK() throws Exception {
        Decks decktobedeleted = new Decks("Hammertime", "Hans", cardlist, "UW");

        given(decksRepository.findDecksByAuthorAndAndName("Hans", "Hammertime")).willReturn(decktobedeleted);

        mockMvc.perform(delete("/decks/author/{author}/{name}", "Hans", "Hammertime")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void givenNoDecks_whenDeleteDecks_thenStatusNotFound() throws Exception {
        given(decksRepository.findDecksByAuthorAndAndName("Michiel", "Aggro")).willReturn(null);

        mockMvc.perform(delete("/decks/author/{author}/{name}", "Michiel", "Aggro")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
