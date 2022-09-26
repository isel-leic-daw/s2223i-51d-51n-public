package com.example.demo.controllers

import com.example.demo.CustomOutputModel
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

data class StudentOutputModel (
    val studentName: String,
    val studentNumber: Int,
)

@RestController
@RequestMapping("mc-examples")
class MessageConversionExamplesController {

    // Using simple strings
    @GetMapping("0")
    fun handler0() = "Hello Web"

    // Using output models
    @GetMapping("1")
    fun handler1() = StudentOutputModel("Alice", 12345)

    @PostMapping("1.1")
    fun handler11(
        @RequestBody student: StudentOutputModel
    ) = "Received $student"

    // using `ResponseEntity` that allows controlling other parts of the response message
    @GetMapping("2")
    fun handler2() = ResponseEntity
        .status(201)
        .contentType(MediaType.parseMediaType("application/vnd.isel.student+json"))
        .header("foo", "bar")
        .body(StudentOutputModel("Alice", 12345))

    // using custom message converters
    @GetMapping("3")
    fun handler3() = URI("https://www.example.com")

    // automatically controlling the media-type
    @GetMapping("4")
    fun handler4() = CustomOutputModel()

}