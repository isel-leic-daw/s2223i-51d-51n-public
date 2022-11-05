import { sayHello } from './module1.js'
import * as URITemplate from 'urijs/src/URITemplate'
import { add } from './module3'

let a = "hello"
let div = document.getElementById("the-div")
console.log(a)
console.log(div)
sayHello()

const uriTemplate: any = new URITemplate("https://example.com/some/path/{id}")
const uri = uriTemplate.expand({
    id: "value with spaces and a /"
})
console.log(`The URI is ${uri}`)
const res = add(2, 3)
console.log(`2 + 3 = ${res}`)
