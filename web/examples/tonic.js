var RiTa = require('rita');
  
var rs = RiTa.RiString("The elephant took a bite!");
var features = rs.features();

console.log(features);
