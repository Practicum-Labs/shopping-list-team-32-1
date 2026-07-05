package com.practicum.shoppinglist.domain.model

sealed class AuthError : Throwable() {
    class InvalidEmail : AuthError()
    class WeakPassword : AuthError()
    class UserAlreadyExists : AuthError()
    class InvalidCredentials : AuthError()
    class Unauthorized : AuthError()
    class Network : AuthError()
    class Server : AuthError()
    class Unknown : AuthError()
}
