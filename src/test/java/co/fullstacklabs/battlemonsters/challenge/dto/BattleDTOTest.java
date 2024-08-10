package co.fullstacklabs.battlemonsters.challenge.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BattleDTOTest {
    @Test
    public void testBuilderAndAccessors() {
        MonsterDTO monsterA = new MonsterDTO();
        MonsterDTO monsterB = new MonsterDTO();
        MonsterDTO winner = monsterA;

        BattleDTO battle = BattleDTO.builder()
                .id(1)
                .monsterA(monsterA)
                .monsterB(monsterB)
                .winner(winner)
                .build();

        assertEquals(1, battle.getId());
        assertEquals(monsterA, battle.getMonsterA());
        assertEquals(monsterB, battle.getMonsterB());
        assertEquals(monsterA, battle.getWinner()); // Note: winner is monsterA
    }

    @Test
    public void testNoArgsConstructor() {
        MonsterDTO monsterA = new MonsterDTO();
        MonsterDTO monsterB = new MonsterDTO();
        MonsterDTO winner = monsterA;

        BattleDTO battle = new BattleDTO();
        battle.setId(1);
        battle.setMonsterA(monsterA);
        battle.setMonsterB(monsterB);
        battle.setWinner(winner);

        assertEquals(1, battle.getId());
        assertEquals(monsterA, battle.getMonsterA());
        assertEquals(monsterB, battle.getMonsterB());
        assertEquals(monsterA, battle.getWinner());
    }

    @Test
    public void testToString() {
        MonsterDTO monsterA = new MonsterDTO();
        MonsterDTO monsterB = new MonsterDTO();
        MonsterDTO winner = monsterA;

        BattleDTO battle = BattleDTO.builder()
                .id(1)
                .monsterA(monsterA)
                .monsterB(monsterB)
                .winner(winner)
                .build();

        String actualString = battle.toString();

        assertTrue(actualString.contains("BattleDTO"));
        assertTrue(actualString.contains("id=1"));
        assertTrue(actualString.contains("monsterA=" + monsterA.toString()));
        assertTrue(actualString.contains("monsterB=" + monsterB.toString()));
        assertTrue(actualString.contains("winner=" + winner.toString()));
    }

    @Test
    public void builderToStringTest() {
        MonsterDTO monsterA = new MonsterDTO();
        MonsterDTO monsterB = new MonsterDTO();
        BattleDTO.BattleDTOBuilder builder = BattleDTO.builder()
                .id(1)
                .monsterA(monsterA)
                .monsterB(monsterB)
                .winner(monsterA);

        String builderToString = builder.toString();

        assertNotNull(builderToString, "Builder toString() should not return null");
        assertTrue(builderToString.startsWith("BattleDTO.BattleDTOBuilder("),
                "Builder toString() should start with the class name");
    }

}
