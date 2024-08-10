package co.fullstacklabs.battlemonsters.challenge.controller;

import java.util.HashSet;
import java.util.List;

import co.fullstacklabs.battlemonsters.challenge.exceptions.UnprocessableEntityException;
import co.fullstacklabs.battlemonsters.challenge.exceptions.UnprocessableFileException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import co.fullstacklabs.battlemonsters.challenge.dto.MonsterDTO;
import co.fullstacklabs.battlemonsters.challenge.service.MonsterService;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.ConstraintViolationException;

/**
 * @author FullStack Labs
 * @version 1.0
 * @since 2022-10
 */
@RestController
@RequestMapping("/monster")
public class MonsterController {
    
    @Autowired
    private MonsterService monsterService;

    @GetMapping("/{id}")
    public MonsterDTO getMonsterById(@PathVariable("id") int monsterId) {
        return monsterService.findById(monsterId);
    }

    @PutMapping
    public MonsterDTO update(@RequestBody MonsterDTO monsterDTO) {
        return monsterService.update(monsterDTO);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        this.monsterService.delete(Integer.parseInt(id));
    }

    @GetMapping("/throwUnprocessableEntity")
    public void throwUnprocessableEntityException() {
        throw new UnprocessableEntityException("Unprocessable entity");
    }

    @GetMapping("/throwConstraintViolation")
    public void throwConstraintViolationException() {
        throw new ConstraintViolationException("Constraint violation", new HashSet<>());
    }

    @PostMapping("/import")
    public void importCsv(@RequestParam("file") MultipartFile file,
                          RedirectAttributes attributes) {
        try{
            monsterService.importFromInputStream(file.getInputStream());
        } catch (Exception ex) {
            throw new UnprocessableFileException(ex.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<MonsterDTO> create(@RequestBody MonsterDTO monsterDTO) {
        return new ResponseEntity<MonsterDTO>(monsterService.create(monsterDTO), HttpStatus.CREATED);
    }
}
