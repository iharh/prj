import * as React from "react";
import { GridBox } from "./GridBox";
import { ColorBox } from "./ColorBox";
import { Range } from "immutable";

export interface TaskGridProps { cellSize: number; }
export interface TaskGridState { colorIndex: number; }

const taskGridStyle = {stroke:"black", fill:"white"};

export class TaskGrid extends React.Component<TaskGridProps, TaskGridState> {
    constructor(props: any) {
        super(props);
        // const colorIdx = props.getColorIndex();
        this.state = { colorIndex: 3 };
    }

    getColorIndex = (): number => {
        // console.log(`getColorIndex colorIndex: ${this.curColorIndex}`);
        return this.state.colorIndex;
    }
    setColorIndex = (colorIdx: number): void => {
        // console.log(`setColorIndex colorIndex: ${colorIdx}`);
        this.setState({ colorIndex: colorIdx });
    }

    render() {
        const props = this.props;

        const makeColorBox = (c: number) => <ColorBox key={c} idx={c} cellSize={props.cellSize} setColorIndex={this.setColorIndex}
            selected={this.state.colorIndex == c}/>;
        const makeColorRow = (cols: number) => <g key={100} transform={"translate(0 5)"}>
            {Range(0, cols).map(makeColorBox)}
        </g>;

        // TODO: is it possible to reuse makeColorBox in some way here?
        const makeGridBox = (c: number) => <GridBox key={c} idx={c} cellSize={props.cellSize} getColorIndex={this.getColorIndex} />;
        const makeGridRow = (rowNum: number, cols: number) => <g key={rowNum} transform={"translate(10 " + (1 + (rowNum + 2)*props.cellSize) +")"}>
            {Range(0, cols+1).map(makeGridBox)}
        </g>;

        return <g style={taskGridStyle}>
            { makeColorRow(4) }
            { Range(0, 3).map((r: number) => makeGridRow(r, r)) }
        </g>;
    }
};
