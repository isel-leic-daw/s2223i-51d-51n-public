import * as React from 'react'
import {
    useState
} from 'react'
import { createRoot } from 'react-dom/client'
import {Timer} from './timer'

const root = createRoot(document.getElementById("the-div"))

/*
const tree = React.createElement("ul", null,
    React.createElement("li", null, "item-1"),
    React.createElement("li", null, "item-2"))
*/

const tree = <ul>
    <li>item-1</li>
    <li>item-2</li>
    <li>item-3</li>
</ul>

type Student = {
    name: string,
    number: number
}

let students: Array<Student> = []

function addStudent() {
    const len = students.length
    students.push({
        name: `student-${len}`,
        number: len
    })
}

function renderStudents(students: Array<Student>): React.ReactElement {
    return (
        <ol>
            {students.map(student => <li key={student.number}>
                <dl>
                    <dt>Name</dt><dd>{student.name}</dd>
                    <dt>Number</dt><dd>{student.number}</dd>
                </dl>
            </li>
            )}
        </ol>
    )
}

function ShowStudents({ students }: { students: Array<Student> }): React.ReactElement {
    return (
        // React.createElement("ol", null, ...) 
        <ol>
            {students.map(student => <li key={student.number}>
                {/* React.createElement(ShowStudent, {student: student, a:"b"}) */}
                <ShowStudent student={student} />
            </li>
            )}
        </ol>
    )
}

// ShowStudent is a component
function ShowStudent({ student }: { student: Student }): React.ReactElement {
    return (
        <dl>
            <dt>Name</dt><dd>{student.name}</dd>
            <dt>Number</dt><dd>{student.number}</dd>
        </dl>
    )
}

/*
setInterval(() => {
    addStudent()
    root.render(<ShowStudents students={students} />)
}, 1000)
*/

type CounterProps = {
    onClick?: () => void
}
function Counter({onClick} : CounterProps): React.ReactElement {
    // 1. Declare the used state
    const [counter, setCounter] = useState(0) // first call to useState, use state slot 0
    const [counter2, setCounter2] = useState(0) // second call to useState, use state slot 1

    const handleDown = () => {
        setCounter(counter - 1)
        setCounter2(counter2 + 1)
        if(onClick) {
            onClick()
        }
    }

    const handleUp = () => {
        setCounter(counter + 1)
        setCounter2(counter2 + 1)
        if(onClick) {
            onClick()
        }
    }

    // 2. Produce the element tree given the props and the state
    return (
        <div>
            <button onClick={handleDown}>-</button>
            {counter}, {counter2}
            <button onClick={handleUp}>+</button>
        </div>
    )
}

type CounterListProps = {
    length: number
}
function CounterList({ length }: CounterListProps) {
    const [totalClicks, setTotalClicks] = useState(0)
    const handleClick = () => {
        setTotalClicks(totalClicks + 1)
    }
    return (
        <div>
            <ol>
                {[...Array(length).keys()].map(ix => <li key={ix}><Counter onClick={handleClick}/></li>)}
            </ol>
            Total clicks = {totalClicks}
        </div>
    )
}

function CounterApp() {
    return (
        <CounterList length={5} />
    )
}

function ExpandableCounterList({ }) {
    const [nOfCounters, setNOfCounters] = useState(1)
    return (
        <div>
            <CounterList length={nOfCounters} />
            <p><button onClick={() => setNOfCounters(nOfCounters + 1)}>add counter</button></p>
            <p><button onClick={() => setNOfCounters(nOfCounters - 1)}>remove counter</button></p>
        </div>
    )
}

function ToggleTimer() {
    const [show, setShow] = useState(true)
    return (
        <div>
            <button onClick={() => setShow(oldShow => !oldShow)}>
                {show ? "hide" : "show"}
            </button>
            {show ? <Timer /> : null}
        </div>
    )
}

root.render(
    <div>
        <h2>Example</h2>
        <CounterApp />
    </div>
)

