package br.unitins.topicos1.prancha.resource;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import org.junit.jupiter.api.Test;
import br.unitins.topicos1.prancha.dto.MarcaDTO;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.MediaType;

@QuarkusTest
public class MarcaResourceTest {

    @Test
    public void testGetAll() {

        given()
        .when()
        .get("/marcas")
        .then().statusCode(200)
        .body("$", notNullValue());

    }

    @Test
    public void testGetByNome() {

        MarcaDTO dto = new MarcaDTO("Mormaii", "Brasil");

        given()
        .contentType(MediaType.APPLICATION_JSON)
        .body(dto)
        .when()
        .post("/marcas")
        .then()
        .statusCode(201);

        given()
        .when()
        .get("/marcas/nome/Mormaii")
        .then()
        .statusCode(200)
        .body("$", not(empty()))
        .body("[0].nome", equalTo("Mormaii"))
        .body("[0].paisOrigem", equalTo("Brasil"));

    }

    @Test
    public void testIncluir() {

        MarcaDTO dto = new MarcaDTO("Rusty", "Estados Unidos");

        given()
        .contentType(MediaType.APPLICATION_JSON)
        .body(dto)
        .when()
        .post("/marcas")
        .then()
        .statusCode(201)
        .body("nome", equalTo("Rusty"))
        .body("paisOrigem", equalTo("Estados Unidos"));

    }

    @Test
    public void testAlterar() {

        MarcaDTO dto = new MarcaDTO("Al Merrick", "Estados Unidos");

        Long id = given()
        .contentType(MediaType.APPLICATION_JSON)
        .body(dto)
        .when()
        .post("/marcas")
        .then()
        .statusCode(201)
        .extract()
        .jsonPath()
        .getObject("id", Long.class);

        MarcaDTO dtoAlterado = new MarcaDTO("Firewire", "Estados Unidos");

        given()
        .contentType(MediaType.APPLICATION_JSON)
        .body(dtoAlterado)
        .when()
        .put("/marcas/" + id)
        .then()
        .statusCode(204);

        given()
        .when()
        .get("/marcas/nome/Firewire")
        .then()
        .statusCode(200)
        .body("[0].nome", equalTo("Firewire"))
        .body("[0].paisOrigem", equalTo("Estados Unidos"));
        
    }

    @Test
    public void testDelete() {

        MarcaDTO dto = new MarcaDTO("DHD", "Austrália");

        Long id = given()
        .contentType(MediaType.APPLICATION_JSON)
        .body(dto)
        .when()
        .post("/marcas")
        .then()
        .statusCode(201)
        .extract()
        .jsonPath()
        .getObject("id", Long.class);

        given()
        .when()
        .delete("/marcas/" + id)
        .then()
        .statusCode(204);

        given()
        .when()
        .get("/marcas")
        .then()
        .body("findAll { it.id == " + id + " }", hasSize(0));

    }
    
}