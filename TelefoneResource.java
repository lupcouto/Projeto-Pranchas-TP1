package br.unitins.topicos1.prancha.resource;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import br.unitins.topicos1.prancha.dto.TelefoneDTO;
import br.unitins.topicos1.prancha.model.Telefone;
import br.unitins.topicos1.prancha.service.TelefoneService;
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

@Path("/telefones")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TelefoneResource {
    
    @Inject
    TelefoneService service;

    @GET
    public List<Telefone> getAll() {
        return service.findAll();
    }

    @GET
    @Path("/numero/{numero}")
    public List<Telefone> getByNumero(@PathParam("numero")String numero) {
        return service.findByNumero(numero);
    }

    @POST
    public Response incluir(@Valid TelefoneDTO dto) {
        var telefone = service.create(dto);
        return Response.status(Response.Status.CREATED).entity(telefone).build();
    }   

    @PUT
    @Path("/{id}")
    public void alterar(@PathParam("id") Long id, @Valid TelefoneDTO dto) {
        service.update(id, dto);
    }

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") Long id) {
        service.delete(id);
    }

}
