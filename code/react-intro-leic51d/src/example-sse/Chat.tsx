import * as React from "react"
import { Listener } from "./Listener"
import { Sender } from "./Sender"

export function Chat() {

    return (
        <>
            <Listener />
            <Sender />
        </>
    )
}