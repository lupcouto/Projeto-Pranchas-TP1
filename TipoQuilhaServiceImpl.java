package br.unitins.topicos1.prancha.service;
import java.util.List;
import br.unitins.topicos1.prancha.dto.TipoQuilhaDTO;
import br.unitins.topicos1.prancha.exception.ValidationException;
import br.unitins.topicos1.prancha.model.TipoQuilha;
import br.unitins.topicos1.prancha.repository.TipoQuilhaRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TipoQuilhaServiceImpl implements TipoQuilhaService {

    @Inject
    TipoQuilhaRepository tipoQuilhaRepository;

    @Override
    public List<TipoQuilha> findAll() {
        return tipoQuilhaRepository.listAll();
    }

    @Override
    public List<TipoQuilha> findByNome(String nome) {
        return tipoQuilhaRepository.findByNome(nome);
    }

    @Override
    public TipoQuilha findById(Long id) {
        TipoQuilha tipoQuilha = tipoQuilhaRepository.findById(id);
        if (tipoQuilha == null) throw ValidationException.of("id", "Tipo Quilha não encontrado");
        return tipoQuilha;
    }

    @Override
    @Transactional
    public TipoQuilha create(TipoQuilhaDTO dto) {
        TipoQuilha tipoQuilha = new TipoQuilha();
        tipoQuilha.setNome(dto.nome());

        tipoQuilhaRepository.persist(tipoQuilha);

        return tipoQuilha;
    }
 
    @Override
    @Transactional
    public void update(Long id, TipoQuilhaDTO dto) {
        TipoQuilha tipoQuilha = tipoQuilhaRepository.findById(id);
        if (tipoQuilha == null) throw ValidationException.of("id", "Tipo Quilha não encontrado");
        tipoQuilha.setNome(dto.nome());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        TipoQuilha tipoQuilha = tipoQuilhaRepository.findById(id);
        if (tipoQuilha == null) throw ValidationException.of("id", "Tipo Quilha não encontrado");
        tipoQuilhaRepository.delete(tipoQuilha);
    }
    
}
