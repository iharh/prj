import * as React from "react";
import * as ColorPalete from "./ColorPalete";

export interface ColorBoxProps {
    cellSize: number;
    idx: number;
    selected: boolean;
    setColorIndex: (colorIndex: number) => void;
}

export class ColorBox extends React.Component<ColorBoxProps, {}> {
    private doOnClick = () => {
        const props = this.props;
        // console.log(`doOnClick colorIndex: ${props.idx}`);
        props.setColorIndex(props.idx)
    }
    render() {
        const props = this.props;
        const colorVal = ColorPalete.BOX_COLORS[props.idx];
        const strokeW = props.selected ? 4 : 1;
        return <rect x={4+props.idx*(props.cellSize+4)} width={props.cellSize} height={props.cellSize} rx={10} ry={10}
            style={{fill: colorVal, strokeWidth: strokeW}}
            onClick={this.doOnClick}/>;
    }
}
