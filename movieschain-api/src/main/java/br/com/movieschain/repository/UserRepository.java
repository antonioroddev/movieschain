package br.com.movieschain.repository;

import br.com.movieschain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByCpf(String cpf);
    boolean existsByCpf(String cpf);
    Optional<User> findByWallet(String wallet);

    boolean existsByWallet(String wallet);
}
