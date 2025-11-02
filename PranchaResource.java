package br.unitins.topicos1.prancha.resource;
import java.util.List;
import br.unitins.topicos1.prancha.dto.PranchaDTO;
import br.unitins.topicos1.prancha.model.Prancha;
import br.unitins.topicos1.prancha.model.TipoPrancha;
import br.unitins.topicos1.prancha.service.PranchaService;
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

@Path("/pranchas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PranchaResource {

    @Inject
    PranchaService service;

    @GET
    public List<Prancha> getAll() {
        return service.findAll();
    }

    @GET
    @Path("/tipo/{tipoPrancha}")
    public List<Prancha> getByTipoPrancha(@PathParam("tipoPrancha")TipoPrancha tipoPrancha) {
        return service.findByTipoPrancha(tipoPrancha);
    }

    @POST
    public Response incluir(@Valid PranchaDTO dto) {
        var prancha = service.create(dto);
        return Response.status(Response.Status.CREATED).entity(prancha).build();
    }

    @PUT
    @Path("/{id}")
    public void alterar(@PathParam("id") Long id, @Valid PranchaDTO dto) {
        service.update(id, dto);
    }

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") Long id) {
        service.delete(id);
    }
    
}
