package org.project;

import org.testng.Assert;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;
import org.utils.*;
import io.restassured.http.ContentType;
import io.restassured.response.Response;


import static io.restassured.RestAssured.*;

public class E2ETest {
	String username="priyamagesan6316@gmail.com";
	String baseUrl="https://www.shoppersstack.com/shopping";
	String token;
	int userId;
	int AddressId;
	int productId;
	long a=4000l;
	
	@Test
	private void login() {
		ShopperLogin s=new ShopperLogin();
		s.setEmail(username);
		s.setPassword("Burgman@6316");
		s.setRole("SHOPPER");
		
		Response post = given().body(s).contentType(ContentType.JSON)
									.when().post(baseUrl+"/users/login");
		
		post.then().assertThat().statusCode(200);
		//post.then().assertThat().time(Matchers.lessThan(3001l));
		
		String mail = post.jsonPath().get("data.email");
		Assert.assertEquals(username, mail);
		
		userId = post.jsonPath().get("data.userId");
		token = post.jsonPath().get("data.jwtToken");
		token="Bearer "+token;
		System.out.println("Login done successfully and generated the token");
		
	}
	
	@Test
	public void getproducts() {
		
		Response response = given().queryParam("zoneId","ALPHA")
				.when().get(baseUrl+"/products/alpha");
		
		response.then().assertThat().statusCode(200);
		response.then().assertThat().time(Matchers.lessThan(a));
		productId=response.jsonPath().get("data[1].productId");
		System.out.println("got a productId :" +productId);
		
	}
	
	
	@Test(dependsOnMethods = "login")
	public void addAddress() {
		Address as=new Address();
		as.setBuildingInfo("no.5");
		as.setCity("Thane");
		as.setCountry("India");
		as.setLandmark("park");
		as.setName("priya");
		as.setPhone("7339343180");
		as.setState("Maharastra");
		as.setPincode("432101");
		as.setType("Home");
		as.setStreetInfo("mumbai cross street");
		
		Response post = given().body(as).contentType(ContentType.JSON)
								.header("Authorization",token)
								.pathParam("shopperId", userId)
						.when().post(baseUrl+"/shoppers/{shopperId}/address");
		
		
		post.then().statusCode(201);
		post.then().assertThat().time(Matchers.lessThan(a));
		
		AddressId = post.jsonPath().get("data.addressId");
		System.out.println("New Address has created successfully :" +AddressId);
			
	}
	
	@Test(dependsOnMethods="login")
	public void getalladdresses()
	{
		Response response = given().header("Authorization",token).pathParam("shopperId",userId)
		.when().get(baseUrl+"/shoppers/{shopperId}/address");
		
		response.then().assertThat().statusCode(200);
		response.then().assertThat().time(Matchers.lessThan(a));
				
		
	}
	
	@Test(dependsOnMethods="addAddress")
	public void getspecificaddress()
	{
	 
		Response body = given().header("Authorization",token)
				.pathParam("shopperId",userId).pathParam("addressId",AddressId)
		        .when().get(baseUrl+"/shoppers/{shopperId}/address/{addressId}");
		
		body.then().statusCode(200);
		body.then().assertThat().time(Matchers.lessThan(a));
		System.out.println("Got a particular address");	
	
   }
	
	@Test(dependsOnMethods = "addAddress")
	public void updateaddress()
	{
		
		updateAddress s=new updateAddress();
		s.setBuildingInfo("no.10");
		s.setCity("pune");
		s.setCountry("Indiaa");
		s.setLandmark("park1");
		s.setName("priyaa");
		s.setPhone("82646498837");
		s.setState("bangaloree");
		s.setPincode("560010");
		s.setType("Home");
		s.setStreetInfo("lilly street");
		
		Response body = given().body(s).contentType(ContentType.JSON).header("Authorization",token)
				.pathParam("shopperId",userId).pathParam("addressId", AddressId)
		        .when().put(baseUrl+"/shoppers/{shopperId}/address/{addressId}");
		
		body.then().statusCode(200);
		//body.then().assertThat().time(Matchers.lessThan(a));
		
		
		
		  
		
		
		
	}
	
	
	
}
