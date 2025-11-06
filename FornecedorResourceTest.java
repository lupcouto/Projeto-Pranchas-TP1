package br.unitins.topicos1.prancha.resource;
import org.junit.jupiter.api.Test;
import br.unitins.topicos1.prancha.dto.FornecedorDTO;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.MediaType;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class FornecedorResourceTest {

    @Test
    public void testGetAll() {

        given()
        .when()
        .get("/fornecedores")
        .then().statusCode(200)
        .body("$", notNullValue());

    }

    @Test
    public void testGetByCnpj() {

        FornecedorDTO dto = new FornecedorDTO("9832403710");

        given()
        .contentType(MediaType.APPLICATION_JSON)
        .body(dto)
        .when()
        .post("/fornecedores")
        .then()
        .statusCode(201);

        given()
        .when()
        .get("/fornecedores/cnpj/9832403710")
        .then()
        .statusCode(200)
        .body("$", not(empty()))
        .body("[0].cnpj", equalTo("9832403710"));

    }

    @Test
    public void testIncluir() {

        FornecedorDTO dto = new FornecedorDTO("123456789");

        given()
        .contentType(MediaType.APPLICATION_JSON)
        .body(dto)
        .when()
        .post("/fornecedores")
        .then()
        .statusCode(201)
        .body("cnpj", equalTo("123456789"));

    }

    @Test
    public void testAlterar() {

        FornecedorDTO dto = new FornecedorDTO("987654321");

        Long id = given()
        .contentType(MediaType.APPLICATION_JSON)
        .body(dto)
        .when()
        .post("/fornecedores")
        .then()
        .statusCode(201)
        .extract()
        .jsonPath()
        .getObject("id", Long.class);

        FornecedorDTO dtoAlterado = new FornecedorDTO("3516584946");

        given()
        .contentType(MediaType.APPLICATION_JSON)
        .body(dtoAlterado)
        .when()
        .put("/fornecedores/" + id)
        .then()
        .statusCode(204);

        given()
        .when()
        .get("/fornecedores/cnpj/3516584946")
        .then()
        .statusCode(200)
        .body("[0].cnpj", equalTo("3516584946"));
        
    }

    @Test
    public void testDelete() {

        FornecedorDTO dto = new FornecedorDTO("01298301264");

        Long id = given()
        .contentType(MediaType.APPLICATION_JSON)
        .body(dto)
        .when()
        .post("/fornecedores")
        .then()
        .statusCode(201)
        .extract()
        .jsonPath()
        .getObject("id", Long.class);

        given()
        .when()
        .delete("/fornecedores/" + id)
        .then()
        .statusCode(204);

        given()
        .when()
        .get("/fornecedores")
        .then()
        .body("findAll {it.id == " + id + " }", hasSize(0));

    }
    
}
