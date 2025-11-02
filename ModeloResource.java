package br.unitins.topicos1.prancha.resource;
import java.util.List;
import br.unitins.topicos1.prancha.dto.ModeloDTO;
import br.unitins.topicos1.prancha.model.Modelo;
import br.unitins.topicos1.prancha.service.ModeloService;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
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

@Path("/modelos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ModeloResource {
    
    @Inject
    ModeloService service;

    @GET
    public List<Modelo> getAll() {
        return service.findAll();
    }

    @GET
    @Path("/nome/{nome}")
    public List<Modelo> getByNome(@PathParam("nome")String nome) {
        return service.findByNome(nome);
    }

    @POST
    public Response incluir(@Valid ModeloDTO dto) {
        var modelo = service.create(dto);
        return Response.status(Response.Status.CREATED).entity(modelo).build();
    }

    @PUT
    @Path("/{id}")
    public void alterar(@PathParam("id") Long id, @Valid ModeloDTO dto) {
        service.update(id, dto);
    }

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") Long id) {
        service.delete(id);
    }
}
