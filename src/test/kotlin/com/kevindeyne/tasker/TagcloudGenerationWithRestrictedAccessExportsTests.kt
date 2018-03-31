package com.kevindeyne.tasker

import com.kevindeyne.tasker.service.KeywordGeneration
import org.codehaus.stax2.XMLInputFactory2
import org.codehaus.stax2.XMLStreamReader2
import org.junit.Assert
import org.junit.Test
import java.io.FileInputStream
import javax.xml.stream.events.XMLEvent

class TagcloudGenerationWithRestrictedAccessExportsTests {

	fun load() : ArrayList<MutableMap<String, String>> {
		val fileName = "I:\\tasker-data\\extract.xml"
		val parsed = ArrayList<MutableMap<String, String>>()
		try {
			val xmlif2 = XMLInputFactory2.newInstance() as XMLInputFactory2
			val reader = xmlif2.createXMLStreamReader(fileName, FileInputStream(fileName)) as XMLStreamReader2

			var currentIndex = 0
			var currentTag = ""

			while (reader.hasNext()) {
				val eventType = reader.next()
				when (eventType) {
					XMLEvent.START_ELEMENT -> {
						val curElement = reader.name.toString()
						currentTag = curElement
						if ("item" == currentTag) {
							parsed.add(mutableMapOf())
						}
					}
					XMLEvent.CHARACTERS -> {
						var content: String? = reader.text.trim { it <= ' ' }
						if (null != content && "" != content) {
							content = content.replace("</p>".toRegex(), " ").replace("<p>".toRegex(), " ").trim { it <= ' ' }
							if (null != parsed[currentIndex][currentTag]) {
								parsed[currentIndex].put(currentTag, parsed[currentIndex][currentTag] + " " + content)
							} else {
								parsed[currentIndex].put(currentTag, content)
							}
						}
					}
					XMLEvent.END_ELEMENT -> {
						val curElement = reader.name.toString()
						if ("item" == curElement) {
							currentIndex++
						}
					}
				}
			}
		} catch (ex: Exception) {
			ex.printStackTrace()
		}

		return parsed
	}

	//@Test
	fun generateTagcloudFromStaticText() {
		val parsed = load()

		val partToLoad = parsed.subList(0, parsed.size/100*70)
		val part2 = parsed.subList(parsed.size/100*70+1, parsed.size)

		val DB: MutableMap<String, MutableSet<String>> = buildDB(partToLoad)

		var index = 0
		var counter = 0.0
		var okay = 0.0
		var fp = 0.0

		for(p in part2){
			if(DB.keys.contains(p["assignee"])){
				counter++
				var biggest: String = checkIndex(part2, index, DB)
				if(biggest.equals(p["assignee"])){
					okay++
				} else {
					fp++
					println("$biggest< vs ${p["assignee"]}: X - ${p["summary"]}")
				}
			}
			index++
		}

		val precision = (okay/(counter))*100

		println("$okay/$counter: $precision%")
		Assert.assertTrue(precision > 40)
	}

	private fun checkIndex(part2: MutableList<MutableMap<String, String>>, index: Int, DB: MutableMap<String, MutableSet<String>>): String {
		val searchFor = loadKeywords(part2, index)

		val m: MutableMap<String, Int> = mutableMapOf<String, Int>()

		for (s in searchFor) {
			for (d in DB.keys) {
				val iter = DB[d]?.iterator()
				if (iter != null) {
					while (iter.hasNext()) {
						if (iter.next() == s) {
							val y = m[d]
							if (y != null) {
								m[d] = 1 + y
							} else {
								m[d] = 1
							}
						}
					}
				}
			}
		}

		var biggest: String = ""
		var biggestInt: Int = -1
		for (y in m) {
			if (biggestInt == -1 || y.value > biggestInt) {
				biggestInt = y.value
				biggest = y.key
			}
		}

		//println(biggest)
		return biggest
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

	private fun loadKeywords(part2: MutableList<MutableMap<String, String>>, indexToLoad: Int): Set<String> {
		val text2 = "${part2[indexToLoad]["summary"]} ${part2[indexToLoad]["description"]}"
		return KeywordGeneration.generateKeywords(text2, "en")
	}

	private fun buildDB(part1: MutableList<MutableMap<String, String>>): MutableMap<String, MutableSet<String>> {
		val DB: MutableMap<String, MutableSet<String>> = mutableMapOf()

		for (i in part1) {
			val reporter = "${i["assignee"]}"
			val text = "${i["summary"]} ${i["description"]}"
			val keywords = KeywordGeneration.generateKeywords(text, "en")

			if(keywords.isNotEmpty()){
				if (null != DB[reporter]) {
					DB[reporter]?.addAll(keywords)
				} else {
					DB[reporter] = keywords.toMutableSet()
				}
			}
		}

		return DB
	}
}
