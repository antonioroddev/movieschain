package br.com.movieschain.repository;

import br.com.movieschain.model.Avaliacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {
    Optional<Avaliacao> findByIdUsuarioAndIdFilme(Long idUsuario, Long idFilme);
    List<Avaliacao> findAllByIdFilme(Long idFilme);

}