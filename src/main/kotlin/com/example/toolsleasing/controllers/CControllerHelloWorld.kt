package com.example.toolsleasing.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class CControllerHelloWorld {

    @GetMapping("/")
    fun blog(): String {
        return "Hello world!"
    }

}