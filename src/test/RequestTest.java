package test;

import java.util.ArrayList;
import java.util.List;

import com.dogo.generator.JDOCGenerator;
import com.jdogo.response.JDOCParser;

import model.Customer;
import model.Location;
import model.Product;
import model.Stock;

public class RequestTest {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

		Location location = new Location();
		location.setId(10);
		location.setOpened(true);
		location.setLocationName("Nasr City");

		Stock stock = new Stock();
		stock.setId(10);
		stock.setLabel("Stock 1");
		stock.setLocation(location);

		Product product = new Product();
		product.setId(1);
		product.setName("Car");
		product.setQuantity(10);
		product.setStock(stock);

		// Setting Object
		Customer customer = new Customer();
		customer.setId(1);
		customer.setName("Khaled");
		customer.setProduct(product);
		customer.setActivated(true);
		
		Customer customer2 = new Customer();
		customer2.setId(2);
		customer2.setName("Mahmoud");
		customer2.setProduct(product);
		customer2.setActivated(true);
		
		
		/////////////////////////////////////////////////
		List<Customer> customers= new ArrayList<>();
		customers.add(customer);
		customers.add(customer2);
		
		///////////////////////////////////////////////////
		// JDOCGenerator generator= new JDOCGenerator(Patient.class);
		// /// Excluded
		// // generator.excludeFieldsFromObject("ObjectSoghnan", excludes);
		// //generator.excludeFieldsFromObject("ObjectKber", excludes);
		// //generator.excludeObject("ObjectSoghnan");
		// generator.excludeObject("Address");
		// //generator.excludeObject("ObjectSoghnan");
		// //generator.excludeFieldsFromObject("ObjectKber", excludes);
		// //generator.excludeFieldsFromObject("Hospital", exx);
		// String jdocCode=generator.generateObject(patient1);
		// String jdocCode2=generator.generateObjectList(patients);

		JDOCGenerator generator = new JDOCGenerator(Customer.class);
		//generator.excludeObject("Hospital");
		//generator.excludeFieldsFromObject("Address", "name", "id");
		// generator.excludeFieldsFromObject("Location","code");

		String jdocCode = generator.generateObject(customer);

		String jdocCode2 = generator.generateObjectList(customers);

		System.out.println("MY JDoc Code " + jdocCode);

		System.out.println(">> DOC " + jdocCode2);
		////
		JDOCParser parser = new JDOCParser();
		//
		System.out.println(">>> PATIENT MODEL " + customer.toString());
		// // Data Header and body
		// Patient patient = (Patient) parser.getObject(Patient.class,
		// jdocCode);ssss
		// System.out.println("<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>");
		//// System.out.println("<<>> "+patient);
		List<Customer> customers2= (List<Customer>) parser.getObjectList(Customer.class, jdocCode2);
		for (Customer cu: customers2) {
			System.out.println(cu.toString());
		}
		//
	}// end main

}// end class
