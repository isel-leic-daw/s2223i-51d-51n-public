export function createElement(
    name: string,
    attrs: { [name: string]: string | boolean },
    ...children: Array<HTMLElement | string | Array<HTMLElement>>
): HTMLElement {
    console.log(`createElement: creating element '${name}'`)
    const elem = document.createElement(name)
    for (const name in attrs) {
        const value = attrs[name]
        if (typeof value === "string") {
            elem.setAttribute(name, value)
        } else {
            if (value) {
                elem.setAttribute(name, "")
            }
        }
    }
    children.forEach(child => {
        if (typeof child == "string") {
            elem.appendChild(document.createTextNode(child))
        } else if (Array.isArray(child)) {
            child.forEach(it => elem.appendChild(it))
        } else {
            elem.appendChild(child)
        }
    })
    return elem
}