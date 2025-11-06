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
        List<TipoQuilha> listaTipoQuilhas = tipoQuilhaRepository.listAll();
        if (listaTipoQuilhas.isEmpty()) {
            throw ValidationException.of("lista", "Nenhum tipo de quilha cadastrado");
        }

        return listaTipoQuilhas;
    }

    @Override
    public List<TipoQuilha> findByNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw ValidationException.of("nome", "Nome é obrigatório");
        }

        List<TipoQuilha> listaTipoQuilhas = tipoQuilhaRepository.findByNome(nome);
        if (listaTipoQuilhas.isEmpty()) {
            throw ValidationException.of("nome", "Nenhum tipo de quilha encontrado com o nome informado");
        }

        return listaTipoQuilhas;
    }

    @Override
    public TipoQuilha findById(Long id) {
        if (id == null || id <= 0) {
            throw ValidationException.of("id", "id inválido");
        }

        TipoQuilha tipoQuilha = tipoQuilhaRepository.findById(id);
        if (tipoQuilha == null) {
            throw ValidationException.of("id", "Tipo Quilha não encontrado");
        }

        return tipoQuilha;
    }

    @Override
    @Transactional
    public TipoQuilha create(TipoQuilhaDTO dto) {
        if (dto == null) {
            throw ValidationException.of("dto", "Dados do tipo quilha são obrigatórios");
        }
        if (dto.nome() == null || dto.nome().isBlank()) {
            throw ValidationException.of("nome", "Nome é obrigatório");
        }

        TipoQuilha tipoQuilha = new TipoQuilha();
        tipoQuilha.setNome(dto.nome());

        tipoQuilhaRepository.persist(tipoQuilha);

        return tipoQuilha;
    }
 
    @Override
    @Transactional
    public void update(Long id, TipoQuilhaDTO dto) {
        if (id == null || id <= 0) {
            throw ValidationException.of("id", "id inválido");
        }

        TipoQuilha tipoQuilha = tipoQuilhaRepository.findById(id);
        if (tipoQuilha == null) {
            throw ValidationException.of("id", "Tipo Quilha não encontrado");
        }

        if (dto == null) {
            throw ValidationException.of("dto", "Dados do tipo quilha são obrigatórios");
        }
        if (dto.nome() == null || dto.nome().isBlank()) {
            throw ValidationException.of("nome", "Nome é obrigatório");
        }

        tipoQuilha.setNome(dto.nome());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (id == null || id <= 0) {
            throw ValidationException.of("id", "id inválido");
        }

        TipoQuilha tipoQuilha = tipoQuilhaRepository.findById(id);
        if (tipoQuilha == null) {
            throw ValidationException.of("id", "Tipo Quilha não encontrado");
        }

        tipoQuilhaRepository.delete(tipoQuilha);
    }
    
}
