/*
 *  Basic types
 */
const aNumber: number = 2
const anotherNumber = 3 // anotherNumber is infered to have type number as well
const aString: string = "hello"
const aBoolean: boolean = true

/*
 *  Array type
 */
const aNumberArray: Array<number> = [1, 2, 3]
const aStringArray: Array<string> = ["hello", "world"]

/*
 *  Function types
 */
// The type is "(a: number, b: number) => number"
const aFunction: (a: number, b: number) => number = function (a: number, b: number) { return a + b }

/*
 *  Object types
 */
type Student = {
    name: string,
    number: number,
}

const alice: Student = {
    name: "Alice",
    number: 123, // cannot be "123"
    // cannot have extra properties
}

type AnotherStudentType = {
    name: string,
    number: number,
    emailAddress?: string,
    phoneNumber: string | undefined
}

const bob: AnotherStudentType = {
    name: "Bob",
    number: 234,
    phoneNumber: undefined,
}

/*
 *  Union Types
 */
type PhoneNumber = string | number

const phoneNumber1: PhoneNumber = "123"
const phoneNumber2: PhoneNumber = 123

function typeNarrowingExample(phoneNumber: PhoneNumber) {
    if (typeof phoneNumber === "string") {
        console.log(phoneNumber.toLowerCase) // can call toLowerCase on phoneNumber because the type was narrowed to string
    } else {
        console.log(phoneNumber - 1) // can use operator '-' on phoneNumber because the type was narrowed to string
    }
}

/*
 *  Descriminated Unions and literal types
 */

// "type" acts as the descriminator and enables type narrowing
type HttpResponseResult =
    | { type: "error", error: Error }
    | { type: "response", statusCode: number }
// What is the type of `type`? It's the type comprised by the single "error" string

function doSomethingWithHttpResponseResult(result: HttpResponseResult) {
    switch(result.type) {
        case "error": {
            const error: Error = result.error
            break
        }
        case "response": {
            const status: number = result.statusCode
            break
        }
        default: {
             // What can we get from result?
        }
    }
}
