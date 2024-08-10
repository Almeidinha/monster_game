package co.fullstacklabs.battlemonsters.challenge.service.impl;



import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import co.fullstacklabs.battlemonsters.challenge.dto.MonsterDTO;
import co.fullstacklabs.battlemonsters.challenge.model.Monster;
import co.fullstacklabs.battlemonsters.challenge.repository.MonsterRepository;

@Service
public class MonsterExtendedService extends MonsterServiceImpl{
    public MonsterExtendedService(MonsterRepository monsterRepository, ModelMapper modelMapper) {
        super(monsterRepository, modelMapper);
    }

    @Override
    public List<MonsterDTO> getAll() {
        List<Monster> monsters = monsterRepository.findAll();
        return monsters.stream().map(monster -> modelMapper.map(monster, MonsterDTO.class))
                .collect(Collectors.toList());
    }



}
