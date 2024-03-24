package br.com.fiap.gerente_carrinho.controller;

import br.com.fiap.gerente_carrinho.dominio.Carrinho;
import br.com.fiap.gerente_carrinho.utils.CodigoResposta;
import br.com.fiap.gerente_carrinho.facade.CarrinhoFacade;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/carrinho")
public class CarrinhoController {

    private static final String ERRO_IEM = "Item NÃO encontrado.";
    private static final String ERRO_CLIENTE = "Cliente NÃO encontrado.";
    private static final String SUCESSO = "Item ADICIONADO ao carrinho com sucesso.";
    private static final String DELETE_SUCESSO = "Item DELETADO do carrinho com sucesso.";
    private static final String COMPRA_FINALIZADA = "Compra finalizada com sucesso";

    private final CarrinhoFacade carrinhoFacade;

    @Autowired
    public CarrinhoController(CarrinhoFacade carrinhoFacade) {
        this.carrinhoFacade = carrinhoFacade;
    }

    @PostMapping
    public ResponseEntity<Object> salva(@RequestBody Carrinho carrinho) {

        CodigoResposta resp = carrinhoFacade.salvar(carrinho);
        if ( resp == CodigoResposta.INTEM_NAO_EXISTE) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ERRO_IEM);
        }
        if ( resp == CodigoResposta.CLIENTE_NAO_EXISTE) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ERRO_CLIENTE);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(SUCESSO);
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteItensPorId(@RequestBody Carrinho carrinho) {

        carrinhoFacade.remove(carrinho);
        return ResponseEntity.ok(DELETE_SUCESSO);
    }

    @GetMapping("/{id_usuario}")
    public ResponseEntity<Object> listaItensCarrinhoUsuario(@PathVariable Long id_usuario) {

        List<Carrinho> carrinhos = carrinhoFacade.listaItensCarrinhoUsuario(id_usuario);

        if (carrinhos.isEmpty()) {
            return ResponseEntity.badRequest().body(ERRO_IEM);
        }

        return ResponseEntity.status(HttpStatus.OK).body(carrinhos);
    }

    @PostMapping("/{login}")
    public ResponseEntity<Object> fechaCarrinho(@PathVariable String login, HttpServletRequest request) {

        String token = request.getHeader("Authorization");

        Long id = carrinhoFacade.usuarioExiste(login);
        if ( id < 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ERRO_CLIENTE);
        }

        CodigoResposta resp = carrinhoFacade.fechaCarrinho(id, login, token);
        if ( resp == CodigoResposta.CARRINHO_VAZIO) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(CodigoResposta.CARRINHO_VAZIO);
        }
        if ( resp == CodigoResposta.FORBIDEDDEN) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(CodigoResposta.FORBIDEDDEN);
        }
        if ( resp == CodigoResposta.ERRO_DIMINUIR_ESTOQUE) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(CodigoResposta.ERRO_DIMINUIR_ESTOQUE);
        }

        return ResponseEntity.status(HttpStatus.OK).body(COMPRA_FINALIZADA);
    }

}
