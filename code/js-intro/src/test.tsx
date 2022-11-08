/** @jsx createElement */
import { createElement } from "./view"

const Test = () => <h3>Test</h3>

export const test = () => document.getElementById("the-div").appendChild(
    <div>
        <h2>Heading 2</h2>
        <ul>
            <li>Item 1</li>
            <li>Item 2</li>
        </ul>
        <Test />
    </div>
)