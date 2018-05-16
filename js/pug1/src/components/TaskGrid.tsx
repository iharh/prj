import * as React from "react";

export const TaskGrid = () => <g style={{stroke:"black", fill:"white"}} >
    <g transform="translate(0 0)">>
        <rect x={1} width={50} height={50} />
        <rect x={51} width={50} height={50} />
    </g>
    <g transform="translate(0 50)">
        <rect x={1} width={50} height={50} />
        <rect x={51} width={50} height={50} />
        <rect x={101} width={50} height={50} />
    </g>
</g>;

// className="square"
//<rect x=1 y=1 width=50 height=50 style="stroke: rgb(34, 34, 34); fill: white;"/>
// stroke: black
