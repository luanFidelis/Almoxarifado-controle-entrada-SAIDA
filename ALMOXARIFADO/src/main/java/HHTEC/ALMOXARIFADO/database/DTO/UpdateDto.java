package HHTEC.ALMOXARIFADO.database.DTO;



import HHTEC.ALMOXARIFADO.database.model.Status;
public class UpdateDto {
    private Long id;
    
     
    private Status status;
   
    public Long getId() {
        return id;
    }

    public Status getStatus() {
        return  status;
    }
    


}