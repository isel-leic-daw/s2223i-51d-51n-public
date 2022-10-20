package pt.isel.daw.tictactow.http

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import pt.isel.daw.tictactow.Either
import pt.isel.daw.tictactow.domain.User
import pt.isel.daw.tictactow.http.model.Problem
import pt.isel.daw.tictactow.http.model.UserCreateInputModel
import pt.isel.daw.tictactow.http.model.UserCreateTokenInputModel
import pt.isel.daw.tictactow.http.model.UserHomeOutputModel
import pt.isel.daw.tictactow.http.model.UserTokenCreateOutputModel
import pt.isel.daw.tictactow.service.TokenCreationError
import pt.isel.daw.tictactow.service.UserCreationError
import pt.isel.daw.tictactow.service.UsersService

@RestController
class UsersController(
    private val userService: UsersService
) {

    @PostMapping(Uris.Users.CREATE)
    fun create(@RequestBody input: UserCreateInputModel): ResponseEntity<*> {
        val res = userService.createUser(input.username, input.password)
        return when (res) {
            is Either.Right -> ResponseEntity.status(201)
                .header(
                    "Location",
                    Uris.Users.byId(res.value).toASCIIString()
                ).build<Unit>()
            is Either.Left -> when (res.value) {
                UserCreationError.InsecurePassword -> Problem.response(400, Problem.insecurePassword)
                UserCreationError.UserAlreadyExists -> Problem.response(400, Problem.userAlreadyExists)
            }
        }
    }

    @PostMapping(Uris.Users.TOKEN)
    fun token(@RequestBody input: UserCreateTokenInputModel): ResponseEntity<*> {
        val res = userService.createToken(input.username, input.password)
        return when (res) {
            is Either.Right -> ResponseEntity.status(200)
                .body(UserTokenCreateOutputModel(res.value))
            is Either.Left -> when (res.value) {
                TokenCreationError.UserOrPasswordAreInvalid -> Problem.response(400, Problem.userOrPasswordAreInvalid)
            }
        }
    }

    @GetMapping(Uris.Users.GET_BY_ID)
    fun getById(@PathVariable id: String) {
        TODO("TODO")
    }

    @GetMapping(Uris.Users.HOME)
    fun getUserHome(user: User): UserHomeOutputModel {
        return UserHomeOutputModel(
            id = user.id.toString(),
            username = user.username,
        )
    }
}