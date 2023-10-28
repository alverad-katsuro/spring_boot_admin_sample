package br.com.alverad.anexos_mongo.service.arquivo.impl;

import java.io.IOException;
import java.util.UUID;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import com.mongodb.client.gridfs.model.GridFSFile;

import br.com.alverad.anexos_mongo.dto.arquivo.ArquivoDTO;
import br.com.alverad.anexos_mongo.exceptions.NotFoundException;
import br.com.alverad.anexos_mongo.service.arquivo.ArquivoService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArquivoServiceImpl implements ArquivoService {

    private final GridFsTemplate gridFsTemplate;

    private final GridFsOperations gridFsOperations;

    @Override
    public ObjectId salvarArquivo(ArquivoDTO arquivo)
            throws IOException {
        if (arquivo.getId() != null) {
            gridFsTemplate.delete(new Query(Criteria.where("_id").is(arquivo.getId())));
        }
        return gridFsTemplate.store(
                arquivo.getArquivo().getInputStream(), arquivo.getNome().orElse(UUID.randomUUID().toString()),
                arquivo.getArquivo().getContentType());
    }

    @Override
    public GridFsResource recuperarArquivo(ObjectId id)
            throws IllegalArgumentException, SecurityException, NotFoundException {
        GridFSFile file = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(id)));
        if (file == null) {
            throw new NotFoundException();
        }
        return gridFsOperations.getResource(file);

    }

    @Override
    public void deleteArquivo(ObjectId id) {
        gridFsTemplate.delete(new Query(Criteria.where("_id").is(id)));
    }

}
