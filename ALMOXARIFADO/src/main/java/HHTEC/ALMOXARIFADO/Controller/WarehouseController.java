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

    public WarehouseController(WarewouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    // http://localhost:8080/v1/Warehouse/show_list
    // TIPO: GET
    // BODY: Não precisa de body
    @GetMapping("/show_list")
    public List<Warehouse> Show_listt() {
        return warehouseService.show_list();
    }

    // http://localhost:8080/v1/Warehouse/filter
    // TIPO: POST
    // BODY:
    // {
    //   "name": "nome do material"
    // }
    @PostMapping("/filter")
    public ResponseEntity<?> filter(@RequestBody WarehouseDTO warehouseDTO) {
        Optional<Warehouse> product = warehouseService.SearchName(warehouseDTO.getName());
        return product
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // http://localhost:8080/v1/Warehouse/add_product
    // TIPO: POST
    // BODY:
    // {
    //   "name": "nome do material",
    //   "typeofmoviment": "ENTRADA ou SAIDA",
    //   "status": "STATUS_DO_MATERIAL",
    //   "imageUrl": "url_da_imagem",
    //   "qrcode": "texto_do_qrcode"
    // }
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

    // http://localhost:8080/v1/Warehouse/delete/1   <-- Coloque o ID na URL
    // TIPO: DELETE
    // BODY: Não precisa de body
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        warehouseService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    // http://localhost:8080/v1/Warehouse/editar/status
    // TIPO: POST
    // BODY:
    // {
    //   "id": ?,
    //   "status": "NOVO_STATUS"
    // }
    @PostMapping("/editar/status")
    public ResponseEntity<Warehouse> editStatus(@RequestBody UpdateDto updateDto) {
        Optional<Warehouse> editWare = warehouseService.editStatus(updateDto.getId(), updateDto.getStatus());
        return editWare
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // http://localhost:8080/v1/Warehouse/editar/imagem
    // TIPO: PATCH
    // BODY:
    // {
    //   "id": 1,
    //   "imageUrl": "nova_url_da_imagem"
    // }
    @PatchMapping("/editar/imagem")
    public ResponseEntity<Warehouse> editImage(@RequestBody WarehouseDTO warehouseDTO) {
        Optional<Warehouse> resultEdit = warehouseService.editImagem(warehouseDTO.getId(), warehouseDTO.getImageUrl());
        return resultEdit
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // http://localhost:8080/v1/Warehouse/qrcode/1   <-- Coloque o ID na URL
    // TIPO: GET
    // BODY: Não precisa de body
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
