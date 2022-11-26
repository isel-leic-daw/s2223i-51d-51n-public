import * as React from 'react'
import {
    useState,
    useEffect,
} from 'react'
import { useFetch } from './useFetch'

export function Show({ url }: { url: string }) {
    const content = useFetch(url)

    if (!content) {
        return (
            <div>
                ...loading...
            </div>
        )
    }

    return (
        <div>
            <pre>
                {JSON.stringify(content, null, 2)}
            </pre>
        </div>
    )
}