import React, { useEffect, useState } from "react"

function range(length: number) { return [...Array(length).keys()] }

type InputListProps = { size: number }
function InputList({ size }: InputListProps) {
    console.log("Rendering input list.")
    return (
        <ul>
            {range(size).map(i => <li key={i}><Input /></li>)}
        </ul>
    )
}

function Input({ }) {
    return <input type="text" />
}

export function App({ }) {
    console.log("Rendering App.")
    const [counter, setCounter] = useState(0)
    useEffect(
        () => {
            const tid = setInterval(() => setCounter(counter + 1), 1000)
            return () => {
                clearInterval(tid)
            }
        }
    )
    return (
        <>
            <h2>React First</h2>
            <h3>Counter = {counter}</h3>
            <InputList size={3} />
        </>
    )
}
