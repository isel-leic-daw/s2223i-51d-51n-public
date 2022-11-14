import React, {
    useEffect,
    useState
} from "react"

import { range } from "./range"

function Counter({ }) {
    const [counter, setCounter] = useState(0)
    return (
        <div>
            <button onClick={() => setCounter(counter - 1)} >-</button>
            {counter}
            <button onClick={() => setCounter(counter + 1)} >+</button>
        </div>
    )
}

function CounterList({ }) {
    const [counter, setCounter] = useState(10)
    return (
        <>
            <button onClick={() => setCounter(counter + 1)}>Add counter</button>
            {range(counter).map(i => <Counter key={i} />)}

        </>
    )
}

function ToggleVisible({ children }: { children: React.ReactNode }) {
    const [visible, setVisible] = useState(true)
    return (
        <>
            <button onClick={() => setVisible(!visible)}>Toggle</button>
            {visible ?<div>{children}</div> : <div></div>}
        </>
    )
}

function ToggleVisible2({ children }: { children: React.ReactNode }) {
    const [visible, setVisible] = useState(true)
    return (
        <>
            <button onClick={() => setVisible(!visible)}>Toggle</button>
            <div hidden={!visible}>{children}</div>
        </>
    )
}

export function App({ }) {
    return (
        <ToggleVisible2>
            <CounterList />
        </ToggleVisible2>
    )
}