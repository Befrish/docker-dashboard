package de.befrish.docker.dashboard.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.flipkart.zjsonpatch.JsonDiff;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public final class YamlMatcher {

    private YamlMatcher() {
        super();
    }

    public static Matcher<String> yamlEqualTo(final String expectedYaml) {
        return new TypeSafeDiagnosingMatcher<>() {
            @Override
            protected boolean matchesSafely(final String actualYaml, final Description description) {
                try {
                    final JsonNode diff = resolvePatch(actualYaml, expectedYaml);
                    final boolean matches = diff.isEmpty();
                    if (!matches) {
                        description.appendText("YAML ")
                                .appendValue(actualYaml)
                                .appendText(System.lineSeparator())
                                .appendText("Diff: " + diff);
                    }
                    return matches;
                } catch (final JsonProcessingException exception) {
                    throw new RuntimeException(exception);
                }
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText("YAML equals to ")
                        .appendValue(expectedYaml);
            }
        };
    }

    private static JsonNode resolvePatch(final String yaml1, final String yaml2) throws JsonProcessingException {
        final ObjectMapper mapper = new YAMLMapper();
        final JsonNode jsonNode1 = mapper.readTree(yaml1);
        final JsonNode jsonNode2 = mapper.readTree(yaml2);
        return JsonDiff.asJson(jsonNode1, jsonNode2);
    }

}
