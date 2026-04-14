package HHTEC.history.database.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
@Entity
@Table(name = "Hystory")
public class History {
    
public History (){
    
    
}

public History (String nameProduct, String collaboratorName, State state){

this.nameProduct = nameProduct;
this.collaboratorName = collaboratorName;
this.state = state;
}

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

private String nameProduct;

private String collaboratorName;

@Enumerated(EnumType.STRING)
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
     * @return String return the collaboratorName
     */
    public String getCollaboratorName() {
        return collaboratorName;
    }

    /**
     * @param collaboratorName the collaboratorName to set
     */
    public void setCollaboratorName(String collaboratorName) {
        this.collaboratorName = collaboratorName;
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

}
