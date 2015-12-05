var RiTa = require('rita'),
  rs = RiTa.RiString("The elephant took a bite!"),
  features = rs.features();

var Canvas = require('canvas'),
  canvas = new Canvas(300, 200),
  ctx = canvas.getContext('2d'),
  y = 20;

for (f in features)
  ctx.fillText(f + ': ' + features[f], 20, y += 20);

canvas.toBuffer();
