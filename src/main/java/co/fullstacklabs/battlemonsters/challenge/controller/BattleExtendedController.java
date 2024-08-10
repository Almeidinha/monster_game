package co.fullstacklabs.battlemonsters.challenge.controller;

import co.fullstacklabs.battlemonsters.challenge.dto.BattleDTO;
import co.fullstacklabs.battlemonsters.challenge.dto.MonsterDTO;
import co.fullstacklabs.battlemonsters.challenge.service.impl.BattleExtendedService;
import co.fullstacklabs.battlemonsters.challenge.service.impl.MonsterExtendedService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;


@RestController
@RequestMapping("/battle")
public class BattleExtendedController {

    private final BattleExtendedService battleService;
    private final MonsterExtendedService monsterService;

    public BattleExtendedController(BattleExtendedService battleService, MonsterExtendedService monsterService) {
        this.battleService = battleService;
        this.monsterService = monsterService;
    }

    @PostMapping("monsterA/{idA}/monsterB/{idB}")
    public BattleDTO startBattle(@PathVariable Integer idA, @PathVariable Integer idB) {
        MonsterDTO monsterA = monsterService.findById(idA);
        MonsterDTO monsterB = monsterService.findById(idB);

        BattleDTO battleDTO = BattleDTO.builder()
                .monsterA(monsterA)
                .monsterB(monsterB)
                .build();

        return battleService.startBattle(battleDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (battleService.delete(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
