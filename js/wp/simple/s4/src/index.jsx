// https://medium.com/the-node-js-collection/modern-javascript-explained-for-dinosaurs-f695e9747b70
import ReactDOM from "react-dom";
var moment = require('moment');

console.log('before hello from main file!');
console.log(moment().startOf('day').fromNow());
console.log('after hello from main file!');

// import React, {FC} from "react";
// function App() {
//  return <h1>Hello World</h1>;
// }

const App = () => {
  return(
    <div>
      <p>Hello World</p>
    </div>
  )
}

ReactDOM.render(<App />, document.getElementById("root"))
