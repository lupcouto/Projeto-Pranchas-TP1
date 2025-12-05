package br.unitins.topicos1.prancha.service;
import java.util.List;
import br.unitins.topicos1.prancha.dto.ItemPedidoDTO;
import br.unitins.topicos1.prancha.exception.ValidationException;
import br.unitins.topicos1.prancha.model.ItemPedido;
import br.unitins.topicos1.prancha.model.Pedido;
import br.unitins.topicos1.prancha.model.Prancha;
import br.unitins.topicos1.prancha.repository.ItemPedidoRepository;
import br.unitins.topicos1.prancha.repository.PedidoRepository;
import br.unitins.topicos1.prancha.repository.PranchaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@ApplicationScoped
public class ItemPedidoServiceImpl implements ItemPedidoService {

    @Inject
    ItemPedidoRepository itemPedidoRepository;

    @Inject
    PedidoRepository pedidoRepository;

    @Inject
    PranchaRepository pranchaRepository;

    private Pedido buscarPedido(Long idPedido) {
        if (idPedido == null || idPedido <= 0) {
            throw ValidationException.of("idPedido", "ID de Pedido é obrigatório e válido.");
        }
        Pedido pedido = pedidoRepository.findById(idPedido);
        if (pedido == null) {
            throw ValidationException.of("idPedido", "Pedido não encontrado.");
        }
        return pedido;
    }

    private Prancha buscarPrancha(Long idPrancha) {
        if (idPrancha == null || idPrancha <= 0) {
            throw ValidationException.of("idPrancha", "ID de Prancha é obrigatório e válido.");
        }
        Prancha prancha = pranchaRepository.findById(idPrancha);
        if (prancha == null) {
            throw ValidationException.of("idPrancha", "Prancha não encontrada.");
        }
        return prancha;
    }

    @Override
    public List<ItemPedido> findAll() {
        List<ItemPedido> listaItens = itemPedidoRepository.listAll();
        if (listaItens.isEmpty()) {
            throw ValidationException.of("Lista de Itens", "Nenhum item de pedido cadastrado.");
        }
        return listaItens;
    }

    @Override
    public List<ItemPedido> findByPedido(Long idPedido) {
        if (idPedido == null || idPedido <= 0) {
            throw ValidationException.of("idPedido", "id de Pedido inválido.");
        }

        List<ItemPedido> listaItens = itemPedidoRepository.findByPedido(idPedido); 
        if (listaItens.isEmpty()) {
            throw ValidationException.of("idPedido", "Nenhum item encontrado para o Pedido informado.");
        }
        return listaItens;
    }

    @Override
    public ItemPedido findById(Long id) {
        if (id == null || id <= 0) {
            throw ValidationException.of("id", "id inválido.");
        }

        ItemPedido itemPedido = itemPedidoRepository.findById(id);
        if (itemPedido == null) {
            throw ValidationException.of("id", "Item de Pedido não encontrado.");
        }
        return itemPedido;
    }

    @Override
    @Transactional
    public ItemPedido create(Long idPedido, @Valid ItemPedidoDTO dto) {
        if (dto == null) {
            throw ValidationException.of("dto", "Dados do Item de Pedido são obrigatórios.");
        }

        Pedido pedido = buscarPedido(idPedido);
        Prancha prancha = buscarPrancha(dto.idPrancha());

        ItemPedido itemPedido = new ItemPedido();
        itemPedido.setPedido(pedido);
        itemPedido.setPrancha(prancha);
        itemPedido.setQuantidade(dto.quantidade());
        itemPedido.setPrecoUnit(dto.precoUnit());
        itemPedido.setSubTotal(dto.precoUnit() * dto.quantidade());

        itemPedidoRepository.persist(itemPedido);

        return itemPedido;
    }

    @Override
    @Transactional
    public void update(Long idItem, Long idPedido, @Valid ItemPedidoDTO dto) {
        ItemPedido item = itemPedidoRepository.findById(idItem);
        if (item == null) {
            throw ValidationException.of("id", "Item de Pedido não encontrado.");
        }

        Pedido pedido = buscarPedido(idPedido);
        Prancha prancha = buscarPrancha(dto.idPrancha());

        item.setPedido(pedido);
        item.setPrancha(prancha);
        item.setQuantidade(dto.quantidade());
        item.setPrecoUnit(dto.precoUnit());
        item.setSubTotal(dto.precoUnit() * dto.quantidade());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (id == null || id <= 0) {
            throw ValidationException.of("id", "id inválido.");
        }

        ItemPedido itemPedido = itemPedidoRepository.findById(id);
        if (itemPedido == null) {
            throw ValidationException.of("id", "Item de Pedido não encontrado.");
        }

        itemPedidoRepository.delete(itemPedido);
    }
    
}