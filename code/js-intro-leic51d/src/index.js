import { sayHello } from "./module1.js"
import { msg } from "./module2.js"
import { add } from "./arith"

sayHello()
console.log(msg.toUpperCase())

const div = document.getElementById("the-div")
div.appendChild(document.createTextNode("Hello World again again"))

console.log(add(2,3))
