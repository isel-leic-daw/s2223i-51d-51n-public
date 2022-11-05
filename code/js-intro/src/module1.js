import { theMessage } from './module2.js'

console.log("module1 running its code, and using webpack serve.")

const somethingPrivateToThisModule = 42

export function sayHello() { 
    const elem = document.getElementById("the-div")
    const textNode = document.createTextNode("Hello 2")
    elem.appendChild(textNode)
 }