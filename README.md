# java-core-server
A multithreaded and non-blocking HTTP server with router, designed to host CPU intensive REST API. Build only with Java Core and requires only Java SE to run. 
compiler source: 1.8
compiler target: 1.8

Build:
mvn clean package

# A sample API 
Basic Java REST API for storing and retrieving 3D shapes  
run: software.zajac.examples.cuboid_api.Main

REST routes 
	
	GET http://localhost:8080/api/v1/shapes/ - returns list of all stored shapes 
	
	POST http://localhost:8080/api/v1/shapes/ - submits new shape 
		
			Data format: RAW, application/json;

			Request data model {
									String id;
									int[] vertices;
									int[] edges;
									int[] faces;
								}
			Sample request 
			{	"id":"cube1",
				"vertices":[
							 100,10,10,
							 100,20,10,
							 200,20,10,
							 200,10,10,
							 100,10,20,
							 100,20,20,
							 200,20,20,
							 200,10,20
							],
				"edges":[
							1,2,
							2,3,
							3,4,
							4,1,
							5,6,
							6,7,
							7,8,
							8,5,
							1,5,
							2,6,
							3,7,
							4,8
						],
				"faces":[]
				}
	
	
