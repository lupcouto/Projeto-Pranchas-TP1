package br.unitins.topicos1.prancha.resource;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import org.junit.jupiter.api.Test;
import br.unitins.topicos1.prancha.dto.*;
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
            .when().post("/marcas")
            .then().statusCode(201)
            .extract().jsonPath().getLong("id");
    }

    private Long criarModelo() {
        Long idMarca = criarMarca();
        ModeloDTO modeloDTO = new ModeloDTO("DNA", idMarca);
        return given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(modeloDTO)
            .when().post("/modelos")
            .then().statusCode(201)
            .extract().jsonPath().getLong("id");
    }

    private Long criarFornecedor() {
        FornecedorDTO fornecedorDTO = new FornecedorDTO("Fornecedor Teste", "63", "999999999", "123456789");
        return given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(fornecedorDTO)
            .when().post("/fornecedores")
            .then().statusCode(201)
            .extract().jsonPath().getLong("id");
    }

    private Long criarTipoQuilha() {
        TipoQuilhaDTO tipoQuilhaDTO = new TipoQuilhaDTO("Single Fin");
        return given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(tipoQuilhaDTO)
            .when().post("/tipos-quilha")
            .then().statusCode(201)
            .extract().jsonPath().getLong("id");
    }

    private Long criarQuilha() {
        Long idTipoQuilha = criarTipoQuilha();
        QuilhaDTO quilhaDTO = new QuilhaDTO("Quilha Central", idTipoQuilha);
        return given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(quilhaDTO)
            .when().post("/quilhas")
            .then().statusCode(201)
            .extract().jsonPath().getLong("id");
    }

    private Long criarPrancha(float tamanho, double valor, int estoque, TipoPrancha tipo, Habilidade habilidade) {
        Long idModelo = criarModelo();
        Long idFornecedor = criarFornecedor();
        Long idQuilha = criarQuilha();

        PranchaDTO dto = new PranchaDTO(tamanho, valor, estoque, tipo, habilidade, idModelo, idFornecedor, idQuilha);

        return given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(dto)
            .when().post("/pranchas")
            .then().statusCode(201)
            .extract().jsonPath().getLong("id");
    }

    @Test
    public void testIncluir() {
        Long id = criarPrancha(5.5f, 2000.0, 10, TipoPrancha.FISH, Habilidade.AVANCADO);

        given()
            .when().get("/pranchas")
            .then()
            .statusCode(200)
            .body("find { it.id == " + id + " }.estoque", equalTo(10));
    }

    @Test
    public void testAlterar() {
        Long id = criarPrancha(5.0f, 3000.0, 5, TipoPrancha.SHORTBOARD, Habilidade.INTERMEDIARIO);

        PranchaDTO dtoAlterado = new PranchaDTO(6.0f, 3500.0, 8, TipoPrancha.LONGBOARD, Habilidade.AVANCADO, criarModelo(), criarFornecedor(), criarQuilha());

        given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(dtoAlterado)
            .when().put("/pranchas/" + id)
            .then().statusCode(204);

        given()
            .when().get("/pranchas")
            .then()
            .statusCode(200)
            .body("find { it.id == " + id + " }.tamanho", equalTo(6.0f))
            .body("find { it.id == " + id + " }.estoque", equalTo(8));
    }

    @Test
    public void testDelete() {
        Long id = criarPrancha(4.5f, 1500.0, 3, TipoPrancha.FUNBOARD, Habilidade.INICIANTE);

        given()
            .when().delete("/pranchas/" + id)
            .then().statusCode(204);

        given()
            .when().get("/pranchas")
            .then()
            .body("findAll { it.id == " + id + " }", hasSize(0));
    }
}