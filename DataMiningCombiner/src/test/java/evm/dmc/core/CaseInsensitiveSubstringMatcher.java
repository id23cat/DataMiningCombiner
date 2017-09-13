package evm.dmc.core;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * Borrowed on {@link https://gist.github.com/spuklo/660c4504d088a1d7f38f}
 *
 */
public class CaseInsensitiveSubstringMatcher extends TypeSafeMatcher<String> {
	private final String subString;

	private CaseInsensitiveSubstringMatcher(final String subString) {
		this.subString = subString;
	}

	@Override
	protected boolean matchesSafely(final String actualString) {
		return actualString.toLowerCase().contains(this.subString.toLowerCase());
	}

	@Override
	public void describeTo(final Description description) {
		description.appendText("containing substring \"" + this.subString + "\"");
	}

	@Factory
	public static Matcher<String> containsIgnoringCase(final String subString) {
		return new CaseInsensitiveSubstringMatcher(subString);
	}
}
