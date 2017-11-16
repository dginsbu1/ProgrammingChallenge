package edu.yu.intro.junit_testing.sqrt.test;
//import edu.yu.intro.junit_testing.sqrt.RefactoredSqrtApproximation;
import edu.yu.intro.junit_testing.sqrt.RefactoredSqrtApproximation;
import org.junit.*; 
import static org.junit.Assert.*;

public class SqrtApproximationTest{
	public static final double delta = 0.001;

	@Test(expected=RuntimeException.class) 
	public void sqrtRootCalculationInputCantBeNegative(){
	RefactoredSqrtApproximation.sqrt(-3);	
	}

	@Test(expected=RuntimeException.class) 
	public void calculateSquareRootsInputCantBeNegative(){
	RefactoredSqrtApproximation.calculateSquareRoots(-4);	
	}

	@Test public void sqrt0(){
		assertEquals(0, RefactoredSqrtApproximation.sqrt(0), delta); 
	}
	@Test public void sqrt2(){
		assertEquals(Math.sqrt(2), RefactoredSqrtApproximation.sqrt(2), delta);
	}
	@Test public void sqrt42(){
		assertEquals(Math.sqrt(42), RefactoredSqrtApproximation.sqrt(42), delta);
	}
	@Test public void sqrt10(){
	assertEquals(Math.sqrt(10), RefactoredSqrtApproximation.sqrt(10), delta);
	}
	@Test public void calculateSquareRootsMinimalLength(){
		double[] min = {0};
		assertArrayEquals(min, RefactoredSqrtApproximation.calculateSquareRoots(0), delta);
	}

	@Test public void calculateSquareRootsHappyPaths(){
		double[] array6 = {Math.sqrt(0), Math.sqrt(1), Math.sqrt(2), Math.sqrt(3), Math.sqrt(4), Math.sqrt(5), Math.sqrt(6)};
		assertArrayEquals(array6, RefactoredSqrtApproximation.calculateSquareRoots(6), delta);
	}
}