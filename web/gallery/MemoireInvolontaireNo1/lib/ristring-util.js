// TODO: test html tags and alignment

var PARAGRAPH_INDENT = 30, INDENT_FIRST_PARAGRAPH = false,
    NON_BREAKING_SPACE = '<sp/>', PARAGRAPH_BREAK = '<p/>',
    LINE_BREAK = '<br/>', E =  '', SP = ' ';

RiString.prototype.splitLetters = function(fnt, fntsz) {

    var l = [], chars = this.text().split(E);

    for ( var i = 0; i < chars.length; i++) {
        if (chars[i] === SP) continue;
        var mx = this.charOffset(fnt, fntsz, i);
        l.push(new RiString(chars[i]).position(mx, this.get('y')));
    }

    return l;
};

RiString.prototype.splitWords = function(fnt, fntsz) {

    //console.log('splitWords', this, this.get('y'));
    var l = [], words = this.text().split(SP);

    for (var i = 0; i < words.length; i++) {
        if (words[i].length) {
            var mx = RiString._wordOffsetFor(fnt, fntsz, this, words, i);
            l.push(new RiString(words[i]).position(mx, this.get('y')));
        }
    }

    return l;
};

RiString.createLines = function(options) {

    var opts = parseOpts(options, {
        fontSize: 12, x: 0, y: 0, w: Number.MAX_VALUE , h: Number.MAX_VALUE
    });

    var p5font = opts.font, txt = opts.text,
        fsize = opts.fontSize, x = opts.x, y = opts.y, w = opts.w, h = opts.h,
        leading = opts.leading || opts.fontSize * 1.2, // make const
        ascent, descent, startX = x, currentX, currentY, rlines = [],
        sb = E, words = [], next, yPos = 0, rt = undefined,
        newParagraph = 0, forceBreak = 0, firstLine = 1,
        maxW = x + w, maxH = y + h;

    if (!txt || !txt.length) return [];

    // 1. get font ascent and descent
    ascent = p5font._textAscent(fsize);
    descent = p5font._textDescent(fsize);
    //console.log('ascent: ',ascent,'descent: ',descent);

    if (Array.isArray(txt)) {

        return layoutArray(txt, x, y, h, ascent, descent, leading);
    }

    // remove line breaks & add spaces around html
    txt = txt.replace(/&gt;/g, '>').replace(/&lt;/g, '<');
    txt = txt.replace(/ ?(<[^>]+>) ?/g, " $1 ");
    txt = txt.replace(/[\r\n]+/g, SP);

    // split into reversed array of words
    RiString._addToStack(txt, words);

    if (!words.length) return [];

    //log("txt.len="+txt.length+" x="+x+" y="+y+" w="+w+" h="+h+" lead="+leading);log(p5font);

    currentY = y + ascent;

    if (INDENT_FIRST_PARAGRAPH)
        startX += PARAGRAPH_INDENT;

    while (words.length > 0) {

        next = words.pop();

        if (!next.length) continue;

        // check for HTML-style tags
        if (/<[^>]+>/.test(next)) {
            if (next === NON_BREAKING_SPACE) {
                sb += SP;
            }
            else if (next === PARAGRAPH_BREAK) {
                newParagraph = true;
            }
            else if (next === LINE_BREAK) {
                forceBreak = true;
            }
            continue;
        }

        // re-calculate our X position
        currentX = startX + font._textWidth(sb + next, fontSize);

        // check it against the line-width
        if (!newParagraph && !forceBreak && currentX < maxW) {
            sb += next + SP;
            // add-word
        }
        else {

            // check yPosition to see if its ok for another line?
            if (RiString._withinBoundsY(currentY, leading, maxH, descent, firstLine)) {

                yPos = firstLine ? currentY : currentY + leading;
                rt = RiString._newRiTextLine(sb, p5font, startX, yPos);
                rlines.push(rt);

                currentY = newParagraph ? rt.get('y') + paragraphLeading : rt.get('y');
                startX = x;
                // reset

                if (newParagraph)
                    startX += PARAGRAPH_INDENT;

                sb = next + SP;
                // reset with next word

                newParagraph = false;
                forceBreak = false;
                firstLine = false;
            }
            else {

                // we've run out of y-space, break the loop and finish
                words.push(next);
                // not needed
                break;
            }
        }
    }

    // check if leftover words can make a new line
    if (RiString._withinBoundsY(currentY, leading, maxH, descent, firstLine)) {

        // TODO: is it possible that there are tags in here?
        yPos = firstLine ? currentY : currentY + leading;
        rlines.push(RiString._newRiTextLine(sb, p5font, x, yPos));
        sb = E;
    }
    else {

        RiString._addToStack(sb, words);
        // save for next (not needed?)
    }

    return rlines;
};

