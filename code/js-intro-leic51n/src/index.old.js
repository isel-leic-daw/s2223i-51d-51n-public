import { logGreetings } from "./module1.js"
import { msg } from "./module2.js"
import * as URITemplate from 'urijs/src/URITemplate'

logGreetings()
const template = new URITemplate("https://example.com/games/{gid}")
const uri = template.expand({
    gid: "this is the game id"
})

const div = document.getElementById("the-div")
div.appendChild(
    document.createTextNode(uri.toString())
)
div.appendChild(
        document.createTextNode("It auto updates!")
    )