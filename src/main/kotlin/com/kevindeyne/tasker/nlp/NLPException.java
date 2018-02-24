package com.kevindeyne.tasker.nlp;

public class NLPException extends RuntimeException {

	private static final long serialVersionUID = 1091607360583152954L;

    public NLPException(Throwable cause) {
        super("NLP Logic has failed", cause);
    }

}
