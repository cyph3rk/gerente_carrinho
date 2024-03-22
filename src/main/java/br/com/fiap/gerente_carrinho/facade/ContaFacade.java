package br.com.fiap.gerente_carrinho.facade;

import br.com.fiap.gerente_carrinho.dominio.Carrinho;
import br.com.fiap.gerente_carrinho.dominio.Conta;
import br.com.fiap.gerente_carrinho.repositorio.ICarrinhoRepositorio;
import br.com.fiap.gerente_carrinho.repositorio.IContaRepositorio;
import br.com.fiap.gerente_carrinho.utils.CodigoResposta;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContaFacade {

    private final IContaRepositorio contaRepositorio;

    @Autowired
    public ContaFacade(IContaRepositorio contaRepositorio) {
        this.contaRepositorio = contaRepositorio;
    }

    public CodigoResposta salvar(Conta conta) {

        contaRepositorio.save(conta);

        return CodigoResposta.OK;
    }

    public CodigoResposta remove(Long id) {

        contaRepositorio.deleteById(id);

        return CodigoResposta.OK;
    }

    public List<Conta> listaContaUsuario(LocalDateTime data) {

        List<Conta> listaConta = contaRepositorio.findItemsByData(data);

        return listaConta.stream()
                .collect(Collectors.toList());
    }
}
