import * as React from 'react'
import {
    useState,
    useEffect,
} from 'react'
import { useFetch } from './useFetch'

export function Show({ url }: { url: string }) {
    const [content, loading] = useFetch(url)
    if (!content) {
        return (
            <p>...loading...</p>
        )
    }
    return (
        <div>
            <p>{loading ? "...reloading..." : null}</p>
            <pre>
                {JSON.stringify(content, null, 2)}
            </pre>
        </div>
    )
}
