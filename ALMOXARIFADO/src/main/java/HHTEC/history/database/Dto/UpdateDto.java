package HHTEC.history.database.Dto;
import com.fasterxml.jackson.annotation.JsonProperty;

import HHTEC.history.database.model.State;
public class UpdateDto {
    private Long id;
    
     
    private State state;
    @JsonProperty("collaboratorName")
    private String colaborattorName;
    public Long getId() {
        return id;
    }

    public State getState() {
        return state;
    }
    

   
    public String getColaborattorName() {
        return colaborattorName;
    }

    public void setColaborattorName(String colaborattorName) {
        this.colaborattorName = colaborattorName;
    }

}