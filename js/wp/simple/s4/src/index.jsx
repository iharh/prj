import { moment } from "moment";
import ReactDOM from "react-dom";

console.log('before hello from main file!');
console.log(moment().startOf('day').fromNow());
console.log('after hello from main file!');

const App = () => {
  return(
    <div>
      <p>Hello World</p>
    </div>
  )
}

ReactDOM.render(<App/>, document.getElementById("root"))
