package br.unitins.topicos1.prancha.resource;
import static io.restassured.RestAssured.given;
import org.junit.jupiter.api.Test;
import br.unitins.topicos1.prancha.dto.PixDTO;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

@QuarkusTest
public class PixResourceTest {

    @Test
    public void testGetAll() {
        given()
        .contentType(ContentType.JSON)
        .when()
        .get("/pix")
        .then()
        .statusCode(200);
    }

    @Test
    public void testGetByChave() {
        PixDTO dto = new PixDTO("chave-teste-123");

        given()
        .contentType(ContentType.JSON)
        .body(dto)
        .when()
        .post("/pix")
        .then()
        .statusCode(201);

        given()
        .when()
        .get("/pix/chave/{chave}", "chave-teste-123")
        .then()
        .statusCode(200);
    }

    @Test
    public void testIncluir() {
        PixDTO dto = new PixDTO("nova-chave-555");

        given()
        .contentType(ContentType.JSON)
        .body(dto)
        .when()
        .post("/pix")
        .then()
        .statusCode(201);
    }

    @Test
    public void testAlterar() {
        PixDTO dto = new PixDTO("chave-alterar-111");

        Number idNumber = given()
        .contentType(ContentType.JSON)
        .body(dto)
        .when()
        .post("/pix")
        .then()
        .statusCode(201)
        .extract()
        .path("id");

        Long id = idNumber.longValue();

        PixDTO dtoAlterado = new PixDTO("chave-alterada-999");

        given()
        .contentType(ContentType.JSON)
        .body(dtoAlterado)
        .when()
        .put("/pix/{id}", id)
        .then()
        .statusCode(204);
    }

    @Test
    public void testDelete() {

        PixDTO dto = new PixDTO("chave-deletar-777");

        Number idNumber = given()
        .contentType(ContentType.JSON)
        .body(dto)
        .when()
        .post("/pix")
        .then()
        .statusCode(201)
        .extract()
        .path("id");

        Long id = idNumber.longValue();

        given()
        .when()
        .delete("/pix/{id}", id)
        .then()
        .statusCode(204);
    }
}