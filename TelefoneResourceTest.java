package br.unitins.topicos1.prancha.resource;
import org.junit.jupiter.api.Test;
import br.unitins.topicos1.prancha.dto.TelefoneDTO;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.MediaType;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class TelefoneResourceTest {

    @Test
    public void testGetAll() {

        given()
        .when()
        .get("/telefones")
        .then()
        .statusCode(200)
        .body("$", notNullValue());

    }

    @Test
    public void testGetByNumero() {

        TelefoneDTO dto = new TelefoneDTO("63", "8924703605");

        given()
        .contentType(MediaType.APPLICATION_JSON)
        .body(dto)
        .when()
        .post("/telefones")
        .then()
        .statusCode(201);

        given()
        .when()
        .get("/telefones/numero/8924703605")
        .then()
        .statusCode(200)
        .body("$", not(empty()))
        .body("[0].numero", equalTo("8924703605"));

    }

    @Test
    public void testIncluir() {

        TelefoneDTO dto = new TelefoneDTO("12", "4518769");

        given()
        .contentType(MediaType.APPLICATION_JSON)
        .body(dto)
        .when()
        .post("/telefones")
        .then()
        .statusCode(201)
        .body("ddd", equalTo("12"))
        .body("numero", equalTo("4518769"));

    }

    @Test
    public void testAlterar() {

        TelefoneDTO dto = new TelefoneDTO("61", "11111111");

        Long id = given()
        .contentType(MediaType.APPLICATION_JSON)
        .body(dto)
        .when()
        .post("/telefones")
        .then()
        .statusCode(201)
        .extract()
        .jsonPath()
        .getObject("id", Long.class);

        TelefoneDTO dtoAlterado = new TelefoneDTO("23", "11111111");

        given()
        .contentType(MediaType.APPLICATION_JSON)
        .body(dtoAlterado)
        .when()
        .put("/telefones/" + id)
        .then()
        .statusCode(204);

        given()
        .when()
        .get("/telefones/numero/11111111")
        .then()
        .statusCode(200)
        .body("[0].numero", equalTo("11111111"));
        
    }

    @Test
    public void testDelete() {

        TelefoneDTO dto = new TelefoneDTO("99", "0292389498");

        Long id = given()
        .contentType(MediaType.APPLICATION_JSON)
        .body(dto)
        .when()
        .post("/telefones")
        .then()
        .statusCode(201)
        .extract()
        .jsonPath()
        .getObject("id", Long.class);

        given()
        .when()
        .delete("/telefones/" + id)
        .then()
        .statusCode(204);

        given()
        .when()
        .get("/telefones")
        .then()
        .body("findAll { it.id == " + id + " }", hasSize(0));

    }

}    