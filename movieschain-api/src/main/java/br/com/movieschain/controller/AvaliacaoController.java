package br.com.movieschain.controller;

import br.com.movieschain.controller.dto.AvaliacaoResponseDTO;
import br.com.movieschain.model.Avaliacao;
import br.com.movieschain.model.User;
import br.com.movieschain.repository.AvaliacaoRepository;
import br.com.movieschain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/avaliacoes")
public class AvaliacaoController {

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> avaliar(@RequestBody Avaliacao avaliacao,
                                     @AuthenticationPrincipal User userAutenticado) {

        Long idUsuario = userAutenticado.getId();

        Optional<Avaliacao> existente = avaliacaoRepository
                .findByIdUsuarioAndIdFilme(idUsuario, avaliacao.getIdFilme());

        if (existente.isPresent()) {
            return ResponseEntity.badRequest().body("Usuário já avaliou esse filme.");
        }

        avaliacao.setIdUsuario(idUsuario);
        Avaliacao nova = avaliacaoRepository.save(avaliacao);
        return ResponseEntity.ok(nova);
    }

    @GetMapping("/{filmeId}")
    public ResponseEntity<List<AvaliacaoResponseDTO>> listarPorFilme(@PathVariable Long filmeId) {
        List<Avaliacao> avaliacoes = avaliacaoRepository.findAllByIdFilme(filmeId);

        List<AvaliacaoResponseDTO> resposta = avaliacoes.stream().map(av -> {
            User user = userRepository.findById(av.getIdUsuario()).orElse(null);
            String nome = (user != null) ? user.getNome() : "Desconhecido";
            return new AvaliacaoResponseDTO(nome, av.getNota(), av.getComentario(), av.getTxHash(), av.getDataCriacao());
        }).toList();

        return ResponseEntity.ok(resposta);
    }

}
