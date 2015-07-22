package rita.wordnet.jwnl.dictionary.morph;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class Util {
	public static String getLemma(String[] tokens, BitSet bits, String delimiter) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < tokens.length; i++) {
			if (i != 0 && !bits.get(i-1)) {
				buf.append(delimiter);
			}
			buf.append(tokens[i]);
		}
		return buf.toString();
	}

	public static boolean increment(BitSet bits, int size) {
		int i = size - 1;
		while (i >= 0 && bits.get(i)) {
			bits.set(i--, false);
		}
		if (i < 0) {
			return false;
		}
		bits.set(i, true);
		return true;
	}

	public static String[] split(String str) {
		char[] chars = str.toCharArray();
		List tokens = new ArrayList();
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < chars.length; i++) {
			if ((chars[i] >= 'a' && chars[i] <= 'z') || chars[i] == '\'') {
				buf.append(chars[i]);
			} else {
				if (buf.length() > 0) {
					tokens.add(buf.toString());
                    buf = new StringBuffer();
				}
			}
		}
        if (buf.length() > 0) {
            tokens.add(buf.toString());
        }
		return (String[]) tokens.toArray(new String[tokens.size()]);
	}

	private Util() {
	}
}