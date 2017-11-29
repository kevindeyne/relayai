package com.kevindeyne.tasker.nlp;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetector;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

public class OpenNLPProcessor {

	private static final String SPACE = " ";

	private static OpenNLPProcessor nlp;

	private SentenceModel sentenceModel = null;
	private TokenizerModel tokenizerModel = null;
	private POSModel posModel = null;

	private OpenNLPProcessor() {
	}

	public static OpenNLPProcessor getInstance() {
		return getInstance("en");
	}

	private static OpenNLPProcessor getInstance(String lang) {
		if (null == nlp) {
			nlp = new OpenNLPProcessor();

			nlp.setSentenceModel(nlp.loadSentenceModel(lang));
			nlp.setTokenizerModel(nlp.loadTokenizerModel(lang));
			nlp.setPosModel(nlp.loadPOSModel(lang));
		}
		return nlp;
	}

	private final List<String> acceptedPOSTags = new ArrayList<String>(Arrays.asList("NN", "N", "JJ"));

	public Set<String> process(String content) {
		Set<String> contentSet = new HashSet<String>();
		String[] sentences = retrieveSentencesFromText(content);

		for (String sentence : sentences) {
			List<String> posTokenized = tokenizer(sentence);
			for (String posToken : posTokenized) {
				String key = posToken.toLowerCase().trim();
				if(isCleanKeyword(key)){
					contentSet.add(key);
				}
			}
		}

		return contentSet;
	}

	public String[] retrieveSentencesFromText(String content){
		SentenceDetector _sentenceDetector = new SentenceDetectorME(sentenceModel);
		String[] sentences = _sentenceDetector.sentDetect(content);
		return sentences;
	}

	private boolean isCleanKeyword(String key) {
		key = key.replaceAll("[a-z]", "");
		key = key.replaceAll(SPACE, "");
		return key.length() == 0;
	}

	private List<String> tokenizer(String sentence) {
		List<String> result = new ArrayList<String>();
		Tokenizer _tokenizer = new TokenizerME(tokenizerModel);

		String[] tokens = _tokenizer.tokenize(sentence);
		String[] posTags = posTagging(tokens);

		for (int i = 0; i < tokens.length; i++) {
			if (acceptedPOSTags.contains(posTags[i])) {
				result.add(tokens[i]);
			}
		}

		return result;
	}

	private String[] posTagging(String[] tokens) {
		POSTaggerME _posTagger = new POSTaggerME(posModel);
		String[] posTaggedValues = _posTagger.tag(tokens);
		return posTaggedValues;
	}

	private POSModel loadPOSModel(String lang) {
		POSModel posModel = null;
		InputStream modelIn = null;
		try {
			modelIn = getClass().getResourceAsStream("/LANG-pos-maxent.bin".replace("LANG", lang));
			posModel = new POSModel(modelIn);
			modelIn.close();
		} catch (final IOException ioe) {
			throw new RuntimeException(ioe);
		} finally {
			if (modelIn != null) {
				try {
					modelIn.close();
				} catch (final IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
		return posModel;
	}

	private TokenizerModel loadTokenizerModel(String lang) {
		TokenizerModel tokenizerModel = null;
		InputStream modelIn = null;
		try {
			modelIn = getClass().getResourceAsStream("/LANG-token.bin".replace("LANG", lang));
			tokenizerModel = new TokenizerModel(modelIn);
			modelIn.close();
		} catch (final IOException ioe) {
			throw new RuntimeException(ioe);
		} finally {
			if (modelIn != null) {
				try {
					modelIn.close();
				} catch (final IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
		return tokenizerModel;
	}

	private SentenceModel loadSentenceModel(String lang) {
		SentenceModel sentenceModel = null;
		InputStream modelIn = null;
		try {
			modelIn = getClass().getResourceAsStream("/LANG-sent.bin".replace("LANG", lang));
			sentenceModel = new SentenceModel(modelIn);
			modelIn.close();
		} catch (final IOException ioe) {
			throw new RuntimeException(ioe);
		} finally {
			if (modelIn != null) {
				try {
					modelIn.close();
				} catch (final IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
		return sentenceModel;
	}

	private void setSentenceModel(SentenceModel sentenceModel) {
		this.sentenceModel = sentenceModel;
	}

	private void setTokenizerModel(TokenizerModel tokenizerModel) {
		this.tokenizerModel = tokenizerModel;
	}

	private void setPosModel(POSModel posModel) {
		this.posModel = posModel;
	}

	public String processNoCount(String text) {
		StringBuffer buffer = new StringBuffer();

		String[] sentences = retrieveSentencesFromText(text);

		for (String sentence : sentences) {
			List<String> posTokenized = tokenizer(sentence);
			for (String posToken : posTokenized) {
				String key = posToken.toLowerCase().trim();
				if(key.length() >= 3){
					if(isCleanKeyword(key)){
						buffer.append(key);
						buffer.append(SPACE);
					}
				}
			}
		}

		return buffer.toString();
	}
}
