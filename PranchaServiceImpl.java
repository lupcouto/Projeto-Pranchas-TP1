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
        List<Prancha> listaPranchas = repository.listAll();
        if (listaPranchas.isEmpty()) {
            throw ValidationException.of("Lista de Pranchas", "Nenhuma prancha cadastrada");
        }

        return listaPranchas;
    }

    @Override
    public List<Prancha> findByTipoPrancha(TipoPrancha tipoPrancha) {
        if (tipoPrancha == null) {
            throw ValidationException.of("tipoPrancha", "TipoPrancha é obrigatório");
        }

        List<Prancha> listaPranchas = repository.findByTipoPrancha(tipoPrancha);
        if (listaPranchas.isEmpty()) {
            throw ValidationException.of("tipoPrancha", "Nenhuma prancha encontrada para o tipo informado");
        }

        return listaPranchas;
    }

    @Override
    public Prancha findById(Long id) {
        if (id == null || id <= 0) {
            throw ValidationException.of("id", "id inválido");
        }

        Prancha prancha = repository.findById(id);
        if (prancha == null) {
            throw ValidationException.of("id", "Prancha não encontrada");
        }

        return prancha;
    }

    @Override
    @Transactional
    public Prancha create(PranchaDTO dto) {
        if (dto == null) {
            throw ValidationException.of("dto", "Dados da prancha são obrigatórios");
        }

        Modelo modelo = modeloRepository.findById(dto.idModelo());
        if (modelo == null) {
            throw ValidationException.of("idModelo", "Modelo não encontrado");
        }

        Quilha quilha = quilhaRepository.findById(dto.idQuilha());
        if (quilha == null) {
            throw ValidationException.of("idQuilha", "Quilha não encontrada");
        }

        Fornecedor fornecedor = fornecedorRepository.findById(dto.idFornecedor());
        if (fornecedor == null) {
            throw ValidationException.of("idFornecedor", "Fornecedor não encontrado");
        }

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
        if (id == null || id <= 0) {
            throw ValidationException.of("id", "id inválido");
        }
        if (dto == null) {
            throw ValidationException.of("dto", "Dados da prancha são obrigatórios");
        }

        Prancha prancha = repository.findById(id);
        if (prancha == null) {
            throw ValidationException.of("id", "Prancha não encontrada");
        } 
        
        Modelo modelo = modeloRepository.findById(dto.idModelo());
        if (modelo == null) {
            throw ValidationException.of("idModelo", "Modelo não encontrado");
        }

        Quilha quilha = quilhaRepository.findById(dto.idQuilha());
        if (quilha == null) {
            throw ValidationException.of("idQuilha", "Quilha não encontrada");
        } 

        Fornecedor fornecedor = fornecedorRepository.findById(dto.idFornecedor());
        if (fornecedor == null) {
            throw ValidationException.of("idFornecedor", "Fornecedor não encontrado");
        }

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
    @Transactional
    public void delete(Long id) {
        if (id == null || id <= 0) {
            throw ValidationException.of("id", "id inválido");
        }

        Prancha prancha = repository.findById(id);
        if (prancha == null) {
            throw ValidationException.of("id", "Prancha não encontrada");
        }

        repository.delete(prancha);
    }
    
}
