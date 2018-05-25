import * as React from "react";

export interface ColorBoxProps {
    cellSize: number;
    idx: number;
    selected: boolean;
    setColorIndex: (colorIndex: number) => void;
}

const BOX_COLORS = [ "#2C93E8", "#F56C4E", "#838690", "#fff" ];

export class ColorBox extends React.Component<ColorBoxProps, {}> {
    private doOnClick = () => {
        const props = this.props;
        // console.log(`doOnClick colorIndex: ${props.idx}`);
        props.setColorIndex(props.idx)
    }
    render() {
        const props = this.props;
        const colorVal = BOX_COLORS[props.idx];
        const strokeW = props.selected ? 4 : 1;
        return <rect x={4+props.idx*(props.cellSize+4)} width={props.cellSize} height={props.cellSize} rx={10} ry={10}
            style={{fill: colorVal, strokeWidth: strokeW}}
            onClick={this.doOnClick}/>;
    }
}
