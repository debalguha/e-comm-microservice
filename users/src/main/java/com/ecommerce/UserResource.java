package com.ecommerce;

import com.ecommerce.usermgmt.mapper.Mappers;
import com.ecommerce.usermgmt.model.UserModel;
import com.ecommerce.usermgmt.repository.UserRepository;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

@Path("/user")
@Slf4j
public class UserResource {

    @Inject
    UserRepository userRepository;

    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> allUsers() {
        return userRepository.findAllUsers()
                .map(s -> s.map(Mappers::map).toList())
                .onItem().transform(Response::ok)
                .onItem().transform(Response.ResponseBuilder::build);
    }

    @GET
    @Path("/id/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> userById(@PathParam("id") long id) {
        return userRepository.findUserById(id)
                .map(Mappers::map)
                .onItem().transform(Response::ok)
                .onItem().transform(Response.ResponseBuilder::build);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> createUser(UserModel user) {
        log.info("Entry!!");
        return userRepository.saveOrUpdateUser(user)
                .map(Mappers::map)
                .onItem().transform(Response::ok)
                .onItem().transform(Response.ResponseBuilder::build);
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> updateUser(UserModel user) {
        return userRepository.saveOrUpdateUser(user)
                .map(Mappers::map)
                .onItem().transform(Response::ok)
                .onItem().transform(Response.ResponseBuilder::build);
    }
}
