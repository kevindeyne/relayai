package com.kevindeyne.tasker

import org.junit.Assert
import org.junit.Test
import org.tartarus.snowball.ext.EnglishStemmer
import com.kevindeyne.tasker.nlp.OpenNLPProcessor

class TagcloudGenerationTests {

	@Test
	fun generateTagcloudFromStaticText() {
		var sampleText =
		" Tab name unchanged when navigating yourname.github.io/# (i.e. Home) after having navigated pages by labels" + 
		" For example,"+		
		" After viewing http://zhaoda.net/#labels/Project, when one want to go back to Home(http://zhaoda.net/#), the Tab name still shows 'Project'." +
		" Chrome Version 45.0.2454.85 (64-bit)" +
		" OS X Yosemite 10.10.5"
		
		val keywords = generateKeywords(sampleText)
		
		Assert.assertTrue(keywords.contains("home"))
		Assert.assertFalse(keywords.contains("http"))
	}
	
	
	@Test
	fun findSimpleKeywordCorrelation() {
		val textMatch1 = "I've recently purchased some bread and found it to be very hard. Was it old or something?"
		val textMatch2 = "I don't like the bread from your store. It feels old and hard."
		val textNoMatch = "I quite like eating lots of fish and perhaps take a sip of tea."
		
		val keywords1 = generateKeywords(textMatch1)
		val keywords2 = generateKeywords(textMatch2)
		val keywords3 = generateKeywords(textNoMatch)
				
		val matchesBetween1and2 = MatchFinder.findMatches(keywords1, keywords2)
		val matchesBetween1and3 = MatchFinder.findMatches(keywords1, keywords3)
		val matchesBetween3and2 = MatchFinder.findMatches(keywords3, keywords2)
		
		println("1 vs 2: $matchesBetween1and2, 1 vs 3: $matchesBetween1and3, 3 vs 2: $matchesBetween3and2")
		
		Assert.assertTrue(matchesBetween1and2 > matchesBetween1and3)
		Assert.assertTrue(matchesBetween1and2 > matchesBetween3and2)
		
		Assert.assertFalse(keywords2.contains("your"))
	}
	
	
	
	///
	fun generateKeywords(text : String) : Set<String> {
		val re = Regex("[^a-z]")
		val newText = text.toLowerCase().replace(re, " ");
		//val splitValues = text.toLowerCase().replace(re, " ").split(" ");
		var splitValues : Set<String> = OpenNLPProcessor.getInstance().process(newText);
		val resultSet = HashSet<String>()

		val stemmer : EnglishStemmer = EnglishStemmer()
		
		for(splitValue in splitValues){
			val value = splitValue.replace(re, " ")
			if(value.length > 2 && isActiveWord(value)){
				stemmer.setCurrent(value)
				stemmer.stem()
				resultSet.add(stemmer.getCurrent())	
			}
		}
		
		//println(resultSet)
		
		return resultSet
	}
	
	fun isActiveWord(value : String) = !arrayOf("http").contains(value)
}
