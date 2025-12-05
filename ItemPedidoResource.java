package br.unitins.topicos1.prancha.resource;
import java.util.List;
import br.unitins.topicos1.prancha.dto.ItemPedidoDTO;
import br.unitins.topicos1.prancha.model.ItemPedido;
import br.unitins.topicos1.prancha.service.ItemPedidoService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/pedidos/{idPedido}/itens")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ItemPedidoResource {

    @Inject
    ItemPedidoService itemPedidoService;

    @GET
    public List<ItemPedidoDTO> getByPedido(@PathParam("idPedido") Long idPedido) {
        return itemPedidoService.findByPedido(idPedido)
           .stream()
           .map(item -> new ItemPedidoDTO(
               item.getPrancha().getId(),
               item.getQuantidade(),
               item.getPrecoUnit()
           ))
           .toList();
    }

    @POST
    public Response incluir(@PathParam("idPedido") Long idPedido, @Valid ItemPedidoDTO dto) {
        ItemPedido item = itemPedidoService.create(idPedido, dto);
        return Response.status(Response.Status.CREATED)
            .entity(new ItemPedidoDTO(
                item.getPrancha().getId(),
                item.getQuantidade(),
                item.getPrecoUnit()
            ))
            .build();
    }   

    @PUT
    @Path("/{idItem}")
    public Response alterar(@PathParam("idPedido") Long idPedido, @PathParam("idItem") Long idItem, @Valid ItemPedidoDTO dto) {
        itemPedidoService.update(idItem, idPedido, dto);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{idItem}")
    public Response delete(@PathParam("idItem") Long idItem) {
        itemPedidoService.delete(idItem);
        return Response.noContent().build();
    }
}