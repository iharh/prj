import * as React from "react";

export interface TaskGridProps { cellSize: number; }

// style={{stroke:"black", fill:"white"}}
const taskGridStyle= {stroke:"black", fill:"white"};

export const TaskGrid = (props: TaskGridProps) => <g style={taskGridStyle}>
    <g transform="translate(0 0)">>
        <rect x={1} width={props.cellSize} height={props.cellSize} />
        <rect x={51} width={props.cellSize} height={props.cellSize} />
    </g>
    <g transform="translate(0 50)">
        <rect x={1} width={props.cellSize} height={props.cellSize} />
        <rect x={51} width={props.cellSize} height={props.cellSize} />
        <rect x={101} width={props.cellSize} height={props.cellSize} />
    </g>
</g>;
