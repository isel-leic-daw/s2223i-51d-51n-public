/*
 * Small example on how to implement drag-and-drop when using the React library.
 * Does not use any external library.
 * 
 * Behaviour:
 * - The example presents SIZE `div` elements, named as "sources", which can be dragged and dropped into SIZE `div` elements, named as "targets".
 * - Each source element can only be dragged once.
 * - Each target element can only receive one source element.
 */
import * as React from "react"
import { useCallback, useReducer } from "react"

// The number of source and target elements
const SIZE = 3

// The CSS style for the wrapper div, in order to use CSS Grid.
const wrapperStyle: React.CSSProperties = {
    display: 'grid',
    gridTemplateColumns: "repeat(3, 1fr)",
    gap: "10px",
    gridAutoRows: "minmax(100px, auto)",
}

// The function to compute the CSS style for a source `div`
function sourceDivStyle(sourceState: SourceState, column: number, row: number): React.CSSProperties {
    return {
        gridColumn: column,
        gridRow: row,
        border: "solid",
        width: "50px",
        height: "50px",
        borderColor: sourceState == 'available' ? 'green' : 'red',
    }
}

// The function to compute the CSS style for a target `div`
function targetDivStyle(targetState: TargetState, column: number, row: number): React.CSSProperties {
    return {
        gridColumn: column,
        gridRow: row,
        border: "solid",
        width: "100px",
        height: "100px",
        borderColor: targetState === undefined ? 'green' : 'red'
    }
}

// Helper function to compute an array `[0..len-1]`
function range(len: number) {
    return [...Array(len).keys()]
}

// The state type for each source element.
type SourceState =
    | 'available'   // the element was already dropped
    | 'used'        // the element is available to be dropped


// The state type for each target element
type TargetState =
    | number        // the target element received the source element with the index provided by the number
    | undefined     // the target element is available to receive a source element


// The overall component state
type State = {
    // Each item in the array represents a source element
    sources: Array<SourceState>

    // Each item in the array represents a target element
    targets: Array<TargetState>
}

/*
 * This example also shows the use of a reducer to manage state, via the `useReducer` hook
 * A reducer is a function that receives a current state and an action and produces a next state
 */
type Action =
    | { type: 'drop', sourceIx: number, targetIx: number } // the `sourceId` element was dropped on the `targetId` element

const initialState = {
    sources: new Array(SIZE).fill('available'),
    targets: new Array(SIZE).fill(undefined),
}

function reduce(state: State, action: Action): State {
    switch (action.type) {
        case 'drop':
            return {
                sources: state.sources.map((value, ix) => ix == action.sourceIx ? 'used' : value),
                targets: state.targets.map((value, ix) => ix == action.targetIx ? action.targetIx : value),
            }

        default:
            // unknown action, type system ensures this cannot happen
            return state
    }
}

// The following handlers can be outside the component because they dont use or change state
function handleDragStart(event: React.DragEvent<HTMLDivElement>) {
    const ix = event.currentTarget.attributes.getNamedItem('data-ix').textContent
    console.log(`dragStart - ${ix}`)
    event.dataTransfer.effectAllowed = "all"
    event.dataTransfer.setData("text/plain", ix)
}

function handleDragOver(event: React.DragEvent<HTMLDivElement>) {
    event.preventDefault()
    console.log("dragOver")
    event.dataTransfer.dropEffect = "copy";
}

export function App() {
    const [state, dispatch] = useReducer(reduce, initialState)

    // Handler for the `drop` event, which dispatches a `drop` event to the reducer managing state.
    const handleDrop = useCallback(function handleDrop(event: React.DragEvent<HTMLDivElement>) {
        // collect information about the source and the target
        const sourceIx = parseInt(event.dataTransfer.getData("text/plain"))
        const targetIx = parseInt(event.currentTarget.attributes.getNamedItem('data-ix').textContent)
    
        // dispatch a `drop` action to the reducer managing state
        dispatch({
            'type': 'drop',
            sourceIx: sourceIx,
            targetIx: targetIx,
        })
    }, [dispatch])
    console.log(state)

    return (
        // a source `div` is draggable only if it is 'available'
        // a source `div` is a drop target only if is available, i.e., its value is still undefined
        <div>
            <p>Source elements, draggable into the target elements</p>
            <div style={wrapperStyle}>
                {state.sources.map((source, ix) => <div
                    key={ix}
                    style={sourceDivStyle(source, ix + 1, 1)}
                    draggable={source == 'available'}
                    onDragStart={handleDragStart}
                    data-ix={ix}
                >{ix}</div>)}
            </div>
            <p> Target elements</p>
            <div style={wrapperStyle}>
                {state.targets.map((target, ix) => <div key={ix}
                    style={targetDivStyle(target, ix + 1, 1)}
                    onDragOver={target === undefined ? handleDragOver : undefined}
                    onDrop={target === undefined ? handleDrop : undefined}
                    data-ix={ix}>
                    {target}
                </div>)}
            </div>
        </div >
    )
}