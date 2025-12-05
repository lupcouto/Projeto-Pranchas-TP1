package br.unitins.topicos1.prancha.resource;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.Consumes;
import java.util.List;
import br.unitins.topicos1.prancha.dto.QuilhaDTO;
import br.unitins.topicos1.prancha.model.Quilha;
import br.unitins.topicos1.prancha.model.TipoQuilha;
import br.unitins.topicos1.prancha.repository.TipoQuilhaRepository;
import br.unitins.topicos1.prancha.service.QuilhaService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.PathParam;

@Path("/quilhas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class QuilhaResource {
    
    @Inject
    QuilhaService service;

    @Inject
    TipoQuilhaRepository tipoQuilhaRepository;

    @GET
    public List<Quilha> getAll() {
        return service.findAll();
    }

    @GET
    @Path("/tipoquilha/{idTipoQuilha}")
    public List<Quilha> getByTipoQuilha(@PathParam("idTipoQuilha")Long id) {
        TipoQuilha tipoQuilha = tipoQuilhaRepository.findById(id);
        return service.findByTipoQuilha(tipoQuilha);
    }

    @POST
    public Response incluir(@Valid QuilhaDTO dto) {
        var quilha = service.create(dto);
        return Response.status(Response.Status.CREATED).entity(quilha).build();
    }

    @PUT
    @Path("/{id}")
    public Response alterar(@PathParam("id") Long id, @Valid QuilhaDTO dto) {
        service.update(id, dto);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        service.delete(id);
        return Response.noContent().build();
    }

}