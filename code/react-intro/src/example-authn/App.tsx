import React from "react"
import {
    createBrowserRouter,
    RouterProvider,
    Link,
} from "react-router-dom"
import { AuthnProvider, useAuthentication } from "./AuthnProvider"
import { RequiresAuthn } from "./RequiresAuthn"
import { Login } from "./Login"

const router = createBrowserRouter([
    {
        path: "/",
        element: <Home />,
    },
    {
        path: "/me",
        element: <RequiresAuthn><UserHome /></RequiresAuthn>,
    },
    {
        path: "/login",
        element: <Login />
    }
])

export function App() {
    return (
        <AuthnProvider>
            <RouterProvider router={router} />
        </AuthnProvider>
    )
}

function Home() {
    return (
        <>
            <h1>Home</h1>
            <Link to="/me">User Home</Link>
        </>
    )
}

function UserHome() {
    const [username] = useAuthentication()
    return (
        <h1>Hello {username}</h1>
    )
}
