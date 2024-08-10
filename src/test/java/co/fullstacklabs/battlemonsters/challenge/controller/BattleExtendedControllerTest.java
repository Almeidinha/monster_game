package co.fullstacklabs.battlemonsters.challenge.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import co.fullstacklabs.battlemonsters.challenge.dto.BattleDTO;
import co.fullstacklabs.battlemonsters.challenge.dto.MonsterDTO;
import co.fullstacklabs.battlemonsters.challenge.repository.BattleRepository;
import co.fullstacklabs.battlemonsters.challenge.service.impl.BattleExtendedService;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import co.fullstacklabs.battlemonsters.challenge.ApplicationConfig;

import org.mockito.Mockito.*;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@Import(ApplicationConfig.class)
public class BattleExtendedControllerTest {

    protected static final String BATTLE_PATH = "/battle";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BattleRepository battleRepository;

    @MockBean
    private BattleExtendedService battleService;

    @Test
    void shouldFailStartBattleWithNonexistentMonsterReturning404StatusCode() throws Exception {
        long monsterAId = 1L;
        long monsterBId = 999L;

        mockMvc.perform(post(BATTLE_PATH + "/monsterA/{idA}/monsterB/{idB}", monsterAId, monsterBId))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldInsertBattleWithMonsterBWinningAndReturning200StatusCode() throws Exception {
        //assertEquals(1,2);
        int monsterAId = 1;
        int monsterBId = 2;

        MonsterDTO winner = new MonsterDTO();
        winner.setName("Old shark");

        BattleDTO battleDTO = BattleDTO.builder()
                .monsterA(MonsterDTO.builder().id(monsterAId).build())
                .monsterB(MonsterDTO.builder().id(monsterBId).build())
                .winner(winner)
                .build();

        when(battleService.startBattle(any(BattleDTO.class))).thenReturn(battleDTO);

        mockMvc.perform(post(BATTLE_PATH + "/monsterA/{idA}/monsterB/{idB}", monsterAId, monsterBId)
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.winner.name", Is.is("Old shark")));
    }

    @Test
    void shouldDeleteBattleSuccessfullyReturning200StatusCode()throws Exception {
        int battleId = 1;
        when(battleService.delete(battleId)).thenReturn(true);
        mockMvc.perform(delete(BATTLE_PATH + "/{id}", battleId))
                .andExpect(status().isNoContent());

    }

    @Test
    void shouldFailDeletingNonexistentBattleReturning404StatusCode() throws Exception {
        long battleId = 999L;
        mockMvc.perform(delete(BATTLE_PATH + "/{id}", battleId))
                .andExpect(status().isNotFound());
    }

}
