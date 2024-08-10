package co.fullstacklabs.battlemonsters.challenge.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import co.fullstacklabs.battlemonsters.challenge.ApplicationConfig;
import co.fullstacklabs.battlemonsters.challenge.dto.MonsterDTO;
import org.springframework.test.web.servlet.MvcResult;


/**
 * @author FullStack Labs
 * @version 1.0
 * @since 2022-10
 */
@SpringBootTest
@AutoConfigureMockMvc
@Import(ApplicationConfig.class)
public class MonsterControllerTest {
    private static final String MONSTER_PATH = "/monster";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @Test    
    void shouldFetchAllMonsters() throws Exception {
        this.mockMvc.perform(get(MONSTER_PATH)).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", Is.is(1)))
                .andExpect(jsonPath("$[0].name", Is.is("Dead Unicorn")))
                .andExpect(jsonPath("$[0].attack", Is.is(50)))
                .andExpect(jsonPath("$[0].defense", Is.is(40)))
                .andExpect(jsonPath("$[0].hp", Is.is(30)))
                .andExpect(jsonPath("$[0].speed", Is.is(25)));
    }

    @Test
    void shouldGetMonsterSuccessfullyReturning200StatusCode() throws Exception {
        long id = 2l;
        this.mockMvc.perform(get(MONSTER_PATH + "/{id}", id)).andExpect(status().isOk())
                .andExpect(jsonPath("$.name", Is.is("Old Shark")));
    }

    @Test
    void shouldGetMonsterNotExistsReturning404StatusCode() throws Exception {
        long id = 3000l;
        this.mockMvc.perform(get(MONSTER_PATH + "/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenUpdateMonster_thenMonsterIsUpdated() throws Exception {
        MonsterDTO inputMonsterDto = MonsterDTO.builder().id(5).name("Monster Updated")
                .attack(50).defense(30).hp(30).speed(22)
                .imageUrl("ImageURL1").build();

        ObjectMapper objectMapper = new ObjectMapper();
        String monsterJson = objectMapper.writeValueAsString(inputMonsterDto);

        this.mockMvc.perform(put(MONSTER_PATH)
                        .contentType("application/json")
                        .content(monsterJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(inputMonsterDto.getName()));
    }

    @Test
    public void whenUnprocessableEntityException_thenResponseStatusIsUnprocessableEntity() throws Exception {
        mockMvc.perform(get(MONSTER_PATH +"/throwUnprocessableEntity"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message").value("Unprocessable entity"));
    }

    @Test
    public void whenConstraintViolationException_thenResponseStatusIsBadRequest() throws Exception {
        mockMvc.perform(get(MONSTER_PATH + "/throwConstraintViolation"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.violations").exists());
    }
    
    

}
