package com.api.consultacpf.service.impl;

import com.api.consultacpf.service.ConsultaCpfService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ConsultaCpfServiceImpl implements ConsultaCpfService {


    private final CPFValidarService cpfValidarService;

    @Override
    public boolean validarCPF(String cpf) {
        return cpfValidarService.validarCPF(cpf);
    }

}
