package org.drools.demo20220715contextlabel;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import org.drools.util.IoUtils;
import org.drools.util.io.ReaderResource;
import org.kie.dmn.api.core.DMNContext;
import org.kie.dmn.api.core.DMNDecisionResult;
import org.kie.dmn.api.core.DMNResult;
import org.kie.dmn.api.core.DMNRuntime;
import org.kie.dmn.api.core.DMNDecisionResult.DecisionEvaluationStatus;
import org.kie.dmn.api.marshalling.DMNMarshaller;
import org.kie.dmn.backend.marshalling.v1x.DMNMarshallerFactory;
import org.kie.dmn.core.internal.utils.DMNRuntimeBuilder;
import org.kie.dmn.core.internal.utils.DynamicDMNContextBuilder;
import org.kie.dmn.core.jsr223.JSR223EvaluatorCompilerFactory;
import org.kie.dmn.model.api.Definitions;
import org.kie.yard.impl1.YaRDParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class LabelYaRDEvaluator {

    private static final Logger LOG = LoggerFactory.getLogger(LabelYaRDEvaluator.class);
    private DMNRuntime dmnRuntime;
    
    @PostConstruct
    public void init() {
        try {
            String yamlDecision = new String(
                    IoUtils.readBytesFromInputStream(this.getClass().getResourceAsStream("/labeling.yard.yml"), true));

            DMNMarshaller dmnMarshaller = DMNMarshallerFactory.newDefaultMarshaller();
            YaRDParser parser = new YaRDParser();
            Definitions definitions = parser.parse(yamlDecision);
            String xml = dmnMarshaller.marshal(definitions);
            LOG.debug("XML:\n{}", xml);

            dmnRuntime = DMNRuntimeBuilder.fromDefaults()
                    .setDecisionLogicCompilerFactory(new JSR223EvaluatorCompilerFactory())
                    .buildConfiguration()
                    .fromResources(Arrays.asList(new ReaderResource(new StringReader(xml))))
                    .getOrElseThrow(RuntimeException::new);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> labels(Map<String, Object> readValue) {
        List<String> results = new ArrayList<>();
        DMNContext dmnContext = new DynamicDMNContextBuilder(dmnRuntime.newContext(), dmnRuntime.getModels().get(0))
                .populateContextWith(readValue);
        DMNResult dmnResult = dmnRuntime.evaluateAll(dmnRuntime.getModels().get(0), dmnContext);
        for (DMNDecisionResult r : dmnResult.getDecisionResults()) {
            LOG.info("{} [{}]: {}", r.getDecisionName(), r.getEvaluationStatus(), r.getResult());
            if (r.getEvaluationStatus() == DecisionEvaluationStatus.SUCCEEDED && r.getResult()!= null) {
                results.add(r.getResult().toString());
            }
        }
        return results;
    }
}
