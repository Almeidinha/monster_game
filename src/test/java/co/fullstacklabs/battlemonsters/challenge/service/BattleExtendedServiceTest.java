package co.fullstacklabs.battlemonsters.challenge.service;


import static org.junit.jupiter.api.Assertions.assertEquals;

import co.fullstacklabs.battlemonsters.challenge.dto.BattleDTO;
import co.fullstacklabs.battlemonsters.challenge.dto.MonsterDTO;
import co.fullstacklabs.battlemonsters.challenge.model.Battle;
import co.fullstacklabs.battlemonsters.challenge.repository.BattleRepository;
import co.fullstacklabs.battlemonsters.challenge.repository.MonsterRepository;
import co.fullstacklabs.battlemonsters.challenge.service.impl.BattleExtendedService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BattleExtendedServiceTest {

    @Mock
    private MonsterRepository monsterRepository;

    @Mock
    private BattleRepository battleRepository;

    @Mock
    private ModelMapper modelMapper;

    @Spy
    @InjectMocks
    private BattleExtendedService battleService;

    private MonsterDTO buildMonsterDTO(int id, String name, int speed, int attack, int defense, int hp) {
        return MonsterDTO.builder()
                .id(id)
                .name(name)
                .speed(speed)
                .attack(attack)
                .defense(defense)
                .hp(hp)
                .build();
    }

    @Test
    void shouldInsertBattleWithMonsterBWinning() {
        Battle battle = new Battle();
        MonsterDTO monsterA = buildMonsterDTO(1, "MonsterA", 100, 50, 20, 210);
        MonsterDTO monsterB = buildMonsterDTO(2, "MonsterB", 100, 80, 50, 200);

        BattleDTO battleDTO = BattleDTO.builder()
                .monsterA(monsterA)
                .monsterB(monsterB)
                .build();

        when(modelMapper.map(any(BattleDTO.class), eq(Battle.class))).thenReturn(battle);
        when(modelMapper.map(any(Battle.class), eq(BattleDTO.class))).thenReturn(battleDTO);
        when(battleRepository.save(any(Battle.class))).thenReturn(battle);

        BattleDTO result = battleService.startBattle(battleDTO);
        MonsterDTO expectedWinner = result.getWinner();

        assertEquals(monsterB.getName(),(expectedWinner.getName()));
        verify(battleRepository, times(1)).save(battle);
        verify(modelMapper, times(1)).map(battleDTO, Battle.class);
        verify(modelMapper, times(1)).map(battle, BattleDTO.class);
    }
    @Test
    void shouldDeleteBattleSuccessfully() {
        int battleId = 1;
        boolean result = battleService.delete(battleId);
        assertTrue(result);
        verify(battleRepository).deleteById(battleId);
    }
}
