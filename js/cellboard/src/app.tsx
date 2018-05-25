import * as React from "react";
import * as ReactDOM from "react-dom";

// import { Hello } from "./components/Hello";
import { TaskGrid } from "./components/TaskGrid";

ReactDOM.render(
    <TaskGrid cellSize={50}/>,
    document.getElementById("gridsvg")
);
