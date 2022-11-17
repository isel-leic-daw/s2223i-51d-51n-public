import React, {
    useState,
    createContext,
    useContext,
} from "react"

type AuthnState = [
    username: string | undefined,
    setUsername: (state: string | undefined) => void,
]

const Context = createContext<AuthnState>([undefined, () => {} ])

export function AuthnProvider({ children }: { children: React.ReactNode }) {
    const [username, setUsername] = useState(undefined)
    return (
        <Context.Provider value={[ username, setUsername ]}>
            {children}
        </Context.Provider>
    )
}

export function useAuthentication(): AuthnState {
    return useContext(Context)
}
