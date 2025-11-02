package br.unitins.topicos1.prancha.dto;
import jakarta.validation.constraints.NotNull;

public record FornecedorDTO(
    
    @NotNull(message = "O cnpj é obrigatório")
    String cnpj
    
) {}
