package br.unitins.topicos1.prancha.resource;
import java.util.List;
import br.unitins.topicos1.prancha.dto.EnderecoDTO;
import br.unitins.topicos1.prancha.model.Endereco;
import br.unitins.topicos1.prancha.service.EnderecoService;
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

@Path("/enderecos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EnderecoResource {

    @Inject
    EnderecoService enderecoService;

    @GET
    public List<Endereco> getAll() {
        return enderecoService.findAll();
    }

    @GET
    @Path("/cep/{cep}")
    public List<Endereco> getByCep(@PathParam("cep") String cep) {
        return enderecoService.findByCep(cep);
    }

    @POST
    public Response incluir(@Valid EnderecoDTO dto) {
        Endereco endereco = enderecoService.create(dto);
        return Response.status(Response.Status.CREATED).entity(endereco).build();
    }

    @PUT
    @Path("/{id}")
    public Response alterar(@PathParam("id") Long id, @Valid EnderecoDTO dto) {
        enderecoService.update(id, dto);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        enderecoService.delete(id);
        return Response.noContent().build();
    }
    
}