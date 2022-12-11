import * as React from "react"
import { useState } from "react"

export function Sender() {
    const [input, setInput] = useState("")
    const [isSubmitting, setIsSubmitting] = useState(false)
    const [status, setStatus] = useState("editing message")

    async function handleSubmit(ev) {
        ev.preventDefault()
        setIsSubmitting(true)
        setStatus("sending...")
        try {
            const resp = await fetch("/api/chat/send", {
                method: "POST",
                headers: {
                    "content-type": "text/plain",
                },
                body: input,
            })
            if (resp.status != 200) {
                setStatus(`error, unexpected status ${resp.status}`)
            }
            setStatus("sent")
            setInput("")
        } catch (err) {
            setStatus(`error, ${err.message}`)
        }
        setIsSubmitting(false)
    }

    function handleChange(ev) {
        const value = ev.currentTarget.value
        setInput(value)
        setStatus("editing message")
    }

    return (
        <div>
            <form onSubmit={handleSubmit}>
                <fieldset disabled={isSubmitting}>
                    <input type="text" value={input} onChange={handleChange} />
                    <p>Status: {status} </p>
                </fieldset>
            </form>
        </div>
    )

}