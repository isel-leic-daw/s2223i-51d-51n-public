function sub(
    a: number,
    b: number
): number {
    return a - b
}

sub(1, 2) * 5

// Basic types
const aString: string = "Hello"
const aNumber: number = 123
const aBoolean: boolean = true

const itHasTypeInference = "yes"

// Arrays
const messages: Array<string> = ["hello", "world"]
const numbers: Array<number> = [1, 2]

// Union/sum types
type stringOrNumber = string | number
const something: Array<stringOrNumber> = ["hello", 123]
const anotherThing: Array<string | number> = ["hello", 123]
const yetAnotherThing: Array<stringOrNumber> = anotherThing
const itsANumber: stringOrNumber = 123
const itsAString: stringOrNumber = "hello"

function doSomething0(input: stringOrNumber) {
    if(typeof input == "string") {
        // type narrowing: type of input was narrowed from (string | number) to string
        input.toUpperCase()
    }else{
        // type narrowing: type of input was narrowed from (string | number) to number
        console.log(input - 7)
    }
}

doSomething0("abc")
doSomething0(42)

// Object types

type Student = {
    name: string,
    number: number,
}

const alice: Student = {
    name: "Alice",
    number: 123,
}

const bob: Student = {
    name: "Bob",
    number: 345,
}

type Teacher = {
    name: string,
    number: number,
}
const students: Array<Student> = [alice, bob]
const teachers: Array<Teacher> = [alice, bob]
const what: Array<{name: string, number: number}> = [alice, bob]

// Descriminated unions
type Result = 
| {kind: "network-error", error: Error}
| {kind: "http-response", status: number}

function doSomething2(result: Result) {
    switch(result.kind) {
        case "network-error": {
            console.log(result.error)
            break;
        }
        case "http-response": {
            console.log(result.status)
            break;
        }
    }
}
