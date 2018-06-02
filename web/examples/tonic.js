var RiTa = require('rita'),
  rs = RiTa.RiString("The elephant took a bite!"),
  features = rs.features();

console.log(features);
