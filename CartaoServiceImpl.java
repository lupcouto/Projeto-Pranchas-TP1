package br.unitins.topicos1.prancha.service;
import java.util.List;
import java.util.stream.Collectors;
import br.unitins.topicos1.prancha.dto.CartaoDTO;
import br.unitins.topicos1.prancha.dto.CartaoResponseDTO;
import br.unitins.topicos1.prancha.exception.ValidationException;
import br.unitins.topicos1.prancha.model.Cartao;
import br.unitins.topicos1.prancha.repository.CartaoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@ApplicationScoped
public class CartaoServiceImpl implements CartaoService {

    @Inject
    CartaoRepository cartaoRepository;

    @Override
    public List<CartaoResponseDTO> findAll() {
        List<Cartao> listaCartoes = cartaoRepository.listAll();
        if (listaCartoes.isEmpty()) {
            throw ValidationException.of("listaCartoes", "Nenhum cartão cadastrado");
        }

        return listaCartoes.stream().map(CartaoResponseDTO::new).collect(Collectors.toList());
    }

    @Override
    public List<CartaoResponseDTO> findByNumeroCartao(String numeroCartao) {
        if (numeroCartao == null || numeroCartao.isBlank()) {
            throw ValidationException.of("numeroCartao", "Número do cartão é obrigatório");
        }

        List<Cartao> listaCartoes = cartaoRepository.findByNumeroCartao(numeroCartao);
        if (listaCartoes.isEmpty()) {
            throw ValidationException.of("numeroCartao", "Nenhum cartão encontrado com esse número");
        }

        return listaCartoes.stream().map(CartaoResponseDTO::new).collect(Collectors.toList());
    }

    @Override
    public CartaoResponseDTO findById(Long id) {
        if (id == null || id <= 0) {
            throw ValidationException.of("id", "id inválido");
        }

        Cartao cartaoEntity = cartaoRepository.findById(id);
        if (cartaoEntity == null) {
            throw ValidationException.of("id", "Cartão não encontrado");
        }

        return new CartaoResponseDTO(cartaoEntity);
    }

    @Override
    @Transactional
    public CartaoResponseDTO create(@Valid CartaoDTO dto) {
        if (dto == null) {
            throw ValidationException.of("dto", "Dados do cartão são obrigatórios");
        }

        Cartao cartaoEntity = new Cartao();
        cartaoEntity.setNumeroCartao(dto.numeroCartao());
        cartaoEntity.setNomeTitular(dto.nomeTitular());
        cartaoEntity.setDataVencimento(dto.dataVencimento());

        cartaoEntity.setDataPagamento(null);
        cartaoEntity.setStatusPagamento(null);

        cartaoRepository.persist(cartaoEntity);

        return new CartaoResponseDTO(cartaoEntity);
    }

    @Override
    @Transactional
    public void update(Long id, @Valid CartaoDTO dto) {
        if (id == null || id <= 0) {
            throw ValidationException.of("id", "id inválido");
        }

        if (dto == null) {
            throw ValidationException.of("dto", "Dados do cartão são obrigatórios");
        }

        Cartao cartaoEntity = cartaoRepository.findById(id);
        if (cartaoEntity == null) {
            throw ValidationException.of("id", "Cartão não encontrado");
        }

        cartaoEntity.setNumeroCartao(dto.numeroCartao());
        cartaoEntity.setNomeTitular(dto.nomeTitular());
        cartaoEntity.setDataVencimento(dto.dataVencimento());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (id == null || id <= 0) {
            throw ValidationException.of("id", "id inválido");
        }

        Cartao cartaoEntity = cartaoRepository.findById(id);

        if (cartaoEntity == null) {
            throw ValidationException.of("id", "Cartão não encontrado");
        }

        cartaoRepository.delete(cartaoEntity);
    }
    
}