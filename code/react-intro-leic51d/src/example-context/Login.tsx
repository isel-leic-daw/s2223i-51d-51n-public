import * as React from 'react'
import { useLoggedIn, useSetLogin } from './Authn'

export function Login() {
    const loggedIn = useLoggedIn()
    const setLogin = useSetLogin()

    return (
        <div>
            <button onClick={() => setLogin(!loggedIn)}>
                {loggedIn ? "Logout" : "Login"}
            </button>
        </div>
    )
}