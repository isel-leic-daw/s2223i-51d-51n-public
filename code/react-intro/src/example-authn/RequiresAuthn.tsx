import React from "react";
import { Navigate, useLocation } from "react-router-dom";
import { useAuthentication } from "./AuthnProvider"

export function RequiresAuthn({ children }: React.PropsWithChildren<{}>): React.ReactElement {
    const [username] = useAuthentication()
    const location = useLocation()

    if (username) {
        return <>{children}</>
    } else {
        return <Navigate to="/login" replace={true} state={{from: location}}/>
    }

}
