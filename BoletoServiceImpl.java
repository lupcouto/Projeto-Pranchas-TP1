package br.unitins.topicos1.prancha.service;
import java.util.List;
import java.util.stream.Collectors;
import br.unitins.topicos1.prancha.dto.BoletoDTO;
import br.unitins.topicos1.prancha.dto.BoletoResponseDTO;
import br.unitins.topicos1.prancha.exception.ValidationException;
import br.unitins.topicos1.prancha.model.Boleto;
import br.unitins.topicos1.prancha.repository.BoletoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@ApplicationScoped
public class BoletoServiceImpl implements BoletoService {

    @Inject
    BoletoRepository boletoRepository;

    @Override
    public List<BoletoResponseDTO> findAll() {
    List<Boleto> listaBoletos = boletoRepository.listAll();

    return listaBoletos.stream()
        .map(BoletoResponseDTO::new)
        .collect(Collectors.toList());
    }

    @Override
    public List<BoletoResponseDTO> findByCodigoBarras(String codigoBarras) {
    List<Boleto> lista = boletoRepository.findByCodigoBarras(codigoBarras);

    return lista.stream()
        .map(BoletoResponseDTO::new)
        .collect(Collectors.toList());
    }


    @Override
    public BoletoResponseDTO findById(Long id) {
        if (id == null || id <= 0) {
            throw ValidationException.of("id", "id inválido");
        }

        Boleto boletoEntity = boletoRepository.findById(id);
        if (boletoEntity == null) {
            throw ValidationException.of("id", "Boleto não encontrado");
        }

        return new BoletoResponseDTO(boletoEntity);
    }

    @Override
    @Transactional
    public BoletoResponseDTO create(@Valid BoletoDTO dto) {
        if (dto == null) {
            throw ValidationException.of("dto", "Dados do boleto são obrigatórios");
        }

        Boleto boletoEntity = new Boleto();
        boletoEntity.setCodigoBarras(dto.codigoBarras());
        boletoEntity.setDataVencimento(dto.dataVencimento());

        boletoEntity.setDataPagamento(null);
        boletoEntity.setStatusPagamento(null);

        boletoRepository.persist(boletoEntity);

        return new BoletoResponseDTO(boletoEntity);
    }

    @Override
    @Transactional
    public void update(Long id, @Valid BoletoDTO dto) {
        if (id == null || id <= 0) {
            throw ValidationException.of("id", "id inválido");
        }

        if (dto == null) {
            throw ValidationException.of("dto", "Dados do boleto são obrigatórios");
        }

        Boleto boletoEntity = boletoRepository.findById(id);
        if (boletoEntity == null) {
            throw ValidationException.of("id", "Cartão não encontrado");
        }

        boletoEntity.setCodigoBarras(dto.codigoBarras());
        boletoEntity.setDataVencimento(dto.dataVencimento());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (id == null || id <= 0) {
            throw ValidationException.of("id", "id inválido");
        }

        Boleto boletoEntity = boletoRepository.findById(id);

        if (boletoEntity == null) {
            throw ValidationException.of("id", "Boleto não encontrado");
        }

        boletoRepository.delete(boletoEntity);
    }
    
}
