import * as React from 'react'
import { useLoggedin, useLogin } from './authnStatus'

export function Login() {
    const loggedin = useLoggedin()
    const setLoggedin = useLogin()

    return (
        <div>
            <h3>Login</h3>
            <button onClick={() => setLoggedin(!loggedin)}>
                {loggedin ? "logout" : "login"}
            </button>
        </div>
    )
}