package co.fullstacklabs.battlemonsters.challenge.service;

import java.util.List;

import co.fullstacklabs.battlemonsters.challenge.dto.BattleDTO;

/**
 * @author FullStack Labs
 * @version 1.0
 * @since 2022-10
 */
public interface BattleService {
    
    List<BattleDTO> getAll();

    BattleDTO startBattle(BattleDTO startBattle);

    boolean delete(int id);
}
