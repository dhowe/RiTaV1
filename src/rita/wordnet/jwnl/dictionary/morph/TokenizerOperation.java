package rita.wordnet.jwnl.dictionary.morph;


import java.util.Arrays;
import java.util.BitSet;
import java.util.Map;

import rita.wordnet.jwnl.JWNLException;
import rita.wordnet.jwnl.util.factory.Param;
import rita.wordnet.jwnl.util.factory.ParamList;
import rita.wordnet.jwnl.wndata.POS;

public class TokenizerOperation extends AbstractDelegatingOperation {
    /**
     * Parameter that determines the operations this operation
     * will perform on the tokens.
     */
    public static final String TOKEN_OPERATIONS = "token_operations";
    /**
     * Parameter that determines the operations this operation
     * will perform on the phrases.
     */
    public static final String PHRASE_OPERATIONS = "phrase_operations";
    /**
     * Parameter list that determines the delimiters this
     * operation will use to concatanate tokens.
     */
    public static final String DELIMITERS = "delimiters";

    private String[] _delimiters;

    public TokenizerOperation() {
    }

    public TokenizerOperation(String[] delimiters) {
        _delimiters = delimiters;
    }

    protected AbstractDelegatingOperation getInstance(Map params) throws JWNLException {
        ParamList delimiters = (ParamList) params.get(DELIMITERS);
        String[] delimiterArray = null;
        if (delimiters == null || delimiters.getParams().size() == 0) {
            delimiterArray = new String[]{" "};
        } else {
            delimiterArray = new String[delimiters.getParams().size()];
            for (int i = 0; i < delimiters.getParams().size(); i++) {
                delimiterArray[i] = ((Param) delimiters.getParams().get(i)).getValue();
            }
        }
        return new TokenizerOperation(delimiterArray);
    }

    protected String[] getKeys() {
        return new String[]{TOKEN_OPERATIONS, PHRASE_OPERATIONS};
    }

    public boolean execute(POS pos, String lemma, BaseFormSet forms) throws JWNLException {
        String[] tokens = Util.split(lemma);
        BaseFormSet[] tokenForms = new BaseFormSet[tokens.length];

        if (!hasDelegate(TOKEN_OPERATIONS)) {
            addDelegate(TOKEN_OPERATIONS, new Operation[]{new LookupIndexWordOperation()});
        }
        if (!hasDelegate(PHRASE_OPERATIONS)) {
            addDelegate(PHRASE_OPERATIONS, new Operation[] {new LookupIndexWordOperation()});
        }

        for (int i = 0; i < tokens.length; i++) {
            tokenForms[i] = new BaseFormSet();
            tokenForms[i].add(tokens[i]);
            delegate(pos, tokens[i], tokenForms[i], TOKEN_OPERATIONS);
        }
        boolean foundForms = false;
        for (int i = 0; i < tokenForms.length; i++) {
            for (int j = tokenForms.length - 1; j >= i; j--) {
                if (tryAllCombinations(pos, tokenForms, i, j, forms)) {
                    foundForms = true;
                }
            }
        }
        return foundForms;
    }

    private boolean tryAllCombinations(
            POS pos, BaseFormSet[] tokenForms, int startIndex, int endIndex, BaseFormSet forms)
            throws JWNLException {

        int length = endIndex - startIndex + 1;
        int[] indexArray = new int[length];
        int[] endArray = new int[length];
        for (int i = 0; i < indexArray.length; i++) {
            indexArray[i] = 0;
            endArray[i] = tokenForms[startIndex + i].size() - 1;
        }

        boolean foundForms = false;
        for (; ;) {
            String[] tokens = new String[length];
            for (int i = 0; i < length; i++) {
                tokens[i] = tokenForms[i + startIndex].getForm(indexArray[i]);
            }
            for (int i = 0; i < _delimiters.length; i++) {
                if (tryAllCombinations(pos, tokens, _delimiters[i], forms)) {
                    foundForms = true;
                }
            }

            if (Arrays.equals(indexArray, endArray)) {
                break;
            }

            for (int i = length - 1; i >= 0; i--) {
                if (indexArray[i] == endArray[i]) {
                    indexArray[i] = 0;
                } else {
                    indexArray[i]++;
                    break;
                }
            }
        }
        return foundForms;
    }

    private boolean tryAllCombinations(
            POS pos, String[] tokens, String delimiter, BaseFormSet forms) throws JWNLException {
        BitSet bits = new BitSet();
        int size = tokens.length - 1;

        boolean foundForms = false;
        do {
            String lemma = Util.getLemma(tokens, bits, delimiter);
            if (delegate(pos, lemma, forms, PHRASE_OPERATIONS)) {
                foundForms = true;
            }
        } while (Util.increment(bits, size));

        return foundForms;
    }
}