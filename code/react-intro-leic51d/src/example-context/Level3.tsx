import * as React from 'react'
import { useLoggedIn } from './Authn'

export function Level3() {
    const loggedin = useLoggedIn()
    return (
        <div>
            <h3>Level 3</h3>
            Logged in: {loggedin.toString()}
        </div>
    )
}