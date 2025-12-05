package br.unitins.topicos1.prancha.service;
import java.util.List;
import br.unitins.topicos1.prancha.dto.EnderecoDTO;
import br.unitins.topicos1.prancha.exception.ValidationException;
import br.unitins.topicos1.prancha.model.Endereco;
import br.unitins.topicos1.prancha.repository.EnderecoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@ApplicationScoped
public class EnderecoServiceImpl implements EnderecoService {

    @Inject
    EnderecoRepository enderecoRepository;

    @Override
    public List<Endereco> findAll() {
        List<Endereco> listaEnderecos = enderecoRepository.listAll();
        if (listaEnderecos.isEmpty()) {
            throw ValidationException.of("Lista de Endereços", "Nenhum endereço cadastrado");
        }

        return listaEnderecos;
    }

    @Override
    public List<Endereco> findByCep(String cep) {
        if (cep == null || cep.isBlank()) {
            throw ValidationException.of("cep", "O CEP é obrigatório");
        }

        List<Endereco> listaEnderecos = enderecoRepository.findByCep(cep);
        if (listaEnderecos.isEmpty()) {
            throw ValidationException.of("cep", "Nenhum endereço encontrado para o CEP informado");
        }

        return listaEnderecos;
    }

    @Override
    public Endereco findById(Long id) {
        if (id == null || id <= 0) {
            throw ValidationException.of("id", "id inválido");
        }

        Endereco endereco = enderecoRepository.findById(id);
        if (endereco == null) {
            throw ValidationException.of("id", "Endereço não encontrado");
        }

        return endereco;
    }

    @Override
    @Transactional
    public Endereco create(@Valid EnderecoDTO dto) {
        if (dto == null) {
            throw ValidationException.of("endereco", "Dados do endereço são obrigatórios");
        }

        if (dto.cidade() == null || dto.cidade().isBlank()) {
            throw ValidationException.of("cidade", "A cidade é obrigatória");
        }

        if (dto.estado() == null || dto.estado().isBlank()) {
            throw ValidationException.of("estado", "O estado é obrigatório");
        }

        if (dto.cep() == null || dto.cep().isBlank()) {
            throw ValidationException.of("cep", "O CEP é obrigatório");
        }

        Endereco endereco = new Endereco();
        endereco.setCidade(dto.cidade());
        endereco.setEstado(dto.estado());
        endereco.setCep(dto.cep());

        enderecoRepository.persist(endereco);

        return endereco;
    }

    @Override
    @Transactional
    public void update(Long id, @Valid EnderecoDTO dto) {
        if (id == null || id <= 0) {
            throw ValidationException.of("id", "id inválido");
        }

        Endereco endereco = enderecoRepository.findById(id);
        if (endereco == null) {
            throw ValidationException.of("id", "Endereço não encontrado");
        }

        endereco.setCidade(dto.cidade());
        endereco.setEstado(dto.estado());
        endereco.setCep(dto.cep());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (id == null || id <= 0) {
            throw ValidationException.of("id", "id inválido");
        }

        Endereco endereco = enderecoRepository.findById(id);
        if (endereco == null) {
            throw ValidationException.of("id", "Endereço não encontrado");
        }

        enderecoRepository.delete(endereco);
    }
    
}