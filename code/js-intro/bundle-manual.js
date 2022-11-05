// Single "bundle" file ...

// ... with all the modules wrapped inside functions that provide the `require` and `exports` symbols.

function main(require, exports) {
    // import { sayHello } from './module1.js'
    // ... with the `import` statements replaced with `require` calls
    const { sayHello } = require('./module1.js')

    let a = "hello"
    let div = document.getElementById("the-div")
    console.log(a)
    console.log(div)
    sayHello()
}

function module1(require, exports) {

    //import { theMessage } from './module2.js'
    const { theMessage } = require('./module2.js')

    console.log("module1 running its code.")

    const somethingPrivateToThisModule = 42

    //export function sayHello() { console.log(`module1 says ${theMessage}`) }
    // ... and with `export` statements replaced with `exports` object assignments.
    exports.sayHello = function sayHello() { console.log(`module1 says ${theMessage}`) }

}

function module2(require, exports) {

    console.log("module2 running its code.")

    const somethingPrivateToThisModule = "42"

    // export const theMessage = "Hello"
    exports.theMessage = "Hello"

}

// ... and some infrastructure code

// ... the global immutable object mapping all the module names to the module functions, i.e., the function containing the module code inside.
const moduleTable = {
    './module1.js': module1,
    './module2.js': module2,
    './main.js': main,
}

// ... the global mutable object mapping all the module names to the exported objects, filled up as the modules are executed.
const exportsTable = {

}

function require(moduleName) {
    // First check if the module already executed and the exported object exists
    const moduleExports = exportsTable[moduleName]
    if(moduleExports) {
        console.log(`Module '${moduleName}' already loaded`)
        return moduleExports
    }

    // If not, create an empty export object and use it on the module execution
    const newModuleExports = {}
    exportsTable[moduleName] = newModuleExports
    const moduleFunction = moduleTable[moduleName]
    if(!moduleFunction) {
        throw new Error(`Module ${moduleName} not found`)
    }
    console.log(`Loading module '${moduleName}'`)
    moduleFunction(require, newModuleExports)
    console.log(`Loaded module '${moduleName}'`)

    return newModuleExports
}

// ... finally, run the entry point module.
require('./main.js')

// TODO does not handle reentrancy