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
        return fornecedorRepository.listAll();
    }

    @Override
    public List<Fornecedor> findByCnpj(String cnpj) {
        return fornecedorRepository.findByCnpj(cnpj);
    }

    @Override
    public Fornecedor findById(Long id) {
        Fornecedor fornecedor = fornecedorRepository.findById(id);
        if (fornecedor == null) throw ValidationException.of("id", "Fornecedor não encontrado");
        return fornecedor;
    }

    @Override
    @Transactional
    public Fornecedor create(FornecedorDTO dto) {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setCnpj(dto.cnpj());

        fornecedorRepository.persist(fornecedor);

        return fornecedor;
    }

    @Override
    @Transactional
    public void update(Long id, FornecedorDTO dto) {
        Fornecedor fornecedor = fornecedorRepository.findById(id);
        if (fornecedor == null) throw ValidationException.of("id", "Fornecedor não encontrado");
        
        fornecedor.setCnpj(dto.cnpj());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Fornecedor fornecedor = fornecedorRepository.findById(id);
        if (fornecedor == null) throw ValidationException.of("id", "Fornecedor não encontrado");
        fornecedorRepository.delete(fornecedor);
    }
    
}
