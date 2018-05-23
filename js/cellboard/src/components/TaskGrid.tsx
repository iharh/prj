import * as React from "react";
import { GridBox } from "./GridBox";

export interface TaskGridProps { cellSize: number; }

// style={{stroke:"black", fill:"white"}}
const taskGridStyle= {stroke:"black", fill:"white"};

export const TaskGrid = (props: TaskGridProps) => <g style={taskGridStyle}>
    <g transform="translate(0 0)">>
        <GridBox x={1} cellSize={props.cellSize} />
        <GridBox x={51} cellSize={props.cellSize} />
    </g>
    <g transform="translate(0 50)">
        <GridBox x={1} cellSize={props.cellSize} />
        <GridBox x={51} cellSize={props.cellSize} />
        <GridBox x={101} cellSize={props.cellSize} />
    </g>
</g>;
