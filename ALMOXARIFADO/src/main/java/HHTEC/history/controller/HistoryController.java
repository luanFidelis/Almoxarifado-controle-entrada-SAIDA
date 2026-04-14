package HHTEC.history.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import HHTEC.history.database.Dto.HistoryDto;
import HHTEC.history.database.Dto.UpdateDto;
import HHTEC.history.database.model.History;
import HHTEC.history.service.HistoryService;


@RestController
@RequestMapping("/v2/History")
public class HistoryController {
    
    private static HistoryService historyService;
    
    public HistoryController(HistoryService historyService){
        this.historyService = historyService;
    }

    // http://localhost:8080/v2/History/VerLista
    // TIPO: GET
    // BODY: Não precisa de body
    @GetMapping("/VerLista")
    public List<History> seeList (){
        return historyService.listHistory();
    }

    // http://localhost:8080/v2/History/buscarId/1   <-- Coloque o ID na URL
    // TIPO: GET
    // BODY: Não precisa de body
    @GetMapping("/buscarId/{id}")
    public ResponseEntity<?> verId (@PathVariable Long id) {
        Optional<History> result = historyService.SeeID(id);
        return result
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
    }
    
    // http://localhost:8080/v2/History/add
    // TIPO: POST
    // BODY:
    // {
    //   "nameProduct": "Nome do Material",
    //   "colaboratorName": "Nome do Colaborador (ou null)",
    //   "state": "STATUS_DO_MATERIAL"
    // }
    @PostMapping("/add")
    public ResponseEntity<History> entryHistory(@RequestBody HistoryDto historyDto) {
        History history = new History(
            historyDto.getNameProduct(), 
            historyDto.getColaboratorName(), 
            historyDto.getState()
        );

        History retorno = historyService.addHistory(history);
        return ResponseEntity.status(201).body(retorno);
    }
    
    // http://localhost:8080/v2/History/editStatus
    // TIPO: POST
    // BODY:
    // {
    //   "id": 1,
    //   "colaboratorName": "Novo Colaborador (ou null)",
    //   "state": "NOVO_STATUS"
    // }
    @CrossOrigin(origins = "*")
    @PostMapping("/editStatus")
    public ResponseEntity<History> editStatus(@RequestBody UpdateDto update) {
        Optional<History> result = historyService.mudarStatus(
            update.getId(),
            update.getColaboratorName(), 
            update.getState()
        );
        
        return result
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
    }
}
