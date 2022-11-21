import * as React from 'react'
import {
    useState
} from 'react'
import { Counter } from './Counter'

export function CounterList() {
    const [firstCounter, setFirstCounter] = useState(0)
    const [secondCounter, setSecondCounter] = useState(0)

    return (
        <div>
            <Counter label="first" value={firstCounter} onClick={(value)=>setFirstCounter(value)}/>
            <Counter label="second" value={secondCounter} onClick={(value)=>setSecondCounter(value)}/>
            Sum: {firstCounter + secondCounter}
        </div>
    )
}