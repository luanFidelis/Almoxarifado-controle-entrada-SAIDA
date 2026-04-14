package HHTEC.ALMOXARIFADO.database.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

import HHTEC.ALMOXARIFADO.database.model.MovimentType;
import HHTEC.ALMOXARIFADO.database.model.Status;

public class WarehouseDTO {
 
   @JsonProperty("id")
    private Long id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("typeofmoviment")
    private MovimentType typeofmoviment;
    @JsonProperty("status")
    private Status status;
    
    @JsonProperty("imageUrl")
    private String imageUrl; 
    
    @JsonProperty("Qrcode")
    private String Qrcode;    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MovimentType getTypeofmoviment() {
        return typeofmoviment;
    }

    public void setTypeofmoviment(MovimentType typeofmoviment) {
        this.typeofmoviment = typeofmoviment;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
    

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
     * @return String return the imageUrl
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * @param imageUrl the imageUrl to set
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * @return String return the Qrcode
     */
    public String getQrcode() {
        return Qrcode;
    }

    /**
     * @param Qrcode the Qrcode to set
     */
    public void setQrcode(String Qrcode) {
        this.Qrcode = Qrcode;
    }
}