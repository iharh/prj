import * as React from "react";
import { GridBox } from "./GridBox";
import { Range } from "immutable";

export interface TaskGridProps { cellSize: number; }

// style={{stroke:"black", fill:"white"}}
const taskGridStyle= {stroke:"black", fill:"white"};

export const TaskGrid = (props: TaskGridProps) => {
    let gridBoxMapper = (i: number) => <GridBox x={1+i*props.cellSize} cellSize={props.cellSize} />;
    return <g style={taskGridStyle}>
        <g transform="translate(0 1)">
            {Range(0, 2).map(gridBoxMapper)}
        </g>
        <g transform="translate(0 51)">
            {[0, 1, 2].map(gridBoxMapper)}
        </g>
    </g>;
};
