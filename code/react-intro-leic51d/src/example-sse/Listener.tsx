import * as React from "react"
import { useEffect, useState } from "react"

const MAX_MESSAGES = 30

function appendLine(lines: Array<string>, line: string): Array<string> {
    const res = [...lines, line]
    if (res.length > MAX_MESSAGES) {
        res.shift()
    }
    return res
}

function eventSourceStatus(eventSource: EventSource) {
    switch(eventSource.readyState) {
        case EventSource.CLOSED:
            return "closed"
        case EventSource.CONNECTING:
            return "connecting..."
        case EventSource.OPEN:
            return "connected"
        default:
            return `unknown (${eventSource.readyState})`
    }
}

export function Listener() {
    const [outputLines, setOutputLines] = useState([])
    const [status, setStatus] = useState('disconnected')

    useEffect(() => {
        console.log(`creating new EventSource`)
        const eventSource = new EventSource("/api/chat/listen")
        setStatus("connecting...")
        eventSource.onopen = (ev) => {
            setStatus(eventSourceStatus(eventSource))
        }
        eventSource.onmessage = (ev) => {
            setStatus(eventSourceStatus(eventSource))
            setOutputLines(outputLines => appendLine(outputLines, ev.data))
        }
        eventSource.onerror = (ev: ErrorEvent) => {
            setStatus(eventSourceStatus(eventSource))
        }

        return () => {
            eventSource.close()
        }
    }, [])

    return (
        <div>
            <textarea value={outputLines.join('\n')} cols={100} rows={MAX_MESSAGES + 2} readOnly={true} />
            <p>Status: {status} </p>
        </div>
    )

}