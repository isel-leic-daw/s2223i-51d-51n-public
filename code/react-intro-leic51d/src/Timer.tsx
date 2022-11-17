import * as React from 'react'
import {
    useState,
    useEffect,
} from 'react'

type TimerProps = {

}
export function Timer({ }: TimerProps): React.ReactElement {
    const [counter, setCounter] = useState(0)
    useEffect(() => {
        console.log("Effect is running")
        const tid = setInterval(
            () => setCounter((oldState) => oldState + 1),
            1000,
        )
        return () => {
            console.log("Cancel effect")
            clearInterval(tid)
        }
    }, [])

    console.log("Timer is returning")
    return (
        <div>
            <span>{counter}</span>
            <button onClick={() => setCounter(0)}>clear</button>
        </div>
    )
}