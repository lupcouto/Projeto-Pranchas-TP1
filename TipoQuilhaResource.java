package br.unitins.topicos1.prancha.resource;
import java.util.List;
import br.unitins.topicos1.prancha.dto.TipoQuilhaDTO;
import br.unitins.topicos1.prancha.model.TipoQuilha;
import br.unitins.topicos1.prancha.service.TipoQuilhaService;
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

@Path("/tipos-quilha")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TipoQuilhaResource {
    
    @Inject
    TipoQuilhaService service;

    @GET
    public List<TipoQuilha> getAll() {
        return service.findAll();
    }

    @GET
    @Path("/nome/{nome}")
    public List<TipoQuilha> getByNome(@PathParam("nome")String nome) {
        return service.findByNome(nome);
    }

    @POST
    public Response incluir(@Valid TipoQuilhaDTO dto) {
        var tipoQuilha = service.create(dto);
        return Response.status(Response.Status.CREATED).entity(tipoQuilha).build();
    }   

    @PUT
    @Path("/{id}")
    public void alterar(@PathParam("id") Long id, @Valid TipoQuilhaDTO dto) {
        service.update(id, dto);
    }

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") Long id) {
        service.delete(id);
    }

}
