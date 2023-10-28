package br.com.alverad.anexos_mongo.dto.arquivo;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArquivoDTO {

    private ObjectId id;

    private Optional<String> nome;

    private MultipartFile arquivo;

}
