package br.unitins.topicos1.prancha.service;
import java.util.List;
import br.unitins.topicos1.prancha.dto.ModeloDTO;
import br.unitins.topicos1.prancha.exception.ValidationException;
import br.unitins.topicos1.prancha.model.Marca;
import br.unitins.topicos1.prancha.model.Modelo;
import br.unitins.topicos1.prancha.repository.MarcaRepository;
import br.unitins.topicos1.prancha.repository.ModeloRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ModeloServiceImpl implements ModeloService {

    @Inject
    ModeloRepository modeloRepository;

    @Inject
    MarcaRepository marcaRepository;

    @Override
    public List<Modelo> findAll() {
        List<Modelo> listaModelos = modeloRepository.listAll();
        if (listaModelos.isEmpty()) {
            throw ValidationException.of("Lista de Modelos", "Nenhum modelo cadastrado");
        }
        return listaModelos;
    }

    @Override
    public List<Modelo> findByNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw ValidationException.of("nome", "Nome é obrigatório");
        }

        List<Modelo> listaModelos = modeloRepository.findByNome(nome);
        if (listaModelos.isEmpty()) {
            throw ValidationException.of("nome", "Nenhum modelo encontrado para o nome informado");
        }

        return listaModelos;
    }

    @Override
    public Modelo findById(Long id) {
        if (id == null || id <= 0) {
            throw ValidationException.of("id", "id inválido");
        }

        Modelo modelo = modeloRepository.findById(id);
        if (modelo == null) {
            throw ValidationException.of("id", "Modelo não encontrado");
        }

        return modelo;
    }

    @Override
    @Transactional
    public Modelo create(ModeloDTO dto) {
        if (dto == null || dto.nome() == null || dto.nome().isBlank()) {
            throw ValidationException.of("nome", "Nome é obrigatório");
        }

        if (dto.idMarca() == null || dto.idMarca() <= 0) {
            throw ValidationException.of("idMarca", "id da marca inválido");
        }

        Marca marca = marcaRepository.findById(dto.idMarca());
        if (marca == null) {
            throw ValidationException.of("idMarca", "Marca não encontrada");
        }

        Modelo modelo = new Modelo();
        modelo.setNome(dto.nome());
        modelo.setMarca(marca);

        modeloRepository.persist(modelo);

        return modelo;
    }

    @Override
    @Transactional
    public void update(Long id, ModeloDTO dto) {
        if (id == null || id <= 0) {
            throw ValidationException.of("id", "id inválido");
        }

        Modelo modelo = modeloRepository.findById(id);
        if (modelo == null) {
            throw ValidationException.of("id", "Modelo não encontrado");
        }

        if (dto == null || dto.nome() == null || dto.nome().isBlank()) {
            throw ValidationException.of("nome", "Nome é obrigatório");
        }

        if (dto.idMarca() == null || dto.idMarca() <= 0) {
            throw ValidationException.of("idMarca", "id da marca inválido");
        }

        Marca marca = marcaRepository.findById(dto.idMarca());
        if (marca == null) {
            throw ValidationException.of("idMarca", "Marca não encontrada");
        }

        modelo.setNome(dto.nome());
        modelo.setMarca(marca);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (id == null || id <= 0) {
            throw ValidationException.of("id", "id inválido");
        }

        Modelo modelo = modeloRepository.findById(id);
        if (modelo == null) {
            throw ValidationException.of("id", "Modelo não encontrado");
        }

        modeloRepository.delete(modelo);
    }
    
}
