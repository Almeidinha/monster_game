package co.fullstacklabs.battlemonsters.challenge.service.impl;

import co.fullstacklabs.battlemonsters.challenge.dto.BattleDTO;
import co.fullstacklabs.battlemonsters.challenge.dto.MonsterDTO;
import co.fullstacklabs.battlemonsters.challenge.exceptions.ResourceNotFoundException;
import co.fullstacklabs.battlemonsters.challenge.model.Battle;
import co.fullstacklabs.battlemonsters.challenge.model.Monster;
import co.fullstacklabs.battlemonsters.challenge.repository.BattleRepository;
import co.fullstacklabs.battlemonsters.challenge.repository.MonsterRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

@Service
public class BattleExtendedService extends BattleServiceImpl {

    private final BattleRepository battleRepository;

    private final ModelMapper modelMapper;

    public BattleExtendedService(BattleRepository battleRepository, ModelMapper modelMapper,
            MonsterRepository monsterRepository) {
        super(battleRepository, modelMapper);
        this.battleRepository = battleRepository;
        this.modelMapper = modelMapper;
    }

    MonsterDTO copyMonsterDTO(MonsterDTO monsterDTO) {
        return MonsterDTO.builder()
                .id(monsterDTO.getId())
                .name(monsterDTO.getName())
                .hp(monsterDTO.getHp())
                .attack(monsterDTO.getAttack())
                .defense(monsterDTO.getDefense())
                .speed(monsterDTO.getSpeed())
                .build();
    }

    @Override
    public BattleDTO startBattle(BattleDTO battleDTO) {
        MonsterDTO monsterA = copyMonsterDTO(battleDTO.getMonsterA());
        MonsterDTO monsterB = copyMonsterDTO(battleDTO.getMonsterB());

        MonsterDTO firstAttacker;
        MonsterDTO secondAttacker;

        if (monsterA.getSpeed().equals(monsterB.getSpeed())) {
            firstAttacker = monsterA.getAttack() > monsterB.getAttack() ? monsterA : monsterB;
            secondAttacker = monsterA.getAttack() > monsterB.getAttack() ? monsterB : monsterA;
        } else {
            firstAttacker = monsterA.getSpeed() > monsterB.getSpeed() ? monsterA : monsterB;
            secondAttacker = monsterA.getSpeed() > monsterB.getSpeed() ? monsterB : monsterA;
        }

        while (firstAttacker.getHp() > 0 && secondAttacker.getHp() > 0) {
            attack(firstAttacker, secondAttacker);
            if (secondAttacker.getHp() <= 0) {
                battleDTO.setWinner(battleDTO.getMonsterA().getId().equals(firstAttacker.getId()) ? battleDTO.getMonsterA() : battleDTO.getMonsterB());
                break;
            }
            attack(secondAttacker, firstAttacker);
            if (firstAttacker.getHp() <= 0) {
                battleDTO.setWinner(battleDTO.getMonsterA().getId().equals(secondAttacker.getId()) ? battleDTO.getMonsterA() : battleDTO.getMonsterB());
            }
        }

        Battle savedBattle = battleRepository.save(modelMapper.map(battleDTO, Battle.class));
        return modelMapper.map(savedBattle, BattleDTO.class);


    }

    private void attack(MonsterDTO attacker, MonsterDTO defender) {
        int damage = attacker.getAttack() - defender.getDefense();
        if (damage <= 0) {
            damage = 1;
        }
        defender.setHp(defender.getHp() - damage);
    }



    @Override
    public boolean delete(int battleId) {
        try {
            battleRepository.deleteById(battleId);
            return true;
        } catch (EmptyResultDataAccessException e ) {
            throw new ResourceNotFoundException("Battle not fount with id " + battleId);
        }
    }
}
