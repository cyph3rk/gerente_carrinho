package br.com.fiap.gerente_carrinho.repositorio;

import br.com.fiap.gerente_carrinho.dominio.Carrinho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface ICarrinhoRepositorio extends JpaRepository<Carrinho, Long> {

    @Modifying
    @Query("delete from Carrinho p where p.id_usuario = :id_usuario and p.id_itens = :id_itens")
    void deletaItemCarrinho(Long id_usuario, Long id_itens);

    @Query("from Carrinho p where p.id_usuario = :idUsuario")
    List<Carrinho> findItemsById_usuario(Long idUsuario);
}
