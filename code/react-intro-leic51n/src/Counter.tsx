import * as React from 'react'
import {
    useState
} from 'react'

type CounterProps = {
    label: string,
    value: number,
    onClick: (value: number) => void,
}
export function Counter({ label, value, onClick }: CounterProps) {

    const [clicks, setClicks] = useState(0)

    console.log("running Counter")
    return (
        <div>
            {label}:
            <button onClick={() => { setClicks(clicks + 1); onClick(value - 1) }}>-</button>
            {value}, {clicks}
            <button onClick={() => { setClicks(clicks + 1); onClick(value + 1) }}>+</button>
        </div>
    )

}
