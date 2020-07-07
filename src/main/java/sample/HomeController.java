package sample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import sample.api.facebook.FacebookGraph;

@Controller
public class HomeController {

	private FacebookGraph facebookGraph;

	@Autowired
	public HomeController(FacebookGraph facebookGraph) {
		this.facebookGraph = facebookGraph;
	}
	
	@GetMapping("/")
	public String home(Model model) {
		model.addAttribute("page", facebookGraph.getPages());
		model.addAttribute("reviews", facebookGraph.getReviewWithRestFb());
		model.addAttribute("pageAccessToken", facebookGraph.pageAccessToken());

		return "home";
	}
	
}
