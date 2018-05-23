import * as React from "react";
import { GridBox } from "./GridBox";
import { Range } from "immutable";

export interface TaskGridProps { cellSize: number; }

// style={{stroke:"black", fill:"white"}}
const taskGridStyle= {stroke:"black", fill:"white"};

export const TaskGrid = (props: TaskGridProps) => {
    let makeGridBox = (i: number) => <GridBox x={1+i*props.cellSize} cellSize={props.cellSize} />;
    let makeRow = (rowNum: number, cols: number) => <g transform={"translate(0 " + (1 + rowNum*props.cellSize) +")"}>
        {Range(0, cols+1).map(makeGridBox)}
    </g>;
    return <g style={taskGridStyle}>
        {Range(0, 3).map((r: number) =>
            makeRow(r, r)
        )}
    </g>;
};
