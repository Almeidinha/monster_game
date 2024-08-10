package co.fullstacklabs.battlemonsters.challenge.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import org.modelmapper.ModelMapper;

import com.univocity.parsers.common.TextParsingException;
import com.univocity.parsers.common.processor.BeanListProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

import co.fullstacklabs.battlemonsters.challenge.dto.MonsterDTO;
import co.fullstacklabs.battlemonsters.challenge.exceptions.ResourceNotFoundException;
import co.fullstacklabs.battlemonsters.challenge.exceptions.UnprocessableFileException;
import co.fullstacklabs.battlemonsters.challenge.model.Monster;
import co.fullstacklabs.battlemonsters.challenge.repository.MonsterRepository;
import co.fullstacklabs.battlemonsters.challenge.service.MonsterService;

/**
 * @author FullStack Labs
 * @version 1.0
 * @since 2022-10
 */
public abstract class MonsterServiceImpl implements MonsterService {

    protected MonsterRepository monsterRepository;
    protected ModelMapper modelMapper;

    public MonsterServiceImpl(MonsterRepository monsterRepository, ModelMapper modelMapper) {
        this.monsterRepository = monsterRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public MonsterDTO create(MonsterDTO monsterDTO) {
        Monster monster = modelMapper.map(monsterDTO, Monster.class);
        monster = monsterRepository.save(monster);
        return modelMapper.map(monster, MonsterDTO.class);
    }

    Monster findMonsterById(int id) {
        return monsterRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Monster not found"));
    }

    @Override
    public MonsterDTO findById(int id) {
        Monster monster = findMonsterById(id);
        return modelMapper.map(monster, MonsterDTO.class);
    }
 
    @Override
    public MonsterDTO update(MonsterDTO monsterDTO) {
        findMonsterById(monsterDTO.getId());
        Monster monster = modelMapper.map(monsterDTO, Monster.class);        
        monsterRepository.save(monster);
        return modelMapper.map(monster, MonsterDTO.class);

    }

    @Override
    public void importFromInputStream(InputStream inputStream) {
        try (Reader inputReader = new InputStreamReader(inputStream, "UTF-8")) {
            BeanListProcessor<MonsterDTO> rowProcessor = new BeanListProcessor<>(MonsterDTO.class);
            CsvParserSettings settings = new CsvParserSettings();
            settings.setHeaderExtractionEnabled(true);
            settings.setProcessor(rowProcessor);
            CsvParser parser = new CsvParser(settings);
            parser.parse(inputReader);
            List<MonsterDTO> monsters = rowProcessor.getBeans();            
            monsters.forEach(m -> create(m));
        } catch (IOException ex) {
            throw new UnprocessableFileException(ex.getMessage());
        } catch (TextParsingException ex) {
            throw new UnprocessableFileException(ex.getMessage());
        }
    }

    @Override
    public void delete(int id) {
        Monster monster = findMonsterById(id);
        monsterRepository.delete(monster);
    }
}
