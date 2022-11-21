import * as React from 'react'
import {
    useContext
} from 'react'
import { useLoggedin } from './authnStatus'

export function Level3() {
    const loggedin = useLoggedin()
    return (
        <div>
            <h3>Level 3</h3>
            {`Logged in? ${loggedin}`}
        </div>
    )
}