package br.unitins.topicos1.prancha.resource;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import br.unitins.topicos1.prancha.dto.BoletoDTO;
import br.unitins.topicos1.prancha.dto.BoletoResponseDTO;
import br.unitins.topicos1.prancha.service.BoletoService;
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

@Path("/boletos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BoletoResource {

    @Inject
    BoletoService boletoService;

    @GET
    public List<BoletoResponseDTO> getAll() {
        return boletoService.findAll();
    }

    @GET
    @Path("/codigoBarras/{codigoBarras}")
    public List<BoletoResponseDTO> getByCodigoBarras(@PathParam("codigoBarras") String codigoBarras) {
        return boletoService.findByCodigoBarras(codigoBarras);
    }

    @POST
    public Response incluir(@Valid BoletoDTO dto) {
        BoletoResponseDTO boleto = boletoService.create(dto);
        return Response.status(Response.Status.CREATED).entity(boleto).build();
    }

    @PUT
    @Path("/{id}")
    public Response alterar(@PathParam("id") Long id, @Valid BoletoDTO dto) {
        boletoService.update(id, dto);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        boletoService.delete(id);
        return Response.noContent().build();
    }
    
}