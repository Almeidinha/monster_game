package co.fullstacklabs.battlemonsters.challenge.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import co.fullstacklabs.battlemonsters.challenge.dto.MonsterDTO;
import co.fullstacklabs.battlemonsters.challenge.exceptions.UnprocessableFileException;
import co.fullstacklabs.battlemonsters.challenge.model.Monster;
import co.fullstacklabs.battlemonsters.challenge.repository.MonsterRepository;
import co.fullstacklabs.battlemonsters.challenge.service.impl.MonsterExtendedService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class MonsterExtendedServiceTest {

    @Mock
    private MonsterRepository monsterRepository;

    @Mock
    private ModelMapper modelMapper;

    @Spy
    @InjectMocks
    private MonsterExtendedService monsterService;

    @Mock
    Monster monster;

    @Test
    public void testDeleteMonsterSuccessfully() {
        int id = 1;
        when(monsterRepository.findById(id)).thenReturn(Optional.of(monster));
        monsterService.delete(id);
        verify(monsterRepository, times(1)).delete(monster);
    }

     @Test
     void testImportCsvSucessfully() throws Exception {
         String file = "data/monsters-correct.csv";
         InputStream inputStream = Files.newInputStream(Paths.get(file));

         when(modelMapper.map(any(), eq(Monster.class))).thenReturn(new Monster());
         when(monsterRepository.save(any())).thenReturn(new Monster());

         monsterService.importFromInputStream(inputStream);

         verify(modelMapper, times(11)).map(any(), eq(Monster.class));
         verify(monsterService, times(11)).create(any(MonsterDTO.class));
         verify(monsterRepository, times(11)).save(any());
     }


    @Test
    public void whenImportFromInputStreamThrowsIOException_thenUnprocessableFileExceptionIsThrown() throws IOException {
        InputStream stream = mock(InputStream.class);
        when(stream.read()).thenThrow(new IOException("test IOException"));

        assertThrows(UnprocessableFileException.class, () -> monsterService.importFromInputStream(stream));
    }

}
