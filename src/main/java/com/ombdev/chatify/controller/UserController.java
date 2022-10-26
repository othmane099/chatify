package com.ombdev.chatify.controller;

import com.ombdev.chatify.model.ActiveUser;
import com.ombdev.chatify.model.Storage;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin("http://127.0.0.1:3000")
public class UserController {

    @GetMapping("/active")
    public List<ActiveUser> list(){
        return Storage.activeUserList;
    }
}
