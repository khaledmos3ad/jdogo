# JDogo API

Jdogo will facititate the way of exchanging data between 2 Services developed in Java Language .

## Getting Started

JDogo Stands for "**J**ava **D**etailed **O**bject **Go**",The main purpose of JDogo is to provide a new way of producing data without taking care about a lot of layers starting from buiding a DTO Object to hold the received Model and external transformation or Mapper to handle the filling process from and to the RequestModel and handle the datatype convesions, Null Values and so on 

With JDogo you will not care about all of these,So you can send any Object tree with any depth level you want and JDogo will handle the mapping perfectly,You can exclude all the Sensitive data or unneeded fields with easy way. 

JDogo define a new request Structure which I called **DOC** stands for "**D**etailed **O**bject **C**arrier", This DOC Model is very flexible to handle and hold data with effecient way without caring about the data type and conversion.

### DOC Example

Below is example of a DOC request generated by JDogo: 

```
 DOC {
    "Header":"id|name|activated|#product<id!name!quantity!#stock<id!label!#location<id!locationName!opened>>>",
    "Data":[
        "1|Khaled|true|#<1!Car!10!#<10!Stock 1!#<10!Nasr City!true>>>",
        "2|Mahmoud|true|#<1!Car!10!#<10!Stock 1!#<10!Nasr City!true>>>"
     ],
     "Status":"200|Ok"
    }
```

As shown above the **DOC** divided into 3 Major Parts (Header ,Data,Status).

* **Header** : Which holds the details of all the carried data Section and remove the excluded ones. 
* **Data Section**: The structure of Data Section above is so flexible ,It don't have any quotations (Single or Double) no defenition about anything,It is just pure data and Jdogo parser in the Other hand can understand and parse anything. 

Note that this data Section contain an array of object as you see that we removed the redendancy of the field name which found in Json array Object , commas and quotations which mean that i will trasmitted more speed than JSon.

Imagine that we have a list of 100 Object and we remove the 100 Header tag Like ' "id": "name": {} ' 
So the size of the Request will be decreased to the half which is goodthing.You can change the delimeters in the next version it will be very flexible so that you can build your DOC structure which meet your needs. 

### How to Generate Doc 

Generation and Parsing is So simple with two Lines of code you can save a lot of efforts.
as Example: 

```
JDOCGenerator generator = new JDOCGenerator(Customer.class);
String docCode = generator.generateObject(customer);  
```

All is Done now with the above two Lines of code , you can generate a big tree of any wanted object with any depth to be exposed. 

To generate List of Objects 

```
String jdocListCode = generator.generateObjectList(customers);
```

### Generation Options:

You can configure the Generator to exclude the Mapping of the Location Object in the Customer Object Tree. 
as Example: 
```
generator.excludeObject("Location"); 
```

You can also exclude any number of fields within any Object you want. 
```
generator.excludeFieldsFromObject("Product", "name", "id"); 
```

Now it is the Time to parse the sent List of Object in the receiver like that 

### JDogo Parser
```
JDOCParser parser = new JDOCParser();
List<Customer> customerList= (List<Customer>) parser.getObjectList(Customer.class, jdocListCode);
```

Now you have a parsed List of Customer List with only these 2 Lines, No Dtos , No Mappers, Validations will be implemeted in the next version and encryption also will be applied.

### What is Next 
The Code is still under Modification and testing and Modulation.. Alot of features will be provided in Version 2. 

### Note
This Version include flat Mapping only .. List, Set, Maps still not added to parsers but it will be included in the next Version .. The code have a simple example with main method to test the functionality. 

### Contribution
Thanks for reading my Document .. feel free to participate me to build a strong API. 

### Contact Me
My Contact: engkhaledmos3ad@gmail.com

