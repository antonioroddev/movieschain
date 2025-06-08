package br.com.movieschain.controller.dto;

public class UserMeDTO {
    private String cpf;
    private String wallet;

    public UserMeDTO(String cpf, String wallet) {
        this.cpf = cpf;
        this.wallet = wallet;
    }

    public String getCpf() {
        return cpf;
    }

    public String getWallet() {
        return wallet;
    }
}
