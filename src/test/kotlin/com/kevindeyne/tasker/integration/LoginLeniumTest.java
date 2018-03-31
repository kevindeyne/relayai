package com.kevindeyne.tasker.integration;

import org.fluentlenium.adapter.junit.FluentTest;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LoginLeniumTest extends FluentTest {

    @Test
    public void testLoginAsDevGetToTaskboard() {
        goTo("http://taskr.us-west-2.elasticbeanstalk.com:8080");
        $("#as-dev").click();
        assertThat(window().title()).contains("board");
    }
}
