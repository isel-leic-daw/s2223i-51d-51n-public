import {
    useState,
    useEffect,
} from 'react'

export function 
useFetch(url: string): string | undefined {
    const [content, setContent] = useState(undefined)
    useEffect(() => {
        let cancelled = false
        async function doFetch() {
            const resp = await fetch(url)
            const body = await resp.json()
            if (!cancelled) {
                setContent(body)
            }
        }
        setContent(undefined)
        doFetch()
        return () => {
            cancelled = true
        }
    }, [url])
    return content
}