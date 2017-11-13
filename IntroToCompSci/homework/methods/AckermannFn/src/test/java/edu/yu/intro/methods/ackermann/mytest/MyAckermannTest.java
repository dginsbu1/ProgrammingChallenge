
package edu.yu.intro.methods.ackermann.mytest;
import edu.yu.intro.methods.ackermann.Ackermann;
import org.junit.*; 
import static org.junit.Assert.*;


public class MyAckermannTest{

	@Test 
	public void ackermann00() { 
		assertEquals("testing ackermann", 1, Ackermann.ackermann(0L,0L)); 
	}
	@Test 
	public void ackermann21() { 
		assertEquals("testing ackermann", 5, Ackermann.ackermann(2L,1L)); 
	}
	@Test 
	public void ackermann33() { 
		assertEquals("testing ackermann", 61, Ackermann.ackermann(3L,3L)); 
	}

	@Test(expected = IllegalArgumentException.class)
	public void firstNegativeInputRejected() { 
		Ackermann.ackermann(-1L,1L); 
	}
	@Test(expected = IllegalArgumentException.class) 
	public void SecondNegativeInputRejected() { 
		Ackermann.ackermann(3L,-2L); 
	}
	
}
	