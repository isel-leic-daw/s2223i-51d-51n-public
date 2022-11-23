import * as React from 'react'
import { Level2 } from './Level2'
import { Login } from './Login'


export function Level1() {
    return(
        <div>
            <h1>Level 1</h1>
            <Login />
            <Level2 />
        </div>
    )
}