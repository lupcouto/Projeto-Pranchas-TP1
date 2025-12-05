package br.unitins.topicos1.prancha.resource;
import java.util.List;
import br.unitins.topicos1.prancha.dto.PedidoDTO;
import br.unitins.topicos1.prancha.dto.PedidoResponseDTO;
import br.unitins.topicos1.prancha.service.PedidoService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/pedidos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PedidoResource {

    @Inject
    PedidoService pedidoService;

    @GET
    public List<PedidoResponseDTO> getAll() {
        return pedidoService.findAll();
    }

    @GET
    @Path("/cliente/{idCliente}")
    public List<PedidoResponseDTO> getByCliente(@PathParam("idCliente") Long idCliente) {
        return pedidoService.findByCliente(idCliente);
    }

    @POST
    public Response incluir(@Valid PedidoDTO dto) {
        var pedido = pedidoService.create(dto);
        return Response.status(Response.Status.CREATED).entity(pedido).build();
    }

    @PUT
    @Path("/{id}")
    public Response alterar(@PathParam("id") Long id, @Valid PedidoDTO dto) {
        pedidoService.update(id, dto);
        return Response.noContent().build();
    }

    @PUT
    @Path("/{id}/pagar")
    public Response pagar(@PathParam("id") Long id) {
        pedidoService.pagar(id);
        return Response.noContent().build(); 
    }

    @PUT
    @Path("/{id}/finalizar")
    @Transactional
    public Response finalizar(@PathParam("id") Long idPedido) {
        pedidoService.finalizar(idPedido);
        return Response.ok("Pedido finalizado com sucesso.").build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        pedidoService.delete(id);
        return Response.noContent().build();
    }
}