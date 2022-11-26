import * as React from 'react'
import { createRoot } from 'react-dom/client'

import { Counter } from './Counter'
import { CounterList } from './CounterList'
import { Timer } from './Timer'
// import {App} from './example-context/App'
//import {App} from './example-fetch/App'
import {App} from './example-router/App'
const root = createRoot(document.getElementById("the-div"))

root.render(
    <App />
)

