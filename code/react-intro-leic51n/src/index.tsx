import * as React from 'react'
import { createRoot } from 'react-dom/client'

import { Counter } from './Counter'
import { Timer } from './Timer'

const root = createRoot(document.getElementById("the-div"))

root.render(
    <div>
        <Timer key={1} label="first timer"/>
        <Timer key={2} label="second timer"/>
    </div>
)

