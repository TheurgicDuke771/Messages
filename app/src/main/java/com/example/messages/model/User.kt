package com.example.messages.model

class User (val uid: String, val username: String, val email: String) {
    constructor(): this("","","")
}