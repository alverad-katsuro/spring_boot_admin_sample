package br.com.alverad.anexos_mongo.controller.arquivo;

import java.io.IOException;

import org.bson.types.ObjectId;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.alverad.anexos_mongo.dto.arquivo.ArquivoDTO;
import br.com.alverad.anexos_mongo.exceptions.NotFoundException;
import br.com.alverad.anexos_mongo.service.arquivo.ArquivoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/arquivo")
public class ArquivoController {

	private final ArquivoService arquivoService;

	@GetMapping(value = "/{id}")
	@Operation(security = { @SecurityRequirement(name = "Bearer") })
	public ResponseEntity<Resource> recuperarArquivo(@PathVariable String id)
			throws IllegalArgumentException, SecurityException, IOException, NotFoundException {

		GridFsResource resource = arquivoService.recuperarArquivo(new ObjectId(id));
		String typeArchive = getExtension(resource.getContentType());

		return ResponseEntity.ok().contentLength(resource.contentLength())
				.contentType(typeArchive.equals("pdf") ? MediaType.APPLICATION_PDF
						: MediaType.APPLICATION_OCTET_STREAM)
				.header("Content-Disposition",
						String.format("inline; filename=%s.%s", resource.getFilename(),
								typeArchive))
				.body(resource);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@Operation(security = { @SecurityRequirement(name = "Bearer") })
	@PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<String> salvar(@ModelAttribute ArquivoDTO arquivoDTO)
			throws IOException, IllegalArgumentException, SecurityException, NotFoundException {
		ObjectId id = arquivoService.salvarArquivo(arquivoDTO);
		return ResponseEntity.created(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(
				this.getClass()).recuperarArquivo(id.toString()))
				.toUri()).body(id.toString());
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping(value = "/{arquivoId}")
	@Operation(security = { @SecurityRequirement(name = "Bearer") })
	public void delete(@PathVariable String arquivoId)
			throws IllegalArgumentException, SecurityException {
		arquivoService.deleteArquivo(new ObjectId(arquivoId));
	}

	private String getExtension(String contentType) {
		switch (contentType) {
			case "application/vnd.openxmlformats-officedocument.wordprocessingml.document":
				return "docx";

			case "application/msword":
				return "doc";

			case "application/vnd.openxmlformats-officedocument.presentationml.presentation":
				return "pptx";

			case "application/vnd.ms-powerpoint":
				return "ppt";

			case MediaType.APPLICATION_PDF_VALUE:
				return "pdf";

			default:
				return contentType.split("/")[1];
		}

	}

}
