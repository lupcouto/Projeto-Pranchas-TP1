package br.unitins.topicos1.prancha.resource;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import org.junit.jupiter.api.Test;
import br.unitins.topicos1.prancha.dto.MarcaDTO;
import br.unitins.topicos1.prancha.dto.ModeloDTO;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.MediaType;

@QuarkusTest
public class ModeloResourceTest {

    private Long criarMarca() {

        MarcaDTO marcaDTO = new MarcaDTO("Pukas", "Espanha");

        return given()
        .contentType(MediaType.APPLICATION_JSON)
        .body(marcaDTO)
        .when()
        .post("/marcas")
        .then()
        .statusCode(201)
        .extract()
        .jsonPath()
        .getLong("id");

    }

    @Test
    public void testGetAll() {

        given()
        .when()
        .get("/modelos")
        .then().statusCode(200)
        .body("$", notNullValue());

    }

    @Test
    public void testGetByNome() {

        Long idMarca = criarMarca();
        ModeloDTO dto = new ModeloDTO("DNA", idMarca);

        given()
        .contentType(MediaType.APPLICATION_JSON)
        .body(dto)
        .when()
        .post("/modelos")
        .then()
        .statusCode(201);

        given()
        .when()
        .get("/modelos/nome/DNA")
        .then()
        .statusCode(200)
        .body("$", not(empty()))
        .body("[0].nome", equalTo("DNA"));

    }

    @Test
    public void testIncluir() {

        Long idMarca = criarMarca();
        ModeloDTO dto = new ModeloDTO("Seaside", idMarca);

        given()
        .contentType(MediaType.APPLICATION_JSON)
        .body(dto)
        .when()
        .post("/modelos")
        .then()
        .statusCode(201)
        .body("nome", equalTo("Seaside"))
        .body("marca.id", equalTo(idMarca.intValue()));

    }

    @Test
    public void testAlterar() {

        Long idMarca = criarMarca();
        ModeloDTO dto = new ModeloDTO("Hawk Pro", idMarca);

        Long id = given()
        .contentType(MediaType.APPLICATION_JSON)
        .body(dto)
        .when()
        .post("/modelos")
        .then()
        .statusCode(201)
        .extract()
        .jsonPath()
        .getLong("id");

        ModeloDTO dtoAlterado = new ModeloDTO("Fusion Fish", idMarca);

        given()
        .contentType(MediaType.APPLICATION_JSON)
        .body(dtoAlterado)
        .when()
        .put("/modelos/" + id)
        .then()
        .statusCode(204);

        given()
        .when()
        .get("/modelos/nome/Fusion Fish")
        .then()
        .statusCode(200)
        .body("[0].nome", equalTo("Fusion Fish"));
        
    }

    @Test
    public void testDelete() {

        Long idMarca = criarMarca();
        ModeloDTO dto = new ModeloDTO("Sweet Spot 3.0", idMarca);

        Long id = given()
        .contentType(MediaType.APPLICATION_JSON)
        .body(dto)
        .when()
        .post("/modelos")
        .then()
        .statusCode(201)
        .extract()
        .jsonPath()
        .getLong("id");

        given()
        .when()
        .delete("/modelos/" + id)
        .then()
        .statusCode(204);

        given()
        .when()
        .get("/modelos")
        .then()
        .body("findAll { it.id == " + id + " }", hasSize(0));

    }
    
}
