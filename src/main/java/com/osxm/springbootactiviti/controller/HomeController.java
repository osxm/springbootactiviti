/**
 * @Title: HomeController.java
 * @Package com.osxm.springbootactiviti.controller
 * @Description: TODO
 * @author oscarchen
 * @date 2020年9月3日
 * @version V1.0
 */
package com.osxm.springbootactiviti.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
  * @ClassName: HomeController
  * @Description: TODO
  * @author oscarchen
  */

@RestController
public class HomeController {

	@GetMapping("/home")
	public String home() {
		return "Home Page";
	}
	
	@GetMapping("/")
	public String index() {
		return "Index Page";
	}
}
