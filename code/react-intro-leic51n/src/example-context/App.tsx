import * as React from 'react'
import { Level1 } from './Level1'
import { AuthnStatusProvider } from './authnStatus'
import { Login } from './Login'

export function App() {
    return (
        <div>
           <AuthnStatusProvider>
                <Login />
                <Level1 />
           </AuthnStatusProvider>
        </div>
    )
}