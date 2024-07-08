package com.api.cadastro.controller;

import com.api.cadastro.model.Pessoa;
import com.api.cadastro.service.impl.PessoaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pessoa")
public class PessoaController {

    @Autowired
    private PessoaServiceImpl pessoaService;
    private static final Logger logger = LoggerFactory.getLogger(PessoaController.class);

    @PostMapping(path = "/cadastro")
    public ResponseEntity<?> cadastrarPessoa(@RequestBody Pessoa pessoa) {
        try {
            Pessoa novaPessoa = pessoaService.salvarPessoa(pessoa);
            return new ResponseEntity<>(novaPessoa, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<Pessoa>> listarPessoas() {
        List<Pessoa> pessoas = pessoaService.listarPessoas();
        logger.info("Listando todas as pessoas");
        return new ResponseEntity<>(pessoas, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPessoaPorId(@PathVariable Long id) {
        Optional<Pessoa> pessoa = pessoaService.buscarPessoaPorId(id);
        if (pessoa.isPresent()) {
            logger.info("Pessoa encontrada: {}", pessoa.get());
            return new ResponseEntity<>(pessoa.get(), HttpStatus.OK);
        } else {
            logger.warn("Pessoa não encontrada com id: {}", id);
            return new ResponseEntity<>("Pessoa não encontrada", HttpStatus.NOT_FOUND);
        }
    }

}
