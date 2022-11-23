import * as React from 'react'
import {
    createBrowserRouter, Link, RouterProvider, useParams,
} from 'react-router-dom'

const router = createBrowserRouter([
    {
        "path": "/",
        "element": <Home />
    },
    {
        "path": "/path1",
        "element": <Screen1 />
    },
    {
        "path": "/path2",
        "element": <Screen2 />
    },
    {
        "path": "/path3",
        "element": <Screen3 />
    },
    {
        "path": "/users/:uid",
        "element": <UserDetail />
    },
    {
        "path": "/users/:uid/games/:gid",
        "element": <UserGameDetail />
    }
])

export function App() {
    return (
        <RouterProvider router={router} />
    )
}

function Home() {
    return (
        <div>
            <h1>Home</h1>
            <ol>
                <li><a href="/path1">screen 1 (using a)</a></li>
                <li><Link to="/path1">screen 1</Link></li>
                <li><Link to="/path2">screen 2</Link></li>
                <li><Link to="/path3">screen 3</Link></li>
                <li><Link to="/users/123">User 123</Link></li>
                <li><Link to="/users/abc">User abc</Link></li>
                <li><Link to="/users/123/games/xyz">User 123 Game xyz</Link></li>
            </ol>
        </div>
    )
}

function Screen1() {
    return (
        <div>
            <h1>Screen 1</h1>
        </div>
    )
}

function Screen2() {
    return (
        <div>
            <h1>Screen 2</h1>
        </div>
    )
}

function Screen3() {
    return (
        <div>
            <h1>Screen 3</h1>
        </div>
    )
}

function UserDetail() {
    const {uid} = useParams()
    return (
        <div>
            <h2>User Detail</h2>
            {uid}
        </div>
    )
}

function UserGameDetail() {
    const {gid, uid} = useParams()
    return (
        <div>
            <h2>User Game Detail</h2>
            {uid}, {gid}
        </div>
    )
}
 