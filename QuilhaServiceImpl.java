package br.unitins.topicos1.prancha.service;
import java.util.List;
import br.unitins.topicos1.prancha.dto.QuilhaDTO;
import br.unitins.topicos1.prancha.exception.ValidationException;
import br.unitins.topicos1.prancha.model.Quilha;
import br.unitins.topicos1.prancha.model.TipoQuilha;
import br.unitins.topicos1.prancha.repository.QuilhaRepository;
import br.unitins.topicos1.prancha.repository.TipoQuilhaRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class QuilhaServiceImpl implements QuilhaService {

    @Inject
    QuilhaRepository quilhaRepository;

    @Inject
    TipoQuilhaRepository tipoQuilhaRepository;

    @Override
    public List<Quilha> findAll() {
        return quilhaRepository.listAll();
    }

    @Override
    public List<Quilha> findByTipoQuilha(TipoQuilha tipoQuilha) {
        return quilhaRepository.findByTipoQuilha(tipoQuilha);
    }

    @Override
    public Quilha findById(Long id) {
        Quilha quilha = quilhaRepository.findById(id);
        if (quilha == null) throw ValidationException.of("id", "Quilha não encontrada");
        return quilha;
    }

    @Override
    @Transactional
    public Quilha create(QuilhaDTO dto) {

        TipoQuilha tipoQuilha = tipoQuilhaRepository.findById(dto.idTipoQuilha());
        if (tipoQuilha == null) throw ValidationException.of("id", "Tipo quilha não encontrada");

        Quilha quilha = new Quilha();
        quilha.setDescricaoQuilha(dto.descricaoQuilha());  
        quilha.setTipoQuilha(tipoQuilha);

        quilhaRepository.persist(quilha);

        return quilha;
    }

    @Override
    @Transactional
    public void update(Long id, QuilhaDTO dto) {
        Quilha quilha = quilhaRepository.findById(id);
        if (quilha == null) throw ValidationException.of("id", "Quilha não encontrada");

        TipoQuilha tipoQuilha = tipoQuilhaRepository.findById(dto.idTipoQuilha());
        if (tipoQuilha == null) throw ValidationException.of("id", "Tipo quilha não encontrada");
        
        quilha.setDescricaoQuilha(dto.descricaoQuilha());
        quilha.setTipoQuilha(tipoQuilha);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Quilha quilha = quilhaRepository.findById(id);
        if (quilha == null) throw ValidationException.of("id", "Quilha não encontrada");

        quilhaRepository.delete(quilha);
    }
    
}
