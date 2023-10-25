package com.nttdata.knot.baseapi.Interfaces.Old_Interfaces;

import java.util.List;

import com.nttdata.knot.baseapi.Models.UserPackage.UserFront;

import reactor.core.publisher.Mono;

public interface IUserService {

    Mono<List<UserFront>> getUserList();

    Mono<UserFront> getUserByName(String name);

}