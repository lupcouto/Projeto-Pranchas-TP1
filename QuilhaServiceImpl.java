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
        List<Quilha> listaQuilhas = quilhaRepository.listAll();
        if (listaQuilhas.isEmpty()) {
            throw ValidationException.of("Lista de Quilhas", "Nenhuma quilha cadastrada");
        }

        return listaQuilhas;
    }

    @Override
    public List<Quilha> findByTipoQuilha(TipoQuilha tipoQuilha) {
        if (tipoQuilha == null) {
            throw ValidationException.of("tipoQuilha", "Tipo quilha é obrigatório");
        }

        List<Quilha> listaQuilhas = quilhaRepository.findByTipoQuilha(tipoQuilha);
        if (listaQuilhas.isEmpty()) {
            throw ValidationException.of("tipoQuilha", "Nenhuma quilha encontrada para o tipo informado");
        }

        return listaQuilhas;
    }

    @Override
    public Quilha findById(Long id) {
        if (id == null || id <= 0) {
            throw ValidationException.of("id", "id inválido");
        }

        Quilha quilha = quilhaRepository.findById(id);
        if (quilha == null) {
            throw ValidationException.of("id", "Quilha não encontrada");
        }

        return quilha;
    }

    @Override
    @Transactional
    public Quilha create(QuilhaDTO dto) {
        if (dto == null || dto.descricaoQuilha() == null || dto.descricaoQuilha().isBlank()) {
            throw ValidationException.of("descricaoQuilha", "Descrição da quilha é obrigatória");
        }

        if (dto.idTipoQuilha() == null || dto.idTipoQuilha() <= 0) {
            throw ValidationException.of("idTipoQuilha", "id do tipo de quilha inválido");
        }

        TipoQuilha tipoQuilha = tipoQuilhaRepository.findById(dto.idTipoQuilha());
        if (tipoQuilha == null) {
            throw ValidationException.of("idTipoQuilha", "Tipo quilha não encontrada");
        }

        Quilha quilha = new Quilha();
        quilha.setDescricaoQuilha(dto.descricaoQuilha());  
        quilha.setTipoQuilha(tipoQuilha);

        quilhaRepository.persist(quilha);

        return quilha;
    }

    @Override
    @Transactional
    public void update(Long id, QuilhaDTO dto) {
        if (id == null || id <= 0) {
            throw ValidationException.of("id", "id inválido");
        }

        Quilha quilha = quilhaRepository.findById(id);
        if (quilha == null) {
            throw ValidationException.of("id", "Quilha não encontrada");
        }

        if (dto == null || dto.descricaoQuilha() == null || dto.descricaoQuilha().isBlank()) {
            throw ValidationException.of("descricaoQuilha", "Descrição da quilha é obrigatória");
        }

        if (dto.idTipoQuilha() == null || dto.idTipoQuilha() <= 0) {
            throw ValidationException.of("idTipoQuilha", "id do tipo de quilha inválido");
        }

        TipoQuilha tipoQuilha = tipoQuilhaRepository.findById(dto.idTipoQuilha());
        if (tipoQuilha == null) {
            throw ValidationException.of("idTipoQuilha", "Tipo de quilha não encontrada");
        }
        
        quilha.setDescricaoQuilha(dto.descricaoQuilha());
        quilha.setTipoQuilha(tipoQuilha);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (id == null || id <= 0) {
            throw ValidationException.of("id", "id inválido");
        }

        Quilha quilha = quilhaRepository.findById(id);
        if (quilha == null) {
            throw ValidationException.of("id", "Quilha não encontrada");
        }

        quilhaRepository.delete(quilha);
    }
    
}
