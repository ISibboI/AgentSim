package de.isibboi.agentsim.ui.component;

import java.text.DecimalFormat;

import de.isibboi.agentsim.game.map.Point;
import de.isibboi.agentsim.ui.Renderer;
import de.isibboi.agentsim.ui.meter.Meter;

/**
 * A label that shows a number.
 * 
 * @author Sebastian Schmidt
 * @since 0.2.0
 */
public class UINumberLabel extends UILabel {
	private DecimalFormat _decimalFormat;
	private String _prefix;
	private String _postfix;
	private double _value;
	
	/**
	 * Creates a new ui number label.
	 * @param renderer The renderer used to render this label.
	 * @param position The position of this label.
	 * @param width The width of this label.
	 */
	public UINumberLabel(final Renderer renderer, final Point position, final int width) {
		super(renderer, position, width);
	}
	
	/**
	 * Creates a new ui number label.
	 * @param renderer The renderer used to render this label.
	 * @param position The position of this label.
	 * @param width The width of this label.
	 * @param prefix The prefix of the number.
	 * @param postfix The postfix of the number.
	 * @param decimalAmount The amount of decimal digits behind the comma that should be shown.
	 * @param value The initial value of the number.
	 */
	public UINumberLabel(final Renderer renderer, final Point position, final int width,
			final String prefix, final String postfix, final int decimalAmount, final double value) {
		super(renderer, position, width);
		
		_prefix = prefix;
		_postfix = postfix;
		setDecimalAmount(decimalAmount);
		setValue(value);
	}

	/**
	 * Sets the amount of decimals to display.
	 * @param decimalAmount The amount of decimals to display.
	 */
	public void setDecimalAmount(final int decimalAmount) {
		String formatString = "###,###,###,###,###,##0";
		
		if (decimalAmount > 0) {
			formatString += ".";
		}
		
		for (int i = 0; i < decimalAmount; i++) {
			formatString += "0";
		}
		
		_decimalFormat = new DecimalFormat(formatString);
 	}

	/**
	 * @return The prefix.
	 */
	public String getPrefix() {
		return _prefix;
	}

	/**
	 * @param prefix The prefix to set.
	 */
	public void setPrefix(final String prefix) {
		this._prefix = prefix;
	}

	/**
	 * @return The postfix.
	 */
	public String getPostfix() {
		return _postfix;
	}

	/**
	 * @param postfix The postfix to set.
	 */
	public void setPostfix(final String postfix) {
		this._postfix = postfix;
	}

	/**
	 * @return The value.
	 */
	public double getValue() {
		return _value;
	}

	/**
	 * @param value The value to set.
	 */
	public void setValue(final double value) {
		_value = value;
		
		setText(_prefix + _decimalFormat.format(_value) + _postfix);
	}
	
	/**
	 * @param meter The meter to extract the value from.
	 */
	public void setValue(final Meter meter) {
		_value = meter.getValue();
		
		setText(_prefix + _decimalFormat.format(_value) + _postfix);
	}
}