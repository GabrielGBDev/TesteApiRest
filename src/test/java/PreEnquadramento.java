import static io.restassured.RestAssured.*;

import io.restassured.http.ContentType;
import org.junit.Test;
import static org.hamcrest.Matchers.*;

public class PreEnquadramento {
    private static final String CNPJ_PAYLOAD = "11091989000102";

    @Test
    public void Enquadrador(){
        String accessToken = given()
                    .header("Authorization", "Basic QVBSREY1ODhjb0hGbk4zY0xUSXMyS2VTdGljYTpWX3g2bUxFVUtLRE5KbFZvRUJpdlZGUGxhaklh")
                    .contentType("application/x-www-form-urlencoded")
                    .formParam("grant_type", "client_credentials")
                .when()
                    .post("https://apis-gateway-h.bndes.gov.br/token")
                .then()
                    .statusCode(200)
                    .body("access_token", notNullValue())
                    .extract().path("access_token");

        String idRequisicao = given()
                    .header("Authorization", "Bearer " + accessToken)
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(CNPJ_PAYLOAD)
                .when()
                    .post("https://apis-gateway-h.bndes.gov.br/bcd/v0/pre-enquadramento")
                .then()
                    .statusCode(anyOf(is(200), is(202)))
                    .body("id", notNullValue())
                    .extract().path("id");

        given()
                    .header("Authorization", "Bearer " + accessToken)
                    .accept(ContentType.JSON)
                .when()
                    .get("https://apis-gateway-h.bndes.gov.br/bcd/v0/pre-enquadramento/" + idRequisicao)
                .then()
                    .statusCode(200)
                    .log().body();
    }
}
