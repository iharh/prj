import * as React from "react";
import { GridBox } from "./GridBox";
import { Range } from "immutable";

export interface TaskGridProps { cellSize: number; }

// style={{stroke:"black", fill:"white"}}
const taskGridStyle= {stroke:"black", fill:"white"};

export const TaskGrid = (props: TaskGridProps) => {
    let gridBoxMapper = (i: number) => <GridBox x={1+i*props.cellSize} cellSize={props.cellSize} />;
    let makeRow = (i: number, j: number) => <g transform={"translate(0 " + (1 + i*props.cellSize) +")"}> {Range(0, 2).map(gridBoxMapper)} </g>;
    return <g style={taskGridStyle}>
        {makeRow(0)}
        {makeRow(1)}
    </g>;
};
