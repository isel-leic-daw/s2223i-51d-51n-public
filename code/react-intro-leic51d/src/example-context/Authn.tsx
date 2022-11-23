import * as React from 'react'
import {
    useState,
    createContext,
    useContext,
} from 'react'

type ContextType = {
    loggedin: boolean,
    setLogin: (v: boolean) => void
}
const LoggedInContext = createContext<ContextType>({
    loggedin: false,
    setLogin: () => {}
})

export function AuthnContainer({ children }: { children: React.ReactNode }) {
    const [loggedin, setLoggedin] = useState(false)
    return (
        <LoggedInContext.Provider value={{loggedin: loggedin, setLogin: setLoggedin }}>
            {children}
        </LoggedInContext.Provider>
    )
}

export function useLoggedIn() {
    return useContext(LoggedInContext).loggedin
}

export function useSetLogin() {
    return useContext(LoggedInContext).setLogin
}
