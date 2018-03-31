package com.kevindeyne.tasker

import com.kevindeyne.tasker.service.KeywordGeneration
import org.junit.Assert
import org.junit.Test

class TagcloudGenerationTests {

	@Test
	fun generateTagcloudFromStaticText() {
		var sampleText =
				" Tab name unchanged when navigating yourname.github.io/# (i.e. Home) after having navigated pages by labels" +
						" For example," +
						" After viewing http://zhaoda.net/#labels/Project, when one want to go back to Home(http://zhaoda.net/#), the Tab name still shows 'Project'." +
						" Chrome Version 45.0.2454.85 (64-bit)" +
						" OS X Yosemite 10.10.5"

		val keywords = KeywordGeneration.generateKeywords(sampleText, "en")

		Assert.assertTrue(keywords.contains("home")) //home is important because it shows that it can lowercase and separate the word from more than just spaces.
		Assert.assertFalse(keywords.contains("http")) //dont interpret the useless parts of an url, but do interpret the interesting parts
		Assert.assertTrue(keywords.contains("zhaoda"))
	}


	@Test
	fun findSimpleKeywordCorrelation() {
		val textMatch1 = "I've recently purchased some bread and found it to be very hard. Was it old or something?"
		val textMatch2 = "I don't like the bread from your store. It feels old and hard."
		val textNoMatch = "I quite like eating lots of fish and perhaps take a sip of tea."

		val keywords1 = KeywordGeneration.generateKeywords(textMatch1, "en")
		val keywords2 = KeywordGeneration.generateKeywords(textMatch2, "en")
		val keywords3 = KeywordGeneration.generateKeywords(textNoMatch, "en")

		val matchesBetween1and2 = MatchFinder.findMatches(keywords1, keywords2)
		val matchesBetween1and3 = MatchFinder.findMatches(keywords1, keywords3)
		val matchesBetween3and2 = MatchFinder.findMatches(keywords3, keywords2)

		println("1 vs 2: $matchesBetween1and2, 1 vs 3: $matchesBetween1and3, 3 vs 2: $matchesBetween3and2")

		Assert.assertTrue(matchesBetween1and2 > matchesBetween1and3)
		Assert.assertTrue(matchesBetween1and2 > matchesBetween3and2)

		Assert.assertFalse(keywords2.contains("your"))
	}
}
