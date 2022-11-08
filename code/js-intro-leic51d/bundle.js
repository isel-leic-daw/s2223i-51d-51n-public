// -- main
function main(require, exports) {
    // import { sayHello } from "./module1.js"
    // import { msg } from "./module2.js"
    const { sayHello } = require("./module1.js")
    const { msg } = require("./module2.js")


    sayHello()
    console.log(msg.toUpperCase())
}

// -- module1
function module1(require, exports) {
    // import { msg } from './module2.js'
    const { msg } = require("./module2.js")

    exports.sayHello = function sayHello() {
        console.log(msg)
    }
}

// -- module2
function module2(require, exports) {
    const msg = "Hello world from module2"
    exports.msg = msg
    console.log(msg)
    const elem = document.getElementById("the-div")
    console.log(elem)
}

// -- infrastructure

const moduleTable = {
    "./module1.js": module1,
    "./module2.js": module2,
    "./main.js": main,
}

const exportsTable = {

}

function require(moduleName) {

    const moduleExports = exportsTable[moduleName]
    if(moduleExports) {
        return moduleExports
    }
    const newModuleExports = {}
    moduleTable[moduleName](require, newModuleExports)
    exportsTable[moduleName] = newModuleExports
    return newModuleExports
}

require("./main.js")
