package de.isibboi.agentsim.ui.components;

import java.text.DecimalFormat;

import javax.swing.JLabel;

/**
 * A JLabel that displays numbers in a certain formatted way.
 * Use {@link #setNumber} for showing a formatted number.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 */
@SuppressWarnings("serial")
public class NumberLabel extends JLabel {
	private final String _preText;
	private final String _postText;
	private final DecimalFormat _numberFormat;

	/**
	 * Creates a new NumberLabel with the given components.
	 * 
	 * @param preText The string that should be put before the number.
	 * @param numberFormat The format of the number.
	 * @param postText The string that should be put after the number.
	 */
	public NumberLabel(final String preText, final DecimalFormat numberFormat, final String postText) {
		super(preText + postText);

		_preText = preText;
		_numberFormat = numberFormat;
		_postText = postText;
	}

	/**
	 * Creates a new NumberLabel with the given components.
	 * There won't be any text after the number.
	 * 
	 * @param preText The string that should be put before the number.
	 * @param numberFormat The format of the number.
	 */
	public NumberLabel(final String preText, final DecimalFormat numberFormat) {
		this(preText, numberFormat, "");
	}

	/**
	 * Sets the value of the number.
	 * @param number The number.
	 */
	public void setNumber(double number) {
		super.setText(_preText + _numberFormat.format(number) + _postText);
	}
}