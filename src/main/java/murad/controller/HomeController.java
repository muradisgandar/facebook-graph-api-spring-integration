package murad.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestParam;
import murad.facebook.FacebookGraph;

@Controller
public class HomeController {

	private final FacebookGraph facebookGraph;

	@Autowired
	public HomeController(FacebookGraph facebookGraph) {
		this.facebookGraph = facebookGraph;
	}
	
	@GetMapping("/pages")
	public String home(Model model) {
		model.addAttribute("page", facebookGraph.getPages());

		return "pages";
	}

	@GetMapping("/get-reviews")
	public String home2(Model model, @RequestParam("pageId") String pageId) {
		model.addAttribute("reviews", facebookGraph.getReviewWithRestFb(pageId));

		return "message";
	}


	
}
