package HHTEC.ALMOXARIFADO.Service;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import HHTEC.ALMOXARIFADO.database.model.Status;
import HHTEC.ALMOXARIFADO.database.model.Warehouse;
import HHTEC.ALMOXARIFADO.database.repository.WarehouseRepository;
@Service
public class WarewouseService {

    private final WarehouseRepository warehouseRepository;
    
    public WarewouseService (WarehouseRepository warehouseRepository){

        this.warehouseRepository = warehouseRepository;
    }

    public List<Warehouse> show_list (){
        return warehouseRepository.findAll();
    }


   public Warehouse addProduct (Warehouse warehouse ){
    return warehouseRepository.save(warehouse);
}

 public Optional<Warehouse> SearchName (String name){
    
    Optional<Warehouse> result =  warehouseRepository.findByName(name);
    
   if (result.isPresent()){
    
    Warehouse returnn = result.get();
    return  Optional.of(returnn);
   }

   return Optional.empty();
 }

public void deleteProduct (Long id){

    warehouseRepository.deleteById(id);
}



public Optional<Warehouse> editStatus(Long id, Status novoStatus) {
   
    Optional<Warehouse> materialBuscado = warehouseRepository.findById(id);

  
    if (materialBuscado.isPresent()) {
        
       
        Warehouse warehouse = materialBuscado.get(); 
        
       
        warehouse.setStatus(novoStatus);
        
       
        warehouseRepository.save(warehouse);
        
       
        return Optional.of(warehouse);
    }
    
   
    return Optional.empty();
}

public Optional<Warehouse> editImagem(Long id, String imagem){

Optional <Warehouse> editImage = warehouseRepository.findById(id);
if(editImage.isPresent()){

    Warehouse warehouse = editImage.get();
    warehouse.setImageUrl(imagem);

    warehouseRepository.save(warehouse);

    return Optional.of(warehouse);
    
}
return Optional.empty();
}

public String gerarQRCodePeloId(Long id) {
        Warehouse material = warehouseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Material não encontrado com o ID: " + id));

        // Substitua pelo IP local da máquina onde o Spring Boot está rodando
        String ipServidor = "192.168.1.15"; 
        
        // Gera o link: http://192.168.1.15:8080/leitorQr.html?id=10
        String conteudoTexto = "http://" + ipServidor + ":8080/leitorQr.html?id=" + material.getId();

        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(conteudoTexto, BarcodeFormat.QR_CODE, 200, 200);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);

            byte[] pngData = outputStream.toByteArray();
            String base64Image = Base64.getEncoder().encodeToString(pngData);
            
            return "data:image/png;base64," + base64Image;

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar QR Code no backend", e);
        }
}
 }



    

