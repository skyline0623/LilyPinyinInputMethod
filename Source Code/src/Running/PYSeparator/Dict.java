package Running.PYSeparator;

public interface Dict {
	/**
	 * 
	 * @param py 
	 * @return 0 when py is a legal Pinyin;
	 * 		   1 when py is the prefix of a legal Pinyin
	 *         2 when py is not possibly a legal Pinyin 
	 */
	public int costs(String py);
}
