import * as React from 'react'
import {
    useState,
    createContext,
    useContext,
} from 'react'

type AuthnContextType = {
    loggedin: boolean,
    setLoggedin: (v: boolean) => void
}
const AuthnContext = createContext<AuthnContextType>({
    loggedin: false,
    setLoggedin: () => {}
})

export function AuthnStatusProvider({children}: {children: React.ReactNode}) {
    const [loggedin, setLoggedin] = useState(false)
    return (
        <AuthnContext.Provider value={{loggedin: loggedin, setLoggedin: setLoggedin}}>
            {children}
        </AuthnContext.Provider>
    )
}

export function useLoggedin(): boolean {
    return useContext(AuthnContext).loggedin
}

export function useLogin() {
    return useContext(AuthnContext).setLoggedin
}