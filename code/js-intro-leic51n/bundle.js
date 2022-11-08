// 1. The wrapped and transformed modules
// -- main
function main(require, exports) {
    // import { logGreetings } from "./module1.js"
    const { logGreetings } = require("./module1.js")
    // import { msg } from "./module2.js"
    const { msg } = require("./module2.js")

    logGreetings()
    console.log(msg)
}

// -- module1
function module1(require, exports) {
    // import { msg } from './module2.js'
    const { msg } = require("./module2.js")

    //export function logGreetings() {
    //    console.log(msg)
    //}
    exports.logGreetings = function logGreetings() {
        console.log(msg)
    }
}

// -- module2
function module2(require, exports) {
    //export const msg = "Hello World"
    exports.msg = "Hello World"

    // not exported
    function logGreetings() {
        console.log(msg)
    }
}

// 2. Some infrastructure data structures and functions
const moduleTable = {
    "./main.js": main,
    "./module1.js": module1,
    "./module2.js": module2,
}

const exportsTable = {

}

function require(moduleName) {
    const exports = exportsTable[moduleName]
    if(exports) {
        return exports
    }
    const newExports = {}
    exportsTable[moduleName] = newExports
    moduleTable[moduleName](require, newExports)
    return newExports
}

// start the show by requiring the entry point
require("./main.js")
