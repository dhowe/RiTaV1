/*jslint node: true */

var input = '../src/rita/rita_dict.js',
    output = '../src/rita/rita_dict_edited.js';

var INTERACTIVE = 1,
    APPENDING = 2,
    COMPLETED = 3;

var lr = new(require('line-by-line'))(input),
    keypress = require('keypress'),
    fs = require("fs"),
    tty = require('tty');

var current,
    state = INTERACTIVE,
    re = /,'nns'/;

// create output file
fs.close(fs.openSync(output, 'w'));

// setup keypress

process.stdin.on('keypress', function(ch, key) {

        //console.log('got: ', key.name);
        if (key && current) {

            if (key.name === 'x') { // reject

                console.log('\nRemoving: ' + current + '\n');
                current = false;
                setTimeout(function() {
                    lr.resume();    
                }, 800);
                
            } else if (key.name === 'q') { // finish

                state = APPENDING;
                console.log('\nAppending remainder...\n');
                lr.resume();
                process.stdin.pause();

            } else if (key.ctrl && key.name === 'c') { // exit

                process.exit();
            }
        }
    });

// setup line-reader

lr.on('error', function(e) {
        throw e;
    });

lr.on('line', function(line) {

        current = line;

        if (state == INTERACTIVE) {

            console.log(line);
            lr.pause();

            if (!re.test(line)) {

                writeAndResume();

            } else {
                // wait for a key
            }

        } else {

            // writing remainder
            writeAndResume();
        }
    });

lr.on('end', function() {

        // All lines are read, file is closed now.
        console.log('Wrote ' + output);
        state = COMPLETED;
        console.log("Write results to '" + input + "'? [N/y]");
        queryUser();
    });

function queryUser() {

    process.stdin.resume();
    process.stdin.once("data", function(data) {

            if (data.toString().trim() === 'y') {

                var backup = input + ".bak";
                try {

                    require('fs-sync').copy(input, backup);
                    require('fs-sync').copy(output, input, {
                            force: true
                        });
                    console.log("Saved original to '" + backup);
                    console.log("Wrote results to '" + input);
                    fs.unlinkSync(input);
                } catch (e) {

                    //require('fs-sync').copy(backup, input);
                    throw e;
                }
            }
            console.log('Done');
            process.exit();
        });
}

function writeAndResume() {

    if (current) fs.appendFile(output, current + '\n');
    current = false;
    lr.resume();
}

// start it up
process.stdin.setRawMode(true);
keypress(process.stdin);
process.stdin.resume();