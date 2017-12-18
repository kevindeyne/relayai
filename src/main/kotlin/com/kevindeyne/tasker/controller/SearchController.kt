package com.kevindeyne.tasker.controller

import com.kevindeyne.tasker.controller.form.SearchForm
import com.kevindeyne.tasker.domain.SearchResult
import com.kevindeyne.tasker.repositories.SearchRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class SearchController(@Autowired val searchRepository : SearchRepository) {
	
	companion object {
		const val SEARCH_GET = "/search"
		const val SEARCH_POST = "/search"
	}
	
	@GetMapping(SEARCH_GET)
	fun getSearch(model : Model) : String {
		model.addAttribute("activePage", "search")
		return "search"
	}
	
	@PostMapping(SEARCH_POST) @ResponseBody
	fun searchForValue(@RequestBody searchValue : SearchForm) :  List<SearchResult> {
		return searchRepository.findInSrcVal(searchValue.search.toLowerCase())
	}
}