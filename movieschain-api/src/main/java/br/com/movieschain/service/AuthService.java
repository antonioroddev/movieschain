package br.com.movieschain.service;

import br.com.movieschain.controller.dto.LoginResponseDTO;
import br.com.movieschain.controller.dto.RegisterWalletRequestDTO;
import br.com.movieschain.model.User;
import br.com.movieschain.repository.UserRepository;
import br.com.movieschain.service.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;


    public LoginResponseDTO loginWithWallet(User user) {
        String token = jwtUtil.generateToken(user);
        return new LoginResponseDTO(token, user.getNome());
    }

    public LoginResponseDTO registerWithWallet(RegisterWalletRequestDTO request) {
        if (request.getCpf() == null || request.getNome() == null || request.getSenha() == null || request.getWallet() == null) {
            throw new RuntimeException("Todos os campos são obrigatórios.");
        }

        if (userRepository.existsByCpf(request.getCpf())) {
            throw new RuntimeException("CPF já cadastrado.");
        }

        if (userRepository.existsByWallet(request.getWallet().toLowerCase())) {
            throw new RuntimeException("Wallet já cadastrada.");
        }

        User user = new User();
        user.setNome(request.getNome());
        user.setCpf(request.getCpf());
        user.setSenha(passwordEncoder.encode(request.getSenha()));
        user.setWallet(request.getWallet().toLowerCase());

        userRepository.save(user);

        String token = jwtUtil.generateToken(user);

        return new LoginResponseDTO(token, user.getNome());
    }

}