RiString.prototype.position = function(x,y) {
    return this.set('x',x).set('y',y);
}

RiString._withinBoundsY = function(currentY, leading, maxY, descent, firstLine) {
    return firstLine ? (currentY + descent) <= maxY :
        (currentY + leading + descent) <= maxY;
  };

RiString._addToStack = function(txt, words) {
    var tmp = txt.split(SP);
    for ( var i = tmp.length - 1; i >= 0; i--)
        words.push(tmp[i]);
};

RiString._newRiTextLine = function(s, pf, xPos, nextY) {
    return new RiString(s.replace(/ *$/,'')).set('x', xPos).set('y', nextY);/// pf);
};

function endsWith(str, ending) {
    if (typeof s != 'string') return false;
    return new RegExp(ending+'$').test(str);
    //return str.slice(-ending.length) == ending;
}

RiString.createWords = function(options) {
    options.splitter = RiString.prototype.splitWords;
    return RiString._createRiStrings(options);
 };

RiString.createLetters = function(options) {
    options.splitter = RiString.prototype.splitLetters;
    return RiString._createRiStrings(options);
};

RiString._createRiStrings = function(options) {

    var fnt = options.font, fntsz = options.fontSize,
        rlines = RiString.createLines(options);

    //console.log(rlines);
    if (!rlines || rlines.length < 1) return [];

    var result = [];
    for (var i = 0; i < rlines.length; i++) {

        var rs = options.splitter.call(rlines[i], fnt, fntsz);
        for (var j = 0; j < rs.length; j++)
            result.push(rs[j]);
    }

    return result;
};

// Returns the pixel x-offset for the word at 'wordIdx'
RiString._wordOffsetFor = function(font, fontSize, rt, words, wordIdx, alignment) {

    //console.log(wordIdx,rt,words);
    if (wordIdx < 0 || wordIdx >= words.length)
        throw new Error("Bad wordIdx=" + wordIdx + " for " + words);

    var xPos = rt.get('x'), align = alignment || RiTa.LEFT;

    if (wordIdx === 0) return xPos;

    // WORKING HERE *****

    var preStr = E, pre = words.slice(0, wordIdx);
    for ( var i = 0; i < pre.length; i++) {
        preStr += pre[i] + SP;
    }

    var tw = font._textWidth(preStr, fontSize);

    //console.log("x="+xPos+" pre='"+preStr+"' tw=" + tw);

    switch (align) {
        case RiTa.LEFT:
            xPos = rt.get('x') + tw;
            break;
        case RiTa.RIGHT:
            xPos = rt.get('x') - tw;
            break;
        case RiTa.CENTER:
            warn("TODO: test center-align here");
            xPos = rt.get('x'); // ?
            break;
    }

    return xPos;
};

RiString.prototype.charOffset = function(fnt, fntsz, charIdx) {

    var theX = this.get('x');

    if (charIdx > 0) {

        var txt = this.text();

        var len = txt.length;
        if (charIdx > len) // -1?
            charIdx = len;

        var sub = txt.substring(0, charIdx);
        theX += fnt._textWidth(sub, fntsz);
    }

    return theX;
},

function layoutArray(lines, x, y, h, ascent, descent, leading) {

    // sets x,y for those that fit within the height

    var lastOk, currentY = y + ascent, result = [];

    for (var k = 0; k < lines.length; k++)
    {
        result.push(RiString(lines[k]).set('x', x).set('y',  currentY));
        if (!RiString._withinBoundsY(currentY, leading, y + h, descent))
            break;
        currentY += leading;
    }

    return result;
}

function parseOpts(options, defaults) {

  if (typeof options !== 'object') {
    options = defaults;
  }
  else {
    for (var key in defaults) {
      if (typeof options[key] === 'undefined')
        options[key] = defaults[key];
    }
  }
  return options;
}
