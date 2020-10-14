package test.Data; 

import Data.XmlLoader;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After;

import static org.junit.Assert.assertEquals;


/** 
* XmlLoader Tester. 
* 
* @author <Authors name> 
* @since <pre>oct. 14, 2020</pre> 
* @version 1.0 
*/ 
public class XmlLoaderTest { 

@Before
public void before() throws Exception { 
} 

@After
public void after() throws Exception { 
} 

/** 
* 
* Method: chargeMap() 
* 
*/ 
@Test
public void testChargeMap() throws Exception {
        XmlLoader xmlLoader = new XmlLoader();
        xmlLoader.chargeMap();
        assertEquals((double) 45.750896, XmlLoader.map.get(479185301).latitude);
} 


} 
