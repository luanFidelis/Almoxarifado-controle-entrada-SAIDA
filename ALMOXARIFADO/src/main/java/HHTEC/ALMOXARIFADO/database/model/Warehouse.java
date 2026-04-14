package HHTEC.ALMOXARIFADO.database.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "WarerouseStats")
public class Warehouse {
public Warehouse(){

}

public Warehouse (String name, MovimentType typeofmoviment, Status status, String imageurl, String QrCode){
    this.name = name;
    this.typeofmoviment = typeofmoviment;
    this.status = status;
    this.imageUrl = imageurl;
    this.Qrcode = QrCode;
}


@GeneratedValue(strategy= GenerationType.IDENTITY)
@Id
private Long id;

private String name;

 @Enumerated(EnumType.STRING)
private MovimentType typeofmoviment;

@Enumerated(EnumType.STRING)
private Status status;

@Column(name = "image_url")
    private String imageUrl; 
@Column(name = "Qrcode")
    private String Qrcode;    

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
     * @return String return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Typeofmoviment return the typeofmoviment
     */
    public MovimentType getTypeofmoviment() {
        return typeofmoviment;
    }

    /**
     * @param typeofmoviment the typeofmoviment to set
     */
    public void setTypeofmoviment(MovimentType typeofmoviment) {
        this.typeofmoviment = typeofmoviment;
    }

    /**
     * @return Status return the status
     */
    public Status getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(Status status) {
        this.status = status;
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
