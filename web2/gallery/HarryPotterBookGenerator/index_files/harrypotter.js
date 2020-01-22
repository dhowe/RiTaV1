var allText;
var startPhrases, bookTitles, chapterTitles;

var seed = "";
var bookTitle = "";
var bookContent;
var isBuilding = false;

String.prototype.format = function() {
	var args = arguments;
	return this.replace(/{(\d+)}/g, function(match, number) {
		return typeof args[number] != 'undefined' ? args[number] : match;
	});
};

$(document).ready(function() {
	$("#generateBookButton").hide();
	$("#generateBookButton").hover(function() {
		$(this).addClass("generateBookButtonHover");
	}, function() {
		$(this).removeClass("generateBookButtonHover");
	});
	$("#generateBookButton").mousedown(function() {
		$(this).addClass("generateBookButtonPressed");
	});
	$("#generateBookButton").mouseup(function() {
		$(this).removeClass("generateBookButtonPressed");
	});

	$("#generateBookButton").click(function(e) {
		$("#bookContent").empty();
		setSeed();
		buildBook(10, 200);
		//$("#generateBookButton").fadeOut(100);

	});

	init();
});
function init() {
	$("#progress").fadeIn(500);

	//load text
	loadProgress(0);
	var request = new XMLHttpRequest();
	request.open('GET', "texts/hp_small.txt", true);
	request.onload = function(e) {
		//document.applet.analyse(request.response);
		allText = request.response;
		loaded();
	}
	request.onprogress = function(e) {
		if(e.lengthComputable) {
			loadProgress((e.loaded / e.total) * 100);
		}
	}
	request.send();

}

function loaded() {
	findStartPhrases();
	findTitles();
	buildWordRelationships(1);

	if(location.hash !== "") {
		setSeed(location.hash.replace("#", ""));
		buildBook(5, 200);
	}

	$("#generateBookButton").fadeIn(500);
	$("#progress").fadeOut(500);
}

function setSeed(seed) {
	if(!seed || seed.length === 0) {
		var text = "";
		var possible = "abcdefghijklmnopqrstuvwxyz0123456789";
		Math.seedrandom();
		for(var i = 0; i < 8; i++)
			text += possible.charAt(Math.floor(Math.random() * possible.length));
		seed = text;
	}
	Math.seedrandom(seed);
	location.hash = "#" + seed;
}

function findStartPhrases() {
	var textNoConv = allText.replace(/“[^“”]*”/, "");

	var matches = textNoConv.match(/\.\s\w*\s\w*\s\w*\s/g);

	startPhrases = [];

	for(var i = 0; i < matches.length; i++) {

		var phrase = matches[i].replace(".", "").trim();
		if(phrase.split(" ").length == 3)
			startPhrases.push(phrase);
	}
}

function findTitles() {
	var matches = allText.match(/the (\w*\s)*\w*\./g);

	bookTitles = [];
	chapterTitles = [];

	for(var m = 0; m < matches.length; m++) {
		var words = RiTa.tokenize(matches[m].replace("the ", ""));
		var tags = RiTa.getPosTags(words);
		var title = "";
		var type = "";
		var len = 0;

		while(type !== "nn" && type !== "nns" && type !== "nnp" && type !== "nnps" && len < tags.length) {
			type = tags[len];
			title += words[len] + " ";
			len++;
		}

		if(title.indexOf(".") == -1 && title.indexOf("-") == -1) {
			if(len > 0) {
				var chapterTitle;

				chapterTitle = "The " + toTitleCase(title);
				chapterTitles.push(chapterTitle.trim());

				chapterTitle = toTitleCase(RiTa.pluralize(title));
				chapterTitles.push(chapterTitle.trim());
			}

			if(len > 1) {
				var bookTitle = toTitleCase(title);
				bookTitles.push(bookTitle.trim());
			}
		}
	}
}

function getStartPhrase() {
	return startPhrases[Math.floor(Math.random() * startPhrases.length)];
}

function getBookTitle() {
	return bookTitles[Math.floor(Math.random() * bookTitles.length)];
}

function getChapterTitle() {
	return chapterTitles[Math.floor(Math.random() * chapterTitles.length)];
}

function toTitleCase(str) {
	return str.replace(/\w\S*/g, function(txt) {
		return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();
	});
}

