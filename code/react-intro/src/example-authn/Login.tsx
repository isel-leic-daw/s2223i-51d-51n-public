import React from 'react'
import {
    useState
} from 'react'
import { useLocation, useNavigate } from 'react-router-dom'
import {
    authenticate
} from './authentication-stub'
import { useAuthentication } from './AuthnProvider'

export function Login(): React.ReactElement {

    const [inputs, setInputs] = useState({ username: "", password: "" })
    const [error, setError] = useState(undefined)
    const [isSubmitting, setIsSubmitting] = useState(false)
    const location = useLocation()
    const [username, setUsername] = useAuthentication()
    const navigate = useNavigate()

    function handleSubmit(ev: React.FormEvent<HTMLFormElement>) {
        ev.preventDefault()
        setIsSubmitting(true)
        const username = inputs.username
        authenticate(username, inputs.password)
            .then(res => {
                setIsSubmitting(false)
                if (res) {
                    setUsername(username)
                    navigate(location.state?.from?.pathname || "/me")
                } else {
                    setError("Invalid username or password")
                }
            })
            .catch(error => {
                setIsSubmitting(false)
                setError(error.message)
            })
    }

    function handleChange(ev: React.FormEvent<HTMLInputElement>) {
        const name = ev.currentTarget.name
        setInputs({ ...inputs, [name]: ev.currentTarget.value })
        setError(undefined)
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
                    <input id="password" type="password" name="password" value={inputs.password} onChange={handleChange} />
                </div>
                <div>
                    <button type="submit">Login</button>
                </div>
            </fieldset>
            {error}
        </form>
    )
}