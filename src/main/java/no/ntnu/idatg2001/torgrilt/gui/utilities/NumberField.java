package no.ntnu.idatg2001.torgrilt.gui.utilities;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;
import javafx.scene.control.TextField;

public class NumberField extends TextField {

  private final DecimalFormat decimalFormat;

  public NumberField(Double value) {
    super();
    decimalFormat = new DecimalFormat("#,##0.00 $", new DecimalFormatSymbols(Locale.US));
    decimalFormat.setParseBigDecimal(true);
    init();
    if (value != null) {
      setDouble(value);
    }
  }

  public void setDouble(double value) {
    setText(decimalFormat.format(value));
  }

  public double getDouble() {
    String value = getText();
    if (isValid(value)) {
      try {
        BigDecimal parsedNumber = (BigDecimal) decimalFormat.parse(value);
        return parsedNumber.doubleValue();
      } catch (ParseException e) {
        throw new NumberFormatException("Invalid double value: " + value);
      }
    } else {
      throw new NumberFormatException("Invalid double value: " + value);
    }
  }

  private boolean isValid(String value) {
    return value != null && !value.isEmpty();
  }

  private void init() {
    textProperty().addListener((observable, oldValue, newValue) -> {
      if (!isValid(newValue)) {
        setText("");
      }
    });
  }
}