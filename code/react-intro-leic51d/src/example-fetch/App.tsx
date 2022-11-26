import * as React from 'react'
import { Show } from './Show'
import {
    useState,
} from 'react'

const defaultUrl = "https://httpbin.org/delay/2"

export function App() {
    const [editUrl, setEditUrl] = useState(defaultUrl)
    const [url, setUrl] = useState(defaultUrl)
    const handleChange = (ev) => {
        setEditUrl(ev.target.value)
    }
    const handleSubmit = (ev) => {
        ev.preventDefault()
        setUrl(editUrl)
    }
    return (
        <div>
            <div>
                <form onSubmit={handleSubmit}>
                    <input type="text" value={editUrl} onChange={handleChange}/>
                    {editUrl.length}
                </form>
            </div>
            <p>{url}</p>
            <Show url={url} />
        </div >
    )
}