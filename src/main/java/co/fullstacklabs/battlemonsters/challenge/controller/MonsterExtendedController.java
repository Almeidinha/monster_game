package co.fullstacklabs.battlemonsters.challenge.controller;

import co.fullstacklabs.battlemonsters.challenge.dto.MonsterDTO;
import co.fullstacklabs.battlemonsters.challenge.exceptions.UnprocessableFileException;
import co.fullstacklabs.battlemonsters.challenge.service.MonsterService;
import co.fullstacklabs.battlemonsters.challenge.service.impl.MonsterExtendedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@RestController
@RequestMapping("/monster")
public class MonsterExtendedController {

    private final MonsterExtendedService extendedService;

    public MonsterExtendedController(MonsterExtendedService extendedService) {
        this.extendedService = extendedService;
    }

    @GetMapping
    public List<MonsterDTO> getAll() {
        return extendedService.getAll();
    }
}
