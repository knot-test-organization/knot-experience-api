package com.nttdata.knot.baseapi.Services;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.nttdata.knot.baseapi.Interfaces.IGithubService;
import com.nttdata.knot.baseapi.Interfaces.Old_Interfaces.IUserService;
import com.nttdata.knot.baseapi.Models.UserPackage.UserFront;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserService implements IUserService {

    private IGithubService githubService;

    public UserService(IGithubService githubService) {
        this.githubService = githubService;
    }

    
    
    @Override
    public Mono<List<UserFront>> getUserList() {
        return this.githubService.getGithubUserList()
                .flatMapMany(Flux::fromIterable)
                .map(user -> new UserFront(user.getName(), user.getId(), user.getName()))
                .collectList();
    }

    @Override
    public Mono<UserFront> getUserByName(String name) {
        return this.githubService.getGithubUserList()
                .flatMapMany(Flux::fromIterable)
                .filter(user -> name.equals(user.getName()))
                .next()
                .map(selectedUser -> new UserFront(selectedUser.getName(), selectedUser.getId(),
                        selectedUser.getName()));
    }

    
}
