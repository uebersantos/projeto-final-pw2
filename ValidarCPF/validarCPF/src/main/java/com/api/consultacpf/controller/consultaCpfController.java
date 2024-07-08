package com.api.consultacpf.controller;

import com.api.consultacpf.service.ConsultaCpfService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/api")
@Validated
@AllArgsConstructor
public class consultaCpfController {

    private final ConsultaCpfService consultaCpfService;
    private static final Logger logger = LoggerFactory.getLogger(ConsultaCpfService.class);
    private final HashMap<String, String> resposta = new HashMap<>();

    @GetMapping("/validate/{cpf}")
    public ResponseEntity<?> validarCPF(@PathVariable String cpf) {
        if (!consultaCpfService.validarCPF(cpf)) {
            String retorno = "Invalid CPF";
            resposta.put("Response", retorno);
            logger.error(retorno);
            return new ResponseEntity<>(resposta, HttpStatus.BAD_REQUEST);
        }

        String retorno = "valid CPF";
        resposta.put("Response", retorno);
        logger.info(retorno);
        return new ResponseEntity<>(resposta, HttpStatus.OK);
    }

}
