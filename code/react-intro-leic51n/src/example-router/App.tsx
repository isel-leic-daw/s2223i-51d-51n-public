import * as React from 'react'
import {
    createBrowserRouter,
    RouterProvider,
    Link,
    useParams,
    useLoaderData,
    Outlet,
} from 'react-router-dom'

const router = createBrowserRouter([
    {
        path: "/",
        element: <Home />,
        children: [

            {
                path: "/me",
                element: <UserDetails />,
            },
            {
                path: "/games/:gid",
                element: <GameDetails />,
                loader: load
            }
        ]
    }
])

function delay(ms: number) {
    return new Promise((resolve, reject) => {
        setTimeout(() => resolve(undefined), ms)
    })
}

async function load() {
    await delay(2000)
    return "some data"
}

export function App() {
    return (
        <RouterProvider router={router} />
    )
}

function Home() {
    return (
        <div>
            <Outlet/>
            <h2>Home</h2>
            <p><Link to="/me">User details</Link></p>
            <ol>
                <li><Link to="/games/1">Game 1</Link></li>
                <li><Link to="/games/2">Game 2</Link></li>
                <li><a href="/games/3">Game 3</a></li>
            </ol>
            
        </div>
    )
}

function UserDetails() {
    return (
        <div>
            <h2>User Details</h2>
        </div>
    )
}

function GameDetails() {
    const { gid } = useParams()
    const data = useLoaderData() as string
    return (
        <div>
            <h2>Game Details</h2>
            <h3>{gid}</h3>
            <h4>{data}</h4>
        </div>
    )
}
