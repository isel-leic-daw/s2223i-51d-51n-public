import * as React from 'react'
import {createRoot} from 'react-dom/client'

const root = createRoot(document.getElementById("the-div"))
/*
root.render(
    React.createElement("ul", null,
        React.createElement("li", null, "item 1"),
        React.createElement("li", null, "item 2"),
        React.createElement("li", null, "item 3"),
    )
)*/

type Student = {
    name: string,
    number: number,
}

let model: Array<Student> = []
let currentSize = 0

function addStudentToModel() {
    model.push({
        name: `student-${currentSize}`,
        number: currentSize,
    })
    currentSize +=  1
}

// yes, the Students function is also a
function Students({students}:{students: Array<Student>}): React.ReactElement {
    return (
        <ol>
            {students.map(student => (
                // *Element* that uses the *component* Student
                // <Student student={student} />
                <li key={student.number}>
                    <Student student={student} />
                </li>
            ))}
        </ol>
    )
}

// This function is called a *component*
// props -> element tree
function Student({student}: {student: Student}): React.ReactElement {
    return(
        <dl>
            {/* React.createElement("dt", null, "Name") */} 
            <dt>Name</dt>
            <dd>{student.name}</dd>
            <dt>Number</dt>
            <dd>{student.number}</dd>
        </dl>
    )
}

setInterval(() => {
    addStudentToModel()
    root.render(
        <Students students={model} />
    )
}, 1000)
