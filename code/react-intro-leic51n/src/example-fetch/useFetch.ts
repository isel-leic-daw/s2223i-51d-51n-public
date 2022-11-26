import {
    useState,
    useEffect,
} from 'react'

export function useFetch(url: string) : [content: any | undefined, loading: boolean] {
    const [loading, setLoading] = useState(false)
    const [content, setContent] = useState(undefined)
    useEffect(() => {
        let cancelled = false
        async function doFetch() {
            const resp = await fetch(url)
            const body = await resp.json()
            if (!cancelled) {
                setLoading(false)
                setContent(body)
            }
        }
        setLoading(true)
        doFetch()
        return () => {
            cancelled = true
        }
    }, [url, setContent])

    return [content, loading]
}