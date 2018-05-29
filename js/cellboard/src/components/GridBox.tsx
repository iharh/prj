import * as React from "react";
import * as ColorPalete from "./ColorPalete";

export interface GridBoxProps {
    cellSize: number;
    idx: number;
    getColorIndex: ()=>number;
}

export interface GridBoxState {
    colorIndex: number;
}

export class GridBox extends React.Component<GridBoxProps, GridBoxState> {
    constructor(props: any) {
        super(props);
        const colorIdx = props.getColorIndex();
        this.state = { colorIndex: colorIdx };
    }
    private doOnClick = () => {
        const props = this.props;
        const colorIdx = props.getColorIndex();
        // console.log(`colorIndex: ${colorIdx}`);
        this.setState({ colorIndex: colorIdx });
    }
    render() {
        const props = this.props;
        const colorVal = ColorPalete.BOX_COLORS[this.state.colorIndex];
        return <rect x={1+props.idx*props.cellSize} width={props.cellSize} height={props.cellSize} rx={5} ry={5}
            style={{fill: colorVal}}
            onClick={this.doOnClick}/>;
    }
}
