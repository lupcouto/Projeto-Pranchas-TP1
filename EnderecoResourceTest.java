package br.unitins.topicos1.prancha.resource;
import static io.restassured.RestAssured.given;
import org.junit.jupiter.api.Test;
import br.unitins.topicos1.prancha.dto.EnderecoDTO;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

@QuarkusTest
public class EnderecoResourceTest {

    @Test
    public void testGetAll() {
        given()
        .contentType(ContentType.JSON)
        .when()
        .get("/enderecos")
        .then()
        .statusCode(200); 
    }

    @Test
    public void testCreate() {
        EnderecoDTO dto = new EnderecoDTO("Palmas", "TO", "77000000");

        given()
        .contentType(ContentType.JSON)
        .body(dto)
        .when()
        .post("/enderecos")
        .then()
        .statusCode(201);
    }

    @Test
    public void testGetByCep() {

        EnderecoDTO dto = new EnderecoDTO("Araguaína", "TO", "77800000"
        );

        given()
        .contentType(ContentType.JSON)
        .body(dto)
        .when()
        .post("/enderecos")
        .then()
        .statusCode(201);

        given()
        .when()
        .get("/enderecos/cep/{cep}", "77800000")
        .then()
        .statusCode(200);
    }

    @Test
    public void testUpdate() {
        EnderecoDTO dto = new EnderecoDTO("Gurupi", "TO", "77400000");

        Number idNumber = given()
        .contentType(ContentType.JSON)
        .body(dto)
        .when()
        .post("/enderecos")
        .then()
        .statusCode(201)
        .extract()
        .path("id");

        Long id = idNumber.longValue();

        EnderecoDTO dtoAlterado = new EnderecoDTO("Paraíso", "TO", "77600000");

        given()
        .contentType(ContentType.JSON)
        .body(dtoAlterado)
        .when()
        .put("/enderecos/{id}", id)
        .then()
        .statusCode(204);
    }

    @Test
    public void testDelete() {
        EnderecoDTO dto = new EnderecoDTO("Porto Nacional", "TO", "77500000");

        Number idNumber = given()
        .contentType(ContentType.JSON)
        .body(dto)
        .when()
        .post("/enderecos")
        .then()
        .statusCode(201)
        .extract()
        .path("id");

        Long id = idNumber.longValue();

        given()
        .when()
        .delete("/enderecos/{id}", id)
        .then()
        .statusCode(204);
    }
}