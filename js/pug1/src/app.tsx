import * as React from "react";
import * as ReactDOM from "react-dom";

import { Hello } from "./components/Hello";
import { TaskGrid } from "./components/TaskGrid";

ReactDOM.render(
    <Hello compiler="TypeScript" framework="React" />,
    document.getElementById("root")
);
ReactDOM.render(
    <TaskGrid/>,
    document.getElementById("rootsvg")
);
    //  g(class="row")
    //    rect(class="square" x="1" y="1" width="50" height="50" style="stroke: rgb(34, 34, 34); fill: white;")
