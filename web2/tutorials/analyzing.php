<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
  <meta http-equiv="content-type" content="text/html; charset=utf-8" />
  <title>Tutorial-Analyzing Text</title>
  <meta name="generator" content="JsDoc" />
  <link rel="stylesheet" href="../css/mainStyle.css">
  <link rel="stylesheet" href="../css/tutorial.css">

</head>

<body>


    <div class="gd-section tutorial pad-top-large">
      <div class="gd-center pad-large">
        <div class="row">
          <div class="col2"></div>
          <div class="col8">
            <h4>
              <a href="index.php">
                <span>Tutorial ></span>
              </a>Analyzing Text</h4>
            <ul class="sublist">
              <li><a href="#Phonemes">Phonemes</a></li>
              <li><a href="#Stresses">Stresses</a></li>
              <!-- <li><a href="#Tokens">Tokens</a></li> -->
              <li><a href="#POS">POS (part-of-speech)</a></li>
            </ul>

            <div id="Phonemes" class="pad-small">
              <h5 class="sub">Phonemes</h5>
              <p>A phoneme is a single "unit" of sound that has meaning in any language. Each phoneme
                represents a different sound a person can make. Since there are only 26 letters in the
                alphabet (and about 40 phonemes), sometimes letter combinations are used to make
                a phoneme. Here is an example:
              </p>
              <div class="example">
                <p>
                  chef = /ʃef/
                  <br /> choir = /kwaɪə/
                  <br /> cheese = /tʃi:z/
                </p>
              </div>
              <p>The "ch" letter combination has three different pronunciations, which are represented by three different phonemes: /ʃ/, /k/ and /tʃ/.
              </p>
              <p>In RiTa, we use Arpabet to represent the phonemes. Arpabet is a phonetic transcription code developed by
                Advanced Research Projects Agency (ARPA) as a part of the Speech Understanding Project. It represents each
                phoneme of English with a distinct sequence of ASCII characters.
              </p>
              <p>The above example would be translated like this if we use Arpabet:</p>
              <div class="example">
                <p>
                  chef = ch eh f
                  <br /> choir = k w ay r
                  <br /> cheese = ch iy z
                </p>
              </div>

              <p>You can use <a href="../reference/RiTa/RiTa.getPhonemes/index.php"><b>RiTa.getPhonemes</b></a> to analyse the phonemes.</p>

              <pre><code class="language-javascript">RiTa.getPhonemes("An elephant is a mammal");</code></pre>

              <p>It will return: 'ae-n eh-l-ax-f-ax-n-t ih-z ey m-ae-m-ax-l'.</p><br/>
              <div class="ref">
                <p>A list of the phonemes used in RiTa:
                  <br />
                  <a href="http://www.rednoise.org/rita/reference/PhonemeTags.php">http://www.rednoise.org/rita/reference/PhonemeTags.php</a></p>
                <p>The reference list from Arpabet to IPA:
                  <br />
                  <a href="https://en.wikipedia.org/wiki/Arpabet/">https://en.wikipedia.org/wiki/ARPABET</a>
                  <p>
              </div>

            </div>
            <hr/>

            <div id="Stresses" class="pad-small">
              <h5 class="sub">Stresses</h5>
              <p>In phonetics, stress refers to the degree of emphasis given to a sound or syllable in speech,
                resulting in relative loudness.
                This is also sometimes called <em>lexical stress</em> or <em>word stress</em>.
              </p>

              <p> <a href="../reference/RiTa/RiTa.getStresses/index.php"><b>RiTa.getStresses</b></a> analyzes
                 text and returns a new string containing the stress for each syllable of the input.
              </p>

              <pre><code class="language-javascript">RiTa.getStresses("computer");</code></pre>


              <p> In this case, it returns 0/1/0, with a <b>1 </b>meaning 'stressed', and <b>0 </b>means ‘unstressed’: e.g., com-PUTE-er</p>
            </div>
            <hr />

            <div id="POS" class="pad-small">
              <h5 class="sub">POS (Part of speech)</h5>
              <p>Each part-of-speech is a category of words which have similar grammatical properties.
                Words that are assigned to the same part-of-speech play similar roles within the structure of sentences.
                Commonly used English parts of speech are noun, verb, adjective, adverb, pronoun, preposition, conjunction,
                article, etc.
              </p>

              <p>You can use <a href="../reference/RiTa/RiTa.getPosTags/index.php"><b>RiTa.getPosTags</b></a> to
                 analyze the part-of-speech (or POS) for you.
              </p>
               <pre><code class="language-javascript">RiTa.getPosTags("I am hungry");</code></pre>
               <p>The outcome would be: ["prp", "vbp", "jj"]</p>
              <p>
              Here you can find <a href="../reference/PennTags.html">a reference list of PENN part-of-speech tagger in RiTa</a>.
              </p>

              <p>By default RiTa uses the <b>PENN </b>tag set and it is very detailed. If you only need the very basic POS categories, you can switch to <b>WordNet-style</b> tags,
                 which covers just the following basic categories:
              </p>
              <div style="line-height:1.618em!important;">
                <p>
                  n -> NOUN
                  <br /> v -> VERB
                  <br /> a -> ADJECTIVE
                  <br /> r -> ADVERB
                  <br />
                </p>
              </div>
            </div>

            <hr/>

            <div id="Next" class="pad-small">
              <p>NEXT > see <a href="transform.php"><b>Transformation</b></a></p>
            </div>

          </div>
          <div class="col2"></div>
        </div>
      </div>
    </div>


</body>

</html>
