package com.gmail.leonidandand.matrix.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	TestPosition.class,
	TestDimension.class,
	TestArrayMatrix.class,
	TestIterableArrayMatrix.class
})
public class AllTests {

}
