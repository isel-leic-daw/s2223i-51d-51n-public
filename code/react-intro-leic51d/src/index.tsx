import * as React from 'react'
import { createRoot } from 'react-dom/client'
// import {App} from './example-context/App'
// import {App} from './example-router/App'
// import { App } from './example-fetch/App'
import {App} from './example-sse/App'

const root = createRoot(document.getElementById("the-div"))

root.render(
    <App />
)

