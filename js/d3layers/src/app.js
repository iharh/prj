// import { add, subtract } from "./script.js";
import * as funcs from "./script.js";
import * as d3 from "./d3.js";

//console.log(funcs.add(5, 15));
//console.log(funcs.subtract(25, 5));
//const arrFun = (name) => name;
//console.log(arrFun("abcde"));

// 8x8
var NUM_ROWS = 3;
var NUM_COLS = 4;
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

    // iterate for rows	
    for (var row = 0; row < numRows; ++row) {
        data.push( new Array() );
        // iterate for cells/columns inside rows
        for (var column = 0; column < numCols; ++column) {
            data[row].push({
                x: xpos,
                y: ypos,
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

var gridData = buildGridData(NUM_ROWS, NUM_COLS);	
//console.log(gridData);

var svg = d3.select("body")
    .append("svg")
    .attr("width", TOTAL_W)
    .attr("height", TOTAL_H)

var gridGroup = svg.append("g");

var row = gridGroup.selectAll(".row")
    .data(gridData)
    .enter().append("g")
    .attr("class", "row")
	
var column = row.selectAll(".square")
    .data(function(d) { return d; })
    .enter().append("rect")
    .attr("class", "square")
    .attr("x", function(d) { return d.x; })
    .attr("y", function(d) { return d.y; })
    .attr("width", CELL_W)
    .attr("height", CELL_H)
    .style("stroke", "#222")
    .style("fill", "white");

var colsGroup2 = svg.append("g");

var column = colsGroup2.selectAll(".square2")
    .data([{"color": "blue"}])
    .enter().append("rect")
    .attr("class", "square2")
    .attr("x", 51)
    .attr("y", 51)
    .attr("width", CELL_W*2 + 1)
    .attr("height", CELL_H)
    .style("stroke", "#222")
    .style("fill", (d) => d.color);
