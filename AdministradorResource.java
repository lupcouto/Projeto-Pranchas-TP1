package br.unitins.topicos1.prancha.resource;
import java.util.List;
import br.unitins.topicos1.prancha.dto.AdministradorDTO;
import br.unitins.topicos1.prancha.model.Administrador;
import br.unitins.topicos1.prancha.service.AdministradorService;
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

@Path("/administradores")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AdministradorResource {

    @Inject
    AdministradorService administradorService;

    @GET
    public List<Administrador> getAll() {
        return administradorService.findAll();
    }

    @GET
    @Path("/nome/{nome}")
    public List<Administrador> getByNome(@PathParam("nome")String nome) {
        return administradorService.findByNome(nome);
    }

    @POST
    public Response incluir(@Valid AdministradorDTO dto) {
        var administrador = administradorService.create(dto);
        return Response.status(Response.Status.CREATED).entity(administrador).build();
    }

    @PUT
    @Path("/{id}")
    public Response alterar(@PathParam("id") Long id, @Valid AdministradorDTO dto) {
        administradorService.update(id, dto);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        administradorService.delete(id);
        return Response.noContent().build();
    }
    
}
