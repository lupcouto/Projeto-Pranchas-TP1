package br.unitins.topicos1.prancha.service;
import java.util.List;
import org.jboss.logging.Logger;
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

    private static final Logger LOG = Logger.getLogger(QuilhaServiceImpl.class);

    @Inject
    QuilhaRepository quilhaRepository;

    @Inject
    TipoQuilhaRepository tipoQuilhaRepository;

    @Override
    public List<Quilha> findAll() {
        LOG.info("Buscando todas as quilhas...");

        List<Quilha> listaQuilhas = quilhaRepository.listAll();

        if (listaQuilhas.isEmpty()) {
            LOG.warn("Nenhuma quilha cadastrada encontrada.");
            throw ValidationException.of("Lista de Quilhas", "Nenhuma quilha cadastrada");
        }

        LOG.info("Quilhas encontradas: " + listaQuilhas.size());
        return listaQuilhas;
    }

    @Override
    public List<Quilha> findByTipoQuilha(TipoQuilha tipoQuilha) {
        LOG.info("Buscando quilhas por tipo...");

        if (tipoQuilha == null) {
            LOG.error("Tipo de quilha nulo informado.");
            throw ValidationException.of("tipoQuilha", "Tipo quilha é obrigatório");
        }

        List<Quilha> listaQuilhas = quilhaRepository.findByTipoQuilha(tipoQuilha);

        if (listaQuilhas.isEmpty()) {
            LOG.warnf("Nenhuma quilha encontrada para o tipo informado: %s", tipoQuilha.getNome());
            throw ValidationException.of("tipoQuilha", "Nenhuma quilha encontrada para o tipo informado");
        }

        LOG.info("Quantidade de quilhas encontradas para o tipo: " + listaQuilhas.size());
        return listaQuilhas;
    }

    @Override
    public Quilha findById(Long id) {
        LOG.infof("Buscando quilha por ID: %d", id);

        if (id == null || id <= 0) {
            LOG.error("ID inválido informado.");
            throw ValidationException.of("id", "id inválido");
        }

        Quilha quilha = quilhaRepository.findById(id);

        if (quilha == null) {
            LOG.warnf("Quilha não encontrada para o ID: %d", id);
            throw ValidationException.of("id", "Quilha não encontrada");
        }

        return quilha;
    }

    @Override
    @Transactional
    public Quilha create(QuilhaDTO dto) {
        LOG.info("Criando nova quilha...");

        if (dto == null || dto.descricaoQuilha() == null || dto.descricaoQuilha().isBlank()) {
            LOG.error("Descrição de quilha inválida.");
            throw ValidationException.of("descricaoQuilha", "Descrição da quilha é obrigatória");
        }

        if (dto.idTipoQuilha() == null || dto.idTipoQuilha() <= 0) {
            LOG.error("ID do tipo de quilha inválido.");
            throw ValidationException.of("idTipoQuilha", "id do tipo de quilha inválido");
        }

        TipoQuilha tipoQuilha = tipoQuilhaRepository.findById(dto.idTipoQuilha());

        if (tipoQuilha == null) {
            LOG.errorf("Tipo de quilha não encontrado. ID: %d", dto.idTipoQuilha());
            throw ValidationException.of("idTipoQuilha", "Tipo quilha não encontrada");
        }

        Quilha quilha = new Quilha();
        quilha.setDescricaoQuilha(dto.descricaoQuilha());
        quilha.setTipoQuilha(tipoQuilha);

        quilhaRepository.persist(quilha);

        LOG.info("Quilha criada com sucesso.");
        return quilha;
    }

    @Override
    @Transactional
    public void update(Long id, QuilhaDTO dto) {
        LOG.infof("Atualizando quilha ID: %d", id);

        if (id == null || id <= 0) {
            LOG.error("ID inválido ao atualizar quilha.");
            throw ValidationException.of("id", "id inválido");
        }

        Quilha quilha = quilhaRepository.findById(id);

        if (quilha == null) {
            LOG.warnf("Quilha não encontrada para atualização. ID: %d", id);
            throw ValidationException.of("id", "Quilha não encontrada");
        }

        if (dto == null || dto.descricaoQuilha() == null || dto.descricaoQuilha().isBlank()) {
            LOG.error("Descrição da quilha inválida na atualização.");
            throw ValidationException.of("descricaoQuilha", "Descrição da quilha é obrigatória");
        }

        if (dto.idTipoQuilha() == null || dto.idTipoQuilha() <= 0) {
            LOG.error("ID de tipo de quilha inválido na atualização.");
            throw ValidationException.of("idTipoQuilha", "id do tipo de quilha inválido");
        }

        TipoQuilha tipoQuilha = tipoQuilhaRepository.findById(dto.idTipoQuilha());

        if (tipoQuilha == null) {
            LOG.errorf("Tipo de quilha não encontrado na atualização. ID: %d", dto.idTipoQuilha());
            throw ValidationException.of("idTipoQuilha", "Tipo de quilha não encontrada");
        }

        quilha.setDescricaoQuilha(dto.descricaoQuilha());
        quilha.setTipoQuilha(tipoQuilha);

        LOG.info("Quilha atualizada com sucesso.");
    }

    @Override
    @Transactional
    public void delete(Long id) {
        LOG.infof("Deletando quilha ID: %d", id);

        if (id == null || id <= 0) {
            LOG.error("ID inválido ao deletar.");
            throw ValidationException.of("id", "id inválido");
        }

        Quilha quilha = quilhaRepository.findById(id);

        if (quilha == null) {
            LOG.warnf("Quilha não encontrada para deleção. ID: %d", id);
            throw ValidationException.of("id", "Quilha não encontrada");
        }

        quilhaRepository.delete(quilha);

        LOG.info("Quilha deletada com sucesso.");
    }
}