import * as React from 'react'
import {
    useState,
    useEffect,
} from 'react'

type TimerProps = {

}
export function Timer({ }: TimerProps): React.ReactElement {
    const [counter, setCounter] = useState(0)
    const [period, setPeriod] = useState(1000)
    useEffect(() => {
        console.log(`Effect is running with period=${period}`)
        const tid = setInterval(
            () => setCounter((oldState) => oldState + 1),
            period,
        )
        return () => {
            console.log(`Cancel effect with period=${period}`)
            clearInterval(tid)
        }
    }, [period, setCounter])

    console.log("Timer is returning")
    return (
        <div>
            <span>{counter}</span>
            <button onClick={() => setPeriod(period-500)}>-</button>
            {period}
            <button onClick={() => setPeriod(period+500)}>+</button>
        </div>
    )
}