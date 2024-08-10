package co.fullstacklabs.battlemonsters.challenge.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MonsterDTOTest {

    @Test
    public void builderToStringTest() {
        MonsterDTO.MonsterDTOBuilder builder = MonsterDTO.builder()
                .id(1)
                .name("Dragon")
                .attack(100)
                .defense(80)
                .hp(200)
                .speed(60)
                .imageUrl("http://example.com/dragon.png");

        String builderToString = builder.toString();

        assertNotNull(builderToString, "Builder toString() should not return null");
        assertTrue(builderToString.startsWith("MonsterDTO.MonsterDTOBuilder("),
                "Builder toString() should start with the class name");
    }

    @Test
    public void testGettersAndSetters() {
        MonsterDTO monster = new MonsterDTO();

        monster.setId(1);
        assertEquals(1, monster.getId(), "The id should be 1");

        monster.setName("Dragon");
        assertEquals("Dragon", monster.getName(), "The name should be Dragon");

        monster.setAttack(100);
        assertEquals(100, monster.getAttack(), "The attack should be 100");

        monster.setDefense(80);
        assertEquals(80, monster.getDefense(), "The defense should be 80");

        monster.setHp(200);
        assertEquals(200, monster.getHp(), "The hp should be 200");

        monster.setSpeed(60);
        assertEquals(60, monster.getSpeed(), "The speed should be 60");

        monster.setImageUrl("http://example.com/dragon.png");
        assertEquals("http://example.com/dragon.png", monster.getImageUrl(), "The imageUrl should be http://example.com/dragon.png");
    }

    @Test
    public void testNoArgsConstructor() {
        MonsterDTO monster = new MonsterDTO();
        monster.setId(1);
        monster.setName("Dragon");
        monster.setAttack(100);
        monster.setDefense(80);
        monster.setHp(200);
        monster.setSpeed(60);
        monster.setImageUrl("http://example.com/dragon.png");

        assertEquals(1, monster.getId());
        assertEquals("Dragon", monster.getName());
        assertEquals(100, monster.getAttack());
        assertEquals(80, monster.getDefense());
        assertEquals(200, monster.getHp());
        assertEquals(60, monster.getSpeed());
        assertEquals("http://example.com/dragon.png", monster.getImageUrl());
    }

    @Test
    public void testAllArgsConstructor() {
        MonsterDTO monster = new MonsterDTO(1, "Dragon", 100, 80, 200, 60, "http://example.com/dragon.png");

        assertEquals(1, monster.getId());
        assertEquals("Dragon", monster.getName());
        assertEquals(100, monster.getAttack());
        assertEquals(80, monster.getDefense());
        assertEquals(200, monster.getHp());
        assertEquals(60, monster.getSpeed());
        assertEquals("http://example.com/dragon.png", monster.getImageUrl());
    }

    @Test
    public void testBuilder() {
        MonsterDTO monster = MonsterDTO.builder()
                .id(1)
                .name("Dragon")
                .attack(100)
                .defense(80)
                .hp(200)
                .speed(60)
                .imageUrl("http://example.com/dragon.png")
                .build();

        assertEquals(1, monster.getId());
        assertEquals("Dragon", monster.getName());
        assertEquals(100, monster.getAttack());
        assertEquals(80, monster.getDefense());
        assertEquals(200, monster.getHp());
        assertEquals(60, monster.getSpeed());
        assertEquals("http://example.com/dragon.png", monster.getImageUrl());
    }

    @Test
    public void testToString() {
        MonsterDTO monster = new MonsterDTO(1, "Dragon", 100, 80, 200, 60, "http://example.com/dragon.png");

        String expectedString = "MonsterDTO(id=1, name=Dragon, attack=100, defense=80, hp=200, speed=60, imageUrl=http://example.com/dragon.png)";
        assertEquals(expectedString, monster.toString());
    }
}
