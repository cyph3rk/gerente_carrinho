package br.com.fiap.gerente_carrinho.facade;

import br.com.fiap.gerente_carrinho.dominio.Carrinho;
import br.com.fiap.gerente_carrinho.repositorio.ICarrinhoRepositorio;
import br.com.fiap.gerente_carrinho.utils.CodigoResposta;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarrinhoFacade {

    private RestTemplate restTemplate;

    private ObjectMapper objectMapper;



    private final ICarrinhoRepositorio carrinhoRepositorio;

    @Autowired
    public CarrinhoFacade(RestTemplate restTemplate, ObjectMapper objectMapper, ICarrinhoRepositorio carrinhoRepositorio) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.carrinhoRepositorio = carrinhoRepositorio;
    }

    public CodigoResposta salvar(Carrinho carrinho) {

        ResponseEntity<String> response;
        try {
            response = restTemplate.getForEntity(
                    "http://localhost:8081/api/itens/{id}",
                    String.class,
                    carrinho.getId_itens()
            );

            JsonNode itensJson = objectMapper.readTree(response.getBody());
            int id = itensJson.get("id").asInt();
        } catch (HttpClientErrorException e) {
            return CodigoResposta.INTEM_NAO_EXISTE;
        } catch (IOException e) {
            return CodigoResposta.ERRO_GENERICO;
        }

        carrinhoRepositorio.save(carrinho);

        return CodigoResposta.OK;
    }

    public CodigoResposta remove(Carrinho carrinho) {
        ResponseEntity<String> response;
        try {
            response = restTemplate.getForEntity(
                    "http://localhost:8081/api/itens/{id}",
                    String.class,
                    carrinho.getId_itens()
            );

            JsonNode itensJson = objectMapper.readTree(response.getBody());
            int id = itensJson.get("id").asInt();
        } catch (HttpClientErrorException e) {
            return CodigoResposta.INTEM_NAO_EXISTE;
        } catch (IOException e) {
            return CodigoResposta.ERRO_GENERICO;
        }
        carrinhoRepositorio.deletaItemCarrinho(carrinho.getId_usuario(), carrinho.getId_itens());

        return CodigoResposta.OK;
    }

    public List<Carrinho> listaItensCarrinhoUsuario(Long id_usuario) {

        List<Carrinho> lisrtaCarrinho = carrinhoRepositorio.findItemsById_usuario(id_usuario);

        return lisrtaCarrinho.stream()
                .collect(Collectors.toList());
    }


}
