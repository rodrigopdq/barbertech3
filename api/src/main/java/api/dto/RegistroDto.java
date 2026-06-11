package api.dto;

import api.model.UsuarioFuncao;

public record RegistroDto(String login, String senha, UsuarioFuncao funcao) {
}