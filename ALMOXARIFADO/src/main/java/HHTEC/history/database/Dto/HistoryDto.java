package HHTEC.history.database.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import HHTEC.history.database.model.State;

public class HistoryDto {

@JsonProperty("id")    
private Long id;
@JsonProperty("name")
private String nameProduct;
@JsonProperty("colaboratorName")
private String colaboratorName;

@JsonProperty("state")
private State state;




    /**
     * @return Long return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

  

   

    /**
     * @return State return the state
     */
    public State getState() {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(State state) {
        this.state = state;
    }


    /**
     * @return String return the nameProduct
     */
    public String getNameProduct() {
        return nameProduct;
    }

    /**
     * @param nameProduct the nameProduct to set
     */
    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }


    /**
     * @return String return the colaboratorName
     */
    public String getColaboratorName() {
        return colaboratorName;
    }

    /**
     * @param colaboratorName the colaboratorName to set
     */
    public void setColaboratorName(String colaboratorName) {
        this.colaboratorName = colaboratorName;
    }

}
