package com.ecommerce;

import com.ecommerce.usermgmt.model.UserModel;
import com.ecommerce.usermgmt.repository.UserRepository;
import io.quarkus.arc.impl.ParameterizedTypeImpl;
import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.vertx.VertxContextSupport;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.util.List;

import static io.restassured.RestAssured.given;

@QuarkusTest
@TestHTTPEndpoint(UserResource.class)
@QuarkusTestResource(DatabaseTestResource.class)
//@TestHTTPEndpoint can also be applied at the class level, in which case REST-assured will automatically prefix all
// requests with the Path of the UserResource:
@Slf4j
public class UserResourceTest {

    @Inject
    private UserRepository repo;

    @AfterEach

    void tearDown() throws Throwable {

        VertxContextSupport.subscribeAndAwait(() -> // offload to the correct Vert.x context
                Panache.withTransaction(() -> { // use a tx to persist the Book entity
                    return repo.deleteAll();
                }));

    }
    @Test
    public void whenPostUser_shouldReturnCreatedUser() {
        final UserModel input = new UserModel(null, "Alice", "alice@exampe.con", "9831319123");
        UserModel output = postOrPutUser(input, Method.POST);
        Assertions.assertEquals(input.getName(), output.getName());
        Assertions.assertEquals(input.getEmail(), output.getEmail());
        Assertions.assertEquals(input.getMobile(), output.getMobile());
        Assertions.assertTrue(output.getId() > 0);

    }

    @Test
    public void whenPutUser_shouldUpdateUser() {
        final UserModel input = new UserModel(null, "Alice", "alice@exampe.con", "9831319123");
        final UserModel postOutput = postOrPutUser(input, Method.POST);
        final UserModel putInput = new UserModel(postOutput.getId(), postOutput.getName(), postOutput.getEmail(), "7467526459");
        final UserModel putOutput = postOrPutUser(putInput, Method.PUT);
        Assertions.assertEquals(putInput.getName(), putOutput.getName());
        Assertions.assertEquals(putInput.getEmail(), putOutput.getEmail());
        Assertions.assertEquals(putInput.getMobile(), putOutput.getMobile());
    }

    @Test
    public void whenGetUser_shouldRetrieve() {
        final UserModel input = new UserModel(null, "Alice", "alice@exampe.con", "9831319123");
        UserModel output = postOrPutUser(input, Method.POST);
        UserModel getOutput = getUser(output.getId());
        Assertions.assertEquals(output.getName(), getOutput.getName());
        Assertions.assertEquals(output.getEmail(), getOutput.getEmail());
        Assertions.assertEquals(output.getMobile(), getOutput.getMobile());
    }

    @Test
    public void whenListUser_shouldRetrieve() {
        postOrPutUser(new UserModel(null, "Alice", "alice@exampe.con", "9831319123"), Method.POST);
        postOrPutUser(new UserModel(null, "Bob", "bob@exampe.con", "9330929329"), Method.POST);
        List<UserModel> users = listUser();
        Assertions.assertEquals(2, users.size());
    }

    private List<UserModel> listUser() {
        Type returnType = new ParameterizedTypeImpl(List.class, UserModel.class);
        return given()
                .accept(ContentType.JSON)
                .when()
                .get("/list")
                .then()
                .statusCode(200)
                .extract()
                .as(returnType);
    }

    private UserModel getUser(long id) {
        return given()
                .accept(ContentType.JSON)
                .when()
                .get("/id/{id}", id)
                .then()
                .statusCode(200)
                .extract()
                .as(UserModel.class);
    }

    private UserModel postOrPutUser(UserModel input, Method method) {
        return given()
                .contentType(ContentType.JSON)
                .body(input)
                .when()
                .request(method)
                .then()
                .statusCode(200)
                .extract()
                .as(UserModel.class);
    }

}