/** @jsx createElement */

import { createElement } from "./createElement"

const root = document.getElementById("the-div")

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

const students: Array<Student> = [alice, bob]

function renderStudents(students: Array<Student>): HTMLElement {
    return createElement(
        "ol", {},
        students.map(student => 
            createElement(
                "li", {},
                createElement(
                    "dl", {},
                    createElement(
                        "dt", {style: "background-color: red"}, "Name"
                    ),
                    createElement(
                        "dd", {}, student.name
                    ),
                    createElement(
                        "dt", {}, "Number"
                    ),
                    createElement(
                        "dd", {}, student.number.toString()
                    )
                )
            )
        )
    )           
}
/*
    <ol>
        <li>
            <dl>
                <dt>Name</dt>
                <dd>student.name>
                ...
            </dl>
        </li>
    </ol>
 */

function renderStudents2(students: Array<Student>): HTMLElement {
    return <ol>
        {students.map(student => <li>
            <dl>
                <dt style="background-color:red">Name</dt>
                <dd>{student.name}</dd>
                <dt>Number</dt>
                <dd>{student.number.toString()}</dd>
            </dl>
        </li>
        )}
    </ol>
}

root?.appendChild(renderStudents2(students))