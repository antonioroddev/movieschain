package br.com.movieschain.controller;

import br.com.movieschain.controller.dto.FilmeListagemDTO;
import br.com.movieschain.model.Avaliacao;
import br.com.movieschain.model.Filme;
import br.com.movieschain.model.User;
import br.com.movieschain.repository.AvaliacaoRepository;
import br.com.movieschain.repository.FilmeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/filmes")
public class FilmeController {

    @Autowired
    private FilmeRepository filmeRepository;
    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    @GetMapping
    public ResponseEntity<List<FilmeListagemDTO>> listarFilmes(@AuthenticationPrincipal User user) {
        List<Filme> filmes = filmeRepository.findAll();

        List<FilmeListagemDTO> resposta = filmes.stream().map(filme -> {
            Optional<Avaliacao> avaliacaoUsuario = avaliacaoRepository.findByIdUsuarioAndIdFilme(user.getId(), filme.getId());
            Integer notaUsuario = (avaliacaoUsuario.isPresent() ) ? avaliacaoUsuario.get().getNota() : null;

            List<Avaliacao> todasAvaliacoes = avaliacaoRepository.findAllByIdFilme(filme.getId());
            var mediaNotas = 0.0;
            if (!todasAvaliacoes.isEmpty()) {
                mediaNotas = todasAvaliacoes.stream()
                        .mapToInt(Avaliacao::getNota)
                        .average()
                        .orElse(0.0);
            }

            return new FilmeListagemDTO(
                    filme.getId(),
                    filme.getTitulo(),
                    filme.getDescricao(),
                    filme.getImagemUrl(),
                    notaUsuario,
                    mediaNotas
            );
        }).toList();

        return ResponseEntity.ok(resposta);
    }


    @PostMapping
    public ResponseEntity<Filme> cadastrarFilme(@RequestBody Filme filme) {
        Filme novo = filmeRepository.save(filme);
        return ResponseEntity.ok(novo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FilmeListagemDTO> getById(@PathVariable Long id, @AuthenticationPrincipal User user) {
        Filme filme = filmeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Filme não encontrado"));

        Optional<Avaliacao> avaliacaoUsuario = avaliacaoRepository.findByIdUsuarioAndIdFilme(user.getId(), filme.getId());
        Integer notaUsuario = (avaliacaoUsuario.isPresent()) ? avaliacaoUsuario.get().getNota() : null;

        List<Avaliacao> todasAvaliacoes = avaliacaoRepository.findAllByIdFilme(filme.getId());
        var mediaNotas = 0.0;
        if (!todasAvaliacoes.isEmpty()) {
            mediaNotas = todasAvaliacoes.stream()
                    .mapToInt(Avaliacao::getNota)
                    .average()
                    .orElse(0.0);
        }

        String txHash = null;
        if (avaliacaoUsuario.isPresent()) {
            txHash = avaliacaoUsuario.get().getTxHash();
        }

        FilmeListagemDTO resposta = new FilmeListagemDTO(
                filme.getId(),
                filme.getTitulo(),
                filme.getDescricao(),
                filme.getImagemUrl(),
                notaUsuario,
                mediaNotas,
                txHash
        );

        return ResponseEntity.ok(resposta);
    }


}
