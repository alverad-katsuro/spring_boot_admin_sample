package br.com.alverad.anexos_mongo.controller.adviser;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.alverad.anexos_mongo.enumeration.ErrorType;
import br.com.alverad.anexos_mongo.erros.ErrorResponse;
import br.com.alverad.anexos_mongo.exceptions.NotFoundException;

/**
 * Responsável por tratar as exceções gerais da aplicação.
 *
 * @author Alfredo Gabriel
 * @since 26/03/2023
 * @version 1.0
 */
@RestControllerAdvice
public class ControllerAdvise {

	/**
	 * Caso algum recurso não seja encontrado no banco de dados sera retornado uma
	 * mensagem padronizada.
	 *
	 * @param ex Excessão capturada.
	 * @return Menssagem.
	 * @author Alfredo Gabriel
	 * @since 26/03/2023
	 */
	@ExceptionHandler(NotFoundException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public ErrorResponse handleNotfoundException(NotFoundException ex) {
		return ErrorResponse.builder()
				.message(ex.getMessage())
				.internalCode(ex.getInternalCode())
				.build();
	}

	/**
	 * Caso alguma entidade não seja valida (dados diferentes do esperado), será
	 * retornado o atributo e a menssagem pre definida.
	 *
	 * @param ex Excessão capturada.
	 * @return Mapa contendo atributo e mensagem de erro.
	 * @author Alfredo Gabriel
	 * @since 26/03/2023
	 */
	@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleArgumentNotValid(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		errors.put(ErrorType.REPORT_001.getMessage(), ErrorType.REPORT_001.getInternalCode());
		ex.getBindingResult().getAllErrors().forEach(error -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		return new ResponseEntity<>(errors, HttpStatus.UNPROCESSABLE_ENTITY);
	}

	/**
	 * Caso a autenticação não seja bem sucedida, será retornado a mensagem
	 * pre-estabelicida.
	 *
	 * @param ex Excessão capturada.
	 * @return Mensagem
	 * @author Alfredo Gabriel
	 * @since 26/03/2023
	 */
	@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
	@ExceptionHandler(BadCredentialsException.class)
	public ErrorResponse handleBadCredentialsException(BadCredentialsException ex) {
		return ErrorResponse.builder()
				.message(ErrorType.USER_002.getMessage())
				.technicalMessage(ex.getLocalizedMessage())
				.internalCode(ErrorType.USER_002.getInternalCode())
				.build();
	}

	/**
	 * Caso a autenticação não seja bem sucedida, será retornado a mensagem
	 * pre-estabelicida.
	 *
	 * @param ex Excessão capturada.
	 * @return Mensagem
	 * @author Alfredo Gabriel
	 * @since 26/03/2023
	 */
	@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
	@ExceptionHandler(AuthenticationException.class)
	public ErrorResponse handleAuthenticationException(AuthenticationException ex) {
		return ErrorResponse.builder()
				.message(ErrorType.USER_002.getMessage())
				.technicalMessage(ex.getLocalizedMessage())
				.internalCode(ErrorType.USER_002.getInternalCode())
				.build();
	}

	/**
	 * Caso a autenticação não seja encontrado nenhum elemento correspondente.
	 *
	 * @param ex Excessão capturada.
	 * @return Mensagem
	 * @author Alfredo Gabriel
	 * @since 21/04/2023
	 */
	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	@ExceptionHandler(NoSuchElementException.class)
	public ErrorResponse handleNoSuchElementException(NoSuchElementException ex) {
		return new ErrorResponse(
				ErrorType.REPORT_004.getMessage(),
				ex.getLocalizedMessage(),
				ErrorType.REPORT_004.getInternalCode());
	}

	/**
	 * Caso a autenticação não seja encontrado nenum exemplo de e-mail
	 *
	 * @param ex Excessão capturada.
	 * @return Mensagem
	 * @author Alfredo Gabriel
	 * @since 21/04/2023
	 */
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ErrorResponse handleNotFound(HttpMessageNotReadableException ex) {
		return new ErrorResponse(
				ErrorType.REPORT_005.getMessage(),
				ex.getLocalizedMessage(),
				ErrorType.REPORT_005.getInternalCode());
	}

	/**
	 * Caso a autenticação não seja encontrado nenum exemplo de e-mail
	 *
	 * @param ex Excessão capturada.
	 * @return Mensagem
	 * @author Alfredo Gabriel
	 * @since 21/04/2023
	 */
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(IllegalArgumentException.class)
	public ErrorResponse handleIllegalArgumentException(IllegalArgumentException ex) {
		return new ErrorResponse(
				ErrorType.REPORT_008.getMessage(),
				ex.getLocalizedMessage(),
				ErrorType.REPORT_008.getInternalCode());
	}

}
