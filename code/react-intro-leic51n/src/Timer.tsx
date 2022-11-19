import * as React from 'react'
import {
    useState,
    useEffect,
} from 'react'

export function Timer({ label }: { label: string }) {
    const [counter, setCounter] = useState(0)
    const [period, setPeriod] = useState(1000)
    useEffect(
        () => {
            console.log("setInterval")
            const tid = setInterval(
                () => {
                    setCounter(currState => currState + 1)
                },
                period
            )
            return () => {
                console.log("clearInterval")
                clearInterval(tid)
            }
        },
        [period, setCounter]
    )
    return(
        <div>
            <div>{label}:{counter}</div>
            <div>
                <button onClick={() => setPeriod(period-500)}>-</button>
                period: {period}
                <button onClick={() => setPeriod(period+500)}>+</button>
            </div>
        </div>
    )
}
