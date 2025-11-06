package br.unitins.topicos1.prancha.resource;

import org.junit.jupiter.api.Test;
import br.unitins.topicos1.prancha.dto.TipoQuilhaDTO;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.MediaType;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class TipoQuilhaResourceTest {

    @Test
    public void testGetAll() {

        given()
        .when()
        .get("/tipos-quilha")
        .then()
        .statusCode(200)
        .body("$", notNullValue());

    }

    @Test
    public void testGetByNome() {

        TipoQuilhaDTO dto = new TipoQuilhaDTO("Single Fin");

        given()
        .contentType(MediaType.APPLICATION_JSON)
        .body(dto)
        .when()
        .post("/tipos-quilha")
        .then()
        .statusCode(201);

        given()
        .when()
        .get("/tipos-quilha/nome/Single Fin")
        .then()
        .statusCode(200)
        .body("$", not(empty()))
        .body("[0].nome", equalTo("Single Fin"));

    }

    @Test
    public void testIncluir() {

        TipoQuilhaDTO dto = new TipoQuilhaDTO("Twin Fin");

        given()
        .contentType(MediaType.APPLICATION_JSON)
        .body(dto)
        .when()
        .post("/tipos-quilha")
        .then()
        .statusCode(201)
        .body("nome", equalTo("Twin Fin"));

    }

    @Test
    public void testAlterar() {

        TipoQuilhaDTO dto = new TipoQuilhaDTO("Thruster");

        Long id = given()
        .contentType(MediaType.APPLICATION_JSON)
        .body(dto)
        .when()
        .post("/tipos-quilha")
        .then()
        .statusCode(201)
        .extract()
        .jsonPath()
        .getObject("id", Long.class);

        TipoQuilhaDTO dtoAlterado = new TipoQuilhaDTO("Quad");

        given()
        .contentType(MediaType.APPLICATION_JSON)
        .body(dtoAlterado)
        .when()
        .put("/tipos-quilha/" + id)
        .then()
        .statusCode(204);

        given()
        .when()
        .get("/tipos-quilha/nome/Quad")
        .then()
        .statusCode(200)
        .body("[0].nome", equalTo("Quad"));
        
    }

    @Test
    public void testDelete() {

        TipoQuilhaDTO dto = new TipoQuilhaDTO("Five Fin");

        Long id = given()
        .contentType(MediaType.APPLICATION_JSON)
        .body(dto)
        .when()
        .post("/tipos-quilha")
        .then()
        .statusCode(201)
        .extract()
        .jsonPath()
        .getObject("id", Long.class);

        given()
        .when()
        .delete("/tipos-quilha/" + id)
        .then()
        .statusCode(204);

        given()
        .when()
        .get("/tipos-quilha")
        .then()
        .body("findAll { it.id == " + id + " }", hasSize(0));

    }

}
