package co.fullstacklabs.battlemonsters.challenge.controller;

import co.fullstacklabs.battlemonsters.challenge.ApplicationConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import co.fullstacklabs.battlemonsters.challenge.dto.MonsterDTO;
import co.fullstacklabs.battlemonsters.challenge.exceptions.ResourceNotFoundException;
import co.fullstacklabs.battlemonsters.challenge.exceptions.UnprocessableFileException;
import co.fullstacklabs.battlemonsters.challenge.service.impl.MonsterExtendedService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.io.InputStream;

import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(ApplicationConfig.class)
public class MonsterExtendedControllerTest {
    protected static final String MONSTER_PATH = "/monster";
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MonsterExtendedService monsterService;

    private MonsterDTO monsterDTO;

    @BeforeEach
    public void setup() {
        monsterDTO = MonsterDTO.builder()
                .id(1)
                .name("Dragon")
                .attack(80)
                .defense(50)
                .hp(100)
                .speed(70)
                .build();
    }


    @Test
    public void whenDeleteInexistingMonster_thenResponseIsNotFound() throws Exception {
        mockMvc.perform(delete(MONSTER_PATH + "/9999")).andExpect(status().isOk());
    }

    @Test
    void shouldCreateReturning201StatusCodeAndDeleteReturning200StatusCode() throws Exception {
        int id = 1;
        when(monsterService.create(any(MonsterDTO.class))).thenReturn(monsterDTO);
        mockMvc.perform(post(MONSTER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Dragon\",\"attack\":80,\"defense\":50,\"hp\":200,\"speed\":70}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Dragon"));

        doNothing().when(monsterService).delete(id);

        mockMvc.perform(delete(MONSTER_PATH + "/{id}", id))
                .andExpect(status().isOk());

    }

    @Test
    void shouldDeleteMonsterReturning404StatusCode() throws Exception {
        int monsterID = 1;
        doThrow(new ResourceNotFoundException("Monster not fount with id " + monsterID)).when(monsterService).delete(monsterID);

        mockMvc.perform(delete(MONSTER_PATH + "/{id}", monsterID))
                .andExpect(status().isNotFound());

    }

    @Test
    void testImportCsvSuccessfullyReturning200StatusCode() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "monster.csv", "text/csv",
                "name,attack,defense,hp,speed\nDragon,80,50,200,70".getBytes());

        mockMvc.perform(multipart(MONSTER_PATH + "/import").file(file))
                .andExpect(status().isOk());

        verify(monsterService, timeout(1)).importFromInputStream(any(InputStream.class));
    }

    @Test
    void testImportCsvNonexistentColumnsReturningInternalServerError() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "monster.csv", "text/csv",
                "names,attacks,defenses,hp,speeds\nDragon,80,50,200,70".getBytes());

        doThrow(new UnprocessableFileException("Error parsing CSV")).when(monsterService).importFromInputStream(any(InputStream.class));

        mockMvc.perform(multipart(MONSTER_PATH + "/import").file(file))
                .andExpect(status().isInternalServerError());

        verify(monsterService, timeout(1)).importFromInputStream(any(InputStream.class));
    }

}
