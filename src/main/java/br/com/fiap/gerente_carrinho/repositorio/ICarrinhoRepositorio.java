package br.com.fiap.gerente_carrinho.repositorio;

import br.com.fiap.gerente_carrinho.dominio.Carrinho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICarrinhoRepositorio extends JpaRepository<Carrinho, Long> {

//    List<Carrinho> findById_cliente(String id_cliente);
}
