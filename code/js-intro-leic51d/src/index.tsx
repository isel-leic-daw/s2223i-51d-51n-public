/** @jsx createElement */
import { createElement } from "./createElement"

/*
const exampleElement = createElement(
    'ul',
    { a: "b", c: true },
    createElement('li', {}, "item1"),
    createElement('li', {}, "item2"),
    createElement('li', {}, "item3")
)
*/

// Extension to the JavaScript language called JSX
// In typescript TSX
const exampleElement: HTMLElement = <ul a="b" c>
    <li>item1</li>
    <li>item2</li>
    <li>item3</li>
    <li>item4</li>
    <li>{(2+3).toString()}</li>
    <ul>
        {["hello","world"].map(it => <li>it</li>)}
    </ul>
</ul>
    

const root = document.getElementById("the-div")

root.appendChild(exampleElement)

type Student = {
    name: string,
    number: number,
    address?: string
}

const students: Array<Student> = [
    { name: "Alice", number: 12345 },
    { name: "Bob", number: 23456 },
]

function renderStudents(students: Array<Student>): HTMLElement {

    return createElement(
        "ol",
        {},
        students.map(student => createElement(
            "li",
            {},
            createElement(
                "dl",
                {},
                createElement("dt", {}, "Name:"),
                createElement("dd", {}, student.name),
                createElement("dt", {}, "Number:"),
                createElement("dd", {}, student.number.toString()),
            ))
        )
    )
}

root.appendChild(renderStudents(students))
