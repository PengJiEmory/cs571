/**
 * Copyright 2015, Emory University
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.emory.mathcs.nlp.component.dep;

import static org.junit.Assert.assertEquals;

import java.io.FileInputStream;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.Test;

import edu.emory.mathcs.nlp.common.util.Joiner;
import edu.emory.mathcs.nlp.component.util.feature.Direction;
import edu.emory.mathcs.nlp.component.util.feature.Field;
import edu.emory.mathcs.nlp.component.util.node.FeatMap;
import edu.emory.mathcs.nlp.component.util.reader.TSVReader;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class DEPNodeTest
{
	@Test
	public void test() throws Exception
	{
		TSVReader<DEPNode> reader = new TSVReader<>(new DEPIndex(1, 2, 3, 4, 5, 6));
		reader.open(new FileInputStream("src/main/resources/dat/wsj_0001.dep"));
		DEPNode[] nodes = reader.next();
		
		// TODO:
		System.out.println(Joiner.join(nodes, "\n"));
	}
	
	@Test
	public void testBasicFields()
	{
		DEPNode node = new DEPNode(1, "Jinho");
		
		assertEquals(1      , node.getID());
		assertEquals("Jinho", node.getWordForm());
		
		node = new DEPNode(1, "Jinho", "jinho", "NNP", new FeatMap("fst=jinho|lst=choi"));
		
		assertEquals(1       , node.getID());
		assertEquals("Jinho" , node.getWordForm());
		assertEquals("jinho" , node.getLemma());
		assertEquals("NNP"   , node.getPOSTag());
		
		node.removeFeat("fst");
		assertEquals(null  , node.getFeat("fst"));
		assertEquals("choi", node.getFeat("lst"));
		
		node.putFeat("fst", "Jinho");
		assertEquals("Jinho", node.getFeat("fst"));
	}
	
	@Test
	public void testSetters()
	{
		DEPNode node1 = new DEPNode(1, "He");
		DEPNode node2 = new DEPNode(2, "bought");
		DEPNode node3 = new DEPNode(3, "a");
		DEPNode node4 = new DEPNode(4, "car");
		
		node2.addDependent(node4, "dobj");
		node2.addDependent(node1, "nsubj");
		node4.addDependent(node3, "det");
		
		List<DEPNode> list = node2.getDependentList();
		assertEquals(node1, list.get(0));
		assertEquals(node4, list.get(1));
	}
	
	//PJ tests start
	@Test
	public void testGetters()
	{    
	    DEPNode node1  = new DEPNode(1 , "David");
	    DEPNode node2  = new DEPNode(2 , "'s");
	    DEPNode node3  = new DEPNode(3 , "officers");
	    DEPNode node4  = new DEPNode(4 , "went");
	    DEPNode node5  = new DEPNode(5 , "to");
	    DEPNode node6  = new DEPNode(6 , "the");
	    DEPNode node7  = new DEPNode(7 , "east");
	    DEPNode node8  = new DEPNode(8 , "land");
	    DEPNode node9  = new DEPNode(9 , "of");
	    DEPNode node10 = new DEPNode(10 , "the");
	    DEPNode node11 = new DEPNode(11, "Ammonites");
	    
	    node1.addDependent(node2);
	    node4.addDependent(node1);
	    node3.addDependent(node1);
	    node4.addDependent(node3);
	    node4.addDependent(node5);
	    node5.addDependent(node8);
	    node8.addDependent(node6, "det");
	    node8.addDependent(node7);
	    node8.addDependent(node9);
	    node9.addDependent(node11);
	    node11.addDependent(node10, "det");
	    
	    String[] labels = {"dobj","nsubj","det","nn", "punct","num","prep"};
	    node1.setLabel("nsubj");
	    assertEquals("nsubj", node1.getLabel());
	    assertEquals(node9, node11.getGrandHead());
	    assertEquals(node3, node1.getHead());
	    assertEquals(node7, node9.getLeftNearestSibling());
	    assertEquals(node7, node9.getLeftNearestSibling(1));
	    assertEquals(node6, node9.getLeftNearestSibling("det"));
	    assertEquals(null, node9.getLeftNearestSibling("num"));
	    assertEquals(node5, node3.getRightNearestSibling());
	    assertEquals(node6, node8.getLeftMostDependent());
	    assertEquals(node7, node8.getLeftMostDependent(1));
	    assertEquals(node7, node8.getLeftNearestDependent());
	    assertEquals(node6, node8.getFirstDependentByLabel(Pattern.compile("d..")));
	    List<DEPNode> nodes = node8.getDescendantList(2);
	    assertEquals(nodes.get(3), node11);
	    node10.setPOSTag("DT");
	    assertEquals(node10, node9.getAnyDescendantByPOSTag("DT"));
	    assertEquals(3, node8.getDependentSize());
	    assertEquals("<<->", node8.getValency(Direction.all));
	    assertEquals(">det", node11.getSubcategorization(Direction.all,  Field.dependency_label));
	    assertEquals(node8, node6.getLowestCommonAncestor(node10));
	    assertEquals("^1|3", node6.getPath(node10, Field.distance));
	}
	//PJ tests end
}
