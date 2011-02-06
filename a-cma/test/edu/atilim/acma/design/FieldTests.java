package edu.atilim.acma.design;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class FieldTests {
	private static Design testDesign;
	private static Type testType;
	private static Field testField;
	
	@BeforeClass
	public static void createDesign() {
		testDesign 	= new Design();
		testType 	= testDesign.create("TestType", Type.class);
		testField 	= testType.createField("TestField");
		testField.setType(testType);
	}
	
	@AfterClass
	public static void destroyDesign() {
		testType 	= null;
		testDesign 	= null;
		testField 	= null;
	}
	
	@Test
	public void testParent() {
		assertEquals(testField.getOwnerType(), testType);
		assertArrayEquals(testType.getFields().toArray(), new Object[] { testField });
		
		testField.setOwnerType(null);
		
		assertEquals(testField.getOwnerType(), null);
		assertEquals(testType.getFields().size(), 0);
	}
	
	@Test
	//Tests accessor of a field
	public void testGetAccessors(){
		//Create two method 
		Method accessorMethod = new Method("TestMethod", testDesign);
		Method accessorMethod2 = new Method("TestMethod2", testDesign);
	
		
		//Set two method as accessedField to the "field" 
		accessorMethod.addAccessedField(testField);
		accessorMethod2.addAccessedField(testField);
		
		//Call getAccessors() method
		List<Method> temp = testField.getAccessors();
		
		//Check these method are really accessed to the "field"
		assertEquals(accessorMethod, temp.get(0));
		assertEquals(accessorMethod2, temp.get(1));
	}
	
	@Test
	public void testType(){
		//Check current testField type
		assertEquals(testType, testField.getType());
		
		Type typeNew = testDesign.create("TestTypeNew", Type.class);
		
		//SetType of the testField type to the typeNew
		testField.setType(typeNew);

		//Check type of testField
		assertEquals(typeNew, testField.getType());	
		
		//Set type of the testField to the null
		testField.setType(null);
		
		//Check type of testField
		assertNull(testField.getType());	

	}
	
	@Test
	public void testPackage(){
		//TODO:
	}
	
	@Test
	public void testIsConstant(){
		//Make field static and final
		testField.setStatic(true);
		testField.setFinal(true);
		
		//Check is field is constant
		assertTrue(testField.isConstant());
		
		//Change this field to not final
		testField.setFinal(false);
		
		//Check is field is constant
		assertFalse(testField.isConstant());
		
		//Change this field to not static but final
		testField.setStatic(false);
		testField.setFinal(true);
		
		//Check is field is constant
		assertFalse(testField.isConstant());
	}
	
	@Test
	public void testToString(){
		//Set type and owner type of testField
		testField.setType(testType);
		testField.setOwnerType(testType);
		
		String toString= "TestType.TestField:TestType";
		String actualToString = testField.toString();
		
		//Check toString method
		assertEquals(toString,actualToString);
	}
	
	@Test
	public void testRemove(){
		
		Type typeRemoveTest = new Type("type", testDesign);
		Field fieldRemoveTest = new Field("field", testDesign);
		fieldRemoveTest.setOwnerType(typeRemoveTest);
		
		assertTrue(fieldRemoveTest.remove());
		
		fieldRemoveTest = new Field("field", testDesign);
		fieldRemoveTest.setType(typeRemoveTest);
		
		assertTrue(fieldRemoveTest.remove());
	
	}
}
