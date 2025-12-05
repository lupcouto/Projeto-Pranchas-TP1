package br.unitins.topicos1.prancha.service;
import java.util.List;
import br.unitins.topicos1.prancha.dto.AdministradorDTO;
import br.unitins.topicos1.prancha.exception.ValidationException;
import br.unitins.topicos1.prancha.model.Administrador;
import br.unitins.topicos1.prancha.model.Telefone;
import br.unitins.topicos1.prancha.repository.AdministradorRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@ApplicationScoped
public class AdministradorServiceImpl implements AdministradorService {

    @Inject
    AdministradorRepository administradorRepository;

    @Override
    public List<Administrador> findAll() {
        List<Administrador> listaAdm = administradorRepository.listAll();
        if (listaAdm.isEmpty()) {
            throw ValidationException.of("Lista de Administradores", "Nenhum administrador cadastrado");
        } 
        return listaAdm;
    }

    @Override
    public List<Administrador> findByNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw ValidationException.of("nome", "Nome é obrigatório");
        }

        List<Administrador> listaAdm = administradorRepository.findByNome(nome);
        if (listaAdm.isEmpty()) {
            throw ValidationException.of("nome", "Nenhum administrador encontrado para o nome informado");
        }

        return listaAdm;
    }

    @Override
    public Administrador findById(Long id) {
        if (id == null || id <= 0) {
            throw ValidationException.of("id", "id inválido");
        }

        Administrador administrador = administradorRepository.findById(id);
        if (administrador == null) throw ValidationException.of("id", "Administrador não encontrado");
        return administrador;
    }

    @Override
    @Transactional
    public Administrador create(@Valid AdministradorDTO dto) {
        if (dto.nome() == null || dto.nome().isBlank()) {
            throw ValidationException.of("nome", "Nome é obrigatório");
        }

        if (dto.ddd() == null || dto.ddd().isBlank()) {
            throw ValidationException.of("DDD", "DDD é obrigatório");
        }

        if (dto.numero() == null || dto.numero().isBlank()) {
            throw ValidationException.of("Número", "Número é obrigatório");
        }

        if (dto.cargo() == null || dto.cargo().isBlank()) {
            throw ValidationException.of("CARGO", "Cargo é obrigatório");
        }
        
        if (dto.statusAdm() == null || dto.statusAdm().isBlank()) {
            throw ValidationException.of("Status Adm", "Status adm é obrigatório");
        }

        Telefone telefone = new Telefone();
        telefone.setDdd(dto.ddd());
        telefone.setNumero(dto.numero());

        Administrador administrador = new Administrador();
        administrador.setNome(dto.nome());
        administrador.setTelefone(telefone);
        administrador.setCargo(dto.cargo());
        administrador.setStatusAdm(dto.statusAdm());

        administradorRepository.persist(administrador);

        return administrador;
    }

    @Override
    @Transactional
    public void update(Long id, @Valid AdministradorDTO dto) {
        if (id == null || id <= 0) {
            throw ValidationException.of("id", "id inválido");
        }

        Administrador administrador = administradorRepository.findById(id);
        if (administrador == null) {
            throw ValidationException.of("id", "Administrador não encontrado");
        }

        if (dto.nome() == null || dto.nome().isBlank()) {
            throw ValidationException.of("nome", "Nome é obrigatório");
        }

        if (dto.ddd() == null || dto.ddd().isBlank()) {
            throw ValidationException.of("DDD", "DDD é obrigatório");
        }

        if (dto.numero() == null || dto.numero().isBlank()) {
            throw ValidationException.of("Número", "Número é obrigatório");
        }

        if (dto.cargo() == null || dto.cargo().isBlank()) {
            throw ValidationException.of("Cargo", "Cargo é obrigatório");
        }

        if (dto.statusAdm() == null || dto.statusAdm().isBlank()) {
            throw ValidationException.of("Status Adm", "Status adm é obrigatório");
        }

        administrador.setNome(dto.nome());
        administrador.setCargo(dto.cargo());
        administrador.setStatusAdm(dto.statusAdm());

        Telefone telefone = administrador.getTelefone();
        if (telefone == null) {
            telefone = new Telefone();
            administrador.setTelefone(telefone);
        }

        telefone.setDdd(dto.ddd());
        telefone.setNumero(dto.numero());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (id == null || id <= 0) {
            throw ValidationException.of("id", "id inválido");
        }

        Administrador administrador = administradorRepository.findById(id);
        if (administrador == null) {
            throw ValidationException.of("id", "Administrador não encontrado");
        }

        administradorRepository.delete(administrador);
    }
    
}