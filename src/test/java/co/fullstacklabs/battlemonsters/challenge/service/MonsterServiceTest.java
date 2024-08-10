package co.fullstacklabs.battlemonsters.challenge.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import co.fullstacklabs.battlemonsters.challenge.exceptions.UnprocessableFileException;
import co.fullstacklabs.battlemonsters.challenge.service.impl.MonsterExtendedService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import co.fullstacklabs.battlemonsters.challenge.dto.MonsterDTO;
import co.fullstacklabs.battlemonsters.challenge.exceptions.ResourceNotFoundException;
import co.fullstacklabs.battlemonsters.challenge.exceptions.UnprocessableEntityException;
import co.fullstacklabs.battlemonsters.challenge.model.Monster;
import co.fullstacklabs.battlemonsters.challenge.repository.MonsterRepository;
import co.fullstacklabs.battlemonsters.challenge.testbuilders.MonsterTestBuilder;

/**
 * @author FullStack Labs
 * @version 1.0
 * @since 2022-10
 */
@ExtendWith(MockitoExtension.class)
public class MonsterServiceTest {
    @InjectMocks
    public MonsterExtendedService monsterService;

    @Mock
    public MonsterRepository monsterRepository;

    @Mock
    private ModelMapper mapper;

   @Test
   public void testGetAll() {
       String monsterName1 = "Monster 1";
       String monsterName2 = "Monster 2";
       Monster monster1 = MonsterTestBuilder.builder().id(1)
               .name(monsterName1).attack(30).defense(20).hp(21).speed(15)
               .imageURL("imageUrl1").build();
   
       Monster monster2 = MonsterTestBuilder.builder().id(2)
               .name(monsterName2).attack(30).defense(20).hp(21).speed(15)
               .imageURL("imageUrl1").build();
   
       List<Monster> monsterList = Arrays.asList(new Monster[]{monster1, monster2});
       Mockito.when(monsterRepository.findAll()).thenReturn(monsterList);
       
       monsterService.getAll();
   
       Mockito.verify(monsterRepository).findAll();
       Mockito.verify(mapper).map(monsterList.get(0), MonsterDTO.class);
       Mockito.verify(mapper).map(monsterList.get(1), MonsterDTO.class);        
   }

    @Test
    public void testGetMonsterByIdSuccessfully() throws Exception {
        int id = 1;
        Monster monster1 = MonsterTestBuilder.builder().build();
        Mockito.when(monsterRepository.findById(id)).thenReturn(Optional.of(monster1));
        monsterService.findById(id);
        Mockito.verify(monsterRepository).findById(id);
        Mockito.verify(mapper).map(monster1, MonsterDTO.class);
    }

    @Test
    public void testGetMonsterByIdNotExists() throws Exception {
        int id = 1;
        Mockito.when(monsterRepository.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> monsterService.findById(id));
    }

    @Test
    public void testDeleteInexistingMonste() throws Exception {
        int id = 999;
        Mockito.when(monsterRepository.findById(id)).thenThrow(new UnprocessableEntityException(""));
        
        Assertions.assertThrows(UnprocessableEntityException.class, () -> monsterService.delete(id));
    }
    

    
}
