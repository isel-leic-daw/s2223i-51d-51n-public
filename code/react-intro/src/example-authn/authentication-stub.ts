
let authenticatedUser: string | null = null

export async function authenticate(username: string, password: string) : Promise<boolean> {
    await delay(2000)
    if(username == "alice" && password == "1234") {
        authenticatedUser = username
        return true
    }
    return false
}

export async function logout() {
    await delay(2000)
    authenticatedUser = null
}

function delay(delayInMs: number) {
    return new Promise((resolve, reject) => {
        setTimeout(() => resolve(undefined), delayInMs)
    })
}
