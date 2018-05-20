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
