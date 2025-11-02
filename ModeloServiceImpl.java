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
        return modeloRepository.listAll();
    }

    @Override
    public List<Modelo> findByNome(String nome) {
        return modeloRepository.findByNome(nome);
    }

    @Override
    public Modelo findById(Long id) {
        Modelo modelo = modeloRepository.findById(id);
        if (modelo == null) throw ValidationException.of("id", "Modelo não encontrado");
        return modelo;
    }

    @Override
    @Transactional
    public Modelo create(ModeloDTO dto) {

        Marca marca = marcaRepository.findById(dto.idMarca());
        if (marca == null) throw ValidationException.of("id", "Marca não encontrada");

        Modelo modelo = new Modelo();
        modelo.setNome(dto.nome());
        modelo.setMarca(marca);

        modeloRepository.persist(modelo);

        return modelo;
    }

    @Override
    @Transactional
    public void update(Long id, ModeloDTO dto) {
        Modelo modelo = modeloRepository.findById(id);
        if (modelo == null) throw ValidationException.of("id", "Modelo não encontrado");

        Marca marca = marcaRepository.findById(dto.idMarca());
        if (marca == null) throw ValidationException.of("id", "Marca não encontrada");

        modelo.setNome(dto.nome());
        modelo.setMarca(marca);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Modelo modelo = modeloRepository.findById(id);
        if (modelo == null) throw ValidationException.of("id", "Modelo não encontrado");
        modeloRepository.delete(modelo);
    }
    
}
