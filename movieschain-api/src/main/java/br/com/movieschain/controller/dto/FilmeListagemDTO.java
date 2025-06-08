package br.com.movieschain.controller.dto;

public class FilmeListagemDTO {
    private Long id;
    private String titulo;
    private String descricao;
    private String imagemUrl;
    private Integer notaUsuario;
    private Double mediaNotas;
    private String txHash;

    public FilmeListagemDTO(Long id, String titulo, String descricao, String imagemUrl, Integer notaUsuario, Double mediaNotas) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.imagemUrl = imagemUrl;
        this.notaUsuario = notaUsuario;
        this.mediaNotas = mediaNotas;
    }

    public FilmeListagemDTO(Long id, String titulo, String descricao, String imagemUrl, Integer notaUsuario, Double mediaNotas, String txHash) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.imagemUrl = imagemUrl;
        this.notaUsuario = notaUsuario;
        this.mediaNotas = mediaNotas;
        this.txHash = txHash;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getImagemUrl() { return imagemUrl; }
    public void setImagemUrl(String imagemUrl) { this.imagemUrl = imagemUrl; }

    public Integer getNotaUsuario() { return notaUsuario; }
    public void setNotaUsuario(Integer notaUsuario) { this.notaUsuario = notaUsuario; }

    public Double getMediaNotas() { return mediaNotas; }
    public void setMediaNotas(Double mediaNotas) { this.mediaNotas = mediaNotas; }

    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }
}
