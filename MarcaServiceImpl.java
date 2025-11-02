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
        return marcaRepository.listAll();
    }

    @Override
    public List<Marca> findByNome(String nome) {
        return marcaRepository.findByNome(nome);
    }

    @Override
    public Marca findById(Long id) {
        Marca marca = marcaRepository.findById(id);
        if (marca == null) throw ValidationException.of("id", "Marca não encontrada");
        return marca;
    }

    @Override
    @Transactional
    public Marca create(MarcaDTO dto) {
        Marca marca = new Marca();
        marca.setNome(dto.nome());
        marca.setPaisOrigem(dto.paisOrigem());

        marcaRepository.persist(marca);

        return marca;
    }

    @Override
    @Transactional
    public void update(Long id, MarcaDTO dto) {
        Marca marca = marcaRepository.findById(id);
        if (marca == null) throw ValidationException.of("id", "Marca não encontrada");

        marca.setNome(dto.nome());
        marca.setPaisOrigem(dto.paisOrigem());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Marca marca = marcaRepository.findById(id);
        if (marca == null) throw ValidationException.of("id", "Marca não encontrada");
        marcaRepository.delete(marca);
    }
    
}
