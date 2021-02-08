import moment from "moment";
import ReactDOM from "react-dom";
import { App } from "@components/App";

console.log('hello from main file!');
console.log(moment().startOf('day').fromNow());

ReactDOM.render(<App/>, document.getElementById("root"))
