package br.unitins.topicos1.prancha.resource;
import static io.restassured.RestAssured.given;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import br.unitins.topicos1.prancha.dto.CartaoDTO;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

@QuarkusTest
public class CartaoResourceTest {

    @Test
    public void testGetAll() {
        given()
        .contentType(ContentType.JSON)
        .when()
        .get("/cartoes")
        .then()
        .statusCode(200);
    }

    @Test
    public void testGetByNumeroCartao() {
        CartaoDTO dto = new CartaoDTO("2222333344445555", "Titular Teste", LocalDate.of(2040, 1, 1));

        given()
        .contentType(ContentType.JSON)
        .body(dto)
        .when()
        .post("/cartoes")
        .then()
        .statusCode(201);

        given()
        .when()
        .get("/cartoes/numeroCartao/{numeroCartao}", "2222333344445555")
        .then()
        .statusCode(200);
    }

    @Test
    public void testIncluir() {
        CartaoDTO dto = new CartaoDTO("1111222233334444", "Titular Inclusão", LocalDate.of(2040, 1, 1));

        given()
        .contentType(ContentType.JSON)
        .body(dto)
        .when()
        .post("/cartoes")
        .then()
        .statusCode(201);
    }

    @Test
    public void testAlterar() {
        CartaoDTO dto = new CartaoDTO("5555666677778888", "Titular Alterar", LocalDate.of(2040, 1, 1));

        Number idNumber = given()
        .contentType(ContentType.JSON)
        .body(dto)
        .when()
        .post("/cartoes")
        .then()
        .statusCode(201)
        .extract()
        .path("id");

        Long id = idNumber.longValue();

        CartaoDTO dtoAlterado = new CartaoDTO("9999000011112222", "Titular Alterado", LocalDate.of(2041, 1, 1));

        given()
        .contentType(ContentType.JSON)
        .body(dtoAlterado)
        .when()
        .put("/cartoes/{id}", id)
        .then()
        .statusCode(204);
    }

    @Test
    public void testDelete() {
        CartaoDTO dto = new CartaoDTO("3333444455556666", "Titular Delete", LocalDate.of(2040, 1, 1));

        Number idNumber = given()
        .contentType(ContentType.JSON)
        .body(dto)
        .when()
        .post("/cartoes")
        .then()
        .statusCode(201)
        .extract()
        .path("id");

        Long id = idNumber.longValue();

        given()
        .when()
        .delete("/cartoes/{id}", id)
        .then()
        .statusCode(204);
    }
}