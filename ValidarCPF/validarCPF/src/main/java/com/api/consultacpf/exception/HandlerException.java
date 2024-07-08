package com.api.consultacpf.exception;

import com.api.consultacpf.service.ConsultaCpfService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;

@ControllerAdvice
public class HandlerException {

    private static final Logger logger = LoggerFactory.getLogger(ConsultaCpfService.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneralException(Exception ex) {
        String motivo = "Erro geral: " + ex.getMessage();
        logger.error(motivo, ex);
        HashMap<String, String> resposta = new HashMap<>();
        resposta.put("Response",
                "Erro desconhecido, por favor verifique os logs ou entre em contato com o suporte da API.");
        return new ResponseEntity<>(resposta, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
