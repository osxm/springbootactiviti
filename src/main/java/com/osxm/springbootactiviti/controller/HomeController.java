/**
 * @Title: HomeController.java
 * @Package com.osxm.springbootactiviti.controller
 * @Description: TODO
 * @author oscarchen
 * @date 2020年9月3日
 * @version V1.0
 */
package com.osxm.springbootactiviti.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
  * @ClassName: HomeController
  * @Description: TODO
  * @author oscarchen
  */

@Controller
public class HomeController {

	@GetMapping("/home")
	public String home() {
		return "index";
	}
}
