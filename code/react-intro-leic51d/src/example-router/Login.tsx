import * as React from "react"
import { useState } from "react"
import { flushSync, unstable_batchedUpdates } from "react-dom"
import { Navigate, useLocation, useNavigate } from "react-router-dom"
import { useSetUser } from "./Authn"

function delay(delayInMs: number) {
    return new Promise((resolve, reject) => {
        setTimeout(() => resolve(undefined), delayInMs)
    })
}

export async function authenticate(username: string, password: string): Promise<string | undefined> {
    await delay(5000)
    if ((username == "alice" || username == "bob") && password == "1234") {
        return username
    }
    return undefined
}

export function Login() {
    console.log("Login")
    const [inputs, setInputs] = useState({
        username: "",
        password: "",
    })
    const [isSubmitting, setIsSubmitting] = useState(false)
    const [error, setError] = useState(undefined)
    const [redirect, setRedirect] = useState(false)
    const setUser = useSetUser()
    const navigate = useNavigate()
    const location = useLocation()
    if(redirect) {
        return <Navigate to={location.state?.source?.pathname || "/me"} replace={true}/>
    }
    function handleChange(ev: React.FormEvent<HTMLInputElement>) {
        const name = ev.currentTarget.name
        setInputs({ ...inputs, [name]: ev.currentTarget.value })
        setError(undefined)
    }
    function handleSubmit(ev: React.FormEvent<HTMLFormElement>) {
        ev.preventDefault()
        setIsSubmitting(true)
        const username = inputs.username
        const password = inputs.password
        authenticate(username, password)
            .then(res => {
                setIsSubmitting(false)
                if (res) {
                    console.log(`setUser(${res})`)
                    const redirect = location.state?.source?.pathname || "/me"
                    setUser(res)
                    setRedirect(true)
                } else {
                    setError("Invalid username or password")
                }
            })
            .catch(error => {
                setIsSubmitting(false)
                setError(error.message)
            })
    }
    
    return (
        <form onSubmit={handleSubmit}>
            <fieldset disabled={isSubmitting}>
                <div>
                    <label htmlFor="username">Username</label>
                    <input id="username" type="text" name="username" value={inputs.username} onChange={handleChange} />
                </div>
                <div>
                    <label htmlFor="password">Password</label>
                    <input id="password" type="text" name="password" value={inputs.password} onChange={handleChange} />
                </div>
                <div>
                    <button type="submit">Login</button>
                </div>
            </fieldset>
            {error}
        </form>
    )
}