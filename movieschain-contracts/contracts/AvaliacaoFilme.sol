// SPDX-License-Identifier: MIT
pragma solidity ^0.8.18;

contract AvaliacaoFilme {
    struct Avaliacao {
        uint8 nota;
        string comentario;
        address autor;
    }

    // filmeId => wallet => Avaliacao
    mapping(uint256 => mapping(address => Avaliacao)) public avaliacoes;

    event Avaliado(uint256 filmeId, address autor, uint8 nota, string comentario);

    function avaliar(uint256 filmeId, uint8 nota, string memory comentario) public {
        require(nota >= 1 && nota <= 5, "Nota invalida");
        require(avaliacoes[filmeId][msg.sender].nota == 0, "Ja avaliou esse filme");

        avaliacoes[filmeId][msg.sender] = Avaliacao(nota, comentario, msg.sender);
        emit Avaliado(filmeId, msg.sender, nota, comentario);
    }

    function getAvaliacao(uint256 filmeId, address autor) public view returns (uint8, string memory) {
        Avaliacao memory a = avaliacoes[filmeId][autor];
        return (a.nota, a.comentario);
    }
}

