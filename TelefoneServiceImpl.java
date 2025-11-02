package br.unitins.topicos1.prancha.service;
import java.util.List;
import br.unitins.topicos1.prancha.dto.TelefoneDTO;
import br.unitins.topicos1.prancha.exception.ValidationException;
import br.unitins.topicos1.prancha.model.Telefone;
import br.unitins.topicos1.prancha.repository.TelefoneRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class TelefoneServiceImpl implements TelefoneService {

    @Inject
    TelefoneRepository telefoneRepository;

    @Override
    public List<Telefone> findAll() {
        return telefoneRepository.listAll();
    }

    @Override
    public List<Telefone> findByNumero(String numero) {
        return telefoneRepository.findByNumero(numero);
    }

    @Override
    public Telefone findById(Long id) {
        Telefone telefone = telefoneRepository.findById(id);
        if (telefone == null) throw ValidationException.of("id", "Telefone não encontrado");
        return telefone;
    }

    @Override
    @Transactional
    public Telefone create(TelefoneDTO dto) {
        Telefone telefone = new Telefone();
        telefone.setDdd(dto.ddd());
        telefone.setNumero(dto.numero());

        telefoneRepository.persist(telefone);

        return telefone;
    }

    @Override
    @Transactional
    public void update(Long id, TelefoneDTO dto) {
        Telefone telefone = telefoneRepository.findById(id);
        if (telefone == null) throw ValidationException.of("id", "Telefone não encontrado");

        telefone.setDdd(dto.ddd());
        telefone.setNumero(dto.numero());

    }

    @Override
    @Transactional
    public void delete(Long id) {
        Telefone telefone = telefoneRepository.findById(id);
        if (telefone == null) throw ValidationException.of("id", "Telefone não encontrado");
        telefoneRepository.delete(telefone);
    }

}