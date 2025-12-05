package br.unitins.topicos1.prancha.resource;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import org.junit.jupiter.api.Test;
import br.unitins.topicos1.prancha.dto.ClienteDTO;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.MediaType;

@QuarkusTest
public class ClienteResourceTest {

    @Test
    public void testGetAll() {
        given()
        .when()
        .get("/clientes")
        .then()
        .statusCode(200)
        .body("$", notNullValue());
    }

    @Test
    public void testGetByCpf() {
        ClienteDTO dto = new ClienteDTO("Luisa Pimentel", "11", "999999999", "12345678900");

        given()
        .contentType(MediaType.APPLICATION_JSON)
        .body(dto)
        .when()
        .post("/clientes")
        .then()
        .statusCode(201);

        given()
        .when()
        .get("/clientes/cpf/12345678900")
        .then()
        .statusCode(200)
        .body("$", not(empty()))
        .body("[0].nome", equalTo("Luisa Pimentel"))
        .body("[0].cpf", equalTo("12345678900"))
        .body("[0].telefone.ddd", equalTo("11"))
        .body("[0].telefone.numero", equalTo("999999999"));
    }

    @Test
    public void testIncluir() {
        ClienteDTO dto = new ClienteDTO("Maria Silva", "21", "988888888", "98765432100");

        given()
        .contentType(MediaType.APPLICATION_JSON)
        .body(dto)
        .when()
        .post("/clientes")
        .then()
        .statusCode(201)
        .body("nome", equalTo("Maria Silva"))
        .body("cpf", equalTo("98765432100"))
        .body("telefone.ddd", equalTo("21"))
        .body("telefone.numero", equalTo("988888888"));
    }

    @Test
    public void testAlterar() {
        ClienteDTO dto = new ClienteDTO("Carlos Souza", "31", "977777777", "11122233344");

        Long id = given()
        .contentType(MediaType.APPLICATION_JSON)
        .body(dto)
        .when()
        .post("/clientes")
        .then()
        .statusCode(201)
        .extract()
        .jsonPath()
        .getObject("id", Long.class);

        ClienteDTO dtoAlterado = new ClienteDTO("Carlos Eduardo", "31", "977777777", "11122233344");

        given()
        .contentType(MediaType.APPLICATION_JSON)
        .body(dtoAlterado)
        .when()
        .put("/clientes/" + id)
        .then()
        .statusCode(204);

        given()
        .when()
        .get("/clientes/cpf/11122233344")
        .then()
        .statusCode(200)
        .body("[0].nome", equalTo("Carlos Eduardo"))
        .body("[0].cpf", equalTo("11122233344"));
    }

    @Test
    public void testDelete() {
        ClienteDTO dto = new ClienteDTO("João Pereira", "41", "966666666", "55566677788");

        Long id = given()
        .contentType(MediaType.APPLICATION_JSON)
        .body(dto)
        .when()
        .post("/clientes")
        .then()
        .statusCode(201)
        .extract()
        .jsonPath()
        .getObject("id", Long.class);

        given()
        .when()
        .delete("/clientes/" + id)
        .then()
        .statusCode(204);

        given()
        .when()
        .get("/clientes")
        .then()
        .body("findAll { it.id == " + id + " }", hasSize(0));
    }
}