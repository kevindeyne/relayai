package com.kevindeyne.tasker.integration;

import org.fluentlenium.adapter.junit.FluentTest;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SimpleFluentLeniumTest extends FluentTest {

    @Test
    public void titleOfDuckDuckGoShouldContainSearchQueryName() {
        goTo("https://duckduckgo.com");
        $("#search_form_input_homepage").fill().with("FluentLenium");
        $("#search_button_homepage").submit();
        assertThat(window().title()).contains("FluentLenium");
    }

}
