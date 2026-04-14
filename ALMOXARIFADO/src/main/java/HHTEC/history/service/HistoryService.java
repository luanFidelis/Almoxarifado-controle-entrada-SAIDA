package HHTEC.history.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import HHTEC.history.database.model.History;
import HHTEC.history.database.model.State;
import HHTEC.history.repository.HistoryRepository;
@Service
public class HistoryService {
    private HistoryRepository historyRepository;

    public HistoryService (HistoryRepository historyRepository){
     this.historyRepository = historyRepository;   
    }

    public List<History> listHistory (){
     return historyRepository.findAll();   
    }


    public Optional<History> SeeID(Long id){
    var SeID = historyRepository.findById(id);
    
    if(SeID.isPresent()){
    History mostrar = SeID.get();

     return Optional.of(mostrar); 
     
    }
     return Optional.empty();
    }

    public History addHistory(History history){
          return historyRepository.save(history);
          
          
    }

    public Optional<History> mudarStatus(Long id, String colaborattorNamer, State state){

    Optional<History> veri = historyRepository.findById(id);

    if(veri.isPresent()){

        History history = veri.get();

        if(state == State.AVAILABLE || state == State.UNAVAILABLE){
              history.setState(state);
              history.setCollaboratorName(null);
              historyRepository.save(history);
              return Optional.of(history);
        }
        else if(state == State.IN_USE){
        history.setState(state);
        history.setCollaboratorName(colaborattorNamer);
        historyRepository.save(history);
    
        return Optional.of(history);
        } 
    }

    return Optional.empty();
}

}

