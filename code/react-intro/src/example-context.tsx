import React, {
    useEffect,
    useState,
    useContext,
    createContext,
} from "react"

import { range } from "./range"

const ExampleContext = createContext({})

function GrandChild({ }) {
    const ctx = useContext(ExampleContext)
    return (
        <div>
            {JSON.stringify(ctx)}
        </div>
    )
}

function Child({ }) {
    return (
        <GrandChild />
    )
}

export function App({ }) {
    const [counter, setCounter] = useState(0)
    useEffect(() => {
        const tid = setInterval(() => setCounter(oldCounter => oldCounter + 1), 1000)
        return () => {
            clearInterval(tid)
        }
    })
    return (
        <ExampleContext.Provider value={{counter: counter}}>
            <Child />
        </ExampleContext.Provider>
    )
}
