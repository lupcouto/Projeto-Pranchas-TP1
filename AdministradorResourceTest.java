package br.unitins.topicos1.prancha.resource;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import org.junit.jupiter.api.Test;
import br.unitins.topicos1.prancha.dto.AdministradorDTO;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.MediaType;

@QuarkusTest
public class AdministradorResourceTest {

    @Test
    public void testGetAll() {
        given()
        .when()
        .get("/administradores")
        .then()
        .statusCode(200)
        .body("$", notNullValue());
    }

    @Test
    public void testGetByNome() {
        AdministradorDTO dto = new AdministradorDTO("Luisa Pimentel", "11", "999999999", "Supervisor", "Ativo");

        given()
        .contentType(MediaType.APPLICATION_JSON)
        .body(dto)
        .when()
        .post("/administradores")
        .then()
        .statusCode(201);

        given()
        .when()
        .get("/administradores/nome/Luisa Pimentel")
        .then()
        .statusCode(200)
        .body("$", not(empty()))
        .body("[0].nome", equalTo("Luisa Pimentel"))
        .body("[0].cargo", equalTo("Supervisor"))
        .body("[0].statusAdm", equalTo("Ativo"))
        .body("[0].telefone.ddd", equalTo("11"))
        .body("[0].telefone.numero", equalTo("999999999"));
    }

    @Test
    public void testIncluir() {
        AdministradorDTO dto = new AdministradorDTO("Maria Silva", "21", "988888888", "Gerente", "Ativo");

        given()
        .contentType(MediaType.APPLICATION_JSON)
        .body(dto)
        .when()
        .post("/administradores")
        .then()
        .statusCode(201)
        .body("nome", equalTo("Maria Silva"))
        .body("cargo", equalTo("Gerente"))
        .body("statusAdm", equalTo("Ativo"))
        .body("telefone.ddd", equalTo("21"))
        .body("telefone.numero", equalTo("988888888"));
    }

    @Test
    public void testAlterar() {
        AdministradorDTO dto = new AdministradorDTO("Carlos Souza", "31", "977777777", "Assistente", "Ativo");

        Long id = given()
        .contentType(MediaType.APPLICATION_JSON)
        .body(dto)
        .when()
        .post("/administradores")
        .then()
        .statusCode(201)
        .extract()
        .jsonPath()
        .getObject("id", Long.class);

        AdministradorDTO dtoAlterado = new AdministradorDTO("Carlos Eduardo", "31", "977777777", "Coordenador", "Ativo");

        given()
        .contentType(MediaType.APPLICATION_JSON)
        .body(dtoAlterado)
        .when()
        .put("/administradores/" + id)
        .then()
        .statusCode(204);

        given()
        .when()
        .get("/administradores/nome/Carlos Eduardo")
        .then()
        .statusCode(200)
        .body("[0].nome", equalTo("Carlos Eduardo"))
        .body("[0].cargo", equalTo("Coordenador"))
        .body("[0].statusAdm", equalTo("Ativo"));
    }

    @Test
    public void testDelete() {
        AdministradorDTO dto = new AdministradorDTO("Teste Delete", "11", "999999999", "Gerente", "Ativo");

        Long id = given()
        .contentType(MediaType.APPLICATION_JSON)
        .body(dto)
        .when()
        .post("/administradores")
        .then()
        .statusCode(201)
        .extract()
        .jsonPath()
        .getObject("id", Long.class);

        given()
        .when()
        .delete("/administradores/" + id)
        .then()
        .statusCode(204); 

        given()
        .when()
        .get("/administradores")
        .then()
        .body("findAll { it.id == " + id + " }", hasSize(0));
    }
}