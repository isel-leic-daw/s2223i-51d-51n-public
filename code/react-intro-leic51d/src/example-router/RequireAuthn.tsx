import * as React from 'react'
import { Navigate, useLocation } from 'react-router-dom'
import { useCurrentUser } from './Authn'

export function RequireAuthn({ children }: { children: React.ReactNode }): React.ReactElement {
    const currentUser = useCurrentUser()
    const location = useLocation()
    console.log(`currentUser = ${currentUser}`)
    if (currentUser) {
        return <>{children}</>
    } else {
        console.log("redirecting to login")
        return <Navigate to="/login" state={{source: location.pathname}} replace={true}/>
    }

}