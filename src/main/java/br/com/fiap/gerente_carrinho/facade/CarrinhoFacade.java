package br.com.fiap.gerente_carrinho.facade;

import br.com.fiap.gerente_carrinho.dominio.Carrinho;
import br.com.fiap.gerente_carrinho.dominio.Conta;
import br.com.fiap.gerente_carrinho.repositorio.ICarrinhoRepositorio;
import br.com.fiap.gerente_carrinho.utils.CodigoResposta;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class CarrinhoFacade {

    private RestTemplate restTemplate;

    private ObjectMapper objectMapper;

    private final ICarrinhoRepositorio carrinhoRepositorio;

    private final ContaFacade contaFacade;

    @Autowired
    public CarrinhoFacade(RestTemplate restTemplate, ObjectMapper objectMapper, ICarrinhoRepositorio carrinhoRepositorio, ContaFacade contaFacade) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.carrinhoRepositorio = carrinhoRepositorio;
        this.contaFacade = contaFacade;
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


    public Long usuarioExiste(String login) {
        try {
            ResponseEntity<String> responseAPI = restTemplate.getForEntity(
                    "http://localhost:8082/users/{login}",
                    String.class,
                    login
            );

            if (responseAPI.getStatusCode() == HttpStatus.OK) {
                String id = responseAPI.getBody();
                return Long.parseLong(id);
            } else {
                return -1L;
            }
        } catch (HttpClientErrorException e) {
            return -1L;
        }

    }

    public CodigoResposta diminuiEstoque(Long id, String qtd, String token) {
        try {
            URI uri = UriComponentsBuilder.fromUriString("http://localhost:8081/api/itens/diminui/{id}/{qtd}")
                    .buildAndExpand(id, qtd)
                    .toUri();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + token);
            HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);

            ResponseEntity<String> responseAPI = restTemplate.exchange(
                    uri,
                    HttpMethod.PUT,
                    requestEntity,
                    String.class
            );

            if (responseAPI.getStatusCode() == HttpStatus.CREATED) {
                return CodigoResposta.OK;
            } else {
                return CodigoResposta.ERRO_DIMINUIR_ESTOQUE;
            }
        } catch (HttpClientErrorException e) {
            return CodigoResposta.ERRO_DIMINUIR_ESTOQUE;
        }
    }

    public JsonNode pegaItem(Long id) {
        JsonNode itensJson = null;
        try {
            ResponseEntity<String> responseAPI = restTemplate.getForEntity(
                    "http://localhost:8081/api/itens/{id}",
                    String.class,
                    id
            );

            if (responseAPI.getStatusCode() == HttpStatus.OK) {
                itensJson = objectMapper.readTree(responseAPI.getBody());
            }
        } catch (HttpClientErrorException e) {
            throw new NoSuchElementException();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return itensJson;
    }

    public CodigoResposta fechaCarrinho(Long id, String login, String token) {

        LocalDateTime data = LocalDateTime.now();

        CodigoResposta resp = CodigoResposta.ERRO_GENERICO;

        List<Carrinho> carrinhoList = carrinhoRepositorio.findItemsById_usuario(id);

        if (carrinhoList.isEmpty()) {
            return CodigoResposta.CARRINHO_VAZIO;
        }

        List<Conta> contaList;
        for (Carrinho carrinho : carrinhoList) {
            JsonNode itensJson = pegaItem(carrinho.getId_itens());
            if (itensJson.isEmpty()) {
                resp = CodigoResposta.ERRO_GENERICO;
                break;
            }
            String nome = itensJson.get("nome").asText();
            String valor = itensJson.get("valor").asText();

            CodigoResposta aux = diminuiEstoque(carrinho.getId_itens(), carrinho.getQuantidade(), token);
            if (aux != CodigoResposta.OK) {
                resp = aux;
                break;
            }

            Conta conta = new Conta();
            conta.setData(data);
            conta.setCliente(login);
            conta.setProduto(nome);
            conta.setValor(valor);
            conta.setQuantidade(carrinho.getQuantidade());
            conta.setTotal("100,00"); //TODO: Implementar o calculo do valor total
            conta.setSituacao("Pago");

            resp = contaFacade.salvar(conta);

            carrinhoRepositorio.deletaItemCarrinho(carrinho.getId_usuario(), carrinho.getId_itens());
        }
        return resp;
    }
}
