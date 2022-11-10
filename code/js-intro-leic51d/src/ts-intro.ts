// primitive types

const aString: string = "hello"
const aNumber: number = 42.0
const aBoolean: boolean = true

// arrays
const aStringArray: Array<string> = ["hello", "world"]

// objects
const alice: { name: string, nbr: number } = {
    name: "Alice",
    nbr: 1234
}

type Student = { name: string, nbr: number }
type Foo = { name: string, nbr: number }

const aux: Student = alice
const foo: Foo = aux

// Sum types
function doSomething(input: string | number) {

    if (typeof input === "string") {
        console.log(input.toUpperCase())
    } else {
        console.log(input - 3)
    }
}

// Descriminated sum types
type Result = 
| {kind: "network-error", error: Error}
| {kind: "http-response", status: number}

function doAnotherThing(result: Result) {
    switch(result.kind) {
        case "network-error": {
            console.log(result.error)
            break;
        }
        
    }
}