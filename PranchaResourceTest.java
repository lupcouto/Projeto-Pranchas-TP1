package br.unitins.topicos1.prancha.resource;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.jupiter.api.Test;

import br.unitins.topicos1.prancha.dto.FornecedorDTO;
import br.unitins.topicos1.prancha.dto.MarcaDTO;
import br.unitins.topicos1.prancha.dto.ModeloDTO;
import br.unitins.topicos1.prancha.dto.PranchaDTO;
import br.unitins.topicos1.prancha.dto.QuilhaDTO;
import br.unitins.topicos1.prancha.dto.TipoQuilhaDTO;
import br.unitins.topicos1.prancha.model.Habilidade;
import br.unitins.topicos1.prancha.model.TipoPrancha;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.MediaType;

@QuarkusTest
public class PranchaResourceTest {

    private Long criarMarca() {

        MarcaDTO marcaDTO = new MarcaDTO("Mormaii", "Brasil");
        return given()
        .contentType(MediaType.APPLICATION_JSON)
            .body(marcaDTO)
            .when()
            .post("/marcas")
            .then()
            .statusCode(201)
            .extract()
            .jsonPath()
            .getObject("id", Long.class);

    }

    private Long criarModelo() {

        Long idMarca = criarMarca();
        ModeloDTO modeloDTO = new ModeloDTO("DNA", idMarca);
        return given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(modeloDTO)
            .when()
            .post("/modelos")
            .then()
            .statusCode(201)
            .extract()
            .jsonPath()
            .getObject("id", Long.class);
    }

    private Long criarFornecedor() {

        FornecedorDTO fornecedorDTO = new FornecedorDTO("9832403710");
        return given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(fornecedorDTO)
            .when()
            .post("/fornecedores")
            .then()
            .statusCode(201)
            .extract()
            .jsonPath()
            .getObject("id", Long.class);
    }

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

    private Long criarQuilha() {

        Long idTipoQuilha = criarTipoQuilha();
        QuilhaDTO quilhaDTO = new QuilhaDTO("Uma quilha central", idTipoQuilha);
        return given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(quilhaDTO)
            .when()
            .post("/quilhas")
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
        .get("/pranchas")
        .then()
        .statusCode(200)
        .body("$", notNullValue());
    }

    @Test
    public void testFindByTipoPrancha() {
        
        Long idModelo = criarModelo();
        Long idFornecedor = criarFornecedor();
        Long idQuilha = criarQuilha();

        PranchaDTO dtoFish = new PranchaDTO(4.2f, 5000.0, TipoPrancha.FISH, Habilidade.INTERMEDIARIO, idModelo, idFornecedor, idQuilha);

        Long idFish = given()
        .contentType(MediaType.APPLICATION_JSON)
        .body(dtoFish)
        .when()
        .post("/pranchas")
        .then()
        .statusCode(201)
        .extract()
        .jsonPath()
        .getObject("id", Long.class);

        PranchaDTO dtoGun = new PranchaDTO(5.0f, 6000.0, TipoPrancha.GUN, Habilidade.AVANCADO, idModelo, idFornecedor, idQuilha);

        given()
        .contentType(MediaType.APPLICATION_JSON)
        .body(dtoGun)
        .when()
        .post("/pranchas")
        .then()
        .statusCode(201);

        given()
        .when()
        .get("/pranchas/tipo/FISH")
        .then()
        .statusCode(200)
        .body("findAll { it.id == " + idFish + " }", hasSize(1))
        .body("find { it.id == " + idFish + " }.tipoPrancha", equalTo("FISH"));

    }

    @Test
    public void testIncluir() {

        Long idModelo = criarModelo();
        Long idFornecedor = criarFornecedor();
        Long idQuilha = criarQuilha();

        PranchaDTO dto = new PranchaDTO(3.5f,7650.0, TipoPrancha.GUN, Habilidade.INTERMEDIARIO, idModelo, idFornecedor, idQuilha);

        given()
        .contentType(MediaType.APPLICATION_JSON)
        .body(dto)
        .when()
        .post("/pranchas")
        .then()
        .statusCode(201)
        .body("tamanho", equalTo(3.5f))
        .body("valor", equalTo(7650.0f));
    }

    @Test
    public void testAlterar() {

        Long idModelo = criarModelo();
        Long idFornecedor = criarFornecedor();
        Long idQuilha = criarQuilha();

        PranchaDTO dto = new PranchaDTO(5.5f,4000.0, TipoPrancha.FISH, Habilidade.INICIANTE, idModelo, idFornecedor, idQuilha);

        Long id = given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(dto)
            .when()
            .post("/pranchas")
            .then()
            .statusCode(201)
            .extract()
            .jsonPath()
            .getObject("id", Long.class);

        PranchaDTO dtoAlterado = new PranchaDTO(7.3f,9000.0, TipoPrancha.LONGBOARD, Habilidade.AVANCADO, idModelo, idFornecedor, idQuilha);

        given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(dtoAlterado)
            .when()
            .put("/pranchas/" + id)
            .then()
            .statusCode(204);

        given()
            .when()
            .get("/pranchas")
            .then()
            .statusCode(200)
            .body("findAll { it.id == " + id + " }[0].tamanho", equalTo(7.3f));
    }

    @Test
    public void testDelete() {

        Long idModelo = criarModelo();
        Long idFornecedor = criarFornecedor();
        Long idQuilha = criarQuilha();

        PranchaDTO dto = new PranchaDTO(3.4f,3550.0, TipoPrancha.SHORTBOARD, Habilidade.INICIANTE, idModelo, idFornecedor, idQuilha);

        Long id = given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(dto)
            .when()
            .post("/pranchas")
            .then()
            .statusCode(201)
            .extract()
            .jsonPath()
            .getObject("id", Long.class);

        given()
            .when()
            .delete("/pranchas/" + id)
            .then()
            .statusCode(204);

        given()
            .when()
            .get("/pranchas")
            .then()
            .body("findAll { it.id == " + id + " }", hasSize(0));
    }
 
}
