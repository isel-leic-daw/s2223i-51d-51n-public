import React from 'react'
import { createRoot } from 'react-dom/client'

// import { App } from './example-router'
// import { App } from './example-first'
// import { App } from './example-counters'
// import {App} from './example-context'
import {App} from './example-authn/App'

createRoot(document.getElementById("container")).render(
    <App />
)
