import * as React from "react";

export interface GridBoxProps { cellSize: number; x: number; }

export const GridBox = (props: GridBoxProps) => <rect x={props.x} width={props.cellSize} height={props.cellSize} />;
