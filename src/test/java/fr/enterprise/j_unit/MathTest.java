package fr.enterprise.j_unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

public class MathTest {

  @Test()
  void testAbsolute() {
    int number = -4;

    assertEquals(Math.abs(number), 4);
    assertNotEquals(Math.abs(number), -4);
  }

  @Test()
  void absoluteThrowsExceptionWhenDividedByZero() throws ArithmeticException {
    try {
      Math.abs(4/0);
      fail("ArithmeticException expected");
    } catch (ArithmeticException e) {
    }

  }
  
  @Test()
  void StringRepeatThrowsExceptionWhenNegativeParam() {
    String str = "Bonjour";
    
    Exception ex = assertThrows(IllegalArgumentException.class, () -> {
      str.repeat(-1);
    });

    assert ex.getMessage().equals("count is negative: -1");
  }
 
}
