package com.api.cadastro.service.impl;

import com.api.cadastro.model.Pessoa;
import com.api.cadastro.repository.PessoaRepository;
import com.api.cadastro.service.PessoaService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PessoaServiceImpl implements PessoaService {

    @Autowired
    private PessoaRepository pessoaRepository;

    private final RestTemplate restTemplate;

    private static final Logger logger = LoggerFactory.getLogger(PessoaServiceImpl.class);
    private final ObjectMapper objectMapper;
    private final String cpfValidationApiUrl = "http://localhost:8082/api/validate";

    @Override
    public Pessoa salvarPessoa(Pessoa pessoa) throws Exception {
        validarCamposObrigatorios(pessoa);
        if (validaCpf(pessoa.getCpf())) {
            if (pessoaRepository.findByCpf(pessoa.getCpf()).isPresent()) {
                throw new Exception("CPF já cadastrado.");
            }
            return pessoaRepository.save(pessoa);
        }

        throw new Exception("CPF inválido");
    }

    @Override
    public List<Pessoa> listarPessoas() {
        return pessoaRepository.findAll();
    }

    @Override
    public Optional<Pessoa> buscarPessoaPorId(Long id) {
        return pessoaRepository.findById(id);
    }

    private void validarCamposObrigatorios(Pessoa pessoa) throws Exception {
        if (pessoa.getCpf() == null || pessoa.getCpf().isEmpty()) {
            throw new Exception("CPF é obrigatório.");
        }
        if (pessoa.getNome() == null || pessoa.getNome().isEmpty()) {
            throw new Exception("Nome é obrigatório.");
        }
        if (pessoa.getEmail() == null || pessoa.getEmail().isEmpty()) {
            throw new Exception("Email é obrigatório.");
        }
        if (pessoa.getDataNascimento() == null) {
            throw new Exception("Data de nascimento é obrigatória.");
        }
    }

    public boolean validaCpf(String cpf) {
        String url = cpfValidationApiUrl + "/" + cpf;
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                return casosDeRetorno(response.getBody());
            }
        } catch (HttpClientErrorException e) {
            logger.error("Failed to call API. Status code: {}", e.getStatusCode());
            if (e.getStatusCode().value() == 400) {
                return false;
            }
        } catch (Exception e) {
            logger.error("Ocorreu um erro na chamada de validação do CPF", e);
        }
        return false;
    }

    private boolean casosDeRetorno(String responseBody) {
        try {
            JsonNode jsonNode = objectMapper.readTree(responseBody);

            if (jsonNode.has("Response") && "valid CPF".equals(jsonNode.get("Response").asText())) {
                return true;
            } else if (jsonNode.has("Response") && "Invalid CPF".equals(jsonNode.get("Response").asText())) {
                return false;
            } else if (jsonNode.has("result") && "Error".equals(jsonNode.get("result").asText()) && jsonNode.has("data")) {
                JsonNode dataNode = jsonNode.get("data");
                if (dataNode.has("message") && dataNode.get("message").asText().contains("Invalid CPF")) {
                    return false;
                }
            }
        } catch (Exception e) {
            logger.error("Ocorreu um erro na chamada de validação do CPF", e);
        }
        return false;
    }

}
