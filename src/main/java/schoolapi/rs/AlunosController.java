package schoolapi.rs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import schoolapi.dto.AlunosDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/alunos")
@RequiredArgsConstructor
@Slf4j
public class AlunosController {

    private final Map<Integer, AlunosDto> repository;

    //@GetMapping  comentado para evitar conflito de endpoint com request param abaixo
    public ResponseEntity<List<AlunosDto>> getAll() {
        log.info("Listing alunos");
        if (repository.isEmpty()) {
            return ResponseEntity.ok(List.of());
        } else {
            return ResponseEntity
                    .ok(new ArrayList<>(repository.values()));
        }
    }

    @GetMapping
    public ResponseEntity<List<AlunosDto>> getByPrefix(@RequestParam(value = "prefixo", required = false) final String prefixo) {
        log.info("Getting cor with prefix {}", prefixo);
        if (Objects.isNull(prefixo)) return getAll();
        var selectedCores =
                repository.values().stream()
                        .filter(aluno -> aluno.getNome().startsWith(prefixo))
                        .collect(Collectors.toList());
        return ResponseEntity
                .ok(selectedCores);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<AlunosDto> getById(@PathVariable("id") final int id) {
        log.info("Getting aluno {}", id);
        var aluno = repository.get(id);
        if (Objects.isNull(aluno)) {
            return ResponseEntity
                    .notFound()
                    .build();
        } else {
            return ResponseEntity
                    .ok(aluno);
        }
    }

    @PostMapping
    public ResponseEntity<AlunosDto> save(@RequestBody final AlunosDto aluno) {
        if (repository.containsKey(aluno.getId())) {
            log.error("Collection contains id {}", aluno.getId());
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .build();
        } else {
            repository.put(aluno.getId(), aluno);
            log.info("Inserted a new aluno {}", aluno);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(aluno);
        }
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<AlunosDto> update(@PathVariable("id") final int id, @RequestBody final AlunosDto alunosDto) {
        log.info("Updating aluno {}", id);
        if (!repository.containsKey(alunosDto.getId())) {
            return ResponseEntity
                    .notFound()
                    .build();
        } else {
            repository.put(id, alunosDto);
            return ResponseEntity
                    .ok(alunosDto);
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") final int id) {
        log.info("Deleting aluno {}", id);
        var aluno = repository.get(id);
        if (Objects.isNull(aluno)) {
            return ResponseEntity
                    .notFound()
                    .build();
        } else {
            repository.remove(id);
            return ResponseEntity
                    .noContent()
                    .build();
        }
    }
}
