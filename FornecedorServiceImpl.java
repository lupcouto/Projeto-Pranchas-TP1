package br.unitins.topicos1.prancha.service;
import java.util.List;
import br.unitins.topicos1.prancha.dto.FornecedorDTO;
import br.unitins.topicos1.prancha.exception.ValidationException;
import br.unitins.topicos1.prancha.model.Fornecedor;
import br.unitins.topicos1.prancha.repository.FornecedorRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class FornecedorServiceImpl implements FornecedorService{

    @Inject 
    FornecedorRepository fornecedorRepository;

    @Override
    public List<Fornecedor> findAll() {
        List<Fornecedor> listaFornecedor = fornecedorRepository.listAll();
        if (listaFornecedor.isEmpty()) {
            throw ValidationException.of("Lista de Fornecedores", "Nenhum fornecedor cadastrado");
        } 
        return listaFornecedor;
    }

    @Override
    public List<Fornecedor> findByCnpj(String cnpj) {
        if (cnpj == null || cnpj.isBlank()) {
            throw ValidationException.of("cnpj", "CNPJ é obrigatório");
        }

        List<Fornecedor> listaFornecedor = fornecedorRepository.findByCnpj(cnpj);
        if (listaFornecedor.isEmpty()) {
            throw ValidationException.of("cnpj", "Nenhum fornecedor encontrado para o CNPJ informado");
        }

        return listaFornecedor;
    }

    @Override
    public Fornecedor findById(Long id) {
        if (id == null || id <= 0) {
            throw ValidationException.of("id", "id inválido");
        }

        Fornecedor fornecedor = fornecedorRepository.findById(id);
        if (fornecedor == null) throw ValidationException.of("id", "Fornecedor não encontrado");
        return fornecedor;
    }

    @Override
    @Transactional
    public Fornecedor create(FornecedorDTO dto) {
        if (dto == null || dto.cnpj() == null || dto.cnpj().isBlank()) {
            throw ValidationException.of("cnpj", "CNPJ é obrigatório");
        }

        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setCnpj(dto.cnpj());

        fornecedorRepository.persist(fornecedor);

        return fornecedor;
    }

    @Override
    @Transactional
    public void update(Long id, FornecedorDTO dto) {
        if (id == null || id <= 0) {
            throw ValidationException.of("id", "id inválido");
        }

        Fornecedor fornecedor = fornecedorRepository.findById(id);
        if (fornecedor == null) {
            throw ValidationException.of("id", "Fornecedor não encontrado");
        }

        if (dto == null || dto.cnpj() == null || dto.cnpj().isBlank()) {
            throw ValidationException.of("cnpj", "CNPJ é obrigatório");
        }

        fornecedor.setCnpj(dto.cnpj());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (id == null || id <= 0) {
            throw ValidationException.of("id", "id inválido");
        }

        Fornecedor fornecedor = fornecedorRepository.findById(id);
        if (fornecedor == null) {
            throw ValidationException.of("id", "Fornecedor não encontrado");
        }

        fornecedorRepository.delete(fornecedor);
    }
    
}
