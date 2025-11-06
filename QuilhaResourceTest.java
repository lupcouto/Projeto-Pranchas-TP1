package br.unitins.topicos1.prancha.resource;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import org.junit.jupiter.api.Test;
import br.unitins.topicos1.prancha.dto.QuilhaDTO;
import br.unitins.topicos1.prancha.dto.TipoQuilhaDTO;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.MediaType;

@QuarkusTest
public class QuilhaResourceTest {

    private Long criarTipoQuilha() {

        TipoQuilhaDTO tipoQuilhaDTO = new TipoQuilhaDTO("Single Fin");

        return given()
        .contentType(MediaType.APPLICATION_JSON)
        .body(tipoQuilhaDTO)
        .when()
        .post("/tipos-quilha")
        .then()
        .statusCode(201)
        .extract()
        .jsonPath()
        .getObject("id", Long.class);

    }

    @Test
    public void testGetAll() {

        given()
        .when()
        .get("/quilhas")
        .then().statusCode(200)
        .body("$", notNullValue());

    }

    @Test
    public void testGetByTipoQuilha() {

        Long idTipoQuilha = criarTipoQuilha();
        QuilhaDTO dto = new QuilhaDTO("Uma quilha central", idTipoQuilha);

        given()
        .contentType(MediaType.APPLICATION_JSON)
        .body(dto)
        .when()
        .post("/quilhas")
        .then()
        .statusCode(201);

        given()
        .when()
        .get("/quilhas/tipoquilha/" + idTipoQuilha)
        .then()
        .statusCode(200)
        .body("$", not(empty()))
        .body("[0].descricaoQuilha", equalTo("Uma quilha central"));

    }

    @Test
    public void testIncluir() {

        Long idTipoQuilha = criarTipoQuilha();
        QuilhaDTO dto = new QuilhaDTO("Duas quilhas laterais", idTipoQuilha);

        given()
        .contentType(MediaType.APPLICATION_JSON)
        .body(dto)
        .when()
        .post("/quilhas")
        .then()
        .statusCode(201)
        .body("descricaoQuilha", equalTo("Duas quilhas laterais"))
        .body("tipoQuilha.id", equalTo(idTipoQuilha.intValue()));

    }

    @Test
    public void testAlterar() {

        Long idTipoQuilha = criarTipoQuilha();
        QuilhaDTO dto = new QuilhaDTO("Três quilhas", idTipoQuilha);

        Long id = given()
        .contentType(MediaType.APPLICATION_JSON)
        .body(dto)
        .when()
        .post("/quilhas")
        .then()
        .statusCode(201)
        .extract()
        .jsonPath()
        .getObject("id", Long.class);

        QuilhaDTO dtoAlterado = new QuilhaDTO("Quatro quilhas", idTipoQuilha);

        given()
        .contentType(MediaType.APPLICATION_JSON)
        .body(dtoAlterado)
        .when()
        .put("/quilhas/" + id)
        .then()
        .statusCode(204);

        given()
        .when()
        .get("/quilhas")
        .then()
        .statusCode(200)
        .body("find { it.id == " + id + " }.descricaoQuilha", equalTo("Quatro quilhas"));
        
    }

    @Test
    public void testDelete() {

        Long idTipoQuilha = criarTipoQuilha();
        QuilhaDTO dto = new QuilhaDTO("Cinco caixas para montar como thruster ou quad", idTipoQuilha);

        Long id = given()
        .contentType(MediaType.APPLICATION_JSON)
        .body(dto)
        .when()
        .post("/quilhas")
        .then()
        .statusCode(201)
        .extract()
        .jsonPath()
        .getObject("id", Long.class);

        given()
        .when()
        .delete("/quilhas/" + id)
        .then()
        .statusCode(204);

        given()
        .when()
        .get("/quilhas")
        .then()
        .body("findAll { it.id == " + id + " }", hasSize(0));

    }
    
}
