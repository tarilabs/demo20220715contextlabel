package org.drools.demo20220715contextlabel;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.StringReader;
import java.util.Arrays;
import java.util.Map;

import javax.inject.Inject;

import org.drools.util.IoUtils;
import org.drools.util.io.ReaderResource;
import org.junit.jupiter.api.Test;
import org.kie.dmn.api.core.DMNContext;
import org.kie.dmn.api.core.DMNResult;
import org.kie.dmn.api.core.DMNRuntime;
import org.kie.dmn.api.marshalling.DMNMarshaller;
import org.kie.dmn.backend.marshalling.v1x.DMNMarshallerFactory;
import org.kie.dmn.core.internal.utils.DMNRuntimeBuilder;
import org.kie.dmn.core.internal.utils.DynamicDMNContextBuilder;
import org.kie.dmn.core.internal.utils.MarshallingStubUtils;
import org.kie.dmn.core.jsr223.JSR223EvaluatorCompilerFactory;
import org.kie.dmn.model.api.Definitions;
import org.kie.yard.impl1.YaRDParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class YaRDTest {
    private static final Logger LOG = LoggerFactory.getLogger(YaRDTest.class);

    @Inject
    ObjectMapper objectMapper;

    @Test
    public void testYaRD() throws Exception {
        String yamlDecision = new String(
                IoUtils.readBytesFromInputStream(this.getClass().getResourceAsStream("/labeling.yard.yml"), true));

        final String CONTEXT = "{\"host\": \"basedidati.milano.local\", \"diskPerc\": 70, \"memPerc\": 50, \"cpuPerc\": 20}";
        LOG.info("INPUT:\n{}", CONTEXT);

        DMNMarshaller dmnMarshaller = DMNMarshallerFactory.newDefaultMarshaller();
        YaRDParser parser = new YaRDParser();
        Definitions definitions = parser.parse(yamlDecision);
        String xml = dmnMarshaller.marshal(definitions);
        LOG.debug("XML:\n{}", xml);

        DMNRuntime dmnRuntime = DMNRuntimeBuilder.fromDefaults()
                .setDecisionLogicCompilerFactory(new JSR223EvaluatorCompilerFactory())
                .buildConfiguration()
                .fromResources(Arrays.asList(new ReaderResource(new StringReader(xml))))
                .getOrElseThrow(RuntimeException::new);
        Map<String, Object> readValue = readJSON(CONTEXT);
        DMNContext dmnContext = new DynamicDMNContextBuilder(dmnRuntime.newContext(), dmnRuntime.getModels().get(0))
                .populateContextWith(readValue);
        DMNResult dmnResult = dmnRuntime.evaluateAll(dmnRuntime.getModels().get(0), dmnContext);
        Object serialized = MarshallingStubUtils.stubDMNResult(dmnResult.getContext().getAll(), Object::toString);
        final String OUTPUT_JSON = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(serialized);
        Map<String, Object> outputJSONasMap = readJSON(OUTPUT_JSON);

        LOG.info("{}", OUTPUT_JSON);
        assertThat(outputJSONasMap).hasFieldOrPropertyWithValue("location", "location.emea.italy.milan");
        assertThat(outputJSONasMap).hasFieldOrPropertyWithValue("type", "type.db");
        assertThat(outputJSONasMap).hasFieldOrPropertyWithValue("oncall", "oncall.EMEA.dbadm");
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> readJSON(final String CONTEXT) throws JsonProcessingException, JsonMappingException {
        return objectMapper.readValue(CONTEXT, Map.class);
    }

}