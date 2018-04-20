// import { add, subtract } from "./script.js";
import * as funcs from "./script.js";
import * as d3sel from 'd3-selection'; 
import * as d3col from 'd3-color'; 

console.log(funcs.add(5, 15));
//console.log(funcs.subtract(25, 5));

const arrFun = (name) => name;

console.log(arrFun("abcde"));

var NUM_ROWS = 8;
var NUM_COLS = 8;
var CELL_W = 50;
var CELL_H = 50;
var STYLE_COLORS = [ "#2C93E8", "#F56C4E", "#838690", "#fff" ];

var TOTAL_W = (CELL_W + 1) * NUM_COLS;
var TOTAL_H = (CELL_H + 1) * NUM_ROWS;

function buildGridData(numRows, numCols) {
    var data = new Array();

    //starting xpos and ypos at 1 so the stroke will show when we make the grid below
    var xpos = 1; 
    var ypos = 1;

    var click = 0;
    
    // iterate for rows	
    for (var row = 0; row < numRows; ++row) {
        data.push( new Array() );
        // iterate for cells/columns inside rows
        for (var column = 0; column < numCols; ++column) {
            click = (row + column) % 3;
            if (row == (numRows - 1) && column == (numCols - 1)) {
                click = 3;
            }
            data[row].push({
                x: xpos,
                y: ypos,
                width: CELL_W,
                height: CELL_H,
                click: click
            })
            // increment the x position. I.e. move it over by 50 (width variable)
            xpos += CELL_W;
        }
        // reset the x position after a row is complete
        xpos = 1;
        // increment the y position for the next row. Move it down 50 (height variable)
        ypos += CELL_H;	
    }
    return data;
}

function onClickFun(d) {
    d3.select(this).style("fill", STYLE_COLORS[(d.click++)%4]);
}

var gridData = buildGridData(NUM_ROWS, NUM_COLS);	
// I like to log the data to the console for quick debugging
//console.log(gridData);

var svg = d3sel.select("body")
    .append("svg")
    .attr("width", TOTAL_W)
    .attr("height", TOTAL_H)

var row = svg.selectAll(".row")
    .data(gridData)
    .enter().append("g")
    .attr("class", "row")
	
var column = row.selectAll(".square")
    .data(function(d) { return d; })
    .enter().append("rect")
    .attr("class", "square")
    .attr("x", function(d) { return d.x; })
    .attr("y", function(d) { return d.y; })
    .attr("width", function(d) { return d.width; })
    .attr("height", function(d) { return d.height; })
    //.style("fill", "#fff")
    .style("stroke", "#222")
    .style("fill", function(d) { return STYLE_COLORS[d.click]; });
    //.on('click', onClickFun)
