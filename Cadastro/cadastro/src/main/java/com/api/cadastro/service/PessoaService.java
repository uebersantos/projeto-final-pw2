package com.api.cadastro.service;

import com.api.cadastro.model.Pessoa;

import java.util.List;
import java.util.Optional;

public interface PessoaService {

    Pessoa salvarPessoa(Pessoa pessoa) throws Exception;

    List<Pessoa> listarPessoas();

    Optional<Pessoa> buscarPessoaPorId(Long id);
}
