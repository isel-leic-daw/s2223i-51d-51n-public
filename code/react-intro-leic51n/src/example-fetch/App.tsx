import * as React from 'react'
import {
    useState,
} from 'react'
import { Show } from './Show'

const defaultUrl = "https://httpbin.org/delay/2"
const MAX = 256
export function App() {
    const [url, setUrl] = useState(defaultUrl)
    const [editUrl, setEditUrl] = useState("")
    function handleChange(ev) {
        const value = ev.target.value
        if (value.length <= MAX) {
            setEditUrl(ev.target.value)
        }
    }
    function handleSubmit(ev) {
        ev.preventDefault()
        setUrl(editUrl)
    }
    return (
        <div>
            <div>
                <form onSubmit={handleSubmit}>
                    <input type="text" value={editUrl} onChange={handleChange} />
                    {MAX - editUrl.length}
                </form>
            </div>
            <p>{url}</p>
            <Show url={url} />
        </div>
    )
}