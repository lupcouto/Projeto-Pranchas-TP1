package br.unitins.topicos1.prancha.resource;
import static io.restassured.RestAssured.given;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import jakarta.inject.Inject;
import br.unitins.topicos1.prancha.dto.BoletoDTO;
import br.unitins.topicos1.prancha.repository.BoletoRepository;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

@QuarkusTest
public class BoletoResourceTest {

    @Inject
    BoletoRepository boletoRepository;

    @TestTransaction
    @BeforeEach
    public void clean() {
        boletoRepository.deleteAll();
    }

    @Test
    public void testGetAll() {
        given()
        .contentType(ContentType.JSON)
        .when()
        .get("/boletos")
        .then()
        .statusCode(200);
    }

    @Test
    public void testGetByCodigoBarras() {
        BoletoDTO dto = new BoletoDTO("123456789000111222333444555666777888999", LocalDate.of(2040, 1, 1));

        given()
        .contentType(ContentType.JSON)
        .body(dto)
        .when()
        .post("/boletos")
        .then()
        .statusCode(201);

        given()
        .when()
        .get("/boletos/codigoBarras/{codigoBarras}", "123456789000111222333444555666777888999")
        .then()
        .statusCode(200);
    }

    @Test
    public void testIncluir() {
        BoletoDTO dto = new BoletoDTO("111222333444555666777888999000", LocalDate.of(2045, 5, 10));

        given()
        .contentType(ContentType.JSON)
        .body(dto)
        .when()
        .post("/boletos")
        .then()
        .statusCode(201);
    }

    @Test
    public void testAlterar() {
        BoletoDTO dto = new BoletoDTO("555666777888000999111222333444", LocalDate.of(2040, 1, 1));

        Number idNumber = given()
        .contentType(ContentType.JSON)
        .body(dto)
        .when()
        .post("/boletos")
        .then()
        .statusCode(201)
        .extract()
        .path("id");

        Long id = idNumber.longValue();

        BoletoDTO dtoAlterado = new BoletoDTO("999888777666555444333222111000", LocalDate.of(2042, 2, 2));

        given()
        .contentType(ContentType.JSON)
        .body(dtoAlterado)
        .when()
        .put("/boletos/{id}", id)
        .then()
        .statusCode(204);
    }

    @Test
    public void testDelete() {
        BoletoDTO dto = new BoletoDTO("000111222333444555666777888999", LocalDate.of(2041, 3, 3));

        Number idNumber = given()
        .contentType(ContentType.JSON)
        .body(dto)
        .when()
        .post("/boletos")
        .then()
        .statusCode(201)
        .extract()
        .path("id");

        Long id = idNumber.longValue();

        given()
        .when()
        .delete("/boletos/{id}", id)
        .then()
        .statusCode(204);
    }
}