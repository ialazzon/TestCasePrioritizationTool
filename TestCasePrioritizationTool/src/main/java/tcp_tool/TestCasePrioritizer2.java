package tcp_tool;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import GA_implementation.GA;
import cap.isula.sample.AcoCapWithIsula;
import greedy.TCP_Greedy_Sequence_Generator;
import trace.*;
import trace.impl.*;


public class TestCasePrioritizer2 {
	private static Scanner input;
	private static String[] rule_names = {
			"Initialize",
			"MetamodelEqName",
			"MetamodelEqPackages",
			"Package2Operation",
			"PackEqMetamodel",
			"PackEqSupPackage",
			"PackEqSubPackages",
			"PackEqClasses",
			"Class2Sort",
			"createSort",
			"EqName",
			"ClassEqIsAbstract",
			"EqPackage",
			"ClassEqSuperTypes",
			"EmptyList",
			"NamedElList",
			"NamedElConstant",
			"ClassEqReferences",
			"ClassEqAttributes",
			"Reference2Operation",
			"EqType",
			"RefEqOpposite",
			"EqLowerBound",
			"EqUpperBound",
			"EqContainingClass",
			"RefEqIsContainment",
			"EqIsOrdered",
			"EqIsUnique",
			"Attribute2Operation",
			"AttEqIsId",
			"AttEqSpecificDefaultValue",
			"Enum2Sort",
			"EnumEqMetaAux",
			"EnumEqDefaultValue",
			"EqLiterals",
			"mtOrdConstant",
			"EnumLitList",
			"EnumLitConstant",
			"EnumLiteral2Operation"
	};
	
	public static void main(String args[]) throws IOException {
		HashSet<Tuple2> coverage_set = new HashSet<>();
		ArrayList<String> test_case_ids = new ArrayList<>();
		 
		File[] files = new File(Paths.get("test_cases_2/").toAbsolutePath().toString()).listFiles();
		for (File file : files) {
			String tcID = StringUtils.substringBetween(file.getName(),"Trace_out", ".xmi");
			if(!test_case_ids.contains(tcID))
				test_case_ids.add(tcID);
			// Load the model using EMF
			ResourceSet resourceSet = new ResourceSetImpl();
			resourceSet.getPackageRegistry().put(TracePackage.eNS_URI, TracePackage.eINSTANCE);
			resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new XMIResourceFactoryImpl());
			
			String loc = Paths.get("test_cases_2/"+ file.getName()).toAbsolutePath().toString(); 
			Resource resource = resourceSet.createResource(URI.createFileURI(loc));

			resource.load(new HashMap<Object, Object>());


			TreeIterator<EObject> ir = resource.getAllContents();
			ArrayList<String> list_rules = new ArrayList<>();
			while(ir.hasNext() ) {
				EObject o = ir.next();
				if(o instanceof TraceLinkImpl) {
					String rule = ((TraceLinkImpl)o).getRuleName();
					if(!list_rules.contains(rule)) list_rules.add(rule);
				}	
			}
			
			for(String r: list_rules) {
				coverage_set.add(new Tuple2(r, tcID));
			}	  
	    }
		
		
		
		String[] tcids = new String[test_case_ids.size()];
		tcids = test_case_ids.toArray(tcids);
		int[][] coverage_matrix = new int[rule_names.length][tcids.length];
		for(int i=0; i<rule_names.length;i++)
			for(int j=0; j< tcids.length; j++) {
				if(coverage_set.contains(new Tuple2(rule_names[i], tcids[j])))
					coverage_matrix[i][j]=1;
						
			}
		
		System.out.println("Number of test cases = "+tcids.length);
		System.out.println("Number of rules = "+ rule_names.length);
		System.out.println("Coverage matrix");
		

		for(int i=0; i<rule_names.length;i++) {
			System.out.println(Arrays.toString(coverage_matrix[i]));
		}
		
		System.out.println("\n\nOrdering returned by Greedy:");
		int[] ordering = TCP_Greedy_Sequence_Generator.getOrderings(rule_names.length, tcids.length, coverage_matrix);
		for(int i: ordering) {
			System.out.print(tcids[i]+" ");
		}
		System.out.println("\nAPRC: "+ TC_SequenceEvaluator_APRD.getAPRD(rule_names.length,tcids.length, coverage_matrix, ordering ));
		
		System.out.println("\n\nOrdering returned by GA:");
		ordering = GA.getOrderings(rule_names.length, tcids.length, coverage_matrix);
		for(int i: ordering) {
			System.out.print(tcids[i]+" ");
		}
		System.out.println("\nAPRC: "+ TC_SequenceEvaluator_APRD.getAPRD(rule_names.length,tcids.length, coverage_matrix, ordering ));
		
		System.out.println("\n\nOrdering returned by ACO:");
		ordering = AcoCapWithIsula.getOrderings(rule_names.length, tcids.length, coverage_matrix);
		for(int i: ordering) {
			System.out.print(tcids[i]+" ");
		}
		System.out.println("\nAPRC: "+ TC_SequenceEvaluator_APRD.getAPRD(rule_names.length,tcids.length, coverage_matrix, ordering ));
		
	}
}

class Tuple2{
	String rule_name;
	String test_case_name;
	
	public Tuple2(String rule_name, String test_case_name) {
		this.rule_name = rule_name;
		this.test_case_name = test_case_name;
	}

	@Override
	public boolean equals(Object obj) {
		return rule_name.equals(((Tuple2)obj).rule_name) && test_case_name.equals(((Tuple2)obj).test_case_name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(rule_name, test_case_name);
	}
	
	
}