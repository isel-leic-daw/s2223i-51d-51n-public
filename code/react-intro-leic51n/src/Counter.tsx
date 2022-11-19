import * as React from 'react'
import {
    useState
} from 'react'

export function Counter({ label }: { label: string }) {

    const [counter, setCounter] = useState(0)
    const [clicks, setClicks] = useState(0)
    
    console.log("running Counter")
    return (
        <div>
            <button onClick={() => { setCounter(counter - 1); setClicks(clicks + 1) }}>-</button>
            {counter}, {clicks}
            <button onClick={() => { setCounter(counter + 1); setClicks(clicks + 1) }}>+</button>
        </div>
    )

}
