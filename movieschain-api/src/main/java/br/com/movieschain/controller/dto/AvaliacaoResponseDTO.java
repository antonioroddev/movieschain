package br.com.movieschain.controller.dto;

import java.time.LocalDateTime;

public class AvaliacaoResponseDTO {
    private String nomeUsuario;
    private int nota;
    private String comentario;
    private String txHash;
    private LocalDateTime dataCriacao;

    public AvaliacaoResponseDTO(String nomeUsuario, int nota, String comentario, String txHash, LocalDateTime dataCriacao) {
        this.nomeUsuario = nomeUsuario;
        this.nota = nota;
        this.comentario = comentario;
        this.txHash = txHash;
        this.dataCriacao = dataCriacao;
    }

    public String getNomeUsuario() { return nomeUsuario; }
    public void setNomeUsuario(String nomeUsuario) { this.nomeUsuario = nomeUsuario; }

    public int getNota() { return nota; }
    public void setNota(int nota) { this.nota = nota; }

    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }

    public String getTxHash() { return txHash; }
    public void setTxHash(String txHash) { this.txHash = txHash; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
}
