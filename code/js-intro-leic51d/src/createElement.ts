export function createElement(
    name: string,
    attributes: { [key: string]: string | boolean },
    ...children: Array<HTMLElement | string | Array<HTMLElement>>
): HTMLElement {
    const elem = document.createElement(name)
    for (const key in attributes) {
        const value = attributes[key]
        if (typeof (value) === "string") {
            elem.setAttribute(key, value)
        } else {
            if (value) {
                elem.setAttribute(key, "")
            }
        }
    }
    children.forEach(child => {
        if (typeof child === "string") {
            elem.appendChild(document.createTextNode(child))
        } else if(Array.isArray(child)) {
            child.forEach(it => elem.appendChild(it))
        } else {
            elem.appendChild(child)
        }
    })
    return elem
}


// example usage
const elem: HTMLElement = createElement(
    'ul',
    { a: "b", c: true },
    createElement('li', {}, "item1"),
    createElement('li', {}, "item2")
)
/*
    <ul a="b">
        <li>item1</li>
        <li>item2</li>
    </ul>
 */