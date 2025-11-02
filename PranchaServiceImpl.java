package br.unitins.topicos1.prancha.service;
import java.util.List;
import br.unitins.topicos1.prancha.dto.PranchaDTO;
import br.unitins.topicos1.prancha.exception.ValidationException;
import br.unitins.topicos1.prancha.model.Fornecedor;
import br.unitins.topicos1.prancha.model.Modelo;
import br.unitins.topicos1.prancha.model.Prancha;
import br.unitins.topicos1.prancha.model.Quilha;
import br.unitins.topicos1.prancha.model.TipoPrancha;
import br.unitins.topicos1.prancha.repository.FornecedorRepository;
import br.unitins.topicos1.prancha.repository.ModeloRepository;
import br.unitins.topicos1.prancha.repository.PranchaRepository;
import br.unitins.topicos1.prancha.repository.QuilhaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class PranchaServiceImpl implements PranchaService {

    @Inject
    PranchaRepository repository;

    @Inject
    ModeloRepository modeloRepository;

    @Inject
    FornecedorRepository fornecedorRepository;

    @Inject
    QuilhaRepository quilhaRepository;

    @Override
    public List<Prancha> findAll() {
        return repository.listAll();
    }

    @Override
    public List<Prancha> findByTipoPrancha(TipoPrancha tipoPrancha) {
        return repository.findByTipoPrancha(tipoPrancha);
    }

    @Override
    public Prancha findById(Long id) {
        Prancha prancha = repository.findById(id);
        if (prancha == null) throw ValidationException.of("id", "Prancha não encontrada");
        return prancha;
    }

    @Override
    @Transactional
    public Prancha create(PranchaDTO dto) {

        Modelo modelo = modeloRepository.findById(dto.idModelo());
        if (modelo == null) throw ValidationException.of("id", "Modelo não encontrado");

        Quilha quilha = quilhaRepository.findById(dto.idQuilha());
        if (quilha == null) throw ValidationException.of("id", "Quilha não encontrada");

        Fornecedor fornecedor = fornecedorRepository.findById(dto.idFornecedor());
        if (fornecedor == null) throw ValidationException.of("id", "Fornecedor não encontrado");

        Prancha prancha = new Prancha();
        prancha.setTamanho(dto.tamanho());
        prancha.setValor(dto.valor());
        prancha.setTipoPrancha(dto.tipoPrancha());
        prancha.setHabilidade(dto.habilidade());
        prancha.setModelo(modelo);
        prancha.setQuilhas(List.of(quilha));
        prancha.setFornecedores(List.of(fornecedor));
        prancha.setTipoPrancha(dto.tipoPrancha());
        prancha.setHabilidade(dto.habilidade());

        repository.persist(prancha);

        return prancha;
    }

    @Override
    @Transactional
    public void update(Long id, PranchaDTO dto) {
        Prancha prancha = repository.findById(id);
        if (prancha == null) throw ValidationException.of("id", "Prancha não encontrada");
        
        Modelo modelo = modeloRepository.findById(dto.idModelo());
        if (modelo == null) throw ValidationException.of("id", "Modelo não encontrado");

        Quilha quilha = quilhaRepository.findById(dto.idQuilha());
        if (quilha == null) throw ValidationException.of("id", "Quilha não encontrada");

        Fornecedor fornecedor = fornecedorRepository.findById(dto.idFornecedor());
        if (fornecedor == null) throw ValidationException.of("id", "Fornecedor não encontrado");

        prancha.setTamanho(dto.tamanho());
        prancha.setValor(dto.valor());
        prancha.setTipoPrancha(dto.tipoPrancha());
        prancha.setHabilidade(dto.habilidade());
        prancha.setModelo(modelo);
        prancha.setQuilhas(List.of(quilha));
        prancha.setFornecedores(List.of(fornecedor));
        prancha.setTipoPrancha(dto.tipoPrancha());
        prancha.setHabilidade(dto.habilidade());
    }
 
    @Override
    public void delete(Long id) {
        Prancha prancha = repository.findById(id);
        if (prancha == null) throw ValidationException.of("id", "Prancha não encontrada");
        repository.delete(prancha);
    }
    
}
