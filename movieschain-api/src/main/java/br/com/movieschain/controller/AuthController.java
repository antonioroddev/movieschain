package br.com.movieschain.controller;

import br.com.movieschain.controller.dto.*;
import br.com.movieschain.model.User;
import br.com.movieschain.repository.UserRepository;
import br.com.movieschain.service.AuthService;
import br.com.movieschain.util.WalletUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;


    @GetMapping("/me")
    public ResponseEntity<?> getMe(@AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.status(401).body("Não autenticado");
        }

        return ResponseEntity.ok(new UserMeDTO(user.getCpf(), user.getWallet()));
    }



    @PostMapping("/wallet-login")
    public ResponseEntity<?> walletLogin(@RequestBody WalletLoginRequestDTO request) {
        String walletAddress = request.getWallet();
        String signedMessage = request.getSignedMessage();
        String originalMessage = request.getOriginalMessage();

        if (walletAddress == null || signedMessage == null || originalMessage == null) {
            return ResponseEntity.badRequest().body("Dados inválidos.");
        }

            boolean isValid = WalletUtils.verifySignature(walletAddress, signedMessage, originalMessage);

            if (!isValid) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Assinatura inválida.");
            }

        User user = userRepository.findByWallet(walletAddress.toLowerCase())
                .orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não encontrado para essa carteira.");
        }

        LoginResponseDTO response = authService.loginWithWallet(user);

        return ResponseEntity.ok(response);
    }



    @PostMapping("/register-wallet")
    public ResponseEntity<?> registerWithWallet(@RequestBody RegisterWalletRequestDTO request) {
        try {
            LoginResponseDTO response = authService.registerWithWallet(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
