package br.unitins.topicos1.prancha.service;
import java.util.List;
import br.unitins.topicos1.prancha.dto.MarcaDTO;
import br.unitins.topicos1.prancha.exception.ValidationException;
import br.unitins.topicos1.prancha.model.Marca;
import br.unitins.topicos1.prancha.repository.MarcaRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MarcaServiceImpl implements MarcaService {

    @Inject
    MarcaRepository marcaRepository;

    @Override
    public List<Marca> findAll() {
        List<Marca> listaMarcas = marcaRepository.listAll();
        if (listaMarcas.isEmpty()) {
            throw ValidationException.of("Lista de Marcas", "Nenhuma marca cadastrada");
        }

        return listaMarcas;
    }

    @Override
    public List<Marca> findByNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw ValidationException.of("nome", "Nome é obrigatório");
        }

        List<Marca> listaMarcas = marcaRepository.findByNome(nome);
        if (listaMarcas.isEmpty()) {
            throw ValidationException.of("nome", "Nenhuma marca encontrada para o nome informado");
        }

        return listaMarcas;
    }

    @Override
    public Marca findById(Long id) {
        if (id == null || id <= 0) {
            throw ValidationException.of("id", "id inválido");
        }

        Marca marca = marcaRepository.findById(id);
        if (marca == null) {
            throw ValidationException.of("id", "Marca não encontrada");
        }

        return marca;
    }

    @Override
    @Transactional
    public Marca create(MarcaDTO dto) {
        if (dto == null || dto.nome() == null || dto.nome().isBlank()) {
            throw ValidationException.of("nome", "Nome é obrigatório");
        }

        if (dto.paisOrigem() == null || dto.paisOrigem().isBlank()) {
            throw ValidationException.of("paisOrigem", "País de origem é obrigatório");
        }

        Marca marca = new Marca();
        marca.setNome(dto.nome());
        marca.setPaisOrigem(dto.paisOrigem());

        marcaRepository.persist(marca);

        return marca;
    }

    @Override
    @Transactional
    public void update(Long id, MarcaDTO dto) {
        if (id == null || id <= 0) {
            throw ValidationException.of("id", "id inválido");
        }

        Marca marca = marcaRepository.findById(id);
        if (marca == null) {
            throw ValidationException.of("id", "Marca não encontrada");
        }

        if (dto == null || dto.nome() == null || dto.nome().isBlank()) {
            throw ValidationException.of("nome", "Nome é obrigatório");
        }
        if (dto.paisOrigem() == null || dto.paisOrigem().isBlank()) {
            throw ValidationException.of("paisOrigem", "País de origem é obrigatório");
        }

        marca.setNome(dto.nome());
        marca.setPaisOrigem(dto.paisOrigem());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (id == null || id <= 0) {
            throw ValidationException.of("id", "id inválido");
        }

        Marca marca = marcaRepository.findById(id);
        if (marca == null) {
            throw ValidationException.of("id", "Marca não encontrada");
        }

        marcaRepository.delete(marca);
    }
    
}
