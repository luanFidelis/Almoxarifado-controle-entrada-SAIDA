package HHTEC.ALMOXARIFADO.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import HHTEC.ALMOXARIFADO.Service.WarewouseService;
import HHTEC.ALMOXARIFADO.database.DTO.UpdateDto;
import HHTEC.ALMOXARIFADO.database.DTO.WarehouseDTO;
import HHTEC.ALMOXARIFADO.database.model.Warehouse;




@RestController
@RequestMapping("/v1/Warehouse")
public class WarehouseController {
 public final WarewouseService warehouseService;
 
 public WarehouseController (WarewouseService warehouseService){
  this.warehouseService =  warehouseService; 
 }

@GetMapping("/show_list")
public List<Warehouse> Show_listt(){

    return warehouseService.show_list();
}

@PostMapping("/filter")
public ResponseEntity<?> filter(@RequestBody WarehouseDTO warehouseDTO) {

    Optional<Warehouse> product = warehouseService.SearchName(warehouseDTO.getName());

    return product
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
}

   
@PostMapping("/add_product")
public ResponseEntity<Warehouse> add_product(@RequestBody WarehouseDTO warehouseDTO) {

    Warehouse warehouse = new Warehouse(
        warehouseDTO.getName(),
        warehouseDTO.getTypeofmoviment(),
        warehouseDTO.getStatus(),
        warehouseDTO.getImageUrl(),
        warehouseDTO.getQrcode()
    );

    Warehouse rey = warehouseService.addProduct(warehouse);

    return ResponseEntity.status(201).body(rey);
}

@DeleteMapping("/delete/{id}") 
public ResponseEntity<Void> deletar(@PathVariable Long id) {
    
    
    warehouseService.deleteProduct(id);
    
    
    return ResponseEntity.noContent().build();
}

@PostMapping("/editar/status")
public ResponseEntity<Warehouse> editStatus ( @RequestBody UpdateDto updateDto ){

 Optional<Warehouse> editWare = warehouseService.editStatus(updateDto.getId(), updateDto.getStatus());

 return editWare
 .map(ResponseEntity ::ok)
 .orElse(ResponseEntity.notFound().build());   
}



@PatchMapping("/editar/imagem")
public ResponseEntity <Warehouse> editImage(@RequestBody WarehouseDTO warehouseDTO){

    Optional<Warehouse> resultEdit = warehouseService.editImagem(warehouseDTO.getId(), warehouseDTO.getImageUrl());
    return resultEdit
    .map(ResponseEntity::ok)
    .orElse(ResponseEntity.notFound().build());
}

@GetMapping("/qrcode/{id}")
    public ResponseEntity<Map<String, String>> obterQrCodeDoMaterial(@PathVariable Long id) {
        try {
            
            String qrCodeBase64 = warehouseService.gerarQRCodePeloId(id);
            
            Map<String, String> response = new HashMap<>();
            response.put("imagemQrCode", qrCodeBase64);
            
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            
            return ResponseEntity.notFound().build();
        }
    }
}
