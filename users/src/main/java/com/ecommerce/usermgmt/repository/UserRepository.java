package com.ecommerce.usermgmt.repository;

import com.ecommerce.usermgmt.entity.User;
import com.ecommerce.usermgmt.mapper.Mappers;
import com.ecommerce.usermgmt.model.UserModel;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.quarkus.hibernate.reactive.panache.common.WithSessionOnDemand;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Optional.ofNullable;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {
    @WithSessionOnDemand
    public Uni<Stream<User>> findAllUsers() {
        return listAll()
                .map(Collection::stream);
    }

    @WithSessionOnDemand
    public Uni<User> findUserById(long id) {
        return findById(id);
    }

    @WithTransaction
    public Uni<User> saveOrUpdateUser(UserModel userModel) {

        return ofNullable(userModel.getId())
                .map(this::findById)
                .map(userUni -> userUni.map(user -> copy(user, Mappers.map(userModel))))
                .map(userUni -> userUni.flatMap(this::persist))
                .orElseGet(() -> this.persist(Mappers.map(userModel)));

    }

    private static User copy(User existing, User newUser) {
        existing.setName(newUser.getName());
        existing.setEmail(newUser.getEmail());
        existing.setMobile(newUser.getMobile());
        return existing;
    }
}
