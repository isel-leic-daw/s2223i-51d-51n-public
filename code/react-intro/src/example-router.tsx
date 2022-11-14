import React, {
    useState,
    useCallback,
    ReactElement
} from "react"
import {
    createBrowserRouter,
    RouterProvider,
    Route,
    useNavigation,
    useNavigate,
    useRouteError,
    useLoaderData,
} from "react-router-dom"

// We start by creating a router, by passing an array of objects to `createBrowserRouter`
// Each object has a path (or path template) and the element to use for that path (element, not component)
const router = createBrowserRouter([
    {
        path: "/",
        element: <InputUsage />,
        errorElement: <ErrorPage />,
    },
    {
        path: "/users/:username",
        element: <ShowUser />,
        loader: loader,
    }
])

// Then we create a `RouterProvider`, passing in the created router
export const App = ({ }) => (
    <RouterProvider router={router} />
)

type InputType = {
    onSubmit: (value: String) => void
}
function Input({ onSubmit }: InputType): ReactElement {
    const [value, setValue] = useState("")

    return (
        <form onSubmit={(ev) => { onSubmit(value); ev.preventDefault() }}>
            <input type="text" onChange={ev => setValue(ev.target.value)} value={value} />
        </form>
    )
}

function InputUsage({ }): ReactElement {
    const navigate = useNavigate()
    const onSubmit = useCallback((value: string) => {
        navigate(`/users/${value}`)
    }, [])

    return (
        <Input onSubmit={onSubmit} />
    )
}

function ShowUser({ }): ReactElement {
    const data = useLoaderData() as string
    return (
        <h2>{data}</h2>
    )
}

function ErrorPage({}) {
    const error = useRouteError() as {statusText: string, message: string}
    return (
        <p><i>{error.statusText || error.message}</i></p>
    )
}

async function loader(...args: any): Promise<string> {

    console.log(args)
    const p = new Promise<string>((resolve, reject) => {
        setTimeout(() => resolve("Hello"), 2000)
    })

    return await p
}
