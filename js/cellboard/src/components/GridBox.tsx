import * as React from "react";

export interface GridBoxProps {
    cellSize: number;
    x: number;
    getColorIndex: ()=>number;
}

const boxColors = [ "#2C93E8", "#F56C4E", "#838690", "#fff" ];

export const GridBox = (props: GridBoxProps) => {
    const colorVal = boxColors[props.getColorIndex()];
    console.log(`colorVal: ${colorVal}`);
    return <rect x={props.x} width={props.cellSize} height={props.cellSize} style={{fill: colorVal}}/>;
}
