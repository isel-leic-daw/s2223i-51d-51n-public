import * as React from 'react'
import { AuthnContainer } from './Authn'
import { Level1 } from './Level1'

export function App() {
    return (
        <div>
            <h1>App</h1>
            <AuthnContainer>
                <Level1 />
            </AuthnContainer>
        </div>
    )
}