function buildWordRelationships(perc) {
	console.log("Building relationships...");

	wordMap = [];
	conversationWordMap = [];

	var tokens = allText.split(" ");

	var wordTriplet = [];

	var isConversation = false;

	var iMax = Math.floor((tokens.length - 1) * perc);

	for(var i = 0; i < iMax; i++) {
		var word = tokens[i];
		var nextWord = tokens[i + 1];

		wordTriplet[2] = wordTriplet[1];
		wordTriplet[1] = wordTriplet[0];
		wordTriplet[0] = word;

		var wordKey = wordTriplet[2] + wordTriplet[1] + wordTriplet[0];

		var count = word.length - word.replace("\"", "").length;
		if(count == 1 && word.indexOf("\"") == 0)
			isConversation = true;
		if(count == 1 && word.indexOf("\"") == word.length - 1)
			isConversation = false;

		// println(isConversation + " " + word);
		if(isConversation) {
			nextWords = conversationWordMap[wordKey];
			if(nextWords === undefined)
				nextWords = [];

			nextWords.push(nextWord);

			conversationWordMap[wordKey] = nextWords;
		} else {
			nextWords = wordMap[wordKey];
			if(nextWords === undefined)
				nextWords = [];

			nextWords.push(nextWord);

			wordMap[wordKey] = nextWords;
		}

		if(i % 10000 === 0) {
			//console.log(i / iMax);
		}

	}

	//console.log("Done. Found " + (wordMap.size() + conversationWordMap.size()) + " word triplets.");
}

function buildBook(nChapters, maxWords) {
	var bookTitle = getBookTitle();
	$("#bookTitle").text(bookTitle);

	for(var chapterIndex = 1; chapterIndex <= nChapters; chapterIndex++) {
		var chapterTitle = getChapterTitle();
		var chapterText = buildChapter(maxWords);

		newChapter(chapterIndex, chapterTitle, chapterText);
	}

	bookFinished();
}

function buildChapter(maxWords) {
	var chapterText = "";
	var nChapterWords = 0;

	while(nChapterWords < 100) {
		var isConversation = false;
		var startPhrase = getStartPhrase();
		if(startPhrase.indexOf("\"") == 0)
			isConversation = true;
		else
			isConversation = false;

		chapterText += startPhrase + " ";
		// println(startPhrase);
		var wordTriplet = startPhrase.split(" ");
		// println(wordTriplet);
		var temp = wordTriplet[0];
		wordTriplet[0] = wordTriplet[2];
		wordTriplet[2] = temp;

		var isFinishingChapter = false;

		while(nChapterWords <= maxWords || (nChapterWords > maxWords && isFinishingChapter)) {
			var word = wordTriplet[0];
			var wordKey = wordTriplet[2] + wordTriplet[1] + wordTriplet[0];

			var count = word.length - word.replace("\"", "").length;
			if(count === 1 && word.indexOf("\"") == 0)
				isConversation = true;
			if(count === 1 && word.indexOf("\"") === word.length - 1)
				isConversation = false;

			var nextWord = "";
			var nextWords;
			if(isConversation)
				nextWords = conversationWordMap[wordKey];
			else
				nextWords = wordMap[wordKey];

			if(nextWords === undefined)
				break;
			nextWord = nextWords[Math.floor(Math.random() * nextWords.length)];

			wordTriplet[2] = wordTriplet[1];
			wordTriplet[1] = wordTriplet[0];
			wordTriplet[0] = nextWord;

			chapterText += nextWord + " ";

			// if over max words and end of sentence, end chapter
			if(isFinishingChapter) {
				if(nextWord.indexOf(".") !== -1)
					break;
			}

			nChapterWords++;

			if(nChapterWords >= maxWords)
				isFinishingChapter = true;
		}
	}
	return chapterText.replace(/#n#t/g, "\n\t");
}

function newChapter(number, title, content) {
	$("#bookContent").append("<div class='chapterTitle'>Chapter {0} - {1}</div>".format(number, title));
	var chapterContent = $("<div class='chapterContent'></div>");
	$("#bookContent").append(chapterContent);
	var lines = content.split("\n");

	var chapterContentText = "";
	for(var i = 0; i < lines.length; i++) {
		chapterContentText += "<p class='chapterParagraph'>{0}</p>".format(lines[i]);
	}
	chapterContent.append(chapterContentText);

	$("#bookContent").append("<br>");

	$("#scroll-loop").height(Math.max((Math.ceil(($("#bookContent").height()) / 286)) * 286), 0);
}

function bookFinished() {
	$("#bookContent").append("<p class='theEnd'>The End</p>");
	refreshTweetButton();
	isBuilding = false;
	//$("#generateBookButton").fadeIn(100);
}

function loadProgress(val) {
	$("#progress").text("Loading: " + val.toFixed(0) + "%");
}

function refreshTweetButton() {
	// Remove existing iframe
	$('#tweetBtn iframe').remove();
	// Generate new markup
	var tweetBtn = $('<a></a>').addClass('twitter-share-button').attr('href', 'http://twitter.com/share').attr('data-url', location.href).attr('data-text', "New Harry Potter book! Harry Potter and the " + $("#bookTitle").text());
	$('#tweetBtn').append(tweetBtn);
	twttr.widgets.load();
}
