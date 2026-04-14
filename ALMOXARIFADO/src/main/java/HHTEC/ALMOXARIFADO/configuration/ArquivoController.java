
package HHTEC.ALMOXARIFADO.configuration;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1/Warehouse")
public class ArquivoController {

    private final String uploadDir = "uploads/";

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImagem(@RequestParam("file") MultipartFile file) {
        try {
            // Cria a pasta uploads caso ela não exista
            Path caminhoPasta = Paths.get(uploadDir);
            if (!Files.exists(caminhoPasta)) {
                Files.createDirectories(caminhoPasta);
            }

           
            String nomeArquivo = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path caminhoCopleto = caminhoPasta.resolve(nomeArquivo);

            
            Files.write(caminhoCopleto, file.getBytes());

            
            String urlDaImagem = "http://localhost:8080/imagens/" + nomeArquivo;

            return ResponseEntity.ok(urlDaImagem);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao fazer upload da imagem");
        }
    }
}