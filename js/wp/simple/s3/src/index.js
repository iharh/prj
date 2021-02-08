// https://medium.com/the-node-js-collection/modern-javascript-explained-for-dinosaurs-f695e9747b70
var moment = require('moment');

console.log('before hello from main file!');
console.log(moment().startOf('day').fromNow());
console.log('after hello from main file!');
