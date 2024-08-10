package co.fullstacklabs.battlemonsters.challenge.testbuilders;

import co.fullstacklabs.battlemonsters.challenge.model.Battle;
import co.fullstacklabs.battlemonsters.challenge.model.Monster;
import lombok.Builder;

/**
 * @author FullStack Labs
 * @version 1.0
 * @since 2022-10
 */
public class BattleTestBuilder {
    
    @Builder
    public static Battle monster(Integer id, Monster monsterA, Monster monsterB, Monster monsterWinner) {
        Battle battle = new Battle();
        battle.setId(id);
        battle.setMonsterA(monsterA);
        battle.setMonsterB(monsterB);
        battle.setWinner(monsterWinner);
        return battle;
    }

    public static BattleBuilder battleBuilder() {
        return new BattleBuilder();
    }

    public static class BattleBuilder {
        private Integer id = 1;
        private Monster monsterA = MonsterTestBuilder.builder().id(1).name("Old Shark").attack(50).
                defense(20).hp(60).speed(90).
                imageURL("https://fsl-assessment-public-files.s3.amazonaws.com/assessment-cc-01/old-shark.png").
                build();
        private Monster monsterB = MonsterTestBuilder.builder().id(2).name("Red Dragon").attack(90).
                defense(80).hp(90).speed(70).
                imageURL("https://fsl-assessment-public-files.s3.amazonaws.com/assessment-cc-01/old-shark.png").
                build();
        private Monster monsterWinner = monsterB;
    }
}
