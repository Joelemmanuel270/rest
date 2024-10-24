package org.project;

import org.testng.Assert;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;
import org.utils.*;
import io.restassured.http.ContentType;
import io.restassured.response.Response;


import static io.restassured.RestAssured.*;

public class E2ETest {
	String username="joelemmamuel07@gmail.com";
	String baseUrl="https://www.shoppersstack.com/shopping";
	String token;
	int userId;
	int addressId;
	int productId;
	
	
	
	@Test
	private void login() {
		ShopperLogin s=new ShopperLogin();
		s.setEmail(username);
		s.setPassword("Password@123");
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
		
	}
	
	@Test
	public void getproducts() {
		
		Response response = given().queryParam("zoneId","ALPHA")
				.when().get(baseUrl+"/products/alpha");
		
		response.then().assertThat().statusCode(200);
		productId=response.jsonPath().get("data[327].productId");
		System.out.println(productId);
		
	}
	
	@Test(dependsOnMethods = "login")
	public void addAddress() {
		Address as=new Address();
		as.setBuildingInfo("n0.5");
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
	
		
	}
	//data.name
	
	
}
