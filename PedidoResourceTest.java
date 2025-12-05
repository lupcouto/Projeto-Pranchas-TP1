package br.unitins.topicos1.prancha.resource;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import br.unitins.topicos1.prancha.dto.*;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.MediaType;

@QuarkusTest
public class PedidoResourceTest {

    private Long criarCliente() {
        ClienteDTO dto = new ClienteDTO("Cliente Teste", "63", "999999999", "12345678910");
        return given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(dto)
            .when().post("/clientes")
            .then().statusCode(201)
            .extract().jsonPath().getLong("id");
    }

    private EnderecoDTO criarEnderecoDTO() {
        return new EnderecoDTO("Palmas", "TO", "77000000");
    }

    private PixDTO criarPixDTO() {
        return new PixDTO("chave-teste-pix");
    }

    private BoletoDTO criarBoletoDTO() {
        return new BoletoDTO("12345678901234567890", LocalDate.now().plusDays(3));
    }

    private Long criarFornecedor() {
        FornecedorDTO dto = new FornecedorDTO("Fornecedor X", "63", "99998888", "123456789");
        return given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(dto)
            .when().post("/fornecedores")
            .then().statusCode(201)
            .extract().jsonPath().getLong("id");
    }

    private Long criarModelo() {
        MarcaDTO marcaDTO = new MarcaDTO("Marca X", "Brasil");
        Long idMarca = given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(marcaDTO)
                .when().post("/marcas")
                .then().statusCode(201)
                .extract().jsonPath().getLong("id");

        ModeloDTO modeloDTO = new ModeloDTO("Modelo X", idMarca);
        return given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(modeloDTO)
                .when().post("/modelos")
                .then().statusCode(201)
                .extract().jsonPath().getLong("id");
    }

    private Long criarTipoQuilha() {
        TipoQuilhaDTO dto = new TipoQuilhaDTO("Single Fin");
        return given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(dto)
            .when().post("/tipos-quilha")
            .then().statusCode(201)
            .extract().jsonPath().getLong("id");
    }

    private Long criarQuilha() {
        Long idTipo = criarTipoQuilha();
        QuilhaDTO dto = new QuilhaDTO("Quilha Teste", idTipo);
        return given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(dto)
            .when().post("/quilhas")
            .then().statusCode(201)
            .extract().jsonPath().getLong("id");
    }

    private Long criarPrancha() {
        Long idFornecedor = criarFornecedor();
        Long idModelo = criarModelo();
        Long idQuilha = criarQuilha();

        PranchaDTO dto = new PranchaDTO(
            5.5f,
            2000.0,
            10, 
            br.unitins.topicos1.prancha.model.TipoPrancha.FISH,
            br.unitins.topicos1.prancha.model.Habilidade.AVANCADO,
            idModelo,
            idFornecedor,
            idQuilha
        );

        return given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(dto)
            .when().post("/pranchas")
            .then().statusCode(201)
            .extract().jsonPath().getLong("id");
    }

    private PedidoDTO criarPedidoDTO(Long idCliente) {
        Long idPrancha1 = criarPrancha();
        Long idPrancha2 = criarPrancha();

        ItemPedidoDTO item1 = new ItemPedidoDTO(idPrancha1, 2, null);
        ItemPedidoDTO item2 = new ItemPedidoDTO(idPrancha2, 1, null);

        return new PedidoDTO(
            idCliente,
            criarEnderecoDTO(),
            "PIX",
            criarPixDTO(),
            null,
            null,
            java.util.List.of(item1, item2)
        );
    }

    @Test
    public void testGetAll() {
        given()
            .when().get("/pedidos")
            .then()
            .statusCode(anyOf(equalTo(200), equalTo(400))); 
    }

    @Test
    public void testIncluir() {
        Long idCliente = criarCliente();
        PedidoDTO dto = criarPedidoDTO(idCliente);

        given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(dto)
            .when().post("/pedidos")
            .then()
            .statusCode(201)
            .body("id", notNullValue())
            .body("cliente.nome", equalTo("Cliente Teste"))
            .body("formaPagamento", equalTo("Pix"));
    }

    @Test
    public void testGetByCliente() {
        Long idCliente = criarCliente();
        PedidoDTO dto = criarPedidoDTO(idCliente);

        given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(dto)
            .when().post("/pedidos")
            .then().statusCode(201);

        given()
            .when().get("/pedidos/cliente/" + idCliente)
            .then()
            .statusCode(200)
            .body("$", notNullValue());
    }

    @Test
    public void testAlterar() {
        Long idCliente = criarCliente();
        PedidoDTO dto = criarPedidoDTO(idCliente);

        Long idPedido = given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(dto)
            .when().post("/pedidos")
            .then().statusCode(201)
            .extract().jsonPath().getLong("id");

        PedidoDTO dtoAlterado = new PedidoDTO(
            idCliente,
            criarEnderecoDTO(),
            "BOLETO",
            null,
            criarBoletoDTO(),
            null,
            dto.itens()
        );

        given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(dtoAlterado)
            .when().put("/pedidos/" + idPedido)
            .then().statusCode(204);
    }

    @Test
    public void testPagar() {
        Long idCliente = criarCliente();
        PedidoDTO dto = criarPedidoDTO(idCliente);

        Long idPedido = given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(dto)
            .when().post("/pedidos")
            .then().statusCode(201)
            .extract().jsonPath().getLong("id");

        given()
            .when().put("/pedidos/" + idPedido + "/pagar")
            .then().statusCode(204);
    }

    @Test
    public void testFinalizar() {
        Long idCliente = criarCliente();
        PedidoDTO dto = criarPedidoDTO(idCliente);

        Long idPedido = given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(dto)
            .when().post("/pedidos")
            .then().statusCode(201)
            .extract().jsonPath().getLong("id");

        given()
            .when().put("/pedidos/" + idPedido + "/finalizar")
            .then()
            .statusCode(200)
            .body(equalTo("Pedido finalizado com sucesso."));
    }

    @Test
    public void testDelete() {
        Long idCliente = criarCliente();
        PedidoDTO dto = criarPedidoDTO(idCliente);

        Long idPedido = given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(dto)
            .when().post("/pedidos")
            .then().statusCode(201)
            .extract().jsonPath().getLong("id");

        given()
            .when().delete("/pedidos/" + idPedido)
            .then().statusCode(204);
    }
